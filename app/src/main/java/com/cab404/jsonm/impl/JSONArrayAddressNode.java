package com.cab404.jsonm.impl;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * JSONArray index address node
 */
class JSONArrayAddressNode implements JSONAddressNode {
    protected final int index;

    public JSONArrayAddressNode(int index) {
        this.index = index;
    }

    @Override
    public Object move(Object jsonObject) {
        try {
            return ((JSONArray) jsonObject).get(index);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void set(Object target, Object value) {
        try {
            ((JSONArray) target).put(index, value);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Array:" + index;
    }

}
