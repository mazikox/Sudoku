package pl.mazurek.springboot.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import pl.mazurek.springboot.entity.MyAccounts;
import pl.mazurek.springboot.service.MyAccountsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class MyAccountsController {

    private final MyAccountsService myAccountsService;


    @PostMapping("/add")
    public void addAccount(@RequestBody MyAccounts account){
        myAccountsService.addAccount(account);
    }

    @GetMapping("/get")
    public Page<MyAccounts> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size){
        return myAccountsService.findAll(page, size);
    }
}
