package pl.mazurek.springboot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.mazurek.springboot.entity.Payees;
import pl.mazurek.springboot.repo.PayeesRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PayeesService {

    private final PayeesRepo payeesRepo;
    private static final String IDPREFIX = "PB_PAYEE_";

    public void addPayees(Payees payees) {
        payeesRepo.save(payees);
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
