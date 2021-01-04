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
import com.android.volley.toolbox.HttpStack;
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
import ru.sviridov.techsupervision.values.Values;
import ru.sviridov.techsupervision.values.ValuesProvider;

public class UpdateService extends Service {
   private static final String UPLOAD_URL = "http://techsupervision-live.appspot.com/content/v1.0/upload";
   private Gson gson;
   private RequestQueue requestQueue;
   /* access modifiers changed from: private */
   public boolean uploading = false;

   public static void call(Context context) {
      context.startService(new Intent(context, UpdateService.class));
   }

   public IBinder onBind(Intent intent) {
      return null;
   }

   public void onCreate() {
      super.onCreate();
      this.requestQueue = Volley.newRequestQueue((Context) this, (HttpStack) new HurlStack());
      this.gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).excludeFieldsWithModifiers(64, 8, 128).create();
   }

   public int onStartCommand(Intent intent, int flags, int startId) {
      super.onStartCommand(intent, flags, startId);
      if (this.uploading) {
         return Service.START_STICKY;
      }
      upload(intent);
      return Service.START_STICKY ;
   }

   public void onDestroy() {
      super.onDestroy();
      this.uploading = false;
      this.requestQueue.cancelAll((RequestQueue.RequestFilter) new RequestQueue.RequestFilter() {
         public boolean apply(Request<?> request) {
            return true;
         }
      });
   }

   private void upload(Intent intent) {
      final UploadVariantData data = prepareUploadData(intent);
      if (!data.isEmpty()) {
         this.uploading = true;
         Response.Listener<JSONObject> lstner = new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
               boolean unused = UpdateService.this.uploading = false;
               if (UpdateService.this.isSuccess(response)) {
                  UpdateService.this.markUploaded(data);
               }
            }
         };
         Response.ErrorListener errLstner = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
               boolean unused = UpdateService.this.uploading = false;
            }
         };
         String body = this.gson.toJson(data);
         //Request request = new JsonObjectRequest(1, UPLOAD_URL, body, lstner, errLstner);
         //VolleyLog.m23v(body, new Object[0]);
         //request.setRetryPolicy(new DefaultRetryPolicy(10000, 3, 1.0f));
       //  this.requestQueue.add(request);
      }
   }

   /* access modifiers changed from: private */
   public boolean isSuccess(JSONObject response) {
      String error = response.isNull("error") ? null : response.optString("error");
      if (error != null) {
       //  Mint.logEvent("upload error", MintLogLevel.Info, "error", error);
      }
      return error == null;
   }

   /* access modifiers changed from: private */
   public void markUploaded(UploadVariantData data) {
      setUploadedVariants("elements", data.getElements());
      setUploadedVariants("materials", data.getMaterials());
      setUploadedElem2mats("elements2materials", data.getElem2mats());
      setUploadedDef2Elems("defects2elements", data.getDef2elems());
      setUploadedVariants("defects", data.getDefects());
      setUploadedDef2Reasons("defects2reasons", data.getDefect2reasons());
      setUploadedVariants("reasons", data.getReasons());
      setUploadedReason2compensations("reasons2compensations", data.getReason2compensations());
      setUploadedVariants("compensations", data.getCompensations());
   }

   private UploadVariantData prepareUploadData(Intent intent) {
      UploadVariantData data = new UploadVariantData();
      User user = new User();
      user.setDbVersion(2);
      user.setToken(StrongMan.userThing);
      data.setApiToken(StrongMan.GLOBAL_THING);
      data.setUser(user);
      data.setMaterials(getVariants("materials"));
      data.setElements(getVariants("elements"));
      data.setElem2mats(getElem2mats());
      data.setDef2elems(getDef2Elems());
      data.setDefects(getVariants("defects"));
      data.setDefect2reasons(getDefect2reasons());
      data.setReasons(getVariants("reasons"));
      data.setReason2compensations(getReason2compensations());
      data.setCompensations(getVariants("compensations"));
      return data;
   }

   public List<Variant> getVariants(String uriPath) {
      Cursor cursor = getContentResolver().query(ValuesProvider.uri(uriPath), (String[]) null, "manually_added=1 AND uploaded=0", (String[]) null, (String) null);
      List<Variant> values = new ArrayList<>(cursor.getCount());
      int inId = cursor.getColumnIndex("_id");
      int inName = cursor.getColumnIndex("name");
      int inManAdd = cursor.getColumnIndex("manually_added");
      int inUploaded = cursor.getColumnIndex("uploaded");
      int inVersion = cursor.getColumnIndex("version");
      while (cursor.moveToNext()) {
         Variant value = new Variant(cursor.getInt(inId), cursor.getString(inName));
         value.setIsManuallyAdded(cursor.getInt(inManAdd) == 1);
         value.setVersion(cursor.getString(inVersion));
         value.setIsUploaded(cursor.getInt(inUploaded) == 1);
         values.add(value);
      }
      return values;
   }

   public List<Elem2Mat> getElem2mats() {
      Cursor cursor = getContentResolver().query(ValuesProvider.uri("elements2materials"), (String[]) null, "manually_added=1 AND uploaded=0", (String[]) null, (String) null);
      List<Elem2Mat> values = new ArrayList<>(cursor.getCount());
      int inElId = cursor.getColumnIndex(Values.E2M.ELEMENT_ID);
      int inMatId = cursor.getColumnIndex(Values.E2M.MATERIAL_ID);
      int inMatElemId = cursor.getColumnIndex("mat_elem_id");
      int inManAdd = cursor.getColumnIndex("manually_added");
      int inUploaded = cursor.getColumnIndex("uploaded");
      int inVersion = cursor.getColumnIndex("version");
      while (cursor.moveToNext()) {
         Elem2Mat value = new Elem2Mat();
         value.setElementId(cursor.getInt(inElId));
         value.setMaterialId(cursor.getInt(inMatId));
         value.setMatElemId(cursor.getInt(inMatElemId));
         value.setIsManuallyAdded(cursor.getInt(inManAdd) == 1);
         value.setVersion(cursor.getString(inVersion));
         value.setIsUploaded(cursor.getInt(inUploaded) == 1);
         values.add(value);
      }
      return values;
   }

   public List<Def2Elem> getDef2Elems() {
      Cursor cursor = getContentResolver().query(ValuesProvider.uri("defects2elements"), (String[]) null, "manually_added=1 AND uploaded=0", (String[]) null, (String) null);
      List<Def2Elem> values = new ArrayList<>(cursor.getCount());
      int inDefId = cursor.getColumnIndex("defect_id");
      int inMatElemId = cursor.getColumnIndex("mat_elem_id");
      int inManAdd = cursor.getColumnIndex("manually_added");
      int inUploaded = cursor.getColumnIndex("uploaded");
      int inVersion = cursor.getColumnIndex("version");
      while (cursor.moveToNext()) {
         Def2Elem value = new Def2Elem();
         value.setDefectId(cursor.getInt(inDefId));
         value.setMatElemId(cursor.getInt(inMatElemId));
         value.setIsManuallyAdded(cursor.getInt(inManAdd) == 1);
         value.setVersion(cursor.getString(inVersion));
         value.setIsUploaded(cursor.getInt(inUploaded) == 1);
         values.add(value);
      }
      return values;
   }

   public void setUploadedVariants(String uriPath, List<Variant> values) {
      if (!values.isEmpty()) {
         StringBuilder sBuilder = new StringBuilder("_id in (");
         for (Variant value : values) {
            sBuilder.append(value.getId());
            sBuilder.append(',');
         }
         sBuilder.deleteCharAt(sBuilder.length() - 1);
         sBuilder.append(')');
         ContentValues cv = new ContentValues();
         cv.put("uploaded", true);
         getContentResolver().update(ValuesProvider.uri(uriPath), cv, sBuilder.toString(), (String[]) null);
      }
   }

   public void setUploadedDef2Elems(String uriPath, List<Def2Elem> values) {
      if (!values.isEmpty()) {
         StringBuilder sBuilder = new StringBuilder();
         sBuilder.append("mat_elem_id").append(" in (");
         for (Def2Elem value : values) {
            sBuilder.append(value.getMatElemId());
            sBuilder.append(',');
         }
         sBuilder.deleteCharAt(sBuilder.length() - 1);
         sBuilder.append(')');
         ContentValues cv = new ContentValues();
         cv.put("uploaded", true);
         getContentResolver().update(ValuesProvider.uri(uriPath), cv, sBuilder.toString(), (String[]) null);
      }
   }

   public void setUploadedElem2mats(String uriPath, List<Elem2Mat> values) {
      if (!values.isEmpty()) {
         StringBuilder sBuilder = new StringBuilder();
         sBuilder.append("mat_elem_id").append(" in (");
         for (Elem2Mat value : values) {
            sBuilder.append(value.getMatElemId());
            sBuilder.append(',');
         }
         sBuilder.deleteCharAt(sBuilder.length() - 1);
         sBuilder.append(')');
         ContentValues cv = new ContentValues();
         cv.put("uploaded", true);
         getContentResolver().update(ValuesProvider.uri(uriPath), cv, sBuilder.toString(), (String[]) null);
      }
   }

   public void setUploadedDef2Reasons(String uriPath, List<Defect2Reason> values) {
      if (!values.isEmpty()) {
         ContentResolver cr = getContentResolver();
         for (Defect2Reason value : values) {
            ContentValues cv = new ContentValues();
            cv.put("uploaded", true);
            cr.update(ValuesProvider.uri(uriPath), cv, String.format("defect_id=%d AND reason_id=%d", new Object[]{Integer.valueOf(value.getDefectId()), Integer.valueOf(value.getReasonId())}), (String[]) null);
         }
      }
   }

   public void setUploadedReason2compensations(String uriPath, List<Reason2Compensation> values) {
      if (!values.isEmpty()) {
         ContentResolver cr = getContentResolver();
         for (Reason2Compensation value : values) {
            ContentValues cv = new ContentValues();
            cv.put("uploaded", true);
            cr.update(ValuesProvider.uri(uriPath), cv, String.format("reason_id=%d AND compensation_id=%d", new Object[]{Integer.valueOf(value.getReasonId()), Integer.valueOf(value.getCompensationId())}), (String[]) null);
         }
      }
   }

   public List<Defect2Reason> getDefect2reasons() {
      Cursor cursor = getContentResolver().query(ValuesProvider.uri("defects2reasons"), (String[]) null, "manually_added=1 AND uploaded=0", (String[]) null, (String) null);
      List<Defect2Reason> values = new ArrayList<>(cursor.getCount());
      int inDefId = cursor.getColumnIndex("defect_id");
      int inReasonId = cursor.getColumnIndex("reason_id");
      int inManAdd = cursor.getColumnIndex("manually_added");
      int inUploaded = cursor.getColumnIndex("uploaded");
      int inVersion = cursor.getColumnIndex("version");
      while (cursor.moveToNext()) {
         Defect2Reason value = new Defect2Reason();
         value.setDefectId(cursor.getInt(inDefId));
         value.setReasonId(cursor.getInt(inReasonId));
         value.setIsManuallyAdded(cursor.getInt(inManAdd) == 1);
         value.setVersion(cursor.getString(inVersion));
         value.setIsUploaded(cursor.getInt(inUploaded) == 1);
         values.add(value);
      }
      return values;
   }

   private List<Reason2Compensation> getReason2compensations() {
      Cursor cursor = getContentResolver().query(ValuesProvider.uri("reasons2compensations"), (String[]) null, "manually_added=1 AND uploaded=0", (String[]) null, (String) null);
      List<Reason2Compensation> values = new ArrayList<>(cursor.getCount());
      int inReasonId = cursor.getColumnIndex("reason_id");
      int inCompId = cursor.getColumnIndex(Values.R2C.COMPENSATION_ID);
      int inManAdd = cursor.getColumnIndex("manually_added");
      int inUploaded = cursor.getColumnIndex("uploaded");
      int inVersion = cursor.getColumnIndex("version");
      while (cursor.moveToNext()) {
         Reason2Compensation value = new Reason2Compensation();
         value.setReasonId(cursor.getInt(inReasonId));
         value.setCompensationId(cursor.getInt(inCompId));
         value.setIsManuallyAdded(cursor.getInt(inManAdd) == 1);
         value.setVersion(cursor.getString(inVersion));
         value.setIsUploaded(cursor.getInt(inUploaded) == 1);
         values.add(value);
      }
      return values;
   }
}
