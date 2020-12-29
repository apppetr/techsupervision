package ru.sviridov.techsupervision.utils.vectors;

import android.content.Context;
import android.graphics.Canvas;

public interface Painting {
   void addPatch(ImagePatch var1);

   Canvas getBuffer();

   Context getContext();

   float getDensity();

   void postInvalidate();
}
