package ru.hw;

import ru.hw.anotations.After;
import ru.hw.anotations.Before;
import ru.hw.anotations.Test;

public class TestExample {

    private ClassForTest classForTest;

    @Before
    public void init() {
        classForTest = new ClassForTest(1);
    }

    //Упадет
    @Test
    public void test1() {
        classForTest.setCount(null);
        classForTest.plus(1);
    }

    @Test
    public void test2() {
        classForTest.setCount(5);
        classForTest.plus(1);
    }

    @Test
    public void test3() {
        classForTest.plus(5);
        classForTest.plus(8);
    }

    @After
    public static void after() {
        //Я не знаю что тут делать, в рамках тестируемого класса
        //какое-то закрывание ресурсов, соединений и тд, которых нет

    }
}
