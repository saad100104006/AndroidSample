package uk.co.transferx.app.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

import uk.co.transferx.app.R;

/**
 * Created by sergey on 04/04/2018.
 */

public class CustomSpinner extends AppCompatSpinner {
    @LayoutRes
    private static final int
            DEFAULT_HEADER_RES = R.layout.spiner_select_result_dark,
            DEFAULT_ITEM_RES = R.layout.one_line_spinner_item;
    @IdRes
    static final int
            DEFAULT_HEADER_LABEL_ID = R.id.tv_spinner_item_title,
            DEFAULT_ITEM_ID = R.id.tv_spinner_item_title,
            DEFAULT_SELECTION_IMAGE_ID = R.id.iv_spinner_selection;
    @LayoutRes
    private int headerRes, itemRes;
    @IdRes
    private int headerLabelId, itemLabelId, selectionImageId;

    private CustomSpinnerArrayAdapter adapter;
    private int hintItemIndex = -1;

    public interface ListenerExecutable {
        void execute(int position, Object object);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomSpinner);
        headerRes = typedArray.getResourceId(R.styleable.CustomSpinner_headerRes, DEFAULT_HEADER_RES);
        headerLabelId = typedArray.getInteger(R.styleable.CustomSpinner_headerLabelId, DEFAULT_HEADER_LABEL_ID);
        itemRes = typedArray.getResourceId(R.styleable.CustomSpinner_dropDownItemRes, DEFAULT_ITEM_RES);
        itemLabelId = typedArray.getInteger(R.styleable.CustomSpinner_dropDownLabelId, DEFAULT_ITEM_ID);
        selectionImageId = typedArray.getInteger(R.styleable.CustomSpinner_dropDownSelectionImageId, DEFAULT_SELECTION_IMAGE_ID);
        typedArray.recycle();
    }

    @Override
    public boolean performClick() {
        adapter.hideHintItem();
        return super.performClick();
    }

    public void setOnItemSelectedListener(ListenerExecutable executable) {
        this.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != hintItemIndex) {
                    adapter.setItemSelected(position);
                    executable.execute(position, adapter.getItem(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setData(Object[] data) {
        adapter = new CustomSpinnerArrayAdapter(getContext(), headerRes, headerLabelId, data);
        adapter.setDropDownViewResource(itemRes);
        adapter.setItemLabelId(itemLabelId);
        adapter.setSelectionImageId(selectionImageId);
        this.setAdapter(adapter);
    }

    public void setDataWithHintItem(Object[] data, CharSequence hint) {
        Object[] dataWithHintItem = new Object[data.length + 1];
        System.arraycopy(data, 0, dataWithHintItem, 0, data.length);
        hintItemIndex = data.length;
        setData(dataWithHintItem);
        adapter.setHintItem(hintItemIndex, hint);
        setHintSelected();
    }

    public void setHintSelected() {
        super.setSelection(hintItemIndex);
    }

    public void bindDataLine(@IdRes int textViewId, TextViewBinder.StringProvider textProvider) {
        adapter.bind(textViewId, textProvider);
    }

    @Override
    public CustomSpinnerArrayAdapter getAdapter() {
        return adapter;
    }
}
