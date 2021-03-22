package com.revature.orm.jpa;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Read in based on Class annotations and part of a <code>Manager</code>'s persistence context, formatted for use in CRUD operations.
 * <p>The middleman between the POJO and the database row</p>
 */
public class EntityTemplate {
    public final ArrayList<Col> columns = new ArrayList<>();
    public String table;
    public String schema;

    public void addCol(String name, Method getter, Method setter, boolean isId) {
        columns.add(new Col(name, getter, setter, isId));
    }

    /**
     * Modified from
     * <url>https://stackoverflow.com/questions/521171/a-java-collection-of-value-pairs-tuples</url>
     * @author Dave Jarvis
     */
    private static class Col{

        private final String name;
        private final Method getter;
        private final Method setter;
        private final boolean isId;
        private final Class<?> dataType;

        public Col(String name, Method getter, Method setter) {
            assert name != null;
            assert getter != null;
            assert setter != null;

            this.name = name;
            this.getter = getter;
            this.setter = setter;
            this.isId = false;
            this.dataType = getter.getReturnType();
        }

        public Col(String name, Method getter, Method setter, boolean isId) {
            assert name != null;
            assert getter != null;
            assert setter != null;

            this.name = name;
            this.getter = getter;
            this.setter = setter;
            this.isId = isId;
            this.dataType = getter.getReturnType();
        }

        public String getName() { return name; }
        public Object getGetter() { return getter; }
        public Method getSetter() { return setter; }
        public boolean isId() {
            return isId;
        }
        public Class<?> getDataType() { return dataType; }

        @Override
        public int hashCode() { return name.hashCode() ^ getter.hashCode() ^ setter.hashCode(); }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Col)) return false;
            Col col = (Col) o;
            return this.name.equals(col.getName()) &&
                    this.getter.equals(col.getGetter()) &&
                    this.setter.equals(col.getSetter());
        }

    }
}
