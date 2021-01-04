package ru.sviridov.techsupervision.utils.vectors.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.v4.internal.view.SupportMenu;

import ru.sviridov.techsupervision.utils.vectors.ImagePatch;
import ru.sviridov.techsupervision.utils.vectors.Painting;
public class NumberCircleImagePatch implements ImagePatch {

   /* renamed from: pt */
   private static final Paint f86pt = new Paint(1);

   /* renamed from: id */
   private int f87id;
   final PointF position;
   Rect rect = new Rect();

   public NumberCircleImagePatch(int id, float x, float y) {
      this.f87id = id;
      this.position = new PointF(x, y);
   }

   public void draw(Painting target, Canvas cvs) {
      f86pt.setTextSize(target.getDensity() * 12.0f);
      String ids = this.f87id + "";
      f86pt.getTextBounds(ids, 0, ids.length(), this.rect);
      f86pt.setColor(SupportMenu.CATEGORY_MASK);
      cvs.drawCircle(this.position.x, this.position.y, (float) this.rect.height(), f86pt);
      f86pt.setColor(-1);
      cvs.drawText(ids, this.position.x - ((float) (this.rect.width() / 2)), this.position.y + ((float) (this.rect.height() / 2)), f86pt);
      target.postInvalidate();
   }
}
