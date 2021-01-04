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
   private Paint neutral = new Paint(1);
   final Rect rax = new Rect();
   private Canvas saved;
   private Bitmap savedData;
   private Deque<ImagePatch> undoBuffer = new ArrayDeque(this.undoLimit);
   int undoLimit = 3;

   public PaintView(Context context, AttributeSet attrs) {
      super(context, attrs);
   }

   public boolean onTouchEvent(MotionEvent event) {
      if (this.currentInstrument == null) {
         return super.onTouchEvent(event);
      }
      this.bufferData.eraseColor(0);
      this.currentInstrument.handleEvent(this, event);
      return true;
   }

   public void draw(Canvas canvas) {
      super.draw(canvas);
      Iterator<ImagePatch> iterator = this.undoBuffer.descendingIterator();
      while (iterator.hasNext()) {
         iterator.next().draw(this, canvas);
      }
      canvas.drawBitmap(this.bufferData, 0.0f, 0.0f, this.neutral);
   }

   public void drawOnBase(Bitmap bmp) {
      int height_padding = (this.saved.getHeight() - ((int) (((float) this.saved.getWidth()) / (((float) bmp.getWidth()) / ((float) bmp.getHeight()))))) / 2;
      this.rax.set(0, height_padding, this.saved.getWidth(), this.saved.getHeight() - height_padding);
      this.saved.drawBitmap(bmp, (Rect) null, this.rax, this.neutral);
      postInvalidate();
   }

   /* access modifiers changed from: protected */
   public void onLayout(boolean changed, int left, int top, int right, int bottom) {
      super.onLayout(changed, left, top, right, bottom);
      if (this.bufferData == null || this.bufferData.getWidth() != right - left || this.bufferData.getHeight() != bottom - top) {
         init(right - left, bottom - top);
      }
   }

   /* access modifiers changed from: protected */
   public void init(int w, int h) {
      this.bufferData = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
      this.savedData = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
      this.buffer = new Canvas(this.bufferData);
      this.saved = new Canvas(this.savedData);
      setBackgroundDrawable(new BitmapDrawable(getResources(), this.savedData));
   }

   public Painter getCurrentInstrument() {
      return this.currentInstrument;
   }

   public void setCurrentInstrument(Painter currentInstrument2) {
      this.currentInstrument = currentInstrument2;
      this.bufferData.eraseColor(0);
      invalidate();
   }

   public Canvas getBuffer() {
      return this.buffer;
   }

   public void addPatch(ImagePatch patch) {
      while (this.undoBuffer.size() >= this.undoLimit) {
         this.undoBuffer.pollLast().draw(this, this.saved);
      }
      this.undoBuffer.push(patch);
      invalidate();
   }

   public float getDensity() {
      if (this.density != 0.0f) {
         return this.density;
      }
      float f = getResources().getDisplayMetrics().density;
      this.density = f;
      return f;
   }

   public void undo() {
      if (!this.undoBuffer.isEmpty()) {
         this.undoBuffer.pop();
      }
      postInvalidate();
   }

   public void fullClear() {
      this.undoBuffer.clear();
      this.bufferData.eraseColor(0);
      this.savedData.eraseColor(0);
   }

   /* access modifiers changed from: protected */
   public void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.bufferData.recycle();
      this.savedData.recycle();
   }

   public Deque<ImagePatch> getUndoBuffer() {
      return this.undoBuffer;
   }

   public void setUndoLimit(int undoLimit2) {
      this.undoLimit = undoLimit2;
   }
}
