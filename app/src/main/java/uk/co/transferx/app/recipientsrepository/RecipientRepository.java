package uk.co.transferx.app.recipientsrepository;

import java.util.List;

import io.reactivex.Single;
import uk.co.transferx.app.dto.RecipientDto;

/**
 * Created by sergey on 15/01/2018.
 */

public interface RecipientRepository {

    Single<List<RecipientDto>> getRecipients();

    void clearRecipients();

    Single<List<RecipientDto>> refreshRecipients();

    void addUser(RecipientDto recipientDto);

    void deleteRecipient(RecipientDto recipientDto);

    void deleteRecipient(final String id);

    void upDateUser(final RecipientDto recipient);

    boolean isShouldRefresh();

    RecipientDto getUserById(final String id);

}
