package uk.co.transferx.app.dto;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import java.math.BigDecimal;
import java.util.List;

import uk.co.transferx.app.pojo.TransactionHistory;

import static uk.co.transferx.app.util.Constants.EMPTY;

/**
 * Created by sergey on 07/02/2018.
 */

public class TransactionDto {

    public TransactionDto(TransactionHistory transactionHistory, List<RecipientDto> recipients) {
        id = transactionHistory.getId();
        recipientId = transactionHistory.getTo();
        currency = transactionHistory.getCurrency();
        amount = new BigDecimal(transactionHistory.getAmount());
        created = transactionHistory.getCreated();
        updated = transactionHistory.getUpdated();
        status = transactionHistory.getStatus();
        Optional<RecipientDto> recipient = Stream.of(recipients).filter(recip -> recip.getId().equals(recipientId)).findFirst();
        recipientName = recipient.isPresent() ? recipient.get().getFavoriteName() : EMPTY;
    }

    public TransactionDto(int id, String recipientId, String recipientName, String currency, BigDecimal amount, long created, long updated, String status) {
        this.id = id;
        this.recipientId = recipientId;
        this.recipientName = recipientName;
        this.currency = currency;
        this.amount = amount;
        this.created = created;
        this.updated = updated;
        this.status = status;
    }

    private final int id;
    private final String recipientId;
    private final String recipientName;
    private final String currency;
    private final BigDecimal amount;
    private final long created;
    private final long updated;
    private final String status;


    public int getId() {
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

    public long getCreated() {
        return created;
    }

    public long getUpdated() {
        return updated;
    }

    public String getStatus() {
        return status;
    }
}
