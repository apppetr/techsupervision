package ru.sviridov.techsupervision.utils.vectors;

import android.view.MotionEvent;

public interface Painter {
   int FLAG_STATUS_INVALIDATED = 1;

   void handleEvent(Painting var1, MotionEvent var2);
}
