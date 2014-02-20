package com.damianw.maizeways.android.navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.damianw.maizeways.android.R;
import com.damianw.maizeways.android.data.RoutesResponse;

/**
 * Created by damian on 2/19/14.
 */
public class RoutesDrawerAdapter extends MBusDrawerAdapter<RoutesResponse.Route> {

    public RoutesDrawerAdapter(Context context, NavigationDrawerFragment parent) {
        super(context, parent);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View navItemView;
        if (convertView == null) {
            navItemView = inflater.inflate(R.layout.routes_drawer_item, null);
        } else {
            navItemView = convertView;
        }

        RoutesResponse.Route item = getItems().get(position);

        TextView text = (TextView) navItemView.findViewById(R.id.routes_drawer_item_text);
        text.setText(item.name);

        if (getSelectedItems().contains(position)) {
            navItemView.setBackgroundResource(android.R.color.black);
        } else {
            navItemView.setBackgroundResource(android.R.color.transparent);
        }

        return navItemView;
    }

}
