package com.patrick.log.changelog.utils;

import com.patrick.log.changelog.annotation.BaseEnum;
import com.patrick.log.changelog.annotation.FieldEnumConverter;
import com.patrick.log.changelog.annotation.FiledTransConvert;
import com.patrick.log.changelog.enums.OperateTaskTypeEnum;
import com.patrick.log.changelog.model.ChangeValueLog;
import com.patrick.log.changelog.model.User;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;


/**
 * @author patrick
 * @date 2021/3/15 上午9:52
 * @Des bean对象操作工具
 * 最簡單的事是堅持，最難的事還是堅持
 */
public class FiledChangeValueUtils {

    // 缓存转换器，防止强引用，内存泄露
    @SuppressWarnings("all")
    private static Map<Class<? extends FieldEnumConverter>, FieldEnumConverter> converterCache =
            Collections.synchronizedMap(new WeakHashMap<>(8));

    /**
     * 计算两个对象(可不同类，比较规则:字段名字和类型一致)属性变化值，如有多个则返回列表
     * 默认归属更新属性操作
     *
     * @param newObj 新对象
     * @param old    旧对象
     * @return 对象值改变列表
     */
    public static List<ChangeValueLog> getFieldValueChangeRecords(Object newObj, Object old) {
        return getFieldValueChangeRecords(newObj, old, OperateTaskTypeEnum.UPDATE, null);
    }

    /**
     * 计算两个对象(可不同类，比较规则:字段名字和类型一致)属性变化值，如有多个则返回列表
     * 自定义操作类型
     *
     * @param newObj 新对象
     * @param old    旧对象
     * @param type   操作类型
     * @return 对象值改变列表
     */
    public static List<ChangeValueLog> getFieldValueChangeRecords(Object newObj, Object old, OperateTaskTypeEnum type) {
        return getFieldValueChangeRecords(newObj, old, type, null);
    }

    /**
     * 获取两个对象之间属性值改变记录列表（对象类型可不同，只要字段名和类型相同即可）
     *
     * @param newObj    新对象
     * @param old       旧对象
     * @param type      操作类型
     * @param predicate 字段过滤器
     * @return 对象值改变列表
     */
    public static List<ChangeValueLog> getFieldValueChangeRecords(Object newObj, Object old, OperateTaskTypeEnum type, Predicate<Field> predicate) {
        Class<?> oldObjClass = old.getClass();
        List<ChangeValueLog> changeRecords = new ArrayList<>();
        for (Field nf : getFields(newObj.getClass())) {
            if (predicate != null && !predicate.test(nf)) {
                continue;
            }
            // 默认属性名字
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
                    newValue = convertEnumValue(convert, newValue);
                    oldValue = convertEnumValue(convert, oldValue);
                    // 判断属性名字是否需要转译
                    fieldName = StringUtils.hasLength(convert.rename()) ? convert.rename() : fieldName;
                }
                changeRecords.add(ChangeValueLog.builder()
                        .fieldName(fieldName)
                        .newValue(newValue)
                        .oldValue(oldValue).type(type.getOpName()).build()
                        //自动设置用户信息
                        .autoSetBaseInfo(User.builder().userId("1").userName("高鹏").build()));
            }
        }
        return changeRecords;
    }

    /**
     * 枚举值类型转换
     *
     * @param filedTransConvert 转换器
     * @param value             转换前
     * @return 转换后
     */
    private static Object convertEnumValue(FiledTransConvert filedTransConvert, Object value) {
        Class<? extends FieldEnumConverter> converterClazz = filedTransConvert.converter();
        // 如果FieldValueConverter不是默认的转换器，就使用该转换器
        if (converterClazz != FieldEnumConverter.class) {
            // 转换器缓存中获取
            FieldEnumConverter fc = converterCache.get(converterClazz);
            if (fc == null) {
                fc = instantiate(converterClazz);
                converterCache.put(converterClazz, fc);
            }
            // 转换值
            return fc.convert(value);
        }

        Class<? extends Enum> enumClass = filedTransConvert.enumConverter();
        if (enumClass != FiledTransConvert.DefaultEnum.class) {
            return getNameByValue((BaseEnum[]) enumClass.getEnumConstants(), value);
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

    /**
     * 获取所有field字段，包含父类继承的
     *
     * @param clazz       字段所属类型
     * @param fieldFilter 过滤器
     * @return
     */
    public static Field[] getFields(Class<?> clazz, Predicate<Field> fieldFilter) {
        List<Field> fields = new ArrayList<>(64);
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
     * @param field  字段
     * @param target 字段所属实例对象
     * @return 字段值
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

    /**
     * 根据枚举值获取其对应的名字
     *
     * @param enums 枚举列表
     * @param value 枚举值
     * @return 枚举名称
     */
    public static <T> String getNameByValue(BaseEnum<T>[] enums, T value) {
        if (value == null) {
            return null;
        }
        for (BaseEnum<T> e : enums) {
            if (value.equals(e.getValue().toString())) {
                return e.getName();
            }
        }
        return null;
    }


    /**
     * 实例化clazz对象
     *
     * @param clazz    目标clazz
     * @param initArgs 初始化参数
     * @param <T>      对象泛型类
     * @return 对象
     */
    public static <T> T instantiate(Class<T> clazz, Object... initArgs) {
        try {
            Constructor<T> declaredConstructor = clazz.getDeclaredConstructor();
            return declaredConstructor.newInstance(initArgs);
        } catch (Exception e) {
            throw new IllegalStateException(String.format("实例化%s对象失败", clazz.getName()), e);
        }
    }

}
