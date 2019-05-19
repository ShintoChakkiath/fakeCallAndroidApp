package com.GoldenApp.fakecall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class CharAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    ArrayList<Charactor> categroyArrayList;
    Context context;

    public class Holder {
        ImageView img;
        TextView tv;
        TextView tv2;
    }

    public CharAdapter(Context mainActivity, ArrayList<Charactor> categroyArrayList) {
        this.categroyArrayList = categroyArrayList;
        this.context = mainActivity;
        inflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.categroyArrayList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        View rowView = null;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.character_row, null);
            holder = new Holder();
            holder.tv = (TextView) rowView.findViewById(R.id.text_row);
            holder.tv2 = (TextView) rowView.findViewById(R.id.text_row2);
            holder.img = (ImageView) rowView.findViewById(R.id.img_row);
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }
        holder.tv.setText((categroyArrayList.get(position)).getName());
        holder.tv2.setText((categroyArrayList.get(position)).getNumber());
        holder.img.setImageResource((categroyArrayList.get(position)).getImgId());
        return rowView;
    }
}
