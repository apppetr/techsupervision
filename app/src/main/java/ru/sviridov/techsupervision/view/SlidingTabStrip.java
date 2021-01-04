package ru.sviridov.techsupervision.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

class SlidingTabStrip extends LinearLayout {
   private static final byte DEFAULT_BOTTOM_BORDER_COLOR_ALPHA = 38;
   private static final int DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS = 0;
   private static final int DEFAULT_SELECTED_INDICATOR_COLOR = -13388315;
   private static final int SELECTED_INDICATOR_THICKNESS_DIPS = 3;
   private final Paint mBottomBorderPaint;
   private final int mBottomBorderThickness;
   private SlidingTabLayout.TabColorizer mCustomTabColorizer;
   private final int mDefaultBottomBorderColor;
   private final SimpleTabColorizer mDefaultTabColorizer;
   private final Paint mSelectedIndicatorPaint;
   private final int mSelectedIndicatorThickness;
   private int mSelectedPosition;
   private float mSelectionOffset;

   SlidingTabStrip(Context context) {
      this(context, (AttributeSet) null);
   }

   SlidingTabStrip(Context context, AttributeSet attrs) {
      super(context, attrs);
      setWillNotDraw(false);
      float density = getResources().getDisplayMetrics().density;
      TypedValue outValue = new TypedValue();
      context.getTheme().resolveAttribute(16842800, outValue, true);
      this.mDefaultBottomBorderColor = setColorAlpha(outValue.data, DEFAULT_BOTTOM_BORDER_COLOR_ALPHA);
      this.mDefaultTabColorizer = new SimpleTabColorizer();
      this.mDefaultTabColorizer.setIndicatorColors(DEFAULT_SELECTED_INDICATOR_COLOR);
      this.mBottomBorderThickness = (int) (0.0f * density);
      this.mBottomBorderPaint = new Paint();
      this.mBottomBorderPaint.setColor(this.mDefaultBottomBorderColor);
      this.mSelectedIndicatorThickness = (int) (3.0f * density);
      this.mSelectedIndicatorPaint = new Paint();
   }

   /* access modifiers changed from: package-private */
   public void setCustomTabColorizer(SlidingTabLayout.TabColorizer customTabColorizer) {
      this.mCustomTabColorizer = customTabColorizer;
      invalidate();
   }

   /* access modifiers changed from: package-private */
   public void setSelectedIndicatorColors(int... colors) {
      this.mCustomTabColorizer = null;
      this.mDefaultTabColorizer.setIndicatorColors(colors);
      invalidate();
   }

   /* access modifiers changed from: package-private */
   public void onViewPagerPageChanged(int position, float positionOffset) {
      this.mSelectedPosition = position;
      this.mSelectionOffset = positionOffset;
      invalidate();
   }

   /* access modifiers changed from: protected */
   public void onDraw(Canvas canvas) {
      int height = getHeight();
      int childCount = getChildCount();
      SlidingTabLayout.TabColorizer tabColorizer = this.mCustomTabColorizer != null ? this.mCustomTabColorizer : this.mDefaultTabColorizer;
      if (childCount > 0) {
         View selectedTitle = getChildAt(this.mSelectedPosition);
         int left = selectedTitle.getLeft();
         int right = selectedTitle.getRight();
         int color = tabColorizer.getIndicatorColor(this.mSelectedPosition);
         if (this.mSelectionOffset > 0.0f && this.mSelectedPosition < getChildCount() - 1) {
            int nextColor = tabColorizer.getIndicatorColor(this.mSelectedPosition + 1);
            if (color != nextColor) {
               color = blendColors(nextColor, color, this.mSelectionOffset);
            }
            View nextTitle = getChildAt(this.mSelectedPosition + 1);
            left = (int) ((this.mSelectionOffset * ((float) nextTitle.getLeft())) + ((1.0f - this.mSelectionOffset) * ((float) left)));
            right = (int) ((this.mSelectionOffset * ((float) nextTitle.getRight())) + ((1.0f - this.mSelectionOffset) * ((float) right)));
         }
         this.mSelectedIndicatorPaint.setColor(color);
         canvas.drawRect((float) left, (float) (height - this.mSelectedIndicatorThickness), (float) right, (float) height, this.mSelectedIndicatorPaint);
      }
      canvas.drawRect(0.0f, (float) (height - this.mBottomBorderThickness), (float) getWidth(), (float) height, this.mBottomBorderPaint);
   }

   private static int setColorAlpha(int color, byte alpha) {
      return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
   }

   private static int blendColors(int color1, int color2, float ratio) {
      float inverseRation = 1.0f - ratio;
      return Color.rgb((int) ((((float) Color.red(color1)) * ratio) + (((float) Color.red(color2)) * inverseRation)), (int) ((((float) Color.green(color1)) * ratio) + (((float) Color.green(color2)) * inverseRation)), (int) ((((float) Color.blue(color1)) * ratio) + (((float) Color.blue(color2)) * inverseRation)));
   }

   /* renamed from: com.google.samples.apps.iosched.ui.widget.SlidingTabStrip$SimpleTabColorizer */
   private static class SimpleTabColorizer implements SlidingTabLayout.TabColorizer {
      private int[] mIndicatorColors;

      private SimpleTabColorizer() {
      }

      public final int getIndicatorColor(int position) {
         return this.mIndicatorColors[position % this.mIndicatorColors.length];
      }

      /* access modifiers changed from: package-private */
      public void setIndicatorColors(int... colors) {
         this.mIndicatorColors = colors;
      }
   }
}
