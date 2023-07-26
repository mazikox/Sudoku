package pl.mazurek.springboot.entity;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

@Service
public class DataDtoMapper implements Function<Data, DataDto> {


    @Override
    public DataDto apply(Data data) {
        return new DataDto(
                data.getId(),
                data.getCategoryCode().getName(),
                new SimpleDateFormat("yyyy-MM-dd").format(new Date(data.getDate())),
                data.getAmount(),
                data.getCurrencyCode(),
                data.getOriginatorAccountNumber(),
                data.getCounterpartyAccount(),
                data.getPaymentType(),
                data.getStatus(),
                data.getTitle()
        );
    }
}
