package uk.co.transferx.app.mainscreen.presenters;

import javax.inject.Inject;

import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.dto.RecipientDto;
import uk.co.transferx.app.pojo.Recipient;

/**
 * Created by sergey on 15.12.17.
 */

public class SendFragmentPresenter extends BasePresenter<SendFragmentPresenter.SendFragmentUI> {

    private RecipientDto recipientDto;
    @Inject
    public SendFragmentPresenter() {
    }

    public void clickMainRecipient() {
        if (ui != null) {
            ui.showDialogRecipients();
        }
    }


    public void setRecipient(RecipientDto recipient){
        recipientDto = recipient;
        if(ui != null){
            ui.showChoosenRecipient(recipientDto);
        }
    }


    public interface SendFragmentUI extends UI {

        void showDialogRecipients();

        void showChoosenRecipient(RecipientDto recipientDto);
    }
}
