package ru.sviridov.techsupervision.utils.vectors.impl.pivots;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;

import org.json.JSONException;

import java.util.ArrayList;
import ru.sviridov.techsupervision.utils.vectors.Painting;
import ru.sviridov.techsupervision.utils.vectors.impl.LineFImagePatch;

public class PivotLineInstrument extends PivotInstrument {
   LineFImagePatch draw = new LineFImagePatch(new PointF(), new PointF());
   PointF sp = new PointF();
   boolean start = true;

   public PivotLineInstrument() throws JSONException {
   }

   public void handleEvent(Painting var1, MotionEvent var2) {
      boolean var3 = this.start;
      this.start = false;
      if (var3 | false) {
         this.pivots = new ArrayList();
         this.sp.set(var2.getX(), var2.getY());
         this.initializePivots(var1);
         this.pressedPivotIndex = 1;
      }

      super.handleEvent(var1, var2);
   }

   protected void initializePivots(Painting var1) {
      this.pivots.add(new PointF(this.sp.x, this.sp.y));
      this.pivots.add(new PointF(this.sp.x, this.sp.y));
   }

   protected boolean insideFigure(PointF var1) {
      return false;
   }

   public void onDraw(Painting var1, Canvas var2) {
      this.draw.set((PointF)this.pivots.get(0), (PointF)this.pivots.get(1));
      this.draw.draw(var1, var2);
      var1.postInvalidate();
   }

   protected void restrictPivots(int var1) {
   }
}
