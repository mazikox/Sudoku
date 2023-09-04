package pl.mazurek.springboot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mazurek.springboot.entity.MyAccount;

@Repository
public interface MyAccountsRepo extends JpaRepository<MyAccount, Long> {

    MyAccount findByAccountNumber(String originatorAccountNumber);
}
