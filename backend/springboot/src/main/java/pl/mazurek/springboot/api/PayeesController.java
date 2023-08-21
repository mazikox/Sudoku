package pl.mazurek.springboot.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import pl.mazurek.springboot.entity.Payees;
import pl.mazurek.springboot.service.PayeesService;

import java.util.List;

@RestController
@RequestMapping("/payees")
@RequiredArgsConstructor
public class PayeesController {

    private final PayeesService payeesService;

    @PostMapping("/add")
    public void addPayees(@RequestBody Payees payees) {
        payeesService.addPayees(payees);
    }

    @PostMapping("/addArray")
    public void addPayeesArray(@RequestBody List<Payees> payees){
        payeesService.addPayeesArray(payees);
    }

    @PutMapping("/update/{id}")
    public void update(@PathVariable String id, @RequestBody Payees payees) {
        payeesService.update(id, payees);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        payeesService.delete(id);
    }

    @GetMapping("/get")
    public Page<Payees> findAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "100") Integer pageSize,
            @RequestParam(defaultValue = "") String sort) {
        return payeesService.find(page, pageSize, sort);
    }

}