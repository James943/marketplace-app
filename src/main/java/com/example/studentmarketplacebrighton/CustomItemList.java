package com.example.studentmarketplacebrighton;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomItemList extends ArrayAdapter {
    private String[] itemNames;
    private String[] itemPrices;
    private Activity context;

    Handler handler;
    public CustomItemList(Activity context, String[] itemNames, String[] itemPrices) {
        super(context, R.layout.row_item, itemNames);
        this.context = context;
        this.itemNames = itemNames;
        this.itemPrices = itemPrices;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            LayoutInflater inflater = context.getLayoutInflater();
            if (convertView == null)
                row = inflater.inflate(R.layout.row_item, null, true);
            TextView textViewNames = (TextView) row.findViewById(R.id.textViewName);
            TextView textViewPrices = (TextView) row.findViewById(R.id.textViewPrice);
            ImageView imageFlag = (ImageView) row.findViewById(R.id.imageViewImage);

            textViewNames.setText(itemNames[position]);
            textViewPrices.setText("£" + itemPrices[position]);
            imageFlag.setImageBitmap(MainActivity.myBitmap[position]);

            return row;
    }

}