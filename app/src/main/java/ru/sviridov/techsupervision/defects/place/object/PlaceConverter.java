package ru.sviridov.techsupervision.defects.place.object;

import android.content.ContentValues;
import android.database.Cursor;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaceConverter implements FieldConverter {
   private Place parceRectangle(JSONObject var1) {
      return new Rectangle(parsePoint(var1, "x", "y"), parsePoint(var1, "x2", "y2"));
   }

   private static Point parsePoint(JSONObject var0) {
      return parsePoint(var0, "x", "y");
   }

   private static Point parsePoint(JSONObject var0, String var1, String var2) {
      byte var3 = 0;
      int var4 = var0.optInt(var1);
      String var6 = var0.optString(var2);
      char var5 = (char)var3;
      if (var6 != null) {
         char var7 = var6.charAt(0);
         var5 = var7;
      }

      return new Point(var4, var5);
   }

   public Place fromCursorValue(Cursor var1, int var2) {
      Object var5;
      try {
         JSONObject var3 = new JSONObject(var1.getString(var2));
         Type var6 = Type.fromString(var3.getString("type"));
         switch(var6) {
         case POINT:
            var5 = parsePoint(var3);
            break;
         case RECTANGLE:
            var5 = this.parceRectangle(var3);
            break;
         default:
            JSONException var7 = new JSONException("unknown place type");
            throw var7;
         }
      } catch (JSONException var4) {
       //  Mint.logException(var4);
         var5 = null;
      }

      return (Place)var5;
   }

   public EntityConverter.ColumnType getColumnType() {
      return EntityConverter.ColumnType.TEXT;
   }

   @Override
   public void toContentValue(Object var1, String var2, ContentValues var3) {

   }

   public void toContentValue(Place var1, String var2, ContentValues var3) {
      var3.put(var2, var1.toStoredValue());
   }
}
