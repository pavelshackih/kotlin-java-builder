package io.pavelshackih.kotlin.java.builder;

public class Main {

    public static void main(String[] args) {
        Bean bean = new BeanBuilder()
                .id(1)
                .name("name")
                .build();
        System.out.println(bean);
    }
}