package com.cab404.jsonm.impl;

import com.cab404.jsonm.core.JSONMaker;
import com.cab404.jsonm.core.JSONTemplate;
import com.cab404.jsonm.core.TemplateInsertion;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Sorry for no comments!
 * Created at 3:12 on 15.02.15
 *
 * @author cab404
 */
public class JSONArrayTemplate implements JSONTemplate {
    public static final String KEY = "$array";

    /**
     * Makes a new array out given objects
     */
    public static TemplateInsertion arr(Object... parameters) {
        return new TemplateInsertion(KEY, parameters);
    }

    @Override
    public Object make(JSONMaker parent, Object... parameters) throws JSONException {
        return new JSONArray(parameters);
    }
}
