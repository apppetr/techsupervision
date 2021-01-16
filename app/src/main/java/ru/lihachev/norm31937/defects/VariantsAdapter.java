package ru.lihachev.norm31937.defects;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import nl.qbusict.cupboard.CupboardFactory;
import ru.lihachev.norm31937.free.R;
import ru.lihachev.norm31937.objects.Variant;
import ru.lihachev.norm31937.widgets.RVCursorAdapter;

public class VariantsAdapter extends RVCursorAdapter<VariantsAdapter.VariantViewHolder> {
   private final LayoutInflater inflater;

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
      holder.checkedTextView.setText(variant.getName());
      holder.descriptiontextView.setText(variant.getS_description());
     // holder.view.setText(variant.getS_description());
      holder.checkedTextView.setChecked(this.selectedIds.get(variant.getId()));
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

   class VariantViewHolder extends RVCursorAdapter.SelectableViewHolder {
      public final CheckedTextView checkedTextView;
      public final TextView descriptiontextView;
      public final TextView  descriptionToggle;
      public final TextView notetextView;
      public final LinearLayout llDescription;
      private boolean visible;

      public boolean isVisible() {
         return visible;
      }

      public void putVisible(boolean visible) {
         this.visible = visible;
      }

      //здесь заполняютя строки нужно сделать это выпадающим списком
      public VariantViewHolder(View itemView) {
         super(itemView);

         this.checkedTextView = (CheckedTextView) itemView.findViewById(R.id.variant);
         this.descriptiontextView = (TextView) itemView.findViewById(R.id.tvDesc);
         this.descriptionToggle = (TextView) itemView.findViewById(R.id.descriptionToggle);
         this.notetextView = (TextView) itemView.findViewById(R.id.tvNote);
         this.checkedTextView.setOnClickListener(this);
         this.descriptionToggle.setOnClickListener(this);
         this.llDescription = (LinearLayout)  itemView.findViewById(R.id.llDescription);//expandable layout

         this.putVisible(false);
         //this.descriptiontextView.setVisibility(View.INVISIBLE);
         //this.notetextView.setVisibility(View.INVISIBLE);
         this.llDescription.setVisibility(View.GONE);
      }
      private ValueAnimator slideAnimator(int start, int end)
      {

         ValueAnimator animator = ValueAnimator.ofInt(start, end);

         animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
               //Update Height
               int value = (Integer) valueAnimator.getAnimatedValue();
               ViewGroup.LayoutParams layoutParams = llDescription.getLayoutParams();
               layoutParams.height = value;
               llDescription.setLayoutParams(layoutParams);
            }
         });
         return animator;
      }

      private void expand()
      {
         llDescription.setVisibility(View.VISIBLE);

         final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
         final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
         llDescription.measure(widthSpec, heightSpec);

         ValueAnimator mAnimator = slideAnimator(0, llDescription.getMeasuredHeight());
         mAnimator.start();
      }

      private void collapse() {
         int finalHeight = llDescription.getHeight();

         ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

         mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
               //Height=0, but it set visibility to GONE
               llDescription.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

         });
         mAnimator.start();
      }
      public void onClick(@NonNull View v) {
        int id =  v.getId();
        switch (id){
           case R.id.descriptionToggle :
              if(llDescription.getVisibility()==View.GONE)
              {
                 expand();
              }
              else
              {
                 collapse();
              }

              break;
           case R.id.variant :
              VariantsAdapter.this.selectedIds.put((int) getItemId(), !((Checkable) v).isChecked());
              VariantsAdapter.this.notifyItemChanged(getLayoutPosition());
              break;

           default :
              break;
        }

      }
   }
}