package pl.mazurek.springboot.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.server.ResponseStatusException;
import pl.mazurek.springboot.entity.Payees;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PayeesServiceTest {

    @Autowired
    private PayeesService payeesService;

    @AfterEach
    void tearDown() {
    }

    @Test
    void isNameValid() {
        Payees payees = addUser();
        Page<Payees> pagePayee = payeesService.find(0, 1, "id");
        Payees foundPayee = pagePayee.getContent().get(0);

        assertThat(foundPayee).isEqualTo(payees);
    }


    @Test
    void isNameTooShortOrLong() {
        Payees payees = new Payees(
                "Mr",
                "669 Witting Ferry Apt. 355, North Dayna",
                "77799744523903246510731838");

        assertThrows(ResponseStatusException.class, () -> payeesService.addPayees(payees));

        Payees payees2 = new Payees(
                "Mrgdsgsdgsdgsdgsdgsdgsddgsdgsdgsdgsd",
                "669 Witting Ferry Apt. 355, North Dayna",
                "77799744523903246510731838");

        assertThrows(ResponseStatusException.class, () -> payeesService.addPayees(payees2));
    }

    @Test
    void addWithoutNecessaryField() {
        Payees payees = new Payees(null,
                "669 Witting Ferry Apt. 355, North Dayna",
                "77799744523903246510731838");
        assertThrows(ResponseStatusException.class, () -> payeesService.addPayees(payees));

        Payees payees2 = new Payees("Mr World",
                null,
                "77799744523903246510731838");
        assertThrows(ResponseStatusException.class, () -> payeesService.addPayees(payees2));

        Payees payees3 = new Payees("Mr World",
                "669 Witting Ferry Apt. 355, North Dayna",
                null);
        assertThrows(ResponseStatusException.class, () -> payeesService.addPayees(payees3));

    }


    @Test
    void find() {
        Payees payees = addUser();
        Page<Payees> result = payeesService.find(0, 5, "");

        assertEquals(payees.getName(), result.getContent().get(0).getName());
    }

    @Test
    void findWithSort() {
        Payees payees = addUser();
        Page<Payees> result = payeesService.find(0, 5, "name");

        assertEquals(payees.getName(), result.getContent().get(0).getName());
    }

    @Test
    void isPayeeDeleted() {
        addUser();
        payeesService.delete("1");
        payeesService.findById("1").ifPresent(result -> {
            assertFalse(result.isActive());
        });
    }

    @Test
    void isPayeeActive() {
        addUser();
        Page<Payees> result = payeesService.find(0, 5, "");
        assertTrue(result.getContent().get(0).isActive());
    }

    @Test
    void isUpdating() {
        addUser();
        Payees newPayee = new Payees("Mr. Amari Kunde",
                "6690 Kshlerin Shoal, Port Lelandside, OR 03408",
                "72503274082283365628773110");
        payeesService.update("1", newPayee);
        payeesService.findById("1").ifPresent(foundPayee -> {
            assertEquals(newPayee.getName(), foundPayee.getName());
            assertEquals(newPayee.getAddress(), payeesService.findById("1").get().getAddress());
            assertEquals(newPayee.getAccountNumber(), payeesService.findById("1").get().getAccountNumber());
            assertEquals(newPayee.isActive(), payeesService.findById("1").get().isActive());
        });

    }


    Payees addUser() {
        Payees payees = new Payees(
                "Mr World",
                "669 Witting Ferry Apt. 355, North Dayna",
                "77799744523903246510731838");

        payeesService.addPayees(payees);
        return payees;
    }
}