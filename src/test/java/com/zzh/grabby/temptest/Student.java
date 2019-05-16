package com.zzh.grabby.temptest;

/**
 * @author zzh
 * @date 2019/2/19
 */
public class Student extends Person {
    static {
        System.out.println("执行student静态代码块");
    }
    {
        System.out.println("执行student构造代码块");
    }

    public Student() {
        super();
        System.out.println("执行student无参构造方法");
    }

    public Student(String name) {
        super(name);
        System.out.println("执行student构造方法");
    }
}
