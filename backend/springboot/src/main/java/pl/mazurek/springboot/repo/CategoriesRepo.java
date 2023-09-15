package pl.mazurek.springboot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mazurek.springboot.entity.Categories;

@Repository
public interface CategoriesRepo extends JpaRepository<Categories, Long> {
    Categories findByName(String name);
}
