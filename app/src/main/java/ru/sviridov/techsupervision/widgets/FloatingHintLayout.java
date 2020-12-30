/*
 * Decompiled with CFR <Could not determine version>.
 *
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.Context
 *  android.text.TextWatcher
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.LinearLayout
 *  android.widget.TextView
 */
package ru.sviridov.techsupervision.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.widgets.FloatingLabelWatcher;

public class FloatingHintLayout
        extends LinearLayout {
   private static final String TAG_HINT = "Hint";
   private TextView tvHint;

   public FloatingHintLayout(Context context) {
      super(context);
      this.init(context, null);
   }

   public FloatingHintLayout(Context context, AttributeSet attributeSet) {
      super(context, attributeSet);
      this.init(context, attributeSet);
   }

   public FloatingHintLayout(Context context, AttributeSet attributeSet, int n) {
      super(context, attributeSet, n);
      this.init(context, attributeSet);
   }

   @TargetApi(value=21)
   public FloatingHintLayout(Context context, AttributeSet attributeSet, int n, int n2) {
      super(context, attributeSet, n, n2);
      this.init(context, attributeSet);
   }

   private void addHintView(int n, TextView textView) {
      this.tvHint = (TextView)LayoutInflater.from((Context)this.getContext()).inflate(R.layout.custom_floating_hint, (ViewGroup)this, false);
      this.tvHint.setText(textView.getHint());
      this.tvHint.setTag((Object)TAG_HINT);
      this.addView((View)this.tvHint, n);
      textView.addTextChangedListener((TextWatcher)new FloatingLabelWatcher(this.getContext(), (View)this.tvHint, true, textView));
   }

   private void init(Context context, @Nullable AttributeSet attributeSet) {
      this.setOrientation(LinearLayout.VERTICAL);
   }

   public void addView(View view, int n, int n2) {
      super.addView(view, n, n2);
   }

   public void addView(@NonNull View view, int n, @NonNull ViewGroup.LayoutParams layoutParams) {
      super.addView(view, n, layoutParams);
      if (TAG_HINT.equals(view.getTag())) return;
      if (!(view instanceof TextView)) return;
      this.addHintView(n + 1, (TextView)view);
   }
}

