package pl.mazurek.springboot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.mazurek.springboot.config.CurrencyExchange;
import pl.mazurek.springboot.entity.Currency;
import pl.mazurek.springboot.entity.MyAccount;
import pl.mazurek.springboot.repo.MyAccountsRepo;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MyAccountsService {

    private final MyAccountsRepo myAccountsRepo;
    private final RestTemplate restTemplate;
    Random random = new Random();


    public MyAccount addAccount(MyAccount account) {
        account.setAccountNumber(generateAccountNumber());
        return myAccountsRepo.save(account);
    }

    public Page<MyAccount> findAll(int page, int size) {
        PageRequest pr = PageRequest.of(page, size);
        return myAccountsRepo.findAll(pr);
    }

    public double countAccountBalance(){
        double sum = 0;
        Currency currency = Currency.PLN;

        for (MyAccount myAccount : myAccountsRepo.findAll()) {
            if(myAccount.getCurrency().equals(currency)){
                sum += myAccount.getBalance();
            }
            else{
                sum += myAccount.getBalance() * CurrencyExchange.getCurrencyExchange(myAccount.getCurrency(), currency, restTemplate);
            }
        }
        return (double) Math.round(sum * 100) / 100;
    }

    private String generateAccountNumber() {
        StringBuilder stringBuilder = new StringBuilder();

        int firstNumber = random.nextInt(9) + 1;
        stringBuilder.append(firstNumber);

        for (int i = 0; i < 25; i++){
            int number = random.nextInt(10);
            stringBuilder.append(number);
        }
        return String.valueOf(stringBuilder);
    }
}
