package ru.sviridov.techsupervision.db;

import android.content.ContentValues;
import android.database.Cursor;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import org.json.JSONException;
import org.json.JSONObject;
import ru.sviridov.techsupervision.objects.Variant;

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
      values.put(key, String.format(PROPERTY_PATTERN, new Object[]{Integer.valueOf(value.getId()), value.getName(), Boolean.valueOf(value.isManuallyAdded()), value.getVersion(), Boolean.valueOf(value.isUploaded())}));
   }

   public EntityConverter.ColumnType getColumnType() {
      return EntityConverter.ColumnType.TEXT;
   }
}

