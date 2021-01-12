package ru.lihachev.norm31937.widgets;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class RVCursorAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

   /* renamed from: ID */
   private static final String f98ID = "_id";
   /* access modifiers changed from: protected */
   public final Context context;
   /* access modifiers changed from: private */
   public Cursor cursor;
   private int idIndx = 0;
   /* access modifiers changed from: private */
   @Nullable
   public onItemSelectedListener listener;
   /* access modifiers changed from: private */
   public onItemSelectedListener longClickListener;
   private boolean noNotifyOncePlease = false;

   /* renamed from: ru.lihachev.norm31937.widgets.RVCursorAdapter$onItemSelectedListener */
   public interface onItemSelectedListener {
      void onItemSelected(Cursor cursor);
   }

   public abstract void onBindViewHolder(VH vh, Cursor cursor2);

   public RVCursorAdapter(@NonNull Context context2, @Nullable Cursor cursor2) {
      this.context = context2;
      this.cursor = cursor2;
      if (cursor2 != null) {
         this.idIndx = cursor2.getColumnIndex("_id");
         initIndexes(cursor2);
      }
      setHasStableIds(true);
   }

   public void setOnItemSelectedListener(@Nullable onItemSelectedListener listener2) {
      this.listener = listener2;
   }

   public void setLongClickListener(@Nullable onItemSelectedListener longClickListener2) {
      this.longClickListener = longClickListener2;
   }

   /* access modifiers changed from: protected */
   public void initIndexes(@NonNull Cursor cursor2) {
   }

   public final void onBindViewHolder(VH h, int position) {
      this.cursor.moveToPosition(position);
      onBindViewHolder(h, this.cursor);
   }

   public int getItemCount() {
      if (this.cursor == null) {
         return 0;
      }
      return this.cursor.getCount();
   }

   public long getItemId(int position) {
      this.cursor.moveToPosition(position);
      return (long) this.cursor.getInt(this.idIndx);
   }

   @Nullable
   public Cursor getItem(int position) {
      if (this.cursor != null) {
         this.cursor.moveToPosition(position);
      }
      return this.cursor;
   }

   /* access modifiers changed from: protected */
   public void noNotifyOncePlease() {
      this.noNotifyOncePlease = true;
   }

   public Cursor swapCursor(@Nullable Cursor cursor2) {
      if (cursor2 != null) {
         this.idIndx = cursor2.getColumnIndex("_id");
         initIndexes(cursor2);
      }
      if (this.cursor == cursor2) {
         return null;
      }
      Cursor cursor3 = this.cursor;
      this.cursor = cursor2;
      if (this.noNotifyOncePlease) {
         this.noNotifyOncePlease = false;
         return cursor3;
      }
      notifyDataSetChanged();
      return cursor3;
   }

   public void changeCursor(Cursor cursor2) {
      Cursor old = swapCursor(cursor2);
      if (old != null) {
         old.close();
      }
   }

   /* renamed from: ru.lihachev.norm31937.widgets.RVCursorAdapter$SelectableViewHolder */
   public abstract class SelectableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
      public SelectableViewHolder(View itemView) {
         super(itemView);
         itemView.setOnClickListener(this);
         itemView.setOnLongClickListener(this);
      }

      public void onClick(@NonNull View v) {
         if (RVCursorAdapter.this.listener != null) {
            RVCursorAdapter.this.cursor.moveToPosition(getAdapterPosition());
            RVCursorAdapter.this.listener.onItemSelected(RVCursorAdapter.this.cursor);
         }
      }

      public boolean onLongClick(@NonNull View v) {
         if (RVCursorAdapter.this.longClickListener == null) {
            return false;
         }
         RVCursorAdapter.this.cursor.moveToPosition(getAdapterPosition());
         RVCursorAdapter.this.longClickListener.onItemSelected(RVCursorAdapter.this.cursor);
         return true;
      }
   }
}

