package com.practice.elliot;

import java.util.HashMap;

public class test {
    public static void main(String[] args) {
        // Contacts.setupDirectory("/data");
        HashMap<String, Contact> myMap = Contacts.getAllFiles("/data");
        for(String key : myMap.keySet()) {
            System.out.println(key);
            System.out.println(myMap.get(key));
        }
    }
}
