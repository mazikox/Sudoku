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
import pl.mazurek.springboot.config.Test;
import pl.mazurek.springboot.config.TransactionMapper;
import pl.mazurek.springboot.entity.*;
import pl.mazurek.springboot.entity.Currency;
import pl.mazurek.springboot.exception.InvalidAmountException;
import pl.mazurek.springboot.repo.CategoriesRepo;
import pl.mazurek.springboot.repo.MyAccountsRepo;
import pl.mazurek.springboot.repo.TransactionRepo;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static pl.mazurek.springboot.config.CurrencyExchange.getCurrencyExchangeRate;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionsService {

    private final TransactionRepo transactionRepo;
    private final MyAccountsRepo myAccountsRepo;
    private final CategoriesRepo categoriesRepo;
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
        double exchangeValueSender = 1.0;
        double exchangeValueReceiver = 1.0;

        if (transaction.getCurrencyCode() != sender.getCurrency()) {
            exchangeValueSender = getCurrencyExchangeRate(transaction.getCurrencyCode(), sender.getCurrency(), restTemplate);
        }

        if (receiver != null && (transaction.getCurrencyCode() != receiver.getCurrency())) {
            exchangeValueReceiver = getCurrencyExchangeRate(transaction.getCurrencyCode(), receiver.getCurrency(), restTemplate);

        }

        double amountSender = transaction.getAmount() * exchangeValueSender;
        amountSender = (double) Math.round(amountSender * 100) / 100;
        double amountReceiver = transaction.getAmount() * exchangeValueReceiver;
        amountReceiver = (double) Math.round(amountReceiver * 100) / 100;

//        Set category by title
        Categories category = findCategoryByTitle(transaction.getTitle());
        transaction.setCategoryCode(category);

        if (sender.getBalance() - amountSender >= 0) {
            sender.setBalance(sender.getBalance() - amountSender);
            myAccountsRepo.save(sender);
            // for existed payee in database
            if (receiver != null) {
                receiver.setBalance(receiver.getBalance() + amountReceiver);
                myAccountsRepo.save(receiver);
            }
            return transactionRepo.save(transaction);
        } else {
            throw new InvalidAmountException();
        }
    }


    public Page<TransactionDto> find(int page, int size, String sort, Long categoryCode) {
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        PageRequest pr = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort));
        return categoryCode == 0 ?
                transactionRepo.findAll(pr).map(TransactionMapper.INSTANCE::transactionToTransactionDto) :
                transactionRepo.findByCategoryCode(new Categories(categoryCode, ""), pr).map(TransactionMapper.INSTANCE::transactionToTransactionDto);
    }

    public Optional<Transactions> findById(Long id) {
        return transactionRepo.findById(id);
    }

    public List<TransactionDtDto> getTransactionsGroupBy(Long dateFrom, Long dateTo) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<TransactionDtDto> transactionList = transactionRepo.groupBy(dateFrom, dateTo);
        Map<String, List<TransactionDtDto>> groupedTransactions = transactionList.stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getCategoryCode().getName()
                ));

        List<TransactionDtDto> result = new ArrayList<>();

        // group by categoryCode with currency change
        for (Map.Entry<String, List<TransactionDtDto>> entry : groupedTransactions.entrySet()) {
            List<TransactionDtDto> transactionEntryList = entry.getValue();
            Categories categoryCode = entry.getValue().get(0).getCategoryCode();
            Currency currencyCode = entry.getValue().get(0).getCurrencyCode();
            Long count = 0L;
            Double sum = 0D;
            Double minAmount = 0D;
            Double maxAmount = 0D;
            Double avgAmount = 0D;

            for (TransactionDtDto transaction : transactionEntryList) {
                count += transaction.getCount();
                if (transaction.getCurrencyCode() != Currency.PLN) {
                    sum += Math.round(transaction.getSum() * getCurrencyExchangeRate(transaction.getCurrencyCode(), Currency.PLN, restTemplate) * 100.0) / 100.0;
                } else {
                    sum += transaction.getSum();
                }
            }
            TransactionDtDto transactionAnalytics = TransactionDtDto.builder()
                    .categoryCode(categoryCode)
                    .currencyCode(currencyCode)
                    .count(count)
                    .sum(sum)
                    .minAmount(minAmount)
                    .maxAmount(maxAmount)
                    .avgAmount(avgAmount)
                    .build();
            result.add(transactionAnalytics);
        }

        return result;
    }

    private Categories findCategoryByTitle(String title){
        try {
            String categoryName = Test.genereteCategoryByTitle(title);
            categoryName = categoryName.substring(0,1).toUpperCase() + categoryName.substring(1);
            categoryName = categoryName.replace("_", " ");
            return categoriesRepo.findByName(categoryName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
