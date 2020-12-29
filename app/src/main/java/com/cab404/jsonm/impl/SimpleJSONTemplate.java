package com.cab404.jsonm.impl;

import com.cab404.jsonm.core.JSONMaker;
import com.cab404.jsonm.core.JSONTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Sorry for no comments!
 * Created at 20:26 on 13.02.15
 *
 * @author cab404
 */
public class SimpleJSONTemplate implements JSONTemplate {

    public static final char
            ESCAPE = '\\',
            USER_ITEM_REPLACER = '*';
    static final char
            ITEM_REPLACER = 0xa78f,
            NUM_FIELD_START = '1';


    protected final List<List<JSONAddressNode>> targets;
    protected final JSONObject original;

    /**
     * Makes a new JSONTemplate. For a replace char look in {@link #USER_ITEM_REPLACER}
     */
    public SimpleJSONTemplate(CharSequence expression) throws JSONException {

        StringBuilder builder = stringBuilder(expression);
        int count = unwrap(builder);

        original = new JSONObject(builder.toString());
        targets = new ArrayList<>();

        for (int i = 0; i < count; i++) targets.add(null);

        JSONUtils.recurseThrough(targets, new ArrayList<JSONAddressNode>(), original);

    }

    @SuppressWarnings("SimplifiableConditionalExpression")
    static boolean escaped(int index, CharSequence what) {
        return index == 0 ?
                // Start of string, not escaped obviously
                false
                :
                // Checking if escape char is not escaped
                what.charAt(index - 1) == ESCAPE && !escaped(index - 1, what);
    }

    static int indexOf(CharSequence where, char what, int start) {
        for (int i = start; i < where.length(); i++)
            if (where.charAt(i) == what)
                return i;
        return -1;
    }

    static StringBuilder stringBuilder(CharSequence seq) {
        return seq instanceof StringBuilder ? (StringBuilder) seq : new StringBuilder(seq);
    }

    /**
     * Sets indexes and deals with escaped chars.
     */
    static int unwrap(StringBuilder what) {
        StringBuilder builder = stringBuilder(what);
        char how_much = NUM_FIELD_START;

        for (int i = indexOf(builder, USER_ITEM_REPLACER, 0); i != -1; i = indexOf(builder, USER_ITEM_REPLACER, i))
            if (!escaped(i, builder))
                builder.replace(i, i++ + 1, "'" + ITEM_REPLACER + how_much++ + "'");
            else
                builder.deleteCharAt(i - 1);

        for (int i = 0; i < builder.length(); i++)
            if (escaped(i, builder))
                builder.deleteCharAt(i - 1);


        return how_much - NUM_FIELD_START;
    }

    public JSONObject obtainTemplateCopy() throws JSONException {
        return JSONUtils.recursiveClone(original);
    }

    @Override
    public Object make(JSONMaker parent, Object... parameters) throws JSONException {

        /* Checking if length of parameter array is matching such in our template */
        if (parameters.length != targets.size())
            throw new RuntimeException(
                    "You must supply exactly " +
                            targets.size() + " object" + (targets.size() == 1 ? "" : "s")
                            + " to this template!"
            );

        JSONObject result = obtainTemplateCopy();

        for (int index = 0; index < parameters.length; index++) {
            List<JSONAddressNode> address = targets.get(index);

            Object current_leaf = result;

            /* Going down to our destination */
            for (int deep = 0; deep < address.size() - 1; deep++)
                current_leaf = address.get(deep).move(current_leaf);

            /* Getting last piece of address and setting value it's pointing to given parameter*/
            address.get(address.size() - 1).set(current_leaf, parameters[index]);

        }

        return result;
    }
}
