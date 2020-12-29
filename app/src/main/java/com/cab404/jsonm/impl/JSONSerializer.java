package com.cab404.jsonm.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Some usefulness for putting JSON back and forth!
 * Created at 13:37 on 30.03.15
 *
 * @author cab404
 */
public class JSONSerializer<T> {

    protected final Class<T> clazz;

    protected final Map<String, Record> description;

    protected static final Class[] JSON_COMPATIBLE_PRIMITIVE = {
            Long.TYPE,
            Long.class,
            Double.TYPE,
            Double.class,
            Integer.TYPE,
            Integer.class,
            Boolean.TYPE,
            Boolean.class,
            String.class,
            JSONObject.class,
            JSONArray.class,
    };

    protected class Record {
        Putter putter;
        Getter getter;
        Class clazz;
        Field field;
    }

    /**
     * Deserializer
     */
    protected interface Putter {
        void put(Object obj, Field where, Object val) throws IllegalAccessException;
    }

    /**
     * Serializer
     */
    protected interface Getter {
        Object get(Object val);
    }

    protected static class DefaultGetter implements Getter {
        public static final DefaultGetter INSTANCE = new DefaultGetter();

        @Override
        public Object get(Object val) {
            return val;
        }
    }

    protected static class EnumGetter implements Getter {
        public static final EnumGetter INSTANCE = new EnumGetter();

        @Override
        public Object get(Object val) {
            return ((Enum) val).name();
        }
    }

    protected static class DefaultPutter implements Putter {
        public static final DefaultPutter INSTANCE = new DefaultPutter();

        @Override
        public void put(Object obj, Field where, Object val) throws IllegalAccessException {
            where.set(obj, val);
        }
    }

    protected static class EnumPutter implements Putter {
        public static final EnumPutter INSTANCE = new EnumPutter();

        @SuppressWarnings("unchecked")
        @Override
        public void put(Object obj, Field where, Object val) throws IllegalAccessException {
            where.set(obj, Enum.valueOf((Class<Enum>) where.getType(), val.toString()));
        }
    }

    protected static class IntPutter implements Putter {
        public static final IntPutter INSTANCE = new IntPutter();

        @Override
        public void put(Object obj, Field where, Object val) throws IllegalAccessException {
            int the_thing;

            if (val instanceof Long)
                the_thing = ((Long) val).intValue();
            else if (val instanceof Integer)
                the_thing = (int) val;
            else throw new RuntimeException("Incompatible types: Integer and " + val.getClass().getName());

            where.set(obj, the_thing);
        }
    }

    @SuppressWarnings("unchecked")
    protected boolean isCompatible(Class cls) {
        if (cls.isEnum()) return true;
        for (Class d : JSON_COMPATIBLE_PRIMITIVE)
            if (cls.isAssignableFrom(d))
                return true;
        return false;
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    public JSONSerializer(Class<T> clazz) {
        this.clazz = clazz;
        description = new HashMap<>();

        for (Field f : clazz.getFields()) {
            if ((f.getModifiers() & Modifier.STATIC) != 0) continue;
            Class cls = f.getType();

            if (!isCompatible(cls)) continue;

            Record record = new Record();
            record.clazz = cls;
            record.field = f;
            record.putter = DefaultPutter.INSTANCE;
            record.getter = DefaultGetter.INSTANCE;

            if (cls.equals(Integer.class)) {
                record.putter = IntPutter.INSTANCE;
            } else if (cls.isEnum()) {
                record.putter = EnumPutter.INSTANCE;
                record.getter = EnumGetter.INSTANCE;
            }

            description.put(f.getName(), record);
        }

    }

    public JSONObject serialize(T what, JSONObject to) throws IllegalAccessException, JSONException {
        for (Record rec : description.values()) {
            to.put(rec.field.getName(), rec.getter.get(rec.field.get(what)));
        }
        return to;
    }

    protected <X> Iterable<X> iter(final Iterator<X> what) {
        return new Iterable<X>() {
            @Override
            public Iterator<X> iterator() {
                return what;
            }
        };
    }

    public T deserialize(JSONObject what, T to) throws JSONException, IllegalAccessException {
        for (String s : iter(what.keys())) {
            if (description.containsKey(s)) {
                Record f = description.get(s);
                f.putter.put(to, f.field, what.get(s));
            }
        }
        return to;
    }


}
