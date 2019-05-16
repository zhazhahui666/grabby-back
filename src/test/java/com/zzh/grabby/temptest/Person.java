package com.zzh.grabby.temptest;

/**
 * @author zzh
 * @date 2019/2/19
 */
public class Person {
    static {
        System.out.println("执行Person静态方法");
    }
    {
        System.out.println("执行person构造代码块");
    }

    public Person() {
        System.out.println("执行person无参构造方法");
    }
    public Person(String name){
        System.out.println("执行person构造方法"+name);
    }
}
