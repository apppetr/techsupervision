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
package ru.lihachev.norm31937.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.lihachev.norm31937.free.R;

public class FloatingHintLayout extends LinearLayout {
   private static final String TAG_HINT = "Hint";
   private TextView tvHint;

   public FloatingHintLayout(Context context) {
      super(context);
      init(context, (AttributeSet) null);
   }

   public FloatingHintLayout(Context context, AttributeSet attrs) {
      super(context, attrs);
      init(context, attrs);
   }

   public FloatingHintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      init(context, attrs);
   }

   @TargetApi(21)
   public FloatingHintLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
      super(context, attrs, defStyleAttr, defStyleRes);
      init(context, attrs);
   }

   private void init(Context context, @Nullable AttributeSet attrs) {
      setOrientation(1);
   }

   public void addView(View child, int width, int height) {
      super.addView(child, width, height);
   }

   public void addView(@NonNull View child, int index, @NonNull ViewGroup.LayoutParams params) {
      super.addView(child, index, params);
      if (!TAG_HINT.equals(child.getTag()) && (child instanceof TextView)) {
         addHintView(index + 1, (TextView) child);
      }
   }

   private void addHintView(int index, TextView textView) {
      this.tvHint = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_floating_hint, this, false);
      this.tvHint.setText(textView.getHint());
      this.tvHint.setTag(TAG_HINT);
      addView(this.tvHint, index);
      textView.addTextChangedListener(new FloatingLabelWatcher(getContext(), this.tvHint, true, textView));
   }
}


