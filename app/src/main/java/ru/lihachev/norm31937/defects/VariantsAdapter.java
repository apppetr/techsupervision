package ru.lihachev.norm31937.defects;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import java.util.ArrayList;
import java.util.List;
import nl.qbusict.cupboard.CupboardFactory;
import ru.lihachev.norm31937.free.R;
import ru.lihachev.norm31937.objects.Variant;
import ru.lihachev.norm31937.widgets.RVCursorAdapter;

public class VariantsAdapter extends RVCursorAdapter<VariantsAdapter.VariantViewHolder> {
   private final LayoutInflater inflater;
   /* access modifiers changed from: private */
   public final SparseBooleanArray selectedIds = new SparseBooleanArray();

   public VariantsAdapter(@NonNull Context context, Cursor cursor) {
      super(context, cursor);
      this.inflater = LayoutInflater.from(context);
   }

   public void setSelectedIds(@NonNull int[] selectedIds2) {
      this.selectedIds.clear();
      for (int selectedValue : selectedIds2) {
         this.selectedIds.put(selectedValue, true);
      }
      notifyDataSetChanged();
   }

   public VariantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new VariantViewHolder(this.inflater.inflate(R.layout.item_variant, parent, false));
   }

   public void onBindViewHolder(VariantViewHolder holder, Cursor cursor) {
      Variant variant = (Variant) CupboardFactory.cupboard().withCursor(cursor).get(Variant.class);
      holder.view.setText(variant.getName());
      holder.view.setChecked(this.selectedIds.get(variant.getId()));
   }

   public List<Variant> getSelected() {
      List<Variant> selectedVariants = new ArrayList<>();
      int count = getItemCount();
      for (int i = 0; i < count; i++) {
         if (this.selectedIds.get((int) getItemId(i))) {
            selectedVariants.add((Variant) CupboardFactory.cupboard().withCursor(getItem(i)).get(Variant.class));
         }
      }
      return selectedVariants;
   }

   /* renamed from: ru.lihachev.norm31937.defects.VariantsAdapter$VariantViewHolder */
   class VariantViewHolder extends RVCursorAdapter.SelectableViewHolder {
      /* access modifiers changed from: private */
      public final CheckedTextView view;

      public VariantViewHolder(View itemView) {
         super(itemView);
         this.view = (CheckedTextView) itemView.findViewById(R.id.variant);
         this.view.setOnClickListener(this);
      }

      public void onClick(@NonNull View v) {
         VariantsAdapter.this.selectedIds.put((int) getItemId(), !((Checkable) v).isChecked());
         VariantsAdapter.this.notifyItemChanged(getLayoutPosition());
      }
   }
}