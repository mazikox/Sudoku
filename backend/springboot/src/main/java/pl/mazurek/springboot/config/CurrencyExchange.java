package pl.mazurek.springboot.config;

import org.springframework.web.client.RestTemplate;
import pl.mazurek.springboot.entity.Currency;

import java.util.Map;

public class CurrencyExchange {

    public static Double getCurrencyExchangeRate(Currency currencyGet, Currency currencyReturn, RestTemplate restTemplate) {
        Map<String, Map<String, Double>> response = (Map<String, Map<String, Double>>) restTemplate.getForObject("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/" + currencyGet.name().toLowerCase() + ".json", Object.class);

        if (response == null) {
            throw new NullPointerException();
        }

        Map<String, Double> responseData = response.get(currencyGet.name().toLowerCase());
        return responseData.get(currencyReturn.name().toLowerCase());
    }
}
