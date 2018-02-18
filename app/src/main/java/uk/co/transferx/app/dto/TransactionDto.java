package uk.co.transferx.app.dto;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import java.math.BigDecimal;
import java.util.List;

import uk.co.transferx.app.pojo.TransactionHistory;

/**
 * Created by sergey on 07/02/2018.
 */

public class TransactionDto {

    private final static String EMPTY = "";

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
