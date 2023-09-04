package pl.mazurek.springboot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import pl.mazurek.springboot.config.Config;
import pl.mazurek.springboot.entity.Currency;
import pl.mazurek.springboot.entity.MyAccount;
import pl.mazurek.springboot.repo.MyAccountsRepo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = Config.class)
class MyAccountServiceTest {

    @Mock
    MyAccountsRepo accountsRepo;
    @InjectMocks
    MyAccountsService accountsService;


    @BeforeEach
    void setUp() {

    }


    @Test
    void addAccount(){
        MyAccount account = createAccount();

        when(accountsRepo.save(Mockito.any(MyAccount.class))).thenReturn(account);

        MyAccount result = accountsService.addAccount(account);

        assertNotNull(result);
        assertNotNull(result.getAccountNumber());
        assertEquals(account, result);

        verify(accountsRepo).save(account);
    }

    @Test
    void findAll(){
        Page<MyAccount> accounts = Mockito.mock(Page.class);

        when(accountsRepo.findAll(Mockito.any(Pageable.class))).thenReturn(accounts);

        Page<MyAccount> accountResponse = accountsService.findAll(1,5);
        PageRequest pr = PageRequest.of(1, 5);

        assertNotNull(accountResponse);
        assertEquals(accounts, accountResponse);
        verify(accountsRepo).findAll(pr);
    }

    MyAccount createAccount(){
        MyAccount account = new MyAccount();
        account.setBalance(100);
        account.setName("account1");
        account.setCurrency(Currency.USD);
        return account;
    }
}