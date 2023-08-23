package pl.mazurek.springboot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pl.mazurek.springboot.entity.Payees;
import pl.mazurek.springboot.repo.PayeesRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static pl.mazurek.springboot.service.PayeesService.IDPREFIX;

class testtests {

    @Mock
    private PayeesRepo payeesRepo;

    private PayeesService payeesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        payeesService = new PayeesService(payeesRepo);
    }

    @Test
    void testAddPayees() {
        Payees payees = new Payees(/* initialize payees object */);

        payeesService.addPayees(payees);

        verify(payeesRepo).save(payees);
    }

    @Test
    void testAddPayeesArray() {
        List<Payees> payeesList = new ArrayList<>();
        payeesList.add(new Payees(/* initialize payees object 1 */));
        payeesList.add(new Payees(/* initialize payees object 2 */));

        payeesService.addPayeesArray(payeesList);

        verify(payeesRepo).saveAll(payeesList);
    }

    @Test
    void testUpdateExistingPayees() {
        String payeesId = "some_id";
        Payees existingPayees = new Payees(/* initialize existing payees object */);
        Payees updatedPayees = new Payees(/* initialize updated payees object */);

        when(payeesRepo.findPayeesByIdIs(payeesId)).thenReturn(Optional.of(existingPayees));

        payeesService.update(payeesId, updatedPayees);

        verify(payeesRepo).save(existingPayees);
    }

    @Test
    void testFindPayees() {
        int page = 0;
        int size = 10;
        String sort = "name";

        Page<Payees> mockPage = mock(Page.class);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sort));

        when(payeesRepo.findAll(pageRequest)).thenReturn(mockPage);

        payeesService.find(page, size, sort);

        verify(payeesRepo).findAll(pageRequest);
    }

    @Test
    void testDeletePayees() {
        String payeesId = "some_id";
        Payees existingPayees = new Payees(/* initialize existing payees object */);

        when(payeesRepo.findPayeesByIdIs(payeesId)).thenReturn(Optional.of(existingPayees));

        payeesService.delete(payeesId);

        verify(payeesRepo).save(existingPayees);
    }

    @Test
    void testFindById() {
        String payeesId = "some_id";
        Payees payees = new Payees(/* initialize payees object */);

        when(payeesRepo.findPayeesByIdIs(IDPREFIX + payeesId)).thenReturn(Optional.of(payees));

        Optional<Payees> result = payeesService.findById(payeesId);

        assertTrue(result.isPresent());
        assertEquals(payees, result.get());
    }
}


