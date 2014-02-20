package com.damianw.maizeways.android.navigation;

/**
 * Created by damian on 2/19/14.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.damianw.maizeways.android.R;
import com.damianw.maizeways.android.data.RoutesResponse;

import java.util.List;
import java.util.TreeSet;

/**
 * Created by damian on 1/12/14.
 */
public class RoutesDrawerAdapter extends ArrayAdapter<RoutesResponse.Route> {
    private Context mContext;
    private List<RoutesResponse.Route> mItems;
    private TreeSet<Integer> mSelectedItems = new TreeSet<Integer>();

    public RoutesDrawerAdapter(Context context, List<RoutesResponse.Route> items) {
        super(context, R.layout.routes_drawer_item);

        mContext = context;
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public RoutesResponse.Route getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).hashCode();
    }

    public void clickItem(int position) {
        if (mSelectedItems.contains(position)) {
            mSelectedItems.remove(position);
        } else {
            mSelectedItems.add(position);
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View navItemView;
        if (convertView == null) {
            navItemView = inflater.inflate(R.layout.routes_drawer_item, null);
        } else {
            navItemView = convertView;
        }

        RoutesResponse.Route item = mItems.get(position);

        TextView text = (TextView) navItemView.findViewById(R.id.routes_drawer_item_text);
        text.setText(item.name);

        if (mSelectedItems.contains(position)) {
            navItemView.setBackgroundResource(android.R.color.black);
        } else {
            navItemView.setBackgroundResource(android.R.color.transparent);
        }

        return navItemView;
    }

}
