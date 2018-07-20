package uk.co.transferx.app.dto;

import java.math.BigDecimal;

import uk.co.transferx.app.pojo.Transaction;

/**
 * Created by sergey on 07/02/2018.
 */

public class TransactionDto {

    public TransactionDto(Transaction transaction) {
        this.id = transaction.getId();
        this.recipientId = transaction.getTo();
        this.recipientName = String.format("%s %s", transaction.getMeta().getRecipientInfo().getFirstName(),
                transaction.getMeta().getRecipientInfo().getLastName());
        this.currency = transaction.getCurrency();
        this.amount = new BigDecimal(transaction.getAmount());
        this.created = transaction.getCreated();
        this.updated = transaction.getUpdated();
        this.status = transaction.getStatus();

    }

    public TransactionDto(String id, String recipientId, String recipientName, String currency, BigDecimal amount, String created, String updated, String status) {
        this.id = id;
        this.recipientId = recipientId;
        this.recipientName = recipientName;
        this.currency = currency;
        this.amount = amount;
        this.created = created;
        this.updated = updated;
        this.status = status;
    }

    private final String id;
    private final String recipientId;
    private final String recipientName;
    private final String currency;
    private final BigDecimal amount;
    private final String created;
    private final String updated;
    private final String status;


    public String getId() {
        return id;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCreated() {
        return created;
    }

    public String getUpdated() {
        return updated;
    }

    public String getStatus() {
        return status;
    }
}
