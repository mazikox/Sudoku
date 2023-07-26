package pl.mazurek.springboot.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mazurek.springboot.entity.Categories;
import pl.mazurek.springboot.service.CategoriesService;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoriesController {

    private final CategoriesService categoriesService;

    @GetMapping("/all")
    public Iterable<Categories> findAll() {
        return categoriesService.findAll();
    }


    @GetMapping("/fill")
    public void fillFromJson() {
        categoriesService.fillFromJson();
    }
}
