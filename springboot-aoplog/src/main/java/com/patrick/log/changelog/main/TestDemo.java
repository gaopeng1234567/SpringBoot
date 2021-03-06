package com.patrick.log.changelog.main;

import com.alibaba.fastjson.JSON;
import com.patrick.log.changelog.model.ChangeValueLog;
import com.patrick.log.changelog.model.Task;
import com.patrick.log.changelog.utils.FiledChangeValueUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author patrick
 * @date 2021/3/15 上午10:33
 * @Des 测试
 * 最簡單的事是堅持，最難的事還是堅持
 */
public class TestDemo {

    public static void main(String[] args) {
        HashMap<String, String> demo = new HashMap<>();
        demo.put("path","fdev-reou/板块/其");
        demo.put("name","【测试报告】_dsdadad");
        demo.put("type","投产类-变更材料类");

        HashMap<String, String> demo3 = new HashMap<>();
        demo3.put("path","fdev-reou/板块/其");
        demo3.put("name","【测试报告】_dsdadad");
        demo3.put("type","投产类-变更材料类");

        HashMap<String, String> demo1 = new HashMap<>();
        demo1.put("path","fdev-reou/板块/其123213");
        demo1.put("name","需求说明书");
        demo1.put("type","投产类-变更材料类");
        List list = new ArrayList<>();
        list.add(demo);
        list.add(demo1);
//        System.out.println(JSON.toJSONString(list, true));


        Task newTask = new Task();
        newTask.setNewDoc(list);

        Task oldTask = new Task();

        List list1 = new ArrayList<>();
        list1.add(demo3);

        oldTask.setId("2");
        oldTask.setName("上线任务");
        oldTask.setDesc("状态修改");
        oldTask.setNewDoc(list1);

        List<ChangeValueLog> fieldValueChangeRecords = FiledChangeValueUtils.getFieldValueChangeRecords(newTask, oldTask);
        System.out.println(JSON.toJSONString(fieldValueChangeRecords, true));
    }
}
