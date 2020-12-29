package ru.sviridov.techsupervision.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;
import ru.sviridov.techsupervision.objects.Def2Elem;
import ru.sviridov.techsupervision.objects.Defect2Reason;
import ru.sviridov.techsupervision.objects.Elem2Mat;
import ru.sviridov.techsupervision.objects.Reason2Compensation;
import ru.sviridov.techsupervision.objects.Variant;
import ru.sviridov.techsupervision.values.ValuesProvider;

public class UpdateService extends Service {
   private static final String UPLOAD_URL = "http://techsupervision-live.appspot.com/content/v1.0/upload";
   private Gson gson;
   private RequestQueue requestQueue;
   private boolean uploading = false;

   public static void call(Context var0) {
      var0.startService(new Intent(var0, UpdateService.class));
   }

   private List getReason2compensations() {
      Cursor var1 = this.getContentResolver().query(ValuesProvider.uri("reasons2compensations"), (String[])null, "manually_added=1 AND uploaded=0", (String[])null, (String)null);
      ArrayList var2 = new ArrayList(var1.getCount());
      int var3 = var1.getColumnIndex("reason_id");
      int var4 = var1.getColumnIndex("compensation_id");
      int var5 = var1.getColumnIndex("manually_added");
      int var6 = var1.getColumnIndex("uploaded");
      int var7 = var1.getColumnIndex("version");

      while(var1.moveToNext()) {
         Reason2Compensation var8 = new Reason2Compensation();
         var8.setReasonId(var1.getInt(var3));
         var8.setCompensationId(var1.getInt(var4));
         boolean var9;
         if (var1.getInt(var5) == 1) {
            var9 = true;
         } else {
            var9 = false;
         }

         var8.setIsManuallyAdded(var9);
         var8.setVersion(var1.getString(var7));
         if (var1.getInt(var6) == 1) {
            var9 = true;
         } else {
            var9 = false;
         }

         var8.setIsUploaded(var9);
         var2.add(var8);
      }

      return var2;
   }

   private boolean isSuccess(JSONObject var1) {
      String var3;
      if (var1.isNull("error")) {
         var3 = null;
      } else {
         var3 = var1.optString("error");
      }

     // if (var3 != null) {
      //   Mint.logEvent("upload error", MintLogLevel.Info, "error", var3);
     // }

      boolean var2;
      if (var3 == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private void markUploaded(UploadVariantData var1) {
      this.setUploadedVariants("elements", var1.getElements());
      this.setUploadedVariants("materials", var1.getMaterials());
      this.setUploadedElem2mats("elements2materials", var1.getElem2mats());
      this.setUploadedDef2Elems("defects2elements", var1.getDef2elems());
      this.setUploadedVariants("defects", var1.getDefects());
      this.setUploadedDef2Reasons("defects2reasons", var1.getDefect2reasons());
      this.setUploadedVariants("reasons", var1.getReasons());
      this.setUploadedReason2compensations("reasons2compensations", var1.getReason2compensations());
      this.setUploadedVariants("compensations", var1.getCompensations());
   }

   private UploadVariantData prepareUploadData(Intent var1) {
      UploadVariantData var3 = new UploadVariantData();
      User var2 = new User();
      var2.setDbVersion(2);
      var2.setToken(StrongMan.userThing);
      var3.setApiToken("d0e849ad-4591-4230-8cab-08e132854e2b");
      var3.setUser(var2);
      var3.setMaterials(this.getVariants("materials"));
      var3.setElements(this.getVariants("elements"));
      var3.setElem2mats(this.getElem2mats());
      var3.setDef2elems(this.getDef2Elems());
      var3.setDefects(this.getVariants("defects"));
      var3.setDefect2reasons(this.getDefect2reasons());
      var3.setReasons(this.getVariants("reasons"));
      var3.setReason2compensations(this.getReason2compensations());
      var3.setCompensations(this.getVariants("compensations"));
      return var3;
   }

   private void upload(Intent var1) {
      final UploadVariantData var2 = this.prepareUploadData(var1);
      if (!var2.isEmpty()) {
         this.uploading = true;
         Response.Listener var4 = new Response.Listener() {
            @Override
            public void onResponse(Object response) {

            }

            public void onResponse(JSONObject var1) {
               UpdateService.this.uploading = false;
               if (UpdateService.this.isSuccess(var1)) {
                  UpdateService.this.markUploaded(var2);
               }

            }
         };
         Response.ErrorListener var3 = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError var1) {
               UpdateService.this.uploading = false;
            }
         };
         String var6 = this.gson.toJson(var2);
        // JsonObjectRequest var5 = new JsonObjectRequest(1, "http://techsupervision-live.appspot.com/content/v1.0/upload", var4, var3);
         VolleyLog.v(String.valueOf(var6));
         //var5.setRetryPolicy(new DefaultRetryPolicy(10000, 3, 1.0F));
        // this.requestQueue.add(var5);
      }

   }

