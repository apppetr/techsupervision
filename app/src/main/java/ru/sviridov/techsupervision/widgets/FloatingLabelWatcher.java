package ru.sviridov.techsupervision.widgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import ru.sviridov.techsupervision.free.R;

public class FloatingLabelWatcher implements TextWatcher {
   int animatedLabel = 1;
   private final Context context;
   private TextView etText;
   private final View label;
   private boolean mHideLabelIfEmpty;

   public FloatingLabelWatcher(Context var1, View var2, boolean var3, TextView var4) {
      this.context = var1;
      this.label = var2;
      this.mHideLabelIfEmpty = var3;
      this.etText = var4;
   }

   public void afterTextChanged(Editable var1) {
      byte var2 = 8;
      if (this.label != null) {
         if (!this.mHideLabelIfEmpty) {
            this.label.setVisibility(View.INVISIBLE);
         } else {
            this.label.setVisibility(View.VISIBLE);
            String var3 = this.etText.getText().toString();
            if (var3.length() != 0 || this.animatedLabel != 1) {
               this.label.clearAnimation();
               View var4 = this.label;
               if (var3.length() != 0) {
                  var2 = 0;
               }

               var4.setVisibility(var2);
               Animation var5;
               if (var3.length() == 0 && this.animatedLabel != 1) {
                  this.animatedLabel = 1;
                  var5 = AnimationUtils.loadAnimation(this.context, R.anim.fadein_to_bottom);
                  this.label.startAnimation(var5);
               } else if (this.animatedLabel != 2) {
                  this.animatedLabel = 2;
                  var5 = AnimationUtils.loadAnimation(this.context, R.anim.fadein_from_bottom);
                  this.label.startAnimation(var5);
               }
            }
         }
      }

   }

   public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
   }

   public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
   }

   public void setmHideLabelIfEmpty(boolean var1) {
      this.mHideLabelIfEmpty = var1;
   }
}
