package com.damianw.maizeways.android.navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.damianw.maizeways.android.MainActivity;
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

        final RoutesResponse.Route item = getItems().get(position);

        TextView text = (TextView) navItemView.findViewById(R.id.routes_drawer_item_text);
        text.setText(item.name);

        View button = navItemView.findViewById(R.id.routes_drawer_item_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() instanceof MainActivity) {
                    MainActivity main = (MainActivity) getContext();
                    main.launchRouteDetailActivity(item);
                }
            }
        });

        if (getParent().getSelectedIndices().contains(position)) {
            navItemView.setBackgroundResource(android.R.color.black);
        } else {
            navItemView.setBackgroundResource(android.R.color.transparent);
        }

        return navItemView;
    }

}
