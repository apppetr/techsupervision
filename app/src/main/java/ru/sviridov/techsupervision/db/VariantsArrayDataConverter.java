package ru.sviridov.techsupervision.db;

import android.content.ContentValues;
import android.database.Cursor;
import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import ru.sviridov.techsupervision.DependencyManager;
import ru.sviridov.techsupervision.objects.Variant;

public class VariantsArrayDataConverter implements FieldConverter {
   public Variant[] fromCursorValue(Cursor var1, int var2) {
      String var3 = var1.getString(var2);
      return (Variant[])DependencyManager.getGson().fromJson(var3, Variant[].class);
   }

   public EntityConverter.ColumnType getColumnType() {
      return EntityConverter.ColumnType.TEXT;
   }

   @Override
   public void toContentValue(Object var1, String var2, ContentValues var3) {

   }

   public void toContentValue(Variant[] var1, String var2, ContentValues var3) {
      var3.put(var2, DependencyManager.getGson().toJson((Object)var1));
   }
}
