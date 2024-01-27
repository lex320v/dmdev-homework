package com.example;

public class Service {
    private final static Common common = new Common();

    public static void main(String[] args) {
        serviceMethod();
    }

    public static void serviceMethod() {
        String commonValue = common.commonMethod();

        System.out.println("log from service");
        System.out.println("value from common: " + commonValue);
    }

}
