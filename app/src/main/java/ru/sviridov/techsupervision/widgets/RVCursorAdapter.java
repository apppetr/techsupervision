package ru.sviridov.techsupervision.widgets;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

public abstract class RVCursorAdapter extends RecyclerView.Adapter {
   private static final String ID = "_id";
   protected final Context context;
   private Cursor cursor;
   private int idIndx = 0;
   @Nullable
   private RVCursorAdapter.onItemSelectedListener listener;
   private RVCursorAdapter.onItemSelectedListener longClickListener;
   private boolean noNotifyOncePlease = false;

   public RVCursorAdapter(@NonNull Context var1, @Nullable Cursor var2) {
      this.context = var1;
      this.cursor = var2;
      if (var2 != null) {
         this.idIndx = var2.getColumnIndex("_id");
         this.initIndexes(var2);
      }

      this.setHasStableIds(true);
   }

   public void changeCursor(Cursor var1) {
      var1 = this.swapCursor(var1);
      if (var1 != null) {
         var1.close();
      }

   }

   @Nullable
   public Cursor getItem(int var1) {
      if (this.cursor != null) {
         this.cursor.moveToPosition(var1);
      }

      return this.cursor;
   }

   public int getItemCount() {
      int var1;
      if (this.cursor == null) {
         var1 = 0;
      } else {
         var1 = this.cursor.getCount();
      }

      return var1;
   }

   public long getItemId(int var1) {
      this.cursor.moveToPosition(var1);
      return (long)this.cursor.getInt(this.idIndx);
   }

   protected void initIndexes(@NonNull Cursor var1) {
   }

   protected void noNotifyOncePlease() {
      this.noNotifyOncePlease = true;
   }

   public final void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
      this.cursor.moveToPosition(var2);
      this.onBindViewHolder(var1, this.cursor);
   }

   public abstract void onBindViewHolder(RecyclerView.ViewHolder var1, Cursor var2);

   public void setLongClickListener(@Nullable RVCursorAdapter.onItemSelectedListener var1) {
      this.longClickListener = var1;
   }

   public void setOnItemSelectedListener(@Nullable RVCursorAdapter.onItemSelectedListener var1) {
      this.listener = var1;
   }

   public Cursor swapCursor(@Nullable Cursor var1) {
      if (var1 != null) {
         this.idIndx = var1.getColumnIndex("_id");
         this.initIndexes(var1);
      }

      if (this.cursor == var1) {
         var1 = null;
      } else {
         Cursor var2 = this.cursor;
         this.cursor = var1;
         if (this.noNotifyOncePlease) {
            this.noNotifyOncePlease = false;
            var1 = var2;
         } else {
            this.notifyDataSetChanged();
            var1 = var2;
         }
      }

      return var1;
   }

   public abstract class SelectableViewHolder extends RecyclerView.ViewHolder implements OnClickListener, OnLongClickListener {
      public SelectableViewHolder(View var2) {
         super(var2);
         var2.setOnClickListener(this);
         var2.setOnLongClickListener(this);
      }

      public void onClick(@NonNull View var1) {
         if (RVCursorAdapter.this.listener != null) {
            int var2 = this.getAdapterPosition();
            RVCursorAdapter.this.cursor.moveToPosition(var2);
            RVCursorAdapter.this.listener.onItemSelected(RVCursorAdapter.this.cursor);
         }

      }

      public boolean onLongClick(@NonNull View var1) {
         boolean var3;
         if (RVCursorAdapter.this.longClickListener != null) {
            int var2 = this.getAdapterPosition();
            RVCursorAdapter.this.cursor.moveToPosition(var2);
            RVCursorAdapter.this.longClickListener.onItemSelected(RVCursorAdapter.this.cursor);
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }

   public interface onItemSelectedListener {
      void onItemSelected(Cursor var1);
   }
}
