package com.damianw.maizeways.android.navigation;

/**
 * Created by damian on 2/19/14.
 */

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.damianw.maizeways.android.R;
import com.damianw.maizeways.android.data.HasID;

import java.util.List;
import java.util.TreeSet;

/**
 * Created by damian on 1/12/14.
 */
public abstract class MBusDrawerAdapter<ResponseType extends HasID> extends ArrayAdapter<ResponseType> {
    private TreeSet<Integer> mSelectedItems = new TreeSet<Integer>();
    NavigationDrawerFragment mParent;

    public MBusDrawerAdapter(Context context, NavigationDrawerFragment<ResponseType> parent) {
        super(context, R.layout.routes_drawer_item);
        mParent = parent;
    }

    @Override
    public int getCount() {
        return mParent.getItems().size();
    }

    @Override
    public ResponseType getItem(int position) {
        return (ResponseType) mParent.getItems().get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((ResponseType) mParent.getItems().get(position)).hashCode();
    }

    public void clickItem(int position) {
        if (mSelectedItems.contains(position)) {
            mSelectedItems.remove(position);
        } else {
            mSelectedItems.add(position);
        }
    }

    public TreeSet<Integer> getSelectedItems() {
        return mSelectedItems;
    }

    public List<ResponseType> getItems() {
        return mParent.getItems();
    }

    public void replaceItems(List<ResponseType> items) {
        clear();
//        mItems = items;
        addAll(items);
        Log.d("MBusDrawerAdapter", "There should now be " + items.size() + " items!");
        notifyDataSetChanged();
    }

}
