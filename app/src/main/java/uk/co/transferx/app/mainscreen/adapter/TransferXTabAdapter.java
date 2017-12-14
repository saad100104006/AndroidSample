package uk.co.transferx.app.mainscreen.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import uk.co.transferx.app.R;
import uk.co.transferx.app.mainscreen.fragments.SendFragment;

/**
 * Created by sergey on 14.12.17.
 */

public class TransferXTabAdapter extends FragmentPagerAdapter {

    private final String[] tabTitles;
    private final Context context;
//    private final Bundle bundle;

    private final SparseArray<View> tabAdapterItems = new SparseArray<>();

    public TransferXTabAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        tabTitles = context.getResources().getStringArray(R.array.tabs);
//        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SendFragment();
            case 1:
                return new SendFragment();
            case 2:
                return new SendFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int item) {
        return tabTitles[item];
    }


    @Override
    public int getCount() {
        return tabTitles.length;
    }

    public View getTabView(int position) {
        View tab = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        TextView tv = tab.findViewById(R.id.tab_title);
        tv.setText(tabTitles[position]);
        tabAdapterItems.put(position, tab);
        if (position == 0) {
            setCheckStatus(position, true);
        }
        return tab;
    }

    public void setCheckStatus(int position, boolean status) {
        View view = tabAdapterItems.get(position);
        TextView title = view.findViewById(R.id.tab_title);
        title.setTextColor(status ? ContextCompat.getColor(context, R.color.white) : ContextCompat.getColor(context, R.color.text_not_active));
    }

}
