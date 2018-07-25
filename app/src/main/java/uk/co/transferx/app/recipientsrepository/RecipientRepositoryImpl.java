package uk.co.transferx.app.recipientsrepository;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.Observable;
import io.reactivex.Single;
import uk.co.transferx.app.api.RecipientsApi;
import uk.co.transferx.app.dto.RecipientDto;
import uk.co.transferx.app.errors.UnauthorizedException;
import uk.co.transferx.app.tokenmanager.TokenManager;

/**
 * Created by sergey on 15/01/2018.
 */

public class RecipientRepositoryImpl implements RecipientRepository {

    private final RecipientsApi recipientsApi;
    private List<RecipientDto> recipientDtos = new ArrayList<>();
    private final TokenManager tokenManager;

    @Inject
    public RecipientRepositoryImpl(final RecipientsApi recipientsApi, final TokenManager tokenManager) {
        this.recipientsApi = recipientsApi;
        this.tokenManager = tokenManager;
    }

    @Override
    public Single<List<RecipientDto>> getRecipients() {
        if (!recipientDtos.isEmpty()) {
            return Single.just(recipientDtos);
        }
        return getFromServer();
    }

    @Override
    public void clearRecipients() {
        recipientDtos.clear();
    }

    private Single<List<RecipientDto>> getFromServer() {
        return tokenManager.getToken()
                .flatMap(token -> recipientsApi.getRecipients(token.getAccessToken()))
                .flatMapObservable(res -> {
                    if (res.code() == HttpsURLConnection.HTTP_OK) {
                        return Observable.fromIterable(res.body().getResipients());
                    }
                    return Observable.error(new UnauthorizedException());
                })
                .map(RecipientDto::new)
                .toList()
                .doAfterSuccess(recipients -> this.recipientDtos.addAll(recipients));
    }

    @Override
    public Single<List<RecipientDto>> refreshRecipients() {
        recipientDtos.clear();
        return getFromServer();
    }

    @Override
    public void addUser(RecipientDto recipientDto) {
        recipientDtos.add(recipientDto);
    }

    @Override
    public void deleteRecipient(RecipientDto recipientDto) {
        recipientDtos.remove(recipientDto);
    }

    @Override
    public void deleteRecipient(final String id) {
        final RecipientDto recipient = Stream.of(recipientDtos)
                .filter(rec -> id.equals(rec.getId()))
                .findFirst()
                .get();
        if (recipient != null) {
            recipientDtos.remove(recipient);
        }
    }

    @Override
    public void upDateUser(final RecipientDto recipient) {
        final RecipientDto oldRecipient = Stream.of(recipientDtos)
                .filter(rec -> recipient.getId().equals(rec.getId()))
                .findFirst()
                .get();

        if (oldRecipient != null) {
            recipientDtos.set(recipientDtos.indexOf(oldRecipient), recipient);
        }
    }

    @Override
    public RecipientDto getUserById(String id) {
        return Stream.of(recipientDtos).filter(res -> id.equals(res.getId())).findFirst().get();
    }
}
