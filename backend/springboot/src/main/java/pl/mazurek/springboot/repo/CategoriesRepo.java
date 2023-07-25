package pl.mazurek.springboot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mazurek.springboot.entity.Categories;

public interface CategoriesRepo extends JpaRepository<Categories, Long> {
}
