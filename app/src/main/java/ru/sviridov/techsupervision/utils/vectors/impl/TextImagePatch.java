package ru.sviridov.techsupervision.utils.vectors.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import ru.sviridov.techsupervision.utils.vectors.ImagePatch;
import ru.sviridov.techsupervision.utils.vectors.Painting;

public class TextImagePatch implements ImagePatch {
   private static final Paint pt = new Paint(1);
   final PointF position;
   final String text;

   public TextImagePatch(String var1, float var2, float var3) {
      this.text = var1;
      this.position = new PointF(var2, var3);
   }

   public void draw(Painting var1, Canvas var2) {
      pt.setTextSize(var1.getDensity() * 12.0F);
      pt.setColor(-16777216);
      var2.drawText(this.text, this.position.x - 1.0F, this.position.y, pt);
      var2.drawText(this.text, this.position.x + 1.0F, this.position.y, pt);
      var2.drawText(this.text, this.position.x, this.position.y - 1.0F, pt);
      var2.drawText(this.text, this.position.x, this.position.y + 1.0F, pt);
      pt.setColor(-1);
      var2.drawText(this.text, this.position.x, this.position.y, pt);
      var1.postInvalidate();
   }
}
