package pl.mazurek.springboot.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.mazurek.springboot.config.TransactionMapper;
import pl.mazurek.springboot.entity.Categories;
import pl.mazurek.springboot.entity.MyAccount;
import pl.mazurek.springboot.entity.TransactionDto;
import pl.mazurek.springboot.entity.Transactions;
import pl.mazurek.springboot.exception.InvalidAmountException;
import pl.mazurek.springboot.repo.MyAccountsRepo;
import pl.mazurek.springboot.repo.TransactionRepo;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static pl.mazurek.springboot.config.CurrencyExchange.getCurrencyExchange;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionsService {

    private final TransactionRepo transactionRepo;
    private final MyAccountsRepo myAccountsRepo;
    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;


    public List<Transactions> findAll() {
        return transactionRepo.findAll();
    }

    public Transactions save(Transactions transaction) {
        if (transaction.getAmount() <= 0) {
            throw new InvalidAmountException();
        }
        MyAccount sender = myAccountsRepo.findByAccountNumber(transaction.getOriginatorAccountNumber());
        MyAccount receiver = myAccountsRepo.findByAccountNumber(transaction.getCounterpartyAccount());

        transaction.setStatus("ENTERED");
        transaction.setDate(Instant.now().getEpochSecond() * 1000);
        double exchangeValue = 1.0;

        if (transaction.getCurrencyCode() != sender.getCurrency()) {
            exchangeValue = getCurrencyExchange(transaction.getCurrencyCode(), sender.getCurrency(), restTemplate);
        }

        double amount = transaction.getAmount() * exchangeValue;
        amount = (double) Math.round(amount * 100) / 100;

        if (sender.getBalance() - amount >= 0) {
            sender.setBalance(sender.getBalance() - amount);
            myAccountsRepo.save(sender);
            if (receiver != null) {
                receiver.setBalance(receiver.getBalance() + amount);
                myAccountsRepo.save(receiver);
            }
            return transactionRepo.save(transaction);
        } else {
            throw new InvalidAmountException();
        }
    }


    public Page<TransactionDto> find(int page, int size, String sort, Long categoryCode) {
        PageRequest pr = PageRequest.of(page, size, Sort.by(sort));
        return categoryCode == 0 ?
                transactionRepo.findAll(pr).map(TransactionMapper.INSTANCE::transactionToTransactionDto) :
                transactionRepo.findByCategoryCode(new Categories(categoryCode, ""), pr).map(TransactionMapper.INSTANCE::transactionToTransactionDto);
    }

    public Optional<Transactions> findById(Long id) {
        return transactionRepo.findById(id);
    }



    public void fillFromJson() {
        File file = new File("backend/springboot/src/main/java/pl/mazurek/springboot/transactions-k.json");

        try {
            JsonNode rootNode = mapper.readTree(file);
            JsonNode dataNode = rootNode.get("data");

            if (dataNode.isArray()) {
                List<Transactions> transactionsList = mapper.readValue(dataNode.toString(), new TypeReference<>() {
                });

                for (Transactions transaction : transactionsList) {
                    save(transaction);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
