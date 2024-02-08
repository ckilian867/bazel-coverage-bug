package com.repro;

public final class Repro {

    private int someField;

    public Repro(int initialValue) {
        this.someField = initialValue;
    }

    public void setSomeField(int value) {
        this.someField = value;
    }

    public int getSomeField() {
        return this.someField;
    }
}