   public List getDef2Elems() {
      Cursor var1 = this.getContentResolver().query(ValuesProvider.uri("defects2elements"), (String[])null, "manually_added=1 AND uploaded=0", (String[])null, (String)null);
      ArrayList var2 = new ArrayList(var1.getCount());
      int var3 = var1.getColumnIndex("defect_id");
      int var4 = var1.getColumnIndex("mat_elem_id");
      int var5 = var1.getColumnIndex("manually_added");
      int var6 = var1.getColumnIndex("uploaded");
      int var7 = var1.getColumnIndex("version");

      while(var1.moveToNext()) {
         Def2Elem var8 = new Def2Elem();
         var8.setDefectId(var1.getInt(var3));
         var8.setMatElemId(var1.getInt(var4));
         boolean var9;
         if (var1.getInt(var5) == 1) {
            var9 = true;
         } else {
            var9 = false;
         }

         var8.setIsManuallyAdded(var9);
         var8.setVersion(var1.getString(var7));
         if (var1.getInt(var6) == 1) {
            var9 = true;
         } else {
            var9 = false;
         }

         var8.setIsUploaded(var9);
         var2.add(var8);
      }

      return var2;
   }

   public List getDefect2reasons() {
      Cursor var1 = this.getContentResolver().query(ValuesProvider.uri("defects2reasons"), (String[])null, "manually_added=1 AND uploaded=0", (String[])null, (String)null);
      ArrayList var2 = new ArrayList(var1.getCount());
      int var3 = var1.getColumnIndex("defect_id");
      int var4 = var1.getColumnIndex("reason_id");
      int var5 = var1.getColumnIndex("manually_added");
      int var6 = var1.getColumnIndex("uploaded");
      int var7 = var1.getColumnIndex("version");

      while(var1.moveToNext()) {
         Defect2Reason var8 = new Defect2Reason();
         var8.setDefectId(var1.getInt(var3));
         var8.setReasonId(var1.getInt(var4));
         boolean var9;
         if (var1.getInt(var5) == 1) {
            var9 = true;
         } else {
            var9 = false;
         }

         var8.setIsManuallyAdded(var9);
         var8.setVersion(var1.getString(var7));
         if (var1.getInt(var6) == 1) {
            var9 = true;
         } else {
            var9 = false;
         }

         var8.setIsUploaded(var9);
         var2.add(var8);
      }

      return var2;
   }

   public List getElem2mats() {
      Cursor var1 = this.getContentResolver().query(ValuesProvider.uri("elements2materials"), (String[])null, "manually_added=1 AND uploaded=0", (String[])null, (String)null);
      ArrayList var2 = new ArrayList(var1.getCount());
      int var3 = var1.getColumnIndex("element_id");
      int var4 = var1.getColumnIndex("material_id");
      int var5 = var1.getColumnIndex("mat_elem_id");
      int var6 = var1.getColumnIndex("manually_added");
      int var7 = var1.getColumnIndex("uploaded");
      int var8 = var1.getColumnIndex("version");

      while(var1.moveToNext()) {
         Elem2Mat var9 = new Elem2Mat();
         var9.setElementId(var1.getInt(var3));
         var9.setMaterialId(var1.getInt(var4));
         var9.setMatElemId(var1.getInt(var5));
         boolean var10;
         if (var1.getInt(var6) == 1) {
            var10 = true;
         } else {
            var10 = false;
         }

         var9.setIsManuallyAdded(var10);
         var9.setVersion(var1.getString(var8));
         if (var1.getInt(var7) == 1) {
            var10 = true;
         } else {
            var10 = false;
         }

         var9.setIsUploaded(var10);
         var2.add(var9);
      }

      return var2;
   }

