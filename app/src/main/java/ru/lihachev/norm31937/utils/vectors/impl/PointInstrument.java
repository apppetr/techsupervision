package ru.lihachev.norm31937.utils.vectors.impl;

import android.graphics.Point;
import android.view.MotionEvent;
import ru.lihachev.norm31937.utils.vectors.Painter;
import ru.lihachev.norm31937.utils.vectors.Painting;

public class PointInstrument implements Painter {
   public void handleEvent(Painting target, MotionEvent event) {
      switch (event.getAction()) {
         case 0:
            target.addPatch(new PointItemPatch(new Point((int) event.getX(), (int) event.getY())));
            return;
         default:
            return;
      }
   }
}
