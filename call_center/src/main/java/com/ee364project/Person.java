package com.ee364project;

import java.util.ArrayList;

public abstract class Person implements HasData, Simulated {
    private static ArrayList<Person> allPersons = new ArrayList<>();
    private String name;

    Person(String name) {
        this.name = name;
        allPersons.add(this);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