   public List getVariants(String var1) {
      Cursor var2 = this.getContentResolver().query(ValuesProvider.uri(var1), (String[])null, "manually_added=1 AND uploaded=0", (String[])null, (String)null);
      ArrayList var3 = new ArrayList(var2.getCount());
      int var4 = var2.getColumnIndex("_id");
      int var5 = var2.getColumnIndex("name");
      int var6 = var2.getColumnIndex("manually_added");
      int var7 = var2.getColumnIndex("uploaded");
      int var8 = var2.getColumnIndex("version");

      while(var2.moveToNext()) {
         Variant var10 = new Variant(var2.getInt(var4), var2.getString(var5));
         boolean var9;
         if (var2.getInt(var6) == 1) {
            var9 = true;
         } else {
            var9 = false;
         }

         var10.setIsManuallyAdded(var9);
         var10.setVersion(var2.getString(var8));
         if (var2.getInt(var7) == 1) {
            var9 = true;
         } else {
            var9 = false;
         }

         var10.setIsUploaded(var9);
         var3.add(var10);
      }

      return var3;
   }

   public IBinder onBind(Intent var1) {
      return null;
   }

   public void onCreate() {
      super.onCreate();
      this.requestQueue = Volley.newRequestQueue(this, new HurlStack());
      this.gson = (new GsonBuilder()).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).excludeFieldsWithModifiers(64, 8, 128).create();
   }

   public void onDestroy() {
      super.onDestroy();
      this.uploading = false;
      this.requestQueue.cancelAll(new RequestQueue.RequestFilter() {
         public boolean apply(Request var1) {
            return true;
         }
      });
   }

   public int onStartCommand(Intent var1, int var2, int var3) {
      super.onStartCommand(var1, var2, var3);
      if (!this.uploading) {
         this.upload(var1);
      }

      return Service.START_STICKY;
   }

   public void setUploadedDef2Elems(String var1, List var2) {
      if (!var2.isEmpty()) {
         StringBuilder var3 = new StringBuilder();
         var3.append("mat_elem_id").append(" in (");
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            var3.append(((Def2Elem)var4.next()).getMatElemId());
            var3.append(',');
         }

         var3.deleteCharAt(var3.length() - 1);
         var3.append(')');
         ContentValues var5 = new ContentValues();
         var5.put("uploaded", true);
         this.getContentResolver().update(ValuesProvider.uri(var1), var5, var3.toString(), (String[])null);
      }

   }

   public void setUploadedDef2Reasons(String var1, List var2) {
      if (!var2.isEmpty()) {
         ContentResolver var3 = this.getContentResolver();
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            Defect2Reason var5 = (Defect2Reason)var4.next();
            ContentValues var6 = new ContentValues();
            var6.put("uploaded", true);
            var3.update(ValuesProvider.uri(var1), var6, String.format("defect_id=%d AND reason_id=%d", var5.getDefectId(), var5.getReasonId()), (String[])null);
         }
      }

   }

   public void setUploadedElem2mats(String var1, List var2) {
      if (!var2.isEmpty()) {
         StringBuilder var3 = new StringBuilder();
         var3.append("mat_elem_id").append(" in (");
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            var3.append(((Elem2Mat)var4.next()).getMatElemId());
            var3.append(',');
         }

         var3.deleteCharAt(var3.length() - 1);
         var3.append(')');
         ContentValues var5 = new ContentValues();
         var5.put("uploaded", true);
         this.getContentResolver().update(ValuesProvider.uri(var1), var5, var3.toString(), (String[])null);
      }

   }

   public void setUploadedReason2compensations(String var1, List var2) {
      if (!var2.isEmpty()) {
         ContentResolver var3 = this.getContentResolver();
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            Reason2Compensation var6 = (Reason2Compensation)var4.next();
            ContentValues var5 = new ContentValues();
            var5.put("uploaded", true);
            var3.update(ValuesProvider.uri(var1), var5, String.format("reason_id=%d AND compensation_id=%d", var6.getReasonId(), var6.getCompensationId()), (String[])null);
         }
      }

   }

   public void setUploadedVariants(String var1, List var2) {
      if (!var2.isEmpty()) {
         StringBuilder var3 = new StringBuilder("_id in (");
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            var3.append(((Variant)var4.next()).getId());
            var3.append(',');
         }

         var3.deleteCharAt(var3.length() - 1);
         var3.append(')');
         ContentValues var5 = new ContentValues();
         var5.put("uploaded", true);
         this.getContentResolver().update(ValuesProvider.uri(var1), var5, var3.toString(), (String[])null);
      }

   }
}
