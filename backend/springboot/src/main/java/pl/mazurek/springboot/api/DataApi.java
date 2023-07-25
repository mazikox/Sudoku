package pl.mazurek.springboot.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import pl.mazurek.springboot.Connect;
import pl.mazurek.springboot.entity.Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/data")
public class DataApi {

    private DataService dataService;

    @Autowired
    public DataApi(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/all")
    public Iterable<Data> findAll() {
        return dataService.findAll();
    }

    @GetMapping("/get")
    public Page<Data> pagination(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "100") Integer size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "1") String categoryCode) {
        return dataService.find(page, size, sort, categoryCode);
    }


    @GetMapping("/fill")
    public void fillFromJson(){
        dataService.fillFromJson();
    }
}