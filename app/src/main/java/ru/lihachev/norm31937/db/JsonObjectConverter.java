package ru.lihachev.norm31937.db;

import android.content.ContentValues;
import android.database.Cursor;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonObjectConverter implements FieldConverter<JSONObject> {
   public JSONObject fromCursorValue(Cursor cursor, int columnIndex) {
      try {
         return new JSONObject(cursor.getString(columnIndex));
      } catch (JSONException e) {
         //Mint.logException(e);
         return null;
      }
   }

   public void toContentValue(JSONObject value, String key, ContentValues values) {
      values.put(key, value.toString());
   }

   public EntityConverter.ColumnType getColumnType() {
      return EntityConverter.ColumnType.TEXT;
   }
}

