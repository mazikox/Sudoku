package pl.mazurek.springboot.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mazurek.springboot.entity.Categories;
import pl.mazurek.springboot.entity.Data;

@RestController
@RequestMapping("/categories")
public class CategoriesApi {

    CategoriesService categoriesService;

    @Autowired
    public CategoriesApi(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping("/all")
    public Iterable<Categories> findAll(){
        return categoriesService.findAll();
    }


    @GetMapping("/fill")
    public void fillFromJson(){
        categoriesService.fillFromJson();
    }
}
