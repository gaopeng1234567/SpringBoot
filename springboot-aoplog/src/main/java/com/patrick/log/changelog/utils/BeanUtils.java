package com.patrick.log.changelog.utils;

import com.patrick.log.changelog.annotation.FiledTransConvert;
import com.patrick.log.changelog.model.ChangeValueLog;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static org.springframework.util.ReflectionUtils.makeAccessible;

/**
 * @author patrick
 * @date 2021/3/15 上午9:52
 * @Des bean对象操作工具
 * 最簡單的事是堅持，最難的事還是堅持
 */
public class BeanUtils {


    /**
     * 获取两个对象之间属性值改变记录列表（对象类型可不同，只要字段名和类型相同即可）
     *
     * @param newObj 新对象
     * @param old    旧对象
     * @return 对象值改变列表
     */
    public static List<ChangeValueLog> getFieldValueChangeRecords(Object newObj, Object old) {
        return getFieldValueChangeRecords(newObj, old, null);
    }

    /**
     * 获取两个对象之间属性值改变记录列表（对象类型可不同，只要字段名和类型相同即可）
     *
     * @param newObj    新对象
     * @param old       旧对象
     * @param predicate 字段过滤器
     * @return 对象值改变列表
     */
    public static List<ChangeValueLog> getFieldValueChangeRecords(Object newObj, Object old, Predicate<Field> predicate) {
        Class<?> oldObjClass = old.getClass();
        List<ChangeValueLog> changeRecords = new ArrayList<>();
        for (Field nf : getFields(newObj.getClass())) {
            if (predicate != null && !predicate.test(nf)) {
                continue;
            }
            String fieldName = nf.getName();
            // 旧值不存在指定字段，直接跳过
            Field oldObjFiled = getField(oldObjClass, fieldName, nf.getType());
            if (oldObjFiled == null) {
                continue;
            }
            Object newValue = getFieldValue(nf, newObj);
            Object oldValue = getFieldValue(oldObjFiled, old);
            if (null != newValue && !Objects.equals(oldValue, newValue)) {
                // 判断字段值是否需要转换，如将枚举值'1'转换为有实际意义的字符串
                FiledTransConvert convert = AnnotationUtils.getAnnotation(nf, FiledTransConvert.class);
                convert = convert == null ? AnnotationUtils.getAnnotation(oldObjFiled, FiledTransConvert.class) : convert;
                if (convert != null) {
                    newValue = convertValue(convert, newValue);
                    oldValue = convertValue(convert, oldValue);
                    // 判断是否需要转译
                    fieldName = StringUtils.hasLength(convert.rename()) ? convert.rename() : fieldName;
                }
                changeRecords.add(new FieldValueChangeRecord(fieldName, oldValue, newValue));
            }
        }
        return changeRecords;
    }

    private static Object convertValue(FiledTransConvert convertAnno, Object value) {
        Class<? extends FieldValueConverter> converterClazz = convertAnno.converter();
        // 如果FieldValueConverter不是默认的转换器，就使用该转换器
        if (converterClazz != FieldValueConverter.class) {
            // 转换器缓存中获取
            FieldValueConverter fc = converterCache.get(converterClazz);
            if (fc == null) {
                fc = BeanUtils.instantiate(converterClazz);
                converterCache.put(converterClazz, fc);
            }
            // 转换值
            return fc.convert(value);
        }

        Class<? extends Enum> enumClass = convertAnno.enumConverter();
        if (enumClass != FieldValueConvert.DefaultEnum.class) {
            return EnumUtils.getNameByValue((NameValueEnum[]) enumClass.getEnumConstants(), value);
        }

        return value;
    }
    /**
     * 获取所有field字段，包含父类继承的
     *
     * @param clazz 字段所属类型
     * @return
     */
    public static Field[] getFields(Class<?> clazz) {
        return getFields(clazz, null);
    }

    public static Field[] getFields(Class<?> clazz, Predicate<Field> fieldFilter) {
        List<Field> fields = new ArrayList<>(32);
        while (Object.class != clazz && clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (fieldFilter != null && !fieldFilter.test(field)) {
                    continue;
                }
                fields.add(field);
            }
            clazz = clazz.getSuperclass();
        }
        return fields.toArray(new Field[0]);
    }

    /**
     * 获取类的字段，包括父亲的
     *
     * @param clazz 字段所属类
     * @param name  类名
     * @param type  field类型
     * @return
     */
    public static Field getField(Class<?> clazz, String name, Class<?> type) {
        Assert.notNull(clazz, "clazz不能为空!");
        while (clazz != Object.class && clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if ((name == null || name.equals(field.getName())) &&
                        (type == null || type.equals(field.getType()))) {
                    return field;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }
    /**
     * 获取字段值
     *
     * @param field    字段
     * @param target  字段所属实例对象
     * @return        字段值
     */
    public static Object getFieldValue(Field field, Object target) {
        ReflectionUtils.makeAccessible(field);
        try {
            return field.get(target);
        } catch (Exception e) {
            throw new IllegalStateException(String.format("获取%s对象的%s字段值错误!"
                    , target.getClass().getName(), field.getName()), e);
        }
    }
}
