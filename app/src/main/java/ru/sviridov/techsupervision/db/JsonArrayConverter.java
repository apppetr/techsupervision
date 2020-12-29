package ru.sviridov.techsupervision.db;

import android.content.ContentValues;
import android.database.Cursor;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import org.json.JSONArray;
import org.json.JSONException;

public class JsonArrayConverter implements FieldConverter {
   public JSONArray fromCursorValue(Cursor var1, int var2) {
      JSONArray var3;
      JSONArray var5;
      try {
         var3 = new JSONArray(var1.getString(var2));
      } catch (JSONException var4) {
      //   Mint.logException(var4);
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

   public void toContentValue(JSONArray var1, String var2, ContentValues var3) {
      var3.put(var2, var1.toString());
   }
}
