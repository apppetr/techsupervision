package com.cab404.jsonm.core;

/**
 * Insertion data for templates, yeah.
 * Created at 2:08 on 13.02.15
 *
 * @author cab404
 */
public class TemplateInsertion {

    public final String templateName;
    public final Object[] parameters;

    public TemplateInsertion(String templateName, Object[] parameters) {
        this.templateName = templateName;
        this.parameters = parameters;
    }


}
