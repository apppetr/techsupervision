package ru.lihachev.norm31937.utils;

import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: ru.lihachev.norm31937.utils.JSONParcelable */
public interface JSONParcelable {

   /* renamed from: ru.lihachev.norm31937.utils.JSONParcelable$Creator */
   public interface Creator<Clazz> {
      Clazz createFromJSONObject(JSONObject jSONObject);
   }

   void writeToJSONObject(JSONObject jSONObject) throws JSONException;
}

