package com.lzy.org.rxjava2.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class Teacher implements Serializable {

    private String course;

    private String name;

    private List<Student> studentList;

    public Teacher(String course, String name) {
        this.course = course;
        this.name = name;
        this.studentList = new ArrayList<>();
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }
}
