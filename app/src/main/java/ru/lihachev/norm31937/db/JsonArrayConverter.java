package ru.lihachev.norm31937.db;

import android.content.ContentValues;
import android.database.Cursor;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import org.json.JSONArray;
import org.json.JSONException;

public class JsonArrayConverter implements FieldConverter<JSONArray> {
   public JSONArray fromCursorValue(Cursor cursor, int columnIndex) {
      try {
         return new JSONArray(cursor.getString(columnIndex));
      } catch (JSONException e) {
       //  Mint.logException(e);
         return null;
      }
   }

   public void toContentValue(JSONArray value, String key, ContentValues values) {
      values.put(key, value.toString());
   }

   public EntityConverter.ColumnType getColumnType() {
      return EntityConverter.ColumnType.TEXT;
   }
}
