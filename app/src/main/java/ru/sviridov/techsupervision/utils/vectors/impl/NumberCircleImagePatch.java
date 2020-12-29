package ru.sviridov.techsupervision.utils.vectors.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import ru.sviridov.techsupervision.utils.vectors.ImagePatch;
import ru.sviridov.techsupervision.utils.vectors.Painting;

public class NumberCircleImagePatch implements ImagePatch {
   private static final Paint pt = new Paint(1);
   private int id;
   final PointF position;
   Rect rect = new Rect();

   public NumberCircleImagePatch(int var1, float var2, float var3) {
      this.id = var1;
      this.position = new PointF(var2, var3);
   }

   public void draw(Painting var1, Canvas var2) {
      pt.setTextSize(var1.getDensity() * 12.0F);
      String var3 = this.id + "";
      pt.getTextBounds(var3, 0, var3.length(), this.rect);
      pt.setColor(-65536);
      var2.drawCircle(this.position.x, this.position.y, (float)this.rect.height(), pt);
      pt.setColor(-1);
      var2.drawText(var3, this.position.x - (float)(this.rect.width() / 2), this.position.y + (float)(this.rect.height() / 2), pt);
      var1.postInvalidate();
   }
}
