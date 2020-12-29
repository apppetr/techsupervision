package com.cab404.jsonm.impl;

import com.cab404.jsonm.core.JSONMaker;
import com.cab404.jsonm.core.JSONTemplate;
import com.cab404.jsonm.core.TemplateInsertion;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Sorry for no comments!
 * Created at 18:07 on 13.02.15
 *
 * @author cab404
 */
public class SimpleJSONMaker implements JSONMaker {

    private Map<String, JSONTemplate> library;

    public SimpleJSONMaker() {
        library = new HashMap<>();
        library.put(JSONArrayTemplate.KEY, new JSONArrayTemplate());
        library.put(JSONObjectTemplate.KEY, new JSONObjectTemplate());
    }

    /**
     * Insertion of a template
     */
    public static TemplateInsertion ins(String templateName, Object... parameters) {
        return new TemplateInsertion(templateName, parameters);
    }

    @Override
    public Object make(String templateName, Object... params) throws JSONException {
        return this.makeFromArray(templateName, params);
    }

    @Override
    public Object eval(TemplateInsertion ins) throws JSONException {
        return this.makeFromArray(ins.templateName, ins.parameters);
    }

    public Object makeFromArray(String templateName, Object[] params) throws JSONException {
        for (int i = 0; i < params.length; i++)
            if (params[i] instanceof TemplateInsertion) {
                TemplateInsertion params_ins = (TemplateInsertion) params[i];
                params[i] = makeFromArray(params_ins.templateName, params_ins.parameters);
            }

        return library.get(templateName).make(this, params);
    }


    @Override
    public JSONMaker add(String templateName, String expression) throws JSONException {
        library.put(templateName, new SimpleJSONTemplate(expression));
        return this;
    }

    protected JSONMaker add(String templateName, JSONTemplate template) {
        library.put(templateName, template);
        return this;
    }

}
