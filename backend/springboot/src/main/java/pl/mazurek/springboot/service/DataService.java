package pl.mazurek.springboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.mazurek.springboot.entity.DataDto;
import pl.mazurek.springboot.config.DataMapper;
import pl.mazurek.springboot.entity.*;
import pl.mazurek.springboot.repo.DataRepo;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataService {

    private final DataRepo dataRepo;
    private final ObjectMapper mapper;


    public List<Data> findAll() {
        return dataRepo.findAll();
    }

    public Data save(Data data) {
        return dataRepo.save(data);
    }


    public Page<DataDto> find(int page, int size, String sort, Long categoryCode) {
        PageRequest pr = PageRequest.of(page, size, Sort.by(sort));
        return categoryCode == 0 ?
                dataRepo.findAll(pr).map(DataMapper.INSTANCE::dataToDataDt) :
                dataRepo.findByCategoryCode(new Categories(categoryCode, ""), pr).map(DataMapper.INSTANCE::dataToDataDt);
    }


    public void fillFromJson() {
        File file = new File("backend/springboot/src/main/java/pl/mazurek/springboot/transactions-k.json");

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
