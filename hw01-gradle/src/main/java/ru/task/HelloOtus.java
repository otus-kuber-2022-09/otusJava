package ru.task;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class HelloOtus {
    public static void main(String[] args) {
        Multiset<String> words = HashMultiset.create();
        words.add("Hello");
        words.add("world!");
        for (String word : words) {
            System.out.println(word);
        }
    }
}
