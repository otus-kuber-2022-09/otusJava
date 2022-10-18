package ru.hw;

import ru.hw.anotations.*;

public class Test {

    private static ClassForTest classForTest;

    @Before
    public static void init() {
        classForTest = new ClassForTest(1);
    }

    //Упадет
    @ru.hw.anotations.Test
    public static void test1() {
        classForTest.setCount(null);
        classForTest.plus(1);
    }

    @ru.hw.anotations.Test
    public static void test2() {
        classForTest.setCount(5);
        classForTest.plus(1);
    }

    @ru.hw.anotations.Test
    public static void test3() {
        System.out.println(classForTest.getCount());
    }

    @After
    public static void after() {
        //Я не знаю что тут делать, в рамках тестируемого класса
        //какое-то закрывание ресурсов, соединений и тд, которых нет

    }
}
