package ru.sviridov.techsupervision.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.sviridov.techsupervision.free.R;

public class FloatingHintLayout extends LinearLayout {
   private static final String TAG_HINT = "Hint";
   private TextView tvHint;

   public FloatingHintLayout(Context var1) {
      super(var1);
      this.init(var1, (AttributeSet)null);
   }

   public FloatingHintLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init(var1, var2);
   }

   public FloatingHintLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.init(var1, var2);
   }

   @TargetApi(21)
   public FloatingHintLayout(Context var1, AttributeSet var2, int var3, int var4) {
      super(var1, var2, var3, var4);
      this.init(var1, var2);
   }

   private void addHintView(int var1, TextView var2) {
      this.tvHint = (TextView)LayoutInflater.from(this.getContext()).inflate(R.layout.custom_floating_hint, this, false);
      this.tvHint.setText(var2.getHint());
      this.tvHint.setTag("Hint");
      this.addView(this.tvHint, var1);
      var2.addTextChangedListener(new FloatingLabelWatcher(this.getContext(), this.tvHint, true, var2));
   }

   private void init(Context var1, @Nullable AttributeSet var2) {
      this.setOrientation(LinearLayout.VERTICAL);
   }

   public void addView(View var1, int var2, int var3) {
      super.addView(var1, var2, var3);
   }

   public void addView(@NonNull View var1, int var2, @NonNull LayoutParams var3) {
      super.addView(var1, var2, var3);
      if (!"Hint".equals(var1.getTag()) && var1 instanceof TextView) {
         this.addHintView(var2 + 1, (TextView)var1);
      }

   }
}
