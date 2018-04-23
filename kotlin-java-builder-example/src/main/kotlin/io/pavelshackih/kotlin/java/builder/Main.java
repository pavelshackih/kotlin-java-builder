package io.pavelshackih.kotlin.java.builder;

public class Main {

    public static void main(String[] args) {
        User user = new UserBuilder()
                .id(1L)
                .name("name")
                .address(
                        new AddressBuilder()
                                .id(1L)
                                .name("street")
                                .build())
                .build();

        System.out.println(user);
    }
}