package pl.mazurek.springboot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@NoArgsConstructor
@Getter
@Setter
public class Transaction {

    Data[] data;

    @Override
    public String toString() {
        return "Transaction{" +
                "data=" + Arrays.toString(data) +
                '}';
    }
}
