package pl.mazurek.springboot.repo;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import pl.mazurek.springboot.entity.Payees;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class PayeesRepoTest {

    @Autowired
    private PayeesRepo underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void checkIsPayeesMadeCorrect() {
        //give
        Payees payees = new Payees();
        payees.setName("Mr. Dusty Lord");
        payees.setAddress("669 Witting Ferry Apt. 355, North Dayna");
        payees.setAccountNumber("77799744523903246510731838");

        underTest.save(payees);
        //when
        Optional<Payees> expected = underTest.findPayeesByIdIs("PB_PAYEE_00001");

        //then
        assertThat(expected).contains(payees);
    }

}