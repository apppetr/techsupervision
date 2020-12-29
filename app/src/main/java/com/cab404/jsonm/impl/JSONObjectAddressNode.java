package com.cab404.jsonm.impl;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * JSONObject key address node
 */
class JSONObjectAddressNode implements JSONAddressNode {
    protected final String key;

    public JSONObjectAddressNode(String key) {
        this.key = key;
    }

    @Override
    public Object move(Object jsonObject) {
        try {
            return ((JSONObject) jsonObject).get(key);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void set(Object target, Object value) {
        try {
            ((JSONObject) target).put(key, value);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Object:" + key;
    }
}
