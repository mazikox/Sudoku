package pl.mazurek.springboot.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.mazurek.springboot.config.TransactionMapper;
import pl.mazurek.springboot.entity.Categories;
import pl.mazurek.springboot.entity.TransactionDto;
import pl.mazurek.springboot.entity.Transactions;
import pl.mazurek.springboot.repo.TransactionRepo;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionsService {

    private final TransactionRepo transactionRepo;
    private final ObjectMapper mapper;


    public List<Transactions> findAll() {
        return transactionRepo.findAll();
    }

    public void save(Transactions transactions) {
        transactions.setStatus("ENTERED");
        transactions.setDate(Instant.now().getEpochSecond() * 1000);
        transactionRepo.save(transactions);
    }


    public Page<TransactionDto> find(int page, int size, String sort, Long categoryCode) {
        PageRequest pr = PageRequest.of(page, size, Sort.by(sort));
        return categoryCode == 0 ?
                transactionRepo.findAll(pr).map(TransactionMapper.INSTANCE::transactionToTransactionDto) :
                transactionRepo.findByCategoryCode(new Categories(categoryCode, ""), pr).map(TransactionMapper.INSTANCE::transactionToTransactionDto);
    }

    public Optional<Transactions> findById(Long id){
        return transactionRepo.findById(id);
    }

    public void add(Transactions transaction){
        transactionRepo.save(transaction);
    }


    public void fillFromJson() {
        File file = new File("backend/springboot/src/main/java/pl/mazurek/springboot/transactions-k.json");

        try {
            JsonNode rootNode = mapper.readTree(file);
            JsonNode dataNode = rootNode.get("data");

            if (dataNode.isArray()) {
                List<Transactions> transactionsList = mapper.readValue(dataNode.toString(), new TypeReference<>() {
                });

                for (Transactions transaction : transactionsList) {
                    save(transaction);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
