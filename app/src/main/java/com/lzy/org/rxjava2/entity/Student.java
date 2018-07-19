package com.lzy.org.rxjava2.entity;

import java.io.Serializable;

/**
 * 类
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @blog https://blog.csdn.net/u012527802
 * https://github.com/linzhiyong
 * https://www.jianshu.com/u/e664ba5d0800
 * @time 2018/7/17
 * @desc
 */
public class Student implements Serializable {

    private int age;

    private String name;

    public Student(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
