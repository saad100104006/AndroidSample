package uk.co.transferx.app.view.pinview;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

/**
 * Created by smilevkiy on 23.11.17.
 */

public class CustomEditPinText extends AppCompatEditText {


    private OnKeyKodEventListener listener;

    public CustomEditPinText(Context context) {
        super(context);
    }

    public CustomEditPinText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditPinText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface OnKeyKodEventListener {
        void onKeyKodEvent(int keyCode, KeyEvent event);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new CustomInputConnection(super.onCreateInputConnection(outAttrs),
                true);
    }

    private class CustomInputConnection extends InputConnectionWrapper {

        public CustomInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (listener != null) {
                Log.d("Sergey", "ivent");
                listener.onKeyKodEvent(event.getKeyCode(), event);
            }
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            // magic: in latest Android, deleteSurroundingText(1, 0) will be called for backspace
            if (beforeLength == 1 && afterLength == 0) {
                // backspace
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }

            return super.deleteSurroundingText(beforeLength, afterLength);
        }

    }

    public void setKeyKodEventListener(OnKeyKodEventListener keyKodEventListener) {
        listener = keyKodEventListener;
    }
}
