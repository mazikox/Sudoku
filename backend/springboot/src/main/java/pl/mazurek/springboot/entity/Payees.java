package pl.mazurek.springboot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import pl.mazurek.springboot.config.IdPrefixedGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
public class Payees {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_generator")
    @GenericGenerator(name = "id_generator",
            strategy = "pl.mazurek.springboot.config.IdPrefixedGenerator",
            parameters = {
                    @Parameter(name = IdPrefixedGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = IdPrefixedGenerator.VALUE_PREFIX_PARAMETER, value = "PB_PAYEE_"),
                    @Parameter(name = IdPrefixedGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d")
            })
    private String id;
    @Size(min = 5, max = 30)
    @NotNull
    private String name;
    @Size(min = 10, max = 100)
    @NotNull
    private String address;
    @Size(min = 26, max = 26)
    @NotNull
    private String accountNumber;
    private boolean active = true;

    public Payees(String name, String address, String accountNumber) {
        this.name = name;
        this.address = address;
        this.accountNumber = accountNumber;
    }
}