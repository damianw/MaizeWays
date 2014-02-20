package com.damianw.maizeways.android.navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.damianw.maizeways.android.MainActivity;
import com.damianw.maizeways.android.R;
import com.damianw.maizeways.android.data.StopsResponse;

/**
 * Created by damian on 2/19/14.
 */
public class StopsDrawerAdapter extends MBusDrawerAdapter<StopsResponse.Stop> {

    public StopsDrawerAdapter(Context context, NavigationDrawerFragment parent) {
        super(context, parent);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View navItemView;
        if (convertView == null) {
            navItemView = inflater.inflate(R.layout.stops_drawer_item, null);
        } else {
            navItemView = convertView;
        }

        final StopsResponse.Stop item = getItems().get(position);

        TextView text = (TextView) navItemView.findViewById(R.id.stops_drawer_item_text);
        text.setText(item.unique_name);

        View button = navItemView.findViewById(R.id.stops_drawer_item_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() instanceof MainActivity) {
                    MainActivity main = (MainActivity) getContext();
                    main.launchStopDetailActivity(item);
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
