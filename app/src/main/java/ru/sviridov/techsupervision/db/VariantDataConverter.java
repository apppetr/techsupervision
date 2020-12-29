package ru.sviridov.techsupervision.db;

import android.content.ContentValues;
import android.database.Cursor;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import org.json.JSONException;
import org.json.JSONObject;
import ru.sviridov.techsupervision.objects.Variant;

public class VariantDataConverter implements FieldConverter {
   private static final String PROPERTY_PATTERN = "{\"id\":%s, \"name\":\"%s\", \"manually_added\": %b, \"is_approved\" : %b, \"version\" : \"%s\"}";

   public Variant fromCursorValue(Cursor var1, int var2) {
      String var5 = var1.getString(var2);

      Variant var6;
      try {
         JSONObject var3 = new JSONObject(var5);
         var6 = new Variant(var3.getInt("id"), var3.getString("name"));
      } catch (JSONException var4) {
     //    Mint.logException(var4);
         var6 = null;
      }

      return var6;
   }

   public EntityConverter.ColumnType getColumnType() {
      return EntityConverter.ColumnType.TEXT;
   }

   @Override
   public void toContentValue(Object var1, String var2, ContentValues var3) {
   }

   public void toContentValue(Variant var1, String var2, ContentValues var3) {
      var3.put(var2, String.format("{\"id\":%s, \"name\":\"%s\", \"manually_added\": %b, \"is_approved\" : %b, \"version\" : \"%s\"}", var1.getId(), var1.getName(), var1.isManuallyAdded(), var1.getVersion(), var1.isUploaded()));
   }
}
