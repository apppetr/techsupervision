package ru.sviridov.techsupervision.utils.vectors.impl;

import android.graphics.Point;
import android.view.MotionEvent;
import ru.sviridov.techsupervision.utils.vectors.Painter;
import ru.sviridov.techsupervision.utils.vectors.Painting;

public class PointInstrument implements Painter {
   public void handleEvent(Painting var1, MotionEvent var2) {
      switch(var2.getAction()) {
      case 0:
         var1.addPatch(new PointItemPatch(new Point((int)var2.getX(), (int)var2.getY())));
      default:
      }
   }
}
