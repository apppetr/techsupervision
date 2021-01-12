package ru.lihachev.norm31937.db;

import android.content.ContentValues;
import android.database.Cursor;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import org.json.JSONException;
import org.json.JSONObject;
import ru.lihachev.norm31937.objects.MaterialVariant;

public class MaterialVariantDataConverter implements FieldConverter<MaterialVariant> {
   private static final String PROPERTY_PATTERN = "{\"id\":%s, \"name\":\"%s\", \"mat_elem_id\":\"%s\"}";

   public MaterialVariant fromCursorValue(Cursor cursor, int columnIndex) {
      try {
         JSONObject json = new JSONObject(cursor.getString(columnIndex));
         return new MaterialVariant(json.getInt("id"), json.getString("name"), Integer.valueOf(json.optInt("mat_elem_id")));
      } catch (JSONException e) {
         //Mint.logException(e);
         return null;
      }
   }

   public void toContentValue(MaterialVariant value, String key, ContentValues values) {
      values.put(key, String.format(PROPERTY_PATTERN, new Object[]{Integer.valueOf(value.getId()), value.getName(), value.getMatElemId()}));
   }

   public EntityConverter.ColumnType getColumnType() {
      return EntityConverter.ColumnType.TEXT;
   }
}
