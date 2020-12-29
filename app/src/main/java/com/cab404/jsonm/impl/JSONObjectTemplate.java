package com.cab404.jsonm.impl;

import com.cab404.jsonm.core.JSONGenerationException;
import com.cab404.jsonm.core.JSONMaker;
import com.cab404.jsonm.core.JSONTemplate;
import com.cab404.jsonm.core.TemplateInsertion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Sorry for no comments!
 * Created at 18:00 on 24.03.15
 *
 * @author cab404
 */
public class JSONObjectTemplate implements JSONTemplate {

    public static final String KEY = "$object";

    public static class JSONMapEntry {
        public String key;
        public Object value;

        public JSONMapEntry(String key, Object value) {
            this.key = key;
            this.value = value;
        }

    }


    @Override
    public Object make(JSONMaker parent, Object... parameters) throws JSONException {
        JSONObject object = new JSONObject();

        for (Object parameter : parameters) {
            if (parameter instanceof JSONMapEntry) {
                JSONMapEntry me = (JSONMapEntry) parameter;
                if (me.value instanceof TemplateInsertion) {
                    TemplateInsertion ti = (TemplateInsertion) me.value;
                    me.value = parent.make(ti.templateName, ti.parameters);
                }
                object.put(me.key, me.value);
            } else if (parameter instanceof JSONObject) {
                object = (JSONObject) parameter;
            } else if (parameter instanceof JSONArray) {
                throw new JSONGenerationException(
                        "Cannot use json array <" + parameter + "> as a target to map modifications " +
                                "- it is not a map!");
            } else {
                throw new JSONGenerationException(
                        "Cannot use argument <" + parameter + "> as a json object modification " +
                                "- it is not a JSONMapEntry!");
            }
        }

        return object;
    }

}
