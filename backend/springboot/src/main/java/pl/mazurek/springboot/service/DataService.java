package pl.mazurek.springboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.mazurek.springboot.entity.*;
import pl.mazurek.springboot.repo.DataRepo;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataService {

    private final DataRepo dataRepo;
    private final DataDtoMapper dataDtoMapper;


    public List<Data> findAll() {
        return dataRepo.findAll();
    }

    public Data save(Data data) {
        return dataRepo.save(data);
    }


    public Page<DataDto> find(int page, int size, String sort, Long categoryCode) {
        PageRequest pr = PageRequest.of(page, size, Sort.by(sort));
        if (categoryCode == 0) {
            return dataRepo.findAll(pr).map(dataDtoMapper);
        } else {
            return dataRepo.findByCategoryCode(new Categories(categoryCode, ""), pr).map(dataDtoMapper);
        }
    }


    public void fillFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("springboot/src/main/java/pl/mazurek/springboot/transactions-k.json");

        Transaction transaction = null;

        try {
            transaction = mapper.readValue(file, Transaction.class);
            for (Data datum : transaction.getData()) {
                save(datum);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
