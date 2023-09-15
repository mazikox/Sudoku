package pl.mazurek.springboot.entity;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
public class TransactionDtDto {
    private Categories categoryCode;
    private Currency currencyCode;
    private Long count;
    private Double sum;
    private Double minAmount;
    private Double maxAmount;
    private Double avgAmount;

}
