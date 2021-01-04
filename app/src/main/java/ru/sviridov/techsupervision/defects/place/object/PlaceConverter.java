package ru.sviridov.techsupervision.defects.place.object;

import android.content.ContentValues;
import android.database.Cursor;

import fr.opensagres.xdocreport.document.docx.DocxConstants;
import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import org.json.JSONException;
import org.json.JSONObject;
public class PlaceConverter implements FieldConverter<Place> {
   private static Point parsePoint(JSONObject jsPoint) {
      return parsePoint(jsPoint, "x", "y");
   }

   private static Point parsePoint(JSONObject jsPoint, String xName, String yName) {
      char y = 0;
      int x = jsPoint.optInt(xName);
      String val = jsPoint.optString(yName);
      if (val != null) {
         y = val.charAt(0);
      }
      return new Point(x, y);
   }

   public Place fromCursorValue(Cursor cursor, int columnIndex) {
      try {
         JSONObject jsValue = new JSONObject(cursor.getString(columnIndex));
         switch (Type.fromString(jsValue.getString(DocxConstants.TYPE_ATTR))) {
            case POINT:
               return parsePoint(jsValue);
            case RECTANGLE:
               return parceRectangle(jsValue);
            default:
               throw new JSONException("unknown place type");
         }
      } catch (JSONException e) {
      //   Mint.logException(e);
         return null;
      }
      //Mint.logException(e);
   }

   private Place parceRectangle(JSONObject jsRect) {
      return new Rectangle(parsePoint(jsRect, "x", "y"), parsePoint(jsRect, "x2", "y2"));
   }

   public void toContentValue(Place value, String key, ContentValues values) {
      values.put(key, value.toStoredValue());
   }

   public EntityConverter.ColumnType getColumnType() {
      return EntityConverter.ColumnType.TEXT;
   }
}
