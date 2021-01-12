package ru.lihachev.norm31937.widgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import ru.lihachev.norm31937.free.R;

public class FloatingLabelWatcher implements TextWatcher {
   int animatedLabel = 1;
   private final Context context;
   private TextView etText;
   private final View label;
   private boolean mHideLabelIfEmpty;

   public FloatingLabelWatcher(Context context2, View label2, boolean mHideLabelIfEmpty2, TextView etText2) {
      this.context = context2;
      this.label = label2;
      this.mHideLabelIfEmpty = mHideLabelIfEmpty2;
      this.etText = etText2;
   }

   public void beforeTextChanged(CharSequence s, int start, int count, int after) {
   }

   public void onTextChanged(CharSequence s, int start, int before, int count) {
   }

   public void setmHideLabelIfEmpty(boolean mHideLabelIfEmpty2) {
      this.mHideLabelIfEmpty = mHideLabelIfEmpty2;
   }

   public void afterTextChanged(Editable s) {
      int i = 8;
      if (this.label != null) {
         if (!this.mHideLabelIfEmpty) {
            this.label.setVisibility(View.VISIBLE);
            return;
         }
         this.label.setVisibility(View.GONE);
         String text = this.etText.getText().toString();
         if (text.length() != 0 || this.animatedLabel != 1) {
            this.label.clearAnimation();
            View view = this.label;
            if (text.length() != 0) {
               i = 0;
            }
            view.setVisibility(i);
            if (text.length() == 0 && this.animatedLabel != 1) {
               this.animatedLabel = 1;
               this.label.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.fadein_to_bottom));
            } else if (this.animatedLabel != 2) {
               this.animatedLabel = 2;
               this.label.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.fadein_from_bottom));
            }
         }
      }
   }
}
