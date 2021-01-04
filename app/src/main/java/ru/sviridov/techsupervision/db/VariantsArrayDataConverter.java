package ru.sviridov.techsupervision.db;

import android.content.ContentValues;
import android.database.Cursor;
import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import ru.sviridov.techsupervision.DependencyManager;
import ru.sviridov.techsupervision.objects.Variant;

public class VariantsArrayDataConverter implements FieldConverter<Variant[]> {
   public Variant[] fromCursorValue(Cursor cursor, int columnIndex) {
      return (Variant[]) DependencyManager.getGson().fromJson(cursor.getString(columnIndex), Variant[].class);
   }

   public void toContentValue(Variant[] value, String key, ContentValues values) {
      values.put(key, DependencyManager.getGson().toJson((Object) value));
   }

   public EntityConverter.ColumnType getColumnType() {
      return EntityConverter.ColumnType.TEXT;
   }
}