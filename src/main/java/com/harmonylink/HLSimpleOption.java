package com.harmonylink;

public class HLSimpleOption<T> {
    private T optionValue;

    public HLSimpleOption(T optionValue) {
        this.optionValue = optionValue;
    }

    // Getter method
    public T getValue() {
        return optionValue;
    }

    // Setter method
    public void setValue(T value) {
        optionValue = value;
    }
}
