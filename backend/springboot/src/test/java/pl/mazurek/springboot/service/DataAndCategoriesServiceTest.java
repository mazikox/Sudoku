package pl.mazurek.springboot.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import pl.mazurek.springboot.entity.Categories;
import pl.mazurek.springboot.entity.Data;
import pl.mazurek.springboot.entity.DataDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DataAndCategoriesServiceTest {

    @Autowired
    DataService dataService;
    @Autowired
    CategoriesService categoriesService;

    @Test
    void saveCategories() {
        addCategories();
    }

    @Test
    void saveData() {
        addDatA();
    }

    @Test
    void findAllData() {
        Data data = addDatA();
        Data result = dataService.findAll().get(0);

        assertEquals(data.getCategoryCode(), result.getCategoryCode());
        assertEquals(data.getTitle(), result.getTitle());
        assertEquals(data.getAmount(), result.getAmount());
        assertEquals(data.getCurrencyCode(), result.getCurrencyCode());
        assertEquals(data.getCounterpartyAccount(), result.getCounterpartyAccount());
    }

    @Test
    void findAllCategories() {
        Categories categories = addCategories();

        assertEquals(categoriesService.findAll().get(0), categories);
    }


    @Test
    void findDataWithoutCategoryCodeWithMapping() {
        Data data = addDatA();
        Page<DataDto> result = dataService.find(0,5,"id",0L);

        assertEquals(data.getCategoryCode().getName(), result.getContent().get(0).getCategoryCode());
        assertEquals(data.getAmount(), result.getContent().get(0).getAmount());
        assertEquals(data.getCurrencyCode(), result.getContent().get(0).getCurrencyCode());
        assertEquals(data.getCounterpartyAccount(), result.getContent().get(0).getCounterpartyAccount());
        assertEquals(data.getTitle(), result.getContent().get(0).getTitle());
        assertEquals(data.getOriginatorAccountNumber(), result.getContent().get(0).getOriginatorAccountNumber());
    }

    @Test
    void findDataWithCategoryCodeWithMapping() {
        Data data = addDatA();
        Page<DataDto> result = dataService.find(0,5,"id",1L);

        assertEquals(data.getCategoryCode().getName(), result.getContent().get(0).getCategoryCode());
        assertEquals(data.getAmount(), result.getContent().get(0).getAmount());
        assertEquals(data.getCurrencyCode(), result.getContent().get(0).getCurrencyCode());
        assertEquals(data.getCounterpartyAccount(), result.getContent().get(0).getCounterpartyAccount());
        assertEquals(data.getTitle(), result.getContent().get(0).getTitle());
        assertEquals(data.getOriginatorAccountNumber(), result.getContent().get(0).getOriginatorAccountNumber());
    }


    Data addDatA(){
        Data data = new Data();
        data.setCategoryCode(addCategories());
        data.setDate(1600387200000L);
        data.setAmount(5000D);
        data.setCurrencyCode("EUR");
        data.setOriginatorAccountNumber("sdgdsgs");
        data.setCounterpartyAccount("AD123124124124");
        data.setPaymentType("DOMESTIC");
        data.setStatus("ENTERED");
        data.setTitle("Title");

        dataService.save(data);
        return data;
    }

    Categories addCategories(){
        Categories categories = new Categories(1L, "Zdrowie i uroda");

        categoriesService.save(categories);
        return categories;
    }
}