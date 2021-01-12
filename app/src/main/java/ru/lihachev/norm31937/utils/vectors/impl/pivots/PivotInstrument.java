package ru.lihachev.norm31937.utils.vectors.impl.pivots;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.MotionEvent;
import com.cab404.jsonm.core.JSONMaker;
import com.cab404.jsonm.impl.SimpleJSONMaker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.lihachev.norm31937.utils.JSONParcelable;
import ru.lihachev.norm31937.utils.JSONUtils;
import ru.lihachev.norm31937.utils.vectors.ImagePatch;
import ru.lihachev.norm31937.utils.vectors.Painter;
import ru.lihachev.norm31937.utils.vectors.Painting;

public abstract class PivotInstrument implements Painter, Parcelable, ImagePatch, JSONParcelable {
   public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {
      public PivotInstrument createFromParcel(Parcel param1) {
         return null;
      }

      public PivotInstrument[] newArray(int var1) {
         return new PivotInstrument[var1];
      }
   };
   protected static final int FIGURE_DRAGGED = 65536;
   public static final JSONParcelable.Creator JSON_CREATOR = new JSONParcelable.Creator() {


      public PivotInstrument createFromJSONObject(JSONObject object) {
         try {
            PivotInstrument instrument = (PivotInstrument) Class.forName(object.optString("class")).newInstance();
            JSONArray pivots = object.optJSONArray("pivots");
            instrument.pivots = new ArrayList();
            for (Object point : JSONUtils.iterate(pivots)) {
               JSONObject  jobj = (JSONObject)point;
               instrument.pivots.add(new PointF((float) jobj.optDouble("x"), (float)jobj.optDouble("y")));
            }
            instrument.onDeserialized(object.opt("dat"));
            return instrument;
         } catch (InstantiationException e) {
            throw new RuntimeException("", e);
         } catch (IllegalAccessException e2) {
            throw new RuntimeException("", e2);
         } catch (ClassNotFoundException e3) {
            throw new RuntimeException("", e3);
         }
      }

   };
   protected Paint paint = new Paint(1);
   PointF pax = new PointF();
   PointF pbx = new PointF();
   public List<PointF> pivots;
   protected int pressedPivotIndex = -1;
   RectF rax = new RectF();
   JSONMaker thing = new SimpleJSONMaker();

   public PivotInstrument() throws JSONException {
      this.thing.add("point", "{'x': *, 'y': *}");
   }

   protected Object additionalObjectData() {
      return null;
   }

   public int describeContents() {
      return 0;
   }

   public void draw(Painting var1, Canvas var2) {
      this.onDraw(var1, var2);
   }

   protected void drawPivot(int var1, Painting var2) {
      PointF var3 = (PointF)this.pivots.get(var1);
      float var4 = this.pivotRadius(var2);
      this.rax.set(var3.x - var4, var3.y - var4, var3.x + var4, var3.y + var4);
      this.paint.setStyle(Style.FILL);
      this.paint.setColor(-1);
      var2.getBuffer().drawOval(this.rax, this.paint);
      this.paint.setStyle(Style.STROKE);
      this.paint.setStrokeWidth(var2.getDensity() * 4.0F);
      this.paint.setColor(-16537357);
      var2.getBuffer().drawOval(this.rax, this.paint);
   }

   public void handleEvent(Painting var1, MotionEvent var2) {
      if (this.pivots == null) {
         this.pivots = new ArrayList();
         this.initializePivots(var1);
      }

      this.handleMotionEvent(var1, var2);
      this.onDraw(var1, var1.getBuffer());

      for(int var3 = 0; var3 < this.pivots.size(); ++var3) {
         this.drawPivot(var3, var1);
      }

   }

   protected void handleMotionEvent(Painting var1, MotionEvent var2) {
      this.pax.set(var2.getX(), var2.getY());
      switch(var2.getAction()) {
      case 0:
         float var3 = this.pivotRadius(var1);
         this.pressedPivotIndex = -1;

         for(int var4 = 0; var4 < this.pivots.size(); ++var4) {
            PointF var5 = (PointF)this.pivots.get(var4);
            this.rax.set(var5.x - var3, var5.y - var3, var5.x + var3, var5.y + var3);
            if (this.rax.contains(this.pax.x, this.pax.y)) {
               this.pressedPivotIndex = var4;
               break;
            }
         }

         if (this.pressedPivotIndex == -1 && this.insideFigure(this.pax)) {
            this.pressedPivotIndex = 65536;
            this.moveFigure();
         }
         break;
      case 1:
         this.pressedPivotIndex = -1;
         break;
      case 2:
         if (this.pressedPivotIndex == 65536) {
            this.moveFigure();
         } else if (this.pressedPivotIndex != -1) {
            ((PointF)this.pivots.get(this.pressedPivotIndex)).set(this.pax.x, this.pax.y);
            this.restrictPivots(this.pressedPivotIndex);
         }
      }

   }

   protected abstract void initializePivots(Painting var1);

   protected abstract boolean insideFigure(PointF var1);

   protected void moveFigure() {
      this.pbx.set(0.0F, 0.0F);

      Iterator var1;
      PointF var2;
      PointF var3;
      for(var1 = this.pivots.iterator(); var1.hasNext(); var3.y += var2.y) {
         var2 = (PointF)var1.next();
         var3 = this.pbx;
         var3.x += var2.x;
         var3 = this.pbx;
      }

      PointF var4 = this.pbx;
      var4.x /= (float)this.pivots.size();
      var4 = this.pbx;
      var4.y /= (float)this.pivots.size();
      var4 = this.pax;
      var4.x -= this.pbx.x;
      var4 = this.pax;
      var4.y -= this.pbx.y;

      for(var1 = this.pivots.iterator(); var1.hasNext(); var2.y += this.pax.y) {
         var2 = (PointF)var1.next();
         var2.x += this.pax.x;
      }

   }

   protected void onDeserialized(Object var1) {
   }

   public abstract void onDraw(Painting var1, Canvas var2);

   protected float pivotRadius(Painting var1) {
      return 16.0F * var1.getDensity();
   }

   protected abstract void restrictPivots(int var1);

   public void writeToJSONObject(JSONObject var1) throws JSONException {
      var1.put("class", this.getClass().getName());
      Iterator var2 = this.pivots.iterator();

      while(var2.hasNext()) {
         PointF var3 = (PointF)var2.next();
         var1.accumulate("pivots", this.thing.make("point", var3.x, var3.y));
      }

      var1.put("dat", this.additionalObjectData());
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(this.getClass().getName());
      var1.writeInt(this.pivots.size());
      Iterator var3 = this.pivots.iterator();

      while(var3.hasNext()) {
         ((PointF)var3.next()).writeToParcel(var1, 0);
      }

   }
}
