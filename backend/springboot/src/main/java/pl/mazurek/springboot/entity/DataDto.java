package pl.mazurek.springboot.entity;

public record DataDto(
        Long id,
        String categoryCode,
        String date,
        Double amount,
        String currencyCode,
        String originatorAccountNumber,
        String counterpartyAccount,
        String paymentType,
        String status,
        String title
) {
}
