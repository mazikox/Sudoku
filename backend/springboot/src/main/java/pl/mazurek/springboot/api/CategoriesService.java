package pl.mazurek.springboot.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.mazurek.springboot.entity.Categories;
import pl.mazurek.springboot.entity.Data;
import pl.mazurek.springboot.repo.CategoriesRepo;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class CategoriesService {

    CategoriesRepo categoriesRepo;

    @Autowired
    public CategoriesService(CategoriesRepo categoriesRepo) {
        this.categoriesRepo = categoriesRepo;
    }

    public Iterable<Categories> findAll() {
        return categoriesRepo.findAll();
    }

    public Categories save(Categories categories) {
        return categoriesRepo.save(categories);
    }


    public void fillFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/java/pl/mazurek/springboot/category.json");

        try {
            List<Categories> categories = mapper.readValue(file, new TypeReference<List<Categories>>() {
            });
            for (Categories category : categories) {
                save(category);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
