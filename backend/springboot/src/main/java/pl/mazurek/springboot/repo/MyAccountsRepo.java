package pl.mazurek.springboot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mazurek.springboot.entity.MyAccounts;

public interface MyAccountsRepo extends JpaRepository<MyAccounts, Long> {
}
