package com.cab404.jsonm.impl;

import com.cab404.jsonm.core.TemplateInsertion;

/**
 * Sorry for no comments!
 * Created at 18:24 on 24.03.15
 *
 * @author cab404
 */
public class MapChainTemplateInsertion extends TemplateInsertion {
    protected MapChainTemplateInsertion(Object... parameters) {
        super(JSONObjectTemplate.KEY, parameters);
    }

    public static MapChainTemplateInsertion map() {
        return new MapChainTemplateInsertion();
    }

    /**
     * Modifies given template. Template should resolve in JSONObject
     */
    public static MapChainTemplateInsertion mod(TemplateInsertion template) {
        return new MapChainTemplateInsertion(template);
    }

    public MapChainTemplateInsertion put(String key, Object value) {
        Object[] extended = new Object[parameters.length + 1];
        System.arraycopy(parameters, 0, extended, 0, parameters.length);
        extended[parameters.length] = new JSONObjectTemplate.JSONMapEntry(key, value);
        return new MapChainTemplateInsertion(extended);
    }

}
