package ru.lihachev.norm31937.defects.place;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ru.lihachev.norm31937.free.R;

public class NumberAdapter extends BaseAdapter {
   private final int count;
   private final LayoutInflater inflater;

   public NumberAdapter(Context context, int count2) {
      this.inflater = LayoutInflater.from(context);
      this.count = count2;
   }

   public int getCount() {
      return this.count;
   }

   public Integer getItem(int position) {
      return Integer.valueOf(position + 1);
   }

   public long getItemId(int position) {
      return (long) position;
   }

   public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
         convertView = this.inflater.inflate(R.layout.support_simple_spinner_dropdown_item, parent, false);
      }
      ((TextView) convertView).setText(String.valueOf(getItem(position)));
      return convertView;
   }
}
