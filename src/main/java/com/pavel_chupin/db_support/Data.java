package com.pavel_chupin.db_support;

import java.io.Serializable;

public class Data implements Serializable {
    private long value;

    public Data() {
    }

    public Data(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Data{" +
                "value=" + value +
                '}';
    }
}
