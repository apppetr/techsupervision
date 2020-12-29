package ru.sviridov.techsupervision.utils;

import org.json.JSONException;
import org.json.JSONObject;

public interface JSONParcelable {
   void writeToJSONObject(JSONObject var1) throws JSONException;

   public interface Creator {
      Object createFromJSONObject(JSONObject var1);
   }
}
