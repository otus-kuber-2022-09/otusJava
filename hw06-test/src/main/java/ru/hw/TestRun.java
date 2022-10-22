package ru.hw;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Все засунул в одну ошибку, чтобы не усложнять. Я знаю что нужно использовать разные ошибки и тд, но в рамках текущей задачи оставлю так, для упрощения.
//Не совсем ясно с фразой "для каждой такой "тройки" надо создать СВОЙ объект класса-теста." Первоначально реализованно через метод before. После добавлено обнуление всех полей через fields. Если это не нужно, можно не учитывать
public class TestRun {

    private static final String beforeConst = "Before";
    private static final String afterConst = "After";
    private static final String testConst = "Test";

    private static Method before;
    private static Method after;
    private static final List<Method> test = new ArrayList<>();
    private static Field[] fields;
    private static Integer successfully = 0;
    private static Integer error = 0;
    private static Object instance;
    //клас в котором производятся тесты
    private static Class<?> clazz;

    public static void startTests(String nameOfClass) throws ClassNotFoundException {
        clazz =  Class.forName(nameOfClass);
        findAnnotationsAndFields();
        runTests();
    }

    private static void createInstance(){

        try {
            instance = clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("Не удалось создать инстанс объекта");
            e.printStackTrace();
        }
    }


    private static void runTests() {
        test.forEach(e -> {
            try {
                createInstance();
                setFields();
                runBefore();
                runTest(e);
                successfully++;
            } catch (Exception ex) {
                System.out.println("Error test: " + e.getName());
                error++;
            } finally {
                try {
                    runAfter();
                } catch (InvocationTargetException | IllegalAccessException exc) {
                    System.out.println("Ну тут мои полномочия все, @After написан не корректно");
                    exc.printStackTrace();
                }
            }

        });
        System.out.println("Success test: " + successfully);
        System.out.println("Error test: " + error);
    }

    private static void runBefore() throws InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        before.invoke(instance);
    }

    private static void runAfter() throws InvocationTargetException, IllegalAccessException {
        after.invoke(instance);
    }

    private static void runTest(Method method) throws InvocationTargetException, IllegalAccessException {
        method.invoke(instance);
    }

    private static void setFields() {
        Arrays.stream(fields).forEach(e -> {
            try {
                e.setAccessible(true);
                e.set(Class.forName(e.getDeclaringClass().getName()).getDeclaredConstructor().newInstance(), null);
            } catch (IllegalAccessException | ClassNotFoundException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
        });
    }

    private static void findAnnotationsAndFields() throws ClassNotFoundException {
        fields = clazz.getDeclaredFields();

        for (Method method : clazz.getDeclaredMethods()) {
            Annotation[] annotations = method.getAnnotations();
            if (annotations.length > 1) {
                throw new ClassNotFoundException("Я не предусматривал возможности нескольких аннотаций");
            }
            Class<? extends Annotation> cl = annotations[0].annotationType();
            switch (cl.getSimpleName()) {
                case (testConst) -> test.add(method);
                case (afterConst) -> {
                    if (after == null) {
                        after = method;
                        break;
                    }
                    throw new ClassNotFoundException("Несколько методов @After");
                }
                case (beforeConst) -> {
                    if (before == null) {
                        before = method;
                        break;
                    }
                    throw new ClassNotFoundException("Несколько методов @Before");
                }
            }

        }
    }
}
