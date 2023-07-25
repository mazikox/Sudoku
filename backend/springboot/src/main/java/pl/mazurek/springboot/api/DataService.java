package pl.mazurek.springboot.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.mazurek.springboot.entity.Data;
import pl.mazurek.springboot.entity.Transaction;
import pl.mazurek.springboot.repo.DataRepo;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class DataService {

    private final DataRepo dataRepo;

    @Autowired
    public DataService(DataRepo dataRepo) {
        this.dataRepo = dataRepo;
    }

    public List<Data> findAll() {
        return dataRepo.findAll();
    }

    public Data save(Data data) {
        return dataRepo.save(data);
    }


    public Page<Data> find(int page, int size, String sort, String categoryCode) {
        PageRequest pr = PageRequest.of(page, size, Sort.by(sort));
        return dataRepo.findByCategoryCode(categoryCode, pr);
    }


    public void fillFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/java/pl/mazurek/springboot/transactions-k.json");

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
