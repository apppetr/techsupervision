package ru.lihachev.norm31937.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.lihachev.norm31937.free.R;

public class FreeWrapperAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {
   private static final int VIEW_TYPE_HEADER = 2;
   private static final int VIEW_TYPE_SIMPLE = 1;
   private final RecyclerView.Adapter<VH> delegate;
   private final View.OnClickListener fullListener;
   private final LayoutInflater inflater;
   /* access modifiers changed from: private */
   public final boolean isSmallWidget;

   public FreeWrapperAdapter(@NonNull Context context, @NonNull RecyclerView.Adapter<VH> adapter, boolean isSmallWidget2, @Nullable View.OnClickListener fullListener2) {
      this.inflater = LayoutInflater.from(context);
      this.delegate = adapter;
      this.fullListener = fullListener2;
      this.isSmallWidget = isSmallWidget2;
      setHasStableIds(true);
   }

   public int getItemViewType(int position) {
      int delegateCount = this.delegate.getItemCount();
      if (delegateCount != 0 && position == delegateCount) {
         return 2;
      }
      return 1;
   }

   public long getItemId(int position) {
      switch (getItemViewType(position)) {
         case 1:
            return this.delegate.getItemId(getDelegatePosition(position));
         default:
            return super.getItemId(position);
      }
   }

   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      switch (viewType) {
         case 1:
            return this.delegate.onCreateViewHolder(parent, viewType);
         case 2:
            return new HeaderViewHolder(this.inflater.inflate(R.layout.item_full_ads, parent, false), this.fullListener);
         default:
            return null;
      }
   }

   public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      switch (getItemViewType(position)) {
         case 1:
            this.delegate.onBindViewHolder((VH) holder, getDelegatePosition(position));
            return;
         default:
            return;
      }
   }

   public int getItemCount() {
      int delegateCount = this.delegate.getItemCount();
      if (delegateCount == 0) {
         return 0;
      }
      return delegateCount + 1;
   }

   private int getDelegatePosition(int position) {
      return position;
   }

   public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
      this.delegate.registerAdapterDataObserver(observer);
   }

   public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
      this.delegate.unregisterAdapterDataObserver(observer);
   }

   /* renamed from: ru.lihachev.norm31937.utils.FreeWrapperAdapter$HeaderViewHolder */
   private class HeaderViewHolder extends RecyclerView.ViewHolder {
      public HeaderViewHolder(View itemView, View.OnClickListener listener) {
         super(itemView);
         TextView tvInstallFull = (TextView) itemView.findViewById(R.id.tvInstallFull);
         tvInstallFull.setOnClickListener(listener);
         if (FreeWrapperAdapter.this.isSmallWidget) {
            tvInstallFull.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
         }
      }
   }
}
