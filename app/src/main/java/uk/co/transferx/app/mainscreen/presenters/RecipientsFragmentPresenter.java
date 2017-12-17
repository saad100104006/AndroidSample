package uk.co.transferx.app.mainscreen.presenters;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.dto.RecipientDto;

/**
 * Created by sergey on 17.12.17.
 */

public class RecipientsFragmentPresenter extends BasePresenter<RecipientsFragmentPresenter.RecipientsFragmentUI> {

    private boolean isInitialized;

    @Inject
    public RecipientsFragmentPresenter() {
    }


    @Override
    public void attachUI(RecipientsFragmentUI ui) {
        super.attachUI(ui);
        if (!isInitialized) {
            generateData();
            isInitialized = true;
        }
    }


    private void generateData() {
        List<RecipientDto> recipientDtos = new ArrayList<>();
        recipientDtos.add(new RecipientDto("Sergey Milewski", null, "PL"));
        recipientDtos.add(new RecipientDto("Ekrem Karatas", null, "TR"));
        recipientDtos.add(new RecipientDto("Evangelos Pappas", null, "GB"));
        recipientDtos.add(new RecipientDto("Windsor Kitaka", null, "GB"));
        recipientDtos.add(new RecipientDto("Paulo Chaves", null, "BR"));
        if (ui != null) {
            ui.setFavoriteRecipients(recipientDtos);
        }
    }

    public interface RecipientsFragmentUI extends UI {

        void setFavoriteRecipients(List<RecipientDto> recipientDtos);

    }
}
