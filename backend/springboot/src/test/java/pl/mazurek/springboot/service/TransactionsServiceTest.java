package pl.mazurek.springboot.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import pl.mazurek.springboot.entity.Categories;
import pl.mazurek.springboot.entity.Currency;
import pl.mazurek.springboot.entity.MyAccount;
import pl.mazurek.springboot.entity.Transactions;
import pl.mazurek.springboot.exception.InvalidAmountException;
import pl.mazurek.springboot.repo.MyAccountsRepo;
import pl.mazurek.springboot.repo.TransactionRepo;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionsServiceTest {

    @Mock
    TransactionRepo transactionRepo;
    @Mock
    MyAccountsRepo myAccountsRepo;
    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    TransactionsService transactionsService;

    @AfterEach
    void tearDown() {
        reset(transactionRepo, myAccountsRepo, restTemplate);
    }

    @AfterAll
    static void afterAll() {
    }

    @Test
    void save() {
        Transactions transaction = Transactions.builder()
                .amount(100.0)
                .categoryCode(new Categories(1L, ""))
                .counterpartyAccount("89930003082193608698823904")
                .originatorAccountNumber("72503274082283365628773110")
                .currencyCode(Currency.PLN)
                .title("title")
                .status("accepted")
                .paymentType("card")
                .build();

        MyAccount myAccount = MyAccount.builder()
                .balance(100)
                .name("testest")
                .currency(Currency.PLN)
                .build();

        when(transactionRepo.save(any(Transactions.class))).thenReturn(transaction);
        when(myAccountsRepo.findByAccountNumber(anyString())).thenReturn(myAccount);

        Transactions result = transactionsService.save(transaction);

        assertNotNull(result);
        assertEquals(transaction, result);

        verify(transactionRepo).save(transaction);
        verify(myAccountsRepo, times(2)).save(any(MyAccount.class));
    }

    @Test
    void saveWithDifferentCurrency() {
        Transactions transaction = Transactions.builder()
                .amount(10.0)
                .categoryCode(new Categories(1L, ""))
                .counterpartyAccount("89930003082193608698823904")
                .originatorAccountNumber("72503274082283365628773110")
                .currencyCode(Currency.EUR)
                .title("title")
                .status("accepted")
                .paymentType("card")
                .build();

        MyAccount myAccount = MyAccount.builder()
                .balance(100)
                .name("testest")
                .currency(Currency.PLN)
                .build();

        Map<String, Double> exchangeRates = new HashMap<>();
        exchangeRates.put(Currency.PLN.name().toLowerCase(), 4.5);

        when(transactionRepo.save(any(Transactions.class))).thenReturn(transaction);
        when(myAccountsRepo.findByAccountNumber(anyString())).thenReturn(myAccount);
        when(restTemplate.getForObject(anyString(), eq(Object.class))).thenReturn(Map.of(Currency.EUR.name().toLowerCase(), exchangeRates));

        Transactions result = transactionsService.save(transaction);

        assertNotNull(result);
        assertEquals(transaction, result);

        verify(transactionRepo).save(transaction);
        verify(myAccountsRepo, times(2)).save(any(MyAccount.class));
    }

    @Test
    void saveWithoutEnoughMoney(){
        Transactions transaction = Transactions.builder()
                .amount(150.0)
                .counterpartyAccount("89930003082193608698823904")
                .originatorAccountNumber("72503274082283365628773110")
                .currencyCode(Currency.PLN)
                .build();

        MyAccount myAccount = MyAccount.builder()
                .balance(100)
                .name("testest")
                .currency(Currency.PLN)
                .build();

        when(myAccountsRepo.findByAccountNumber(anyString())).thenReturn(myAccount);

        assertThrows(InvalidAmountException.class, () -> transactionsService.save(transaction));
    }

    @Test
    void saveWithoutEnoughMoneyWithDifferentCurrency(){
        Transactions transaction = Transactions.builder()
                .amount(50.0)
                .counterpartyAccount("89930003082193608698823904")
                .originatorAccountNumber("72503274082283365628773110")
                .currencyCode(Currency.EUR)
                .build();

        MyAccount myAccount = MyAccount.builder()
                .balance(100)
                .name("testest")
                .currency(Currency.PLN)
                .build();

        Map<String, Double> exchangeRates = new HashMap<>();
        exchangeRates.put(Currency.PLN.name().toLowerCase(), 4.6);

        when(myAccountsRepo.findByAccountNumber(anyString())).thenReturn(myAccount);
        when(restTemplate.getForObject(anyString(), eq(Object.class))).thenReturn(Map.of(Currency.EUR.name().toLowerCase(), exchangeRates));

        assertThrows(InvalidAmountException.class, () -> transactionsService.save(transaction));
    }

    @Test
    void saveWithNegativeAmount(){
        Transactions transaction = Transactions.builder()
                .amount(-150.0)
                .build();

        Transactions transaction2 = Transactions.builder()
                .amount(0.0)
                .build();


        assertThrows(InvalidAmountException.class, () -> transactionsService.save(transaction));
        assertThrows(InvalidAmountException.class, () -> transactionsService.save(transaction2));
    }
}