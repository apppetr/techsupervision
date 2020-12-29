package ru.sviridov.techsupervision.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class GonaTextView extends android.support.v7.widget.AppCompatTextView {
   public GonaTextView(Context var1) {
      super(var1);
   }

   public GonaTextView(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public GonaTextView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }



   public void setText(CharSequence var1, BufferType var2) {
      super.setText(var1, var2);
      byte var3;
      if (TextUtils.isEmpty(var1)) {
         var3 = 8;
      } else {
         var3 = 0;
      }

      this.setVisibility(var3);
   }
}
