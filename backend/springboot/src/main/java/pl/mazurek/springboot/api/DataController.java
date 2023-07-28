package pl.mazurek.springboot.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.mazurek.springboot.entity.Data;
import pl.mazurek.springboot.entity.DataDto;
import pl.mazurek.springboot.service.DataService;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataController {

    private final DataService dataService;

    @GetMapping("/all")
    public Iterable<Data> findAll() {
        return dataService.findAll();
    }

    @GetMapping("/get")
    public Page<DataDto> pagination(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "100") Integer pageSize,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "0") Long categoryCode) {
        return dataService.find(page, pageSize, sort, categoryCode);
    }


    @GetMapping("/fill")
    public void fillFromJson() {
        dataService.fillFromJson();
    }


}