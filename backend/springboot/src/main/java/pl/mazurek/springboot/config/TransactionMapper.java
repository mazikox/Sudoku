package pl.mazurek.springboot.config;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pl.mazurek.springboot.entity.TransactionDto;
import pl.mazurek.springboot.entity.Transactions;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(source = "categoryCode.name", target = "categoryCode")
    @Mapping(target = "date", expression = "java(convertUnixToDate(transactions.getDate()))")
    TransactionDto transactionToTransactionDto(Transactions transactions);
    @Mapping(source = "categoryCode", target = "categoryCode.name")
    @Mapping(target = "date", expression = "java(getCurrentDate())")
    Transactions transactionDtoToTransaction(TransactionDto transactionDto);

    default LocalDate convertUnixToDate(Long unixTimestamp) {
        return LocalDate.ofInstant(Instant.ofEpochMilli(unixTimestamp), ZoneId.systemDefault());
    }

    default long getCurrentDate(){
        return Instant.now().getEpochSecond();
    }

}
