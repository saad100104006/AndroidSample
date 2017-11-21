package uk.co.transferx.app.view.pinview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import uk.co.transferx.app.R;

/**
 * Created by sergey on 19.11.17.
 */

public class CharView extends FrameLayout {

    private TextView pinCharTextView;
    private FrameLayout secretCharBackground;
    private int secretColorRes = R.color.white;

    public CharView(Context context) {
        super(context);
        init(context);
    }

    public CharView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CharView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.char_view, this);

        pinCharTextView = findViewById(R.id.pin_char_view_text_view);
        secretCharBackground = findViewById(R.id.pin_char_view_secret);
        secretCharBackground.setBackground(new SecretCharDrawable(Color.TRANSPARENT));
    }

    public void setText(String value) {
        pinCharTextView.setText(value);
    }

    public String getText() {
        return pinCharTextView.getText().toString();
    }

    public void setColor(@ColorRes int color) {
        setTextColor(color);
    }

    public void setTextColor(@ColorRes int color) {
        pinCharTextView.setTextColor(getResources().getColor(color));
    }

    public void applySecretColor() {
        if (secretColorRes > 0) {
            secretCharBackground.setBackground(new SecretCharDrawable(getContext().getResources().getColor(secretColorRes)));
        }
    }

    public void setSecretColor(@ColorRes int color) {
        this.secretColorRes = color;
    }

    public void hideChar() {
        pinCharTextView.setVisibility(View.INVISIBLE);
        applySecretColor();
    }

    public void showChar() {
        pinCharTextView.setVisibility(View.VISIBLE);
    }

    public void setTextSize(int unit, float size) {
        pinCharTextView.setTextSize(unit, size);
    }

    public void setCharWidth(int size) {
        FrameLayout.LayoutParams params = new LayoutParams(size, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(params);
    }
}
