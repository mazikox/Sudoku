package pl.mazurek.springboot.entity;


import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;


@Entity
@Getter
@Setter
@lombok.Data
@ToString
public class Data {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "category_code_id")
    private Categories categoryCode;
    private Long date;
    private Double amount;
    private String currencyCode;
    private String originatorAccountNumber;
    private String counterpartyAccount;
    private String paymentType;
    private String status;
    private String title;

}
