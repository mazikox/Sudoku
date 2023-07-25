package pl.mazurek.springboot.entity;

import java.util.Arrays;

public class Transaction {

    Data data[];

    public Transaction() {
    }

    public Data[] getData() {
        return data;
    }

    public void setData(Data[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "data=" + Arrays.toString(data) +
                '}';
    }
}
