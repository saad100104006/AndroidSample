package uk.co.transferx.app.view;

import android.support.annotation.IdRes;

/**
 * Created by sergey on 04/04/2018.
 */

class TextViewBinder {
    @IdRes
    int textViewId;
    StringProvider textProvider;

    TextViewBinder(int textViewId, StringProvider textProvider) {
        this.textViewId = textViewId;
        this.textProvider = textProvider;
    }

    public interface StringProvider {
        CharSequence provide(Object objectToDescribe);
    }

}
