package uk.co.transferx.app.view.pinview;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import uk.co.transferx.app.R;

/**
 * Created by sergey on 21.11.17.
 */

public class SingleCharView extends FrameLayout {

    private CustomEditPinText charView;
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
        FrameLayout.LayoutParams fp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(fp);
        setLayoutTransition(new LayoutTransition());
        setId(View.generateViewId());
        setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        setUpEditText(context);
        addView(charView);
        setUpSecureImageView(context);
        addView(circle);

    }

    private void setUpEditText(Context context) {
        charView = new CustomEditPinText(context);
        float sizeCharView = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        charView.setWidth((int) sizeCharView);
        charView.setHeight((int) sizeCharView);
        charView.setMaxLines(1);
        charView.setInputType(InputType.TYPE_CLASS_NUMBER);
        charView.setTextColor(ContextCompat.getColor(context, R.color.black));
        charView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        charView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        charView.setGravity(Gravity.CENTER);
        charView.setId(View.generateViewId());
        InputFilter[] inputFilters = new InputFilter[1];
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//workaround for KitKat :(
            inputFilters[0] = new InputFilter.LengthFilter(2);
            charView.setFilters(inputFilters);
            charView.setText(" ");
            charView.setSelection(1);
            return;
        }
        inputFilters[0] = new InputFilter.LengthFilter(1);
        charView.setFilters(inputFilters);
    }


    private void setUpSecureImageView(Context context) {
        circle = new ImageView(context);
        circle.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        circle.setScaleType(ImageView.ScaleType.CENTER);
        circle.setId(View.generateViewId());
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

    public void setSecureBlackCircle() {
        circle.setImageDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.circle));
    }

    public void setError() {
        circle.setImageDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.circle_red));
    }

    public void setText(CharSequence charSequence) {
        charView.setText(charSequence);
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        charView.addTextChangedListener(textWatcher);
    }
}
