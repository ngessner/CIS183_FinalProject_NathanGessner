package com.example.stockvault;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ItemCustomAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<Object> items; // Use Object to allow Products and Materials

    public ItemCustomAdapter(Context context, ArrayList<Object> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.customadapter_items, parent, false);
        }

        TextView title = convertView.findViewById(R.id.item_title);
        TextView subtitle = convertView.findViewById(R.id.item_subtitle);
        TextView date = convertView.findViewById(R.id.item_date);

        Object item = items.get(position);

        if (item instanceof Product)
        {
            Product product = (Product) item;
            title.setText(product.getProductName());
            subtitle.setText(product.getDescription());
            date.setText(product.getDateCreated());
        }
        else if (item instanceof Material)
        {
            Material material = (Material) item;
            title.setText(material.getMaterialName());
            subtitle.setText(material.getDescription());
            date.setText(material.getDateAdded());
        }

        return convertView;
    }

    // add clear and addAll methods
    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<?> newItems) {
        items.addAll(newItems);
    }
}
