package ru.sviridov.techsupervision.defects.place;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ru.sviridov.techsupervision.free.R;

public class NumberAdapter extends BaseAdapter {
   private final int count;
   private final LayoutInflater inflater;

   public NumberAdapter(Context var1, int var2) {
      this.inflater = LayoutInflater.from(var1);
      this.count = var2;
   }

   public int getCount() {
      return this.count;
   }

   public Integer getItem(int var1) {
      return var1 + 1;
   }

   public long getItemId(int var1) {
      return (long)var1;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      View var4 = var2;
      if (var2 == null) {
         var4 = this.inflater.inflate(R.layout.support_simple_spinner_dropdown_item, var3, false);
      }

      ((TextView)var4).setText(String.valueOf(this.getItem(var1)));
      return var4;
   }
}
