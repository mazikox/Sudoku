package pl.mazurek.springboot.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mazurek.springboot.entity.Categories;
import pl.mazurek.springboot.entity.Data;


@Repository
public interface DataRepo extends JpaRepository<Data,Long> {

    Page<Data> findByCategoryCode(Categories categories, Pageable pageable);


}
