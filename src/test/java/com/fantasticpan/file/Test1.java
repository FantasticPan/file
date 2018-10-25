package com.fantasticpan.file;

import org.junit.Test;

/**
 * Created by FantasticPan on 2018/10/25.
 */
public class Test1 {

    @Test
    public void test() {
        String s = "文件内容为空 ！,文件大小限制1M ！,文件后缀名有误 ！,提交成功！,提交失败，请与工作人员联系";
        for (String s1 : s.split(",")) {
            System.out.println(s1);
        }
    }
}
