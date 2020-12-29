package ru.sviridov.techsupervision.utils.vectors.impl.pivots;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Paint.Style;

import org.json.JSONException;

import ru.sviridov.techsupervision.utils.vectors.Painting;

public class PivotOvalInstrument extends PivotInstrument {
   public PivotOvalInstrument() throws JSONException {
   }

   protected void initializePivots(Painting var1) {
      float var2 = (float)Math.min(var1.getBuffer().getHeight(), var1.getBuffer().getWidth()) / 3.0F;
      float var3 = (float)(var1.getBuffer().getWidth() / 2);
      float var4 = (float)(var1.getBuffer().getHeight() / 2);
      PointF var8 = new PointF(var3, var4 - var2);
      PointF var5 = new PointF(var3, var4 + var2);
      PointF var6 = new PointF(var3 - var2, var4);
      PointF var7 = new PointF(var3 + var2, var4);
      this.pivots.add(var6);
      this.pivots.add(var8);
      this.pivots.add(var7);
      this.pivots.add(var5);
   }

   public boolean insideFigure(PointF var1) {
      return true;
   }

   public void onDraw(Painting var1, Canvas var2) {
      this.paint.setColor(-65536);
      this.paint.setStyle(Style.STROKE);
      this.paint.setStrokeWidth(2.0F * var1.getDensity());
      this.rax.set(((PointF)this.pivots.get(0)).x, ((PointF)this.pivots.get(1)).y, ((PointF)this.pivots.get(2)).x, ((PointF)this.pivots.get(3)).y);
      var2.drawOval(this.rax, this.paint);
      var1.postInvalidate();
   }

   protected void restrictPivots(int var1) {
      int var2 = (var1 + 2) % 4;
      PointF var3;
      PointF var4;
      float var5;
      if (var1 % 2 == 0) {
         ((PointF)this.pivots.get(var1)).y = ((PointF)this.pivots.get(var2)).y;
         var3 = (PointF)this.pivots.get((var1 + 1) % 4);
         var4 = (PointF)this.pivots.get((var1 + 3) % 4);
         var5 = ((PointF)this.pivots.get(var2)).x;
         var5 += (((PointF)this.pivots.get(var1)).x - ((PointF)this.pivots.get(var2)).x) / 2.0F;
         var4.x = var5;
         var3.x = var5;
      } else {
         ((PointF)this.pivots.get(var1)).x = ((PointF)this.pivots.get(var2)).x;
         var4 = (PointF)this.pivots.get((var1 + 1) % 4);
         var3 = (PointF)this.pivots.get((var1 + 3) % 4);
         var5 = ((PointF)this.pivots.get(var2)).y;
         var5 += (((PointF)this.pivots.get(var1)).y - ((PointF)this.pivots.get(var2)).y) / 2.0F;
         var3.y = var5;
         var4.y = var5;
      }

   }
}
