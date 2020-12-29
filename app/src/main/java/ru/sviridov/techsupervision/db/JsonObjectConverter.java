package ru.sviridov.techsupervision.db;

import android.content.ContentValues;
import android.database.Cursor;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonObjectConverter implements FieldConverter {
   public JSONObject fromCursorValue(Cursor var1, int var2) {
      JSONObject var3;
      JSONObject var5;
      try {
         var3 = new JSONObject(var1.getString(var2));
      } catch (JSONException var4) {
       //  Mint.logException(var4);
         var5 = null;
         return var5;
      }

      var5 = var3;
      return var5;
   }

   public EntityConverter.ColumnType getColumnType() {
      return EntityConverter.ColumnType.TEXT;
   }

   @Override
   public void toContentValue(Object var1, String var2, ContentValues var3) {

   }

   public void toContentValue(JSONObject var1, String var2, ContentValues var3) {
      var3.put(var2, var1.toString());
   }
}
