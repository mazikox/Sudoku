package pl.mazurek.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TransactionDtDto {
    private Categories categoryCode;
    private Long count;
    private Double sum;
    private Double minAmount;
    private Double maxAmount;
    private Double avgAmount;

}
