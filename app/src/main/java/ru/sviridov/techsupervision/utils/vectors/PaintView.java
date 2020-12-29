package ru.sviridov.techsupervision.utils.vectors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class PaintView extends View implements Painting {
   private Canvas buffer;
   private Bitmap bufferData;
   private Painter currentInstrument = null;
   float density;
   private Paint neutral;
   final Rect rax = new Rect();
   private Canvas saved;
   private Bitmap savedData;
   private Deque undoBuffer;
   int undoLimit = 3;

   public PaintView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.undoBuffer = new ArrayDeque(this.undoLimit);
      this.neutral = new Paint(1);
   }

   public void addPatch(ImagePatch var1) {
      while(this.undoBuffer.size() >= this.undoLimit) {
         ((ImagePatch)this.undoBuffer.pollLast()).draw(this, this.saved);
      }

      this.undoBuffer.push(var1);
      this.invalidate();
   }

   public void draw(Canvas var1) {
      super.draw(var1);
      Iterator var2 = this.undoBuffer.descendingIterator();

      while(var2.hasNext()) {
         ((ImagePatch)var2.next()).draw(this, var1);
      }

      var1.drawBitmap(this.bufferData, 0.0F, 0.0F, this.neutral);
   }

   public void drawOnBase(Bitmap var1) {
      int var2 = (int)((float)this.saved.getWidth() / ((float)var1.getWidth() / (float)var1.getHeight()));
      var2 = (this.saved.getHeight() - var2) / 2;
      this.rax.set(0, var2, this.saved.getWidth(), this.saved.getHeight() - var2);
      this.saved.drawBitmap(var1, (Rect)null, this.rax, this.neutral);
      this.postInvalidate();
   }

   public void fullClear() {
      this.undoBuffer.clear();
      this.bufferData.eraseColor(0);
      this.savedData.eraseColor(0);
   }

   public Canvas getBuffer() {
      return this.buffer;
   }

   public Painter getCurrentInstrument() {
      return this.currentInstrument;
   }

   public float getDensity() {
      float var1;
      if (this.density != 0.0F) {
         var1 = this.density;
      } else {
         var1 = this.getResources().getDisplayMetrics().density;
         this.density = var1;
      }

      return var1;
   }

   public Deque getUndoBuffer() {
      return this.undoBuffer;
   }

   protected void init(int var1, int var2) {
      this.bufferData = Bitmap.createBitmap(var1, var2, Config.ARGB_8888);
      this.savedData = Bitmap.createBitmap(var1, var2, Config.ARGB_8888);
      this.buffer = new Canvas(this.bufferData);
      this.saved = new Canvas(this.savedData);
      this.setBackgroundDrawable(new BitmapDrawable(this.getResources(), this.savedData));
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.bufferData.recycle();
      this.savedData.recycle();
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      if (this.bufferData == null || this.bufferData.getWidth() != var4 - var2 || this.bufferData.getHeight() != var5 - var3) {
         this.init(var4 - var2, var5 - var3);
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      boolean var2;
      if (this.currentInstrument == null) {
         var2 = super.onTouchEvent(var1);
      } else {
         this.bufferData.eraseColor(0);
         this.currentInstrument.handleEvent(this, var1);
         var2 = true;
      }

      return var2;
   }

   public void setCurrentInstrument(Painter var1) {
      this.currentInstrument = var1;
      this.bufferData.eraseColor(0);
      this.invalidate();
   }

   public void setUndoLimit(int var1) {
      this.undoLimit = var1;
   }

   public void undo() {
      if (!this.undoBuffer.isEmpty()) {
         this.undoBuffer.pop();
      }

      this.postInvalidate();
   }
}
