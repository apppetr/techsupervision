package ru.sviridov.techsupervision.utils.vectors.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.view.ViewCompat;

import ru.sviridov.techsupervision.utils.vectors.ImagePatch;
import ru.sviridov.techsupervision.utils.vectors.Painting;

public class TextImagePatch implements ImagePatch {

   /* renamed from: pt */
   private static final Paint f88pt = new Paint(1);
   final PointF position;
   final String text;

   public TextImagePatch(String text2, float x, float y) {
      this.text = text2;
      this.position = new PointF(x, y);
   }

   public void draw(Painting target, Canvas cvs) {
      f88pt.setTextSize(target.getDensity() * 12.0f);
      f88pt.setColor(ViewCompat.MEASURED_STATE_MASK);
      cvs.drawText(this.text, this.position.x - 1.0f, this.position.y, f88pt);
      cvs.drawText(this.text, this.position.x + 1.0f, this.position.y, f88pt);
      cvs.drawText(this.text, this.position.x, this.position.y - 1.0f, f88pt);
      cvs.drawText(this.text, this.position.x, this.position.y + 1.0f, f88pt);
      f88pt.setColor(-1);
      cvs.drawText(this.text, this.position.x, this.position.y, f88pt);
      target.postInvalidate();
   }
}
