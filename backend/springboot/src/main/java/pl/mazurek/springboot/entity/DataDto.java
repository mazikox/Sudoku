package pl.mazurek.springboot.entity;

import lombok.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataDto {

    private Long id;
    private String categoryCode;
    private LocalDate date;
    private Double amount;
    private String currencyCode;
    private String originatorAccountNumber;
    private String counterpartyAccount;
    private String paymentType;
    private String status;
    private String title;
}
