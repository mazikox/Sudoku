package pl.mazurek.springboot.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import pl.mazurek.springboot.entity.Categories;
import pl.mazurek.springboot.entity.TransactionDto;
import pl.mazurek.springboot.entity.Transactions;

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
        addTransaction();
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

        assertEquals(transactions.getCategoryCode().getName(), result.getContent().get(0).getCategoryCode());
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

        assertEquals(transactions.getCategoryCode().getName(), result.getContent().get(0).getCategoryCode());
        assertEquals(transactions.getAmount(), result.getContent().get(0).getAmount());
        assertEquals(transactions.getCurrencyCode(), result.getContent().get(0).getCurrencyCode());
        assertEquals(transactions.getCounterpartyAccount(), result.getContent().get(0).getCounterpartyAccount());
        assertEquals(transactions.getTitle(), result.getContent().get(0).getTitle());
        assertEquals(transactions.getOriginatorAccountNumber(), result.getContent().get(0).getOriginatorAccountNumber());
    }


    Transactions addTransaction(){
        Transactions transactions = new Transactions();
        transactions.setCategoryCode(addCategories());
        transactions.setDate(1600387200000L);
        transactions.setAmount(5000D);
        transactions.setCurrencyCode("EUR");
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