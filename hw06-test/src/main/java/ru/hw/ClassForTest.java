package ru.hw;

//Клас, который будем тестировать
public class ClassForTest {

    private Integer count;

    public ClassForTest(Integer count) {
        this.count = count;
    }

    public void plus(Integer num){
        count+=num;
    }
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
