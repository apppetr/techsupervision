package ru.sviridov.techsupervision.defects;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import java.util.ArrayList;
import java.util.List;
import nl.qbusict.cupboard.CupboardFactory;
import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.objects.Variant;
import ru.sviridov.techsupervision.widgets.RVCursorAdapter;

public class VariantsAdapter extends RVCursorAdapter {
   private final LayoutInflater inflater;
   private final SparseBooleanArray selectedIds;

   public VariantsAdapter(@NonNull Context var1, Cursor var2) {
      super(var1, var2);
      this.inflater = LayoutInflater.from(var1);
      this.selectedIds = new SparseBooleanArray();
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder var1, Cursor var2) {

   }

   public List getSelected() {
      ArrayList var1 = new ArrayList();
      int var2 = 0;

      for(int var3 = this.getItemCount(); var2 < var3; ++var2) {
         if (this.selectedIds.get((int)this.getItemId(var2))) {
            var1.add((Variant)CupboardFactory.cupboard().withCursor(this.getItem(var2)).get(Variant.class));
         }
      }

      return var1;
   }

   public void onBindViewHolder(VariantsAdapter.VariantViewHolder var1, Cursor var2) {
      Variant var3 = (Variant)CupboardFactory.cupboard().withCursor(var2).get(Variant.class);
      var1.view.setText(var3.getName());
      var1.view.setChecked(this.selectedIds.get(var3.getId()));
   }

   public VariantsAdapter.VariantViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      return new VariantsAdapter.VariantViewHolder(this.inflater.inflate(R.layout.item_variant, var1, false));
   }

   public void setSelectedIds(@NonNull int[] var1) {
      this.selectedIds.clear();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         int var4 = var1[var3];
         this.selectedIds.put(var4, true);
      }

      this.notifyDataSetChanged();
   }

   class VariantViewHolder extends RVCursorAdapter.SelectableViewHolder {
      private final CheckedTextView view;

      public VariantViewHolder(View var2) {
         super(var2);
         this.view = (CheckedTextView)var2.findViewById(R.id.variant);
         this.view.setOnClickListener(this);
      }

      public void onClick(@NonNull View var1) {
         SparseBooleanArray var2 = VariantsAdapter.this.selectedIds;
         int var3 = (int)this.getItemId();
         boolean var4;
         if (!((Checkable)var1).isChecked()) {
            var4 = true;
         } else {
            var4 = false;
         }

         var2.put(var3, var4);
         VariantsAdapter.this.notifyItemChanged(this.getLayoutPosition());
      }
   }
}
