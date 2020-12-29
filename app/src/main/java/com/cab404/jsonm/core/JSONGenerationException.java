package com.cab404.jsonm.core;

import org.json.JSONException;

/**
 * Sorry for no comments!
 * Created at 6:55 on 25.03.15
 *
 * @author cab404
 */
public class JSONGenerationException extends JSONException {
    public JSONGenerationException(String s) {
        super(s);
    }

    public JSONGenerationException(Throwable throwable) {
        super(String.valueOf(throwable));
    }
}
