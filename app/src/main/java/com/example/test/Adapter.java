package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<User> arrayList;
    private  int layout;

    public Adapter(Context context, ArrayList<User> arrayList, int layout) {
        this.context = context;
        this.arrayList = arrayList;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        TextView txtID = view.findViewById(R.id.id);
        TextView txtFirstName = view.findViewById(R.id.firstName);
        TextView txtLastName = view.findViewById(R.id.lastName);
        TextView txtEmail = view.findViewById(R.id.Email);

        txtID.setText(arrayList.get(position).getId()+"");
        txtFirstName.setText(arrayList.get(position).getFirstName());
        txtLastName.setText(arrayList.get(position).getLastName());
        txtEmail.setText(arrayList.get(position).getEmail());

        return view;
    }

}
