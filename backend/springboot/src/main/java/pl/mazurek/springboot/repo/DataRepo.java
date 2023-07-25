package pl.mazurek.springboot.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.mazurek.springboot.entity.Data;

import java.util.List;


@Repository
public interface DataRepo extends JpaRepository<Data,Long> {

    Page<Data> findByCategoryCode(String categoryCode, Pageable pageable);

}
