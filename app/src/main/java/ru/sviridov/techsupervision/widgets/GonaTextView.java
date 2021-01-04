package ru.sviridov.techsupervision.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
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



   public void setText(CharSequence text, TextView.BufferType type) {
      super.setText(text, type);
      setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
   }
}
