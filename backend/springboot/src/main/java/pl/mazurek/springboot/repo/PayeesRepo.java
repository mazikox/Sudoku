package pl.mazurek.springboot.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mazurek.springboot.entity.Payees;

import java.util.Optional;

@Repository
public interface PayeesRepo extends JpaRepository<Payees, Long> {

    Optional<Payees> findPayeesByIdIs(String id);

    Page<Payees> findPayeesByActive(PageRequest pq, boolean isActive);
}
