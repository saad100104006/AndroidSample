package uk.co.transferx.app.recipientsrepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import uk.co.transferx.app.api.RecipientsApi;
import uk.co.transferx.app.dto.RecipientDto;
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
      //  generateData();
    }

//Stub should be deleted when server is up.
    private void generateData() {
        recipientDtos.add(new RecipientDto("id1", "Sergey Milewski", null, "PL", "+12345678"));
        recipientDtos.add(new RecipientDto("id2", "Ekrem Karatas", null, "TR", "+12345678"));
        recipientDtos.add(new RecipientDto("id3", "Evangelos Pappas", null, "GB", "+12345678"));
        recipientDtos.add(new RecipientDto("id4", "Windsor Kitaka", null, "GB", "+12345678"));
        recipientDtos.add(new RecipientDto("id5", "Paulo Chaves", null, "BR", "+12345678"));
        recipientDtos.add(new RecipientDto("id6", "Super Man", null, "UN", "+12345678"));
        recipientDtos.add(new RecipientDto("id7", "Zili Boba", null, "MU", "+12345678"));
        recipientDtos.add(new RecipientDto("id8", "Rembo Rembo", null, "ZX", "+12345678"));
        recipientDtos.add(new RecipientDto("id9", "Tom Papacha", null, "XU", "+12345678"));

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
        return recipientsApi.getRecipients(tokenManager.getToken())
                .flatMap(resp -> Observable.fromIterable(resp.body()))
                .map(RecipientDto::new)
                .toList()
                .doAfterSuccess(list -> this.recipientDtos.addAll(list));
    }

    @Override
    public Single<List<RecipientDto>> refreshRecipients() {
        recipientDtos.clear();
        return getFromServer();
    }
}
