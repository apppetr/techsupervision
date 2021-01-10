package ru.sviridov.techsupervision.utils;

import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: ru.sviridov.techsupervision.utils.JSONParcelable */
public interface JSONParcelable {

   /* renamed from: ru.sviridov.techsupervision.utils.JSONParcelable$Creator */
   public interface Creator<Clazz> {
      Clazz createFromJSONObject(JSONObject jSONObject);
   }

   void writeToJSONObject(JSONObject jSONObject) throws JSONException;
}

