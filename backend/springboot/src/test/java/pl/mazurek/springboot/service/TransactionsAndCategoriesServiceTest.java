package pl.mazurek.springboot.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import pl.mazurek.springboot.entity.Categories;
import pl.mazurek.springboot.entity.Currency;
import pl.mazurek.springboot.entity.TransactionDto;
import pl.mazurek.springboot.entity.Transactions;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TransactionsAndCategoriesServiceTest {

    @Autowired
    TransactionsService transactionsService;
    @Autowired
    CategoriesService categoriesService;

    @Test
    void saveCategories() {
        addCategories();
    }

    @Test
    void saveTransaction() {
        Transactions transactions = addTransaction();

        Optional<Transactions> savedTransaction = transactionsService.findById(transactions.getId());

        assertEquals(transactions.getCategoryCode(), savedTransaction.get().getCategoryCode());
        assertEquals(transactions.getTitle(), savedTransaction.get().getTitle());
        assertEquals(transactions.getAmount(), savedTransaction.get().getAmount());
        assertEquals(transactions.getCurrencyCode(), savedTransaction.get().getCurrencyCode());
        assertEquals(transactions.getCounterpartyAccount(), savedTransaction.get().getCounterpartyAccount());
    }

    @Test
    void findAllTransaction() {
        Transactions transactions = addTransaction();
        Transactions result = transactionsService.findAll().get(0);

        assertEquals(transactions.getCategoryCode(), result.getCategoryCode());
        assertEquals(transactions.getTitle(), result.getTitle());
        assertEquals(transactions.getAmount(), result.getAmount());
        assertEquals(transactions.getCurrencyCode(), result.getCurrencyCode());
        assertEquals(transactions.getCounterpartyAccount(), result.getCounterpartyAccount());
    }

    @Test
    void findAllCategories() {
        Categories categories = addCategories();

        assertEquals(categoriesService.findAll().get(0), categories);
    }


    @Test
    void findTransactionWithoutCategoryCodeWithMapping() {
        Transactions transactions = addTransaction();
        Page<TransactionDto> result = transactionsService.find(0,5,"id",0L);

        assertEquals(transactions.getAmount(), result.getContent().get(0).getAmount());
        assertEquals(transactions.getCurrencyCode(), result.getContent().get(0).getCurrencyCode());
        assertEquals(transactions.getCounterpartyAccount(), result.getContent().get(0).getCounterpartyAccount());
        assertEquals(transactions.getTitle(), result.getContent().get(0).getTitle());
        assertEquals(transactions.getOriginatorAccountNumber(), result.getContent().get(0).getOriginatorAccountNumber());
    }

    @Test
    void findTransactionWithCategoryCodeWithMapping() {
        Transactions transactions = addTransaction();
        Page<TransactionDto> result = transactionsService.find(0,5,"id",1L);

        assertEquals(transactions.getAmount(), result.getContent().get(0).getAmount());
        assertEquals(transactions.getCurrencyCode(), result.getContent().get(0).getCurrencyCode());
        assertEquals(transactions.getCounterpartyAccount(), result.getContent().get(0).getCounterpartyAccount());
        assertEquals(transactions.getTitle(), result.getContent().get(0).getTitle());
        assertEquals(transactions.getOriginatorAccountNumber(), result.getContent().get(0).getOriginatorAccountNumber());
    }


    Transactions addTransaction(){
        Transactions transactions = new Transactions();
        transactions.setAmount(5000D);
        transactions.setCurrencyCode(Currency.EUR);
        transactions.setOriginatorAccountNumber("sdgdsgs");
        transactions.setCounterpartyAccount("AD123124124124");
        transactions.setPaymentType("DOMESTIC");
        transactions.setStatus("ENTERED");
        transactions.setTitle("Title");

        transactionsService.save(transactions);
        return transactions;
    }

    Categories addCategories(){
        Categories categories = new Categories(1L, "Zdrowie i uroda");

        categoriesService.save(categories);
        return categories;
    }
}