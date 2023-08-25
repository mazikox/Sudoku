package pl.mazurek.springboot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.mazurek.springboot.entity.MyAccounts;
import pl.mazurek.springboot.repo.MyAccountsRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyAccountsService {

    private final MyAccountsRepo myAccountsRepo;

    public void addAccount(MyAccounts account){
        myAccountsRepo.save(account);
    }

    public Page<MyAccounts> findAll(int page, int size){
        PageRequest pr = PageRequest.of(page, size);
        return myAccountsRepo.findAll(pr);
    }
}
