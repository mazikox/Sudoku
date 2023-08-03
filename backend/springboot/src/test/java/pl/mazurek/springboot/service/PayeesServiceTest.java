package pl.mazurek.springboot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pl.mazurek.springboot.entity.Payees;
import pl.mazurek.springboot.repo.PayeesRepo;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PayeesServiceTest {

    @Mock
    private PayeesRepo payeesRepo;
    private PayeesService underTest;

    @BeforeEach
    void setUp() {
        underTest = new PayeesService(payeesRepo);
    }


    @Test
    void canGetAllPayees() {
        PageRequest pr = PageRequest.of(0, 10);
        // when
        underTest.find(0, 10, "");
        //then
        verify(payeesRepo).findAll(pr);
    }

    @Test
    void canGetAllPayeesWithSort() {
        PageRequest pr = PageRequest.of(0, 10, Sort.by("accountNumber"));
        // when
        underTest.find(0, 10, "accountNumber");
        //then
        verify(payeesRepo).findAll(pr);
    }

    @Test
    void canAddPayees() {
        //given
        Payees payees = new Payees(
                "Mr. Dusty Gaylord",
                "669 Witting Ferry Apt. 355, North Dayna",
                "77799744523903246510731838");
        //when
        underTest.addPayees(payees);
        //then
        ArgumentCaptor<Payees> payeesArgumentCaptor = ArgumentCaptor.forClass(Payees.class);

        verify(payeesRepo).save(payeesArgumentCaptor.capture());

        Payees capturedPayee = payeesArgumentCaptor.getValue();

        assertThat(capturedPayee).isEqualTo(payees);
    }

    @Test
    void canAddPayeesArray() {
        //given
        List<Payees> payeesList = List.of(new Payees[]{
                new Payees(
                        "Mr. Dusty Gaylord",
                        "669 Witting Ferry Apt. 355, North Dayna",
                        "77799744523903246510731838"),
                new Payees(
                        "Miss Felicity Wisozk",
                        "6193 Madaline Street, East Hildegard, CO 40469",
                        "07392682329673867600575925"),
                new Payees(
                        "Dr. Tiana Wiza",
                        "Unit 5346 Box 1213, DPO AE 44367",
                        "89930003082193608698823904"
                )
        });
        //when
        underTest.addPayeesArray(payeesList);
        //then
        ArgumentCaptor<List<Payees>> payeesArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(payeesRepo).saveAll(payeesArgumentCaptor.capture());

        List<Payees> capturedPayee = payeesArgumentCaptor.getValue();

        assertThat(capturedPayee).isEqualTo(payeesList);
    }


    @Test
    @Disabled
    void update() {
    }

    @Test
    @Disabled
    void delete() {
    }
}