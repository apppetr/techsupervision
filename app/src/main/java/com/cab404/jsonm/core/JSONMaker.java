package com.cab404.jsonm.core;

import org.json.JSONException;

/**
 * Thing that makes a thing using a lot of thing-making things.
 * Created at 1:41 on 13.02.15
 *
 * @author cab404
 */
public interface JSONMaker {

    public Object make(String templateName, Object... params) throws JSONException;

    Object eval(TemplateInsertion ins) throws JSONException;

    /**
     * Adds new template. Template should be an JSONObject
     */
    public JSONMaker add(String templateName, String expression) throws JSONException;

}
