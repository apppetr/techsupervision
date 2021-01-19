package ru.lihachev.norm31937.db;

import android.content.ContentValues;
import android.database.Cursor;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import org.json.JSONException;
import org.json.JSONObject;
import ru.lihachev.norm31937.objects.Variant;

public class VariantDataConverter implements FieldConverter<Variant> {
   private static final String PROPERTY_PATTERN = "{\"id\":%s, \"name\":\"%s\", \"manually_added\": %b, \"is_approved\" : %b, \"version\" : \"%s\"}";

   public Variant fromCursorValue(Cursor cursor, int columnIndex) {
      try {
         JSONObject json = new JSONObject(cursor.getString(columnIndex));
         return new Variant(json.getInt("id"), json.getString("name"));
      } catch (JSONException e) {
       //  Mint.logException(e);
         return null;
      }
   }

   public void toContentValue(Variant value, String key, ContentValues values) {
      values.put(key, String.format(PROPERTY_PATTERN, value.getId(), value.getName(), value.isManuallyAdded(), value.getVersion(), value.isUploaded()));
   }

   public EntityConverter.ColumnType getColumnType() {
      return EntityConverter.ColumnType.TEXT;
   }
}

