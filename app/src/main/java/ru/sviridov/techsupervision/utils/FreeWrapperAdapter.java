package ru.sviridov.techsupervision.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class FreeWrapperAdapter extends RecyclerView.Adapter {
   private static final int VIEW_TYPE_HEADER = 2;
   private static final int VIEW_TYPE_SIMPLE = 1;
   private final RecyclerView.Adapter delegate;
   private final OnClickListener fullListener;
   private final LayoutInflater inflater;
   private final boolean isSmallWidget;

   public FreeWrapperAdapter(@NonNull Context var1, @NonNull RecyclerView.Adapter var2, boolean var3, @Nullable OnClickListener var4) {
      this.inflater = LayoutInflater.from(var1);
      this.delegate = var2;
      this.fullListener = var4;
      this.isSmallWidget = var3;
      this.setHasStableIds(true);
   }

   private int getDelegatePosition(int var1) {
      return var1;
   }

   public int getItemCount() {
      int var1 = this.delegate.getItemCount();
      if (var1 == 0) {
         var1 = 0;
      } else {
         ++var1;
      }

      return var1;
   }

   public long getItemId(int var1) {
      long var2;
      switch(this.getItemViewType(var1)) {
      case 1:
         var2 = this.delegate.getItemId(this.getDelegatePosition(var1));
         break;
      default:
         var2 = super.getItemId(var1);
      }

      return var2;
   }

   public int getItemViewType(int var1) {
      byte var2 = 1;
      int var3 = this.delegate.getItemCount();
      if (var3 != 0 && var1 == var3) {
         var2 = 2;
      }

      return var2;
   }

   public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
      switch(this.getItemViewType(var2)) {
      case 1:
         this.delegate.onBindViewHolder(var1, this.getDelegatePosition(var2));
      case 2:
      default:
      }
   }

   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      Object var3;
      switch(var2) {
      case 1:
         var3 = this.delegate.onCreateViewHolder(var1, var2);
         break;
      case 2:
         var3 = new FreeWrapperAdapter.HeaderViewHolder(this.inflater.inflate(2130903095, var1, false), this.fullListener);
         break;
      default:
         var3 = null;
      }

      return (RecyclerView.ViewHolder)var3;
   }

   public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver var1) {
      this.delegate.registerAdapterDataObserver(var1);
   }

   public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver var1) {
      this.delegate.unregisterAdapterDataObserver(var1);
   }

   private class HeaderViewHolder extends RecyclerView.ViewHolder {
      public HeaderViewHolder(View var2, OnClickListener var3) {
         super(var2);
         TextView var4 = (TextView)var2.findViewById(2131558578);
         var4.setOnClickListener(var3);
         if (FreeWrapperAdapter.this.isSmallWidget) {
            var4.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, (Drawable)null, (Drawable)null);
         }

      }
   }
}
