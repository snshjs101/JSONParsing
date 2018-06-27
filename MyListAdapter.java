package com.jose.saneesh.jsonparsing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Saneesh on 6/26/2018.
 */

class MyListAdapter extends BaseAdapter {
    private static List<DataFields> list = Collections.emptyList();
    // List<DataFields> list1 = Collections.emptyList();
    private LayoutInflater layoutInflater;
    Context c;

    MyListAdapter(Context context, List<DataFields> list) {
        c = context;
        layoutInflater = LayoutInflater.from(context);
        this.list = list;


    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = layoutInflater.inflate(R.layout.row_layout, parent, false);
        TextView name = (TextView) row.findViewById((R.id.name));
        TextView description = (TextView) row.findViewById((R.id.description));
        TextView url = row.findViewById(R.id.url);
        DataFields tmp = list.get(position);
        name.setText(tmp.name);
        description.setText(tmp.description);
        url.setText(tmp.url);
        return row;


    }
}