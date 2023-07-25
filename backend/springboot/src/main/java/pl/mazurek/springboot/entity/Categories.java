package pl.mazurek.springboot.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Categories {

    @Id
    private Long categoryCodeId;
    private String name;
}
