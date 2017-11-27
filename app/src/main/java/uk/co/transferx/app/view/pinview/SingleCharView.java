package uk.co.transferx.app.view.pinview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import uk.co.transferx.app.R;

/**
 * Created by sergey on 21.11.17.
 */

public class SingleCharView extends FrameLayout {

    private CustomEditPinText charView;
    private FrameLayout container;
    private ImageView circle;

    public SingleCharView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SingleCharView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SingleCharView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SingleCharView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.single_char_view_layout, this);
        charView = findViewById(R.id.pin_char);
        container = findViewById(R.id.pin_secret);
        circle = findViewById(R.id.circle);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { //workaround for KitKat :(
            charView.setText(" ");
            charView.setSelection(1);
        }

    }

    @Override
    public void setTag(Object tag) {
        charView.setTag(tag);
    }

    @Override
    public Object getTag() {
        return charView.getTag();
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        charView.setOnFocusChangeListener(l);
    }


    public EditText getEditChar() {
        return charView;
    }

    public void setOnKeyBackSpaceListener(CustomEditPinText.OnKeyBackSpaceListener onKeyCodEventListener) {
        charView.setKeyBackSpaceListener(onKeyCodEventListener);
    }

    @Override
    public void setVisibility(int visibility) {
        charView.setVisibility(visibility);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        circle.setOnClickListener(l);
    }

    public void disableSecurityCircle() {
        circle.setImageDrawable(null);
    }

    public void setSecureCircle(Drawable background) {
        circle.setImageDrawable(background);
    }

    public void setText(CharSequence charSequence) {
        charView.setText(charSequence);
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        charView.addTextChangedListener(textWatcher);
    }
}
