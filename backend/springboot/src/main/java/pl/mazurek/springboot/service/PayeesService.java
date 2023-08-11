package pl.mazurek.springboot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.server.ResponseStatusException;
import pl.mazurek.springboot.entity.Payees;
import pl.mazurek.springboot.repo.PayeesRepo;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class PayeesService {

    private final PayeesRepo payeesRepo;
    private static final String IDPREFIX = "PB_PAYEE_";

    private static final AtomicInteger COUNTER = new AtomicInteger(0);



    public void addPayees(Payees payees) {
        try {
            payeesRepo.save(payees);
        }catch (TransactionSystemException e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong body", e);
        }
    }

    public void addPayeesArray(List<Payees> payees) {
        payeesRepo.saveAll(payees);
    }

    public void update(String id, Payees payees) {
        Optional<Payees> optionalPayees = payeesRepo.findPayeesByIdIs(IDPREFIX + id);

        if (optionalPayees.isPresent()) {
            Payees existingPayees = optionalPayees.orElseThrow();
            if (payees.getName() != null)
                existingPayees.setName(payees.getName());
            if (payees.getAddress() != null)
                existingPayees.setAddress(payees.getAddress());
            if (payees.getAccountNumber() != null)
                existingPayees.setAccountNumber(payees.getAccountNumber());
            payeesRepo.save(existingPayees);
        }
    }

    public Page<Payees> find(int page, int size, String sort) {
        PageRequest pr = sort.equals("") ? PageRequest.of(page, size) : PageRequest.of(page, size, Sort.by(sort));
        return payeesRepo.findAll(pr);
    }

    public void delete(String id) {
        Optional<Payees> optionalPayees = payeesRepo.findPayeesByIdIs(IDPREFIX + id);
        if (optionalPayees.isPresent()) {
            Payees existingPayees = optionalPayees.get();
            existingPayees.setActive(false);
            payeesRepo.save(existingPayees);
        }
    }

    public Optional<Payees> findById(String id) {
        return payeesRepo.findPayeesByIdIs(IDPREFIX + id);
    }

}
