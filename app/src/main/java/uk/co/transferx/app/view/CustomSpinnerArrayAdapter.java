package uk.co.transferx.app.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import timber.log.Timber;
import uk.co.transferx.app.R;
import uk.co.transferx.app.pojo.Card;

import static uk.co.transferx.app.util.Constants.VISA;

/**
 * Created by sergey on 04/04/2018.
 */

public class CustomSpinnerArrayAdapter extends ArrayAdapter {
    private Context mContext;
    private final LayoutInflater inflater;
    @LayoutRes
    private int dropDownView, headerView;
    @IdRes
    private int selectionImageId, itemLabelId, imagePhotoId;
    private int currentSelection = -4, hintItemIndex = -1;
    private CharSequence hint;
    private boolean hintVisible = false;
    private SparseArray<TextViewBinder> labelsToBind;
    private Object[] objects;
    private final int hintColor;
    private final ImgMapper imgMapper = new ImgMapper();

    public CustomSpinnerArrayAdapter(Context context, @LayoutRes int resource, @IdRes int textViewResourceId, Object[] objects, boolean iconVisible, int hintColor) {
        super(context, resource, textViewResourceId, objects);
        this.objects = objects;
        headerView = resource;
        mContext = context;
        this.hintColor = hintColor;
        labelsToBind = new SparseArray<>();
        selectionImageId = CustomSpinner.DEFAULT_SELECTION_IMAGE_ID;
        itemLabelId = CustomSpinner.DEFAULT_ITEM_ID;
        imagePhotoId = CustomSpinner.DEFAULT_ITEM_IMG;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bind(itemLabelId, imagePhotoId, Object::toString);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(dropDownView, parent, false);
        }
        return getCustomView(position, convertView);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == hintItemIndex) {
            return getHintView(parent);
        } else {
            return getHeaderView(position, convertView, parent);
        }
    }

    private View getHeaderView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        bindLabels(convertView, position);
        Object item = objects[position];
        if (position != hintItemIndex && item instanceof Card) {
            Card card = (Card) item;
            ((ImageView) convertView.findViewById(R.id.image_recip)).setImageDrawable(ContextCompat.getDrawable(getContext(), imgMapper.mapCardType(card.getType())));
        }
        if (position != hintItemIndex && item instanceof String) {
            ImageView flag = convertView.findViewById(R.id.image_recip);
            if (flag != null) {
                flag.setImageDrawable(ContextCompat.getDrawable(getContext(), imgMapper.mapNameToFlag((String) item)));
            }
        }
        return convertView;
    }

    private View getHintView(ViewGroup parent) {
        View view = inflater.inflate(headerView, parent, false);
        ((TextView) view.findViewById(itemLabelId)).setText(hint);
        return view;
    }

    @Override
    public void setDropDownViewResource(@LayoutRes int resource) {
        super.setDropDownViewResource(resource);
        dropDownView = resource;
    }

    void setHintItem(int hintItemIndex, CharSequence hint) {
        this.hintItemIndex = hintItemIndex;
        this.hint = setColor(hint);
        this.hintVisible = true;
    }

    private CharSequence setColor(CharSequence hint) {
        SpannableStringBuilder str = new SpannableStringBuilder(hint);
        str.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, hintColor)), 0, hint.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return str;
    }

    void hideHintItem() {
        hintVisible = false;
    }

    void bind(@IdRes int textViewId, int imageId, TextViewBinder.StringProvider textProvider) {
        labelsToBind.append(textViewId, new TextViewBinder(textViewId, textProvider, imageId));
    }

    private View getCustomView(int position, View convertView) {
        TextView textView = convertView.findViewById(itemLabelId);
        ImageView imageView = convertView.findViewById(selectionImageId);
        ImageView itemImg = convertView.findViewById(imagePhotoId);
        Object item = objects[position];
        if (position == currentSelection) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            textView.setTypeface(ResourcesCompat.getFont(convertView.getContext(), R.font.montserrat));
            imageView.setVisibility(View.INVISIBLE);
        }

        if (position != hintItemIndex) {
            bindLabels(convertView, position);
        }
        if (position != hintItemIndex && item instanceof Card) {
            Card card = (Card) item;
            itemImg.setImageDrawable(ContextCompat.getDrawable(getContext(), imgMapper.mapCardType(card.getType())));
        }
        if (position != hintItemIndex && item instanceof String) {
            if (itemImg != null) {
                itemImg.setImageDrawable(ContextCompat.getDrawable(getContext(), imgMapper.mapNameToFlag((String) item)));
            }
        }
        return convertView;
    }

    private void bindLabels(View convertView, int position) {
        for (int i = 0; i < labelsToBind.size(); i++) {
            TextViewBinder binder = labelsToBind.get(labelsToBind.keyAt(i));
            ((TextView) convertView.findViewById(binder.textViewId))
                    .setText(binder.textProvider.provide(getItem(position)));

        }
    }

    @Override
    public int getCount() {
        if (hintItemIndex >= 0 && !hintVisible) {
            return super.getCount() - 1;
        }
        return super.getCount();
    }

    public void setItemSelected(int selected) {
        this.currentSelection = selected;
    }

    void setItemLabelId(int itemLabelId) {
        this.itemLabelId = itemLabelId;
    }

    void setSelectionImageId(int selectionImageId) {
        this.selectionImageId = selectionImageId;
    }

    void setImageId(int imagePhotoId) {
        this.imagePhotoId = imagePhotoId;
    }
}
