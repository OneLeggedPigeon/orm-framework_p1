package com.revature.orm.jpa;

import com.revature.orm.datatype.DataTypeEnums;
import com.revature.orm.db.Prepared;
import com.revature.orm.db.VariablePrepared;

import java.lang.reflect.Method;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Read in based on Class annotations as part of the ManagerFactory Constructor. Part of a <code>Manager</code>'s persistence context, formatted for use in CRUD operations.
 * <p>The middleman between the POJO and the database row</p>
 */
public class EntityTemplate {
    private final ArrayList<Col> columns = new ArrayList<>();
    private final String table;
    private final String schema;

    private VariablePrepared templateStatements;
    // held separately from the other columns
    private Col idColumn;

    public EntityTemplate(String table, String schema){
        this.table = table;
        this.schema = schema;
    }

    public ArrayList<Prepared> getStatements(ContextType type){
        // Busy wait in case the thread assigning this needs a few more milliseconds, max 3 seconds
        int waitTime = 0;
        while (templateStatements == null && waitTime < 300){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            waitTime++;
        }
        assert templateStatements != null;
        return templateStatements.getStatements(type);
    }

    public void addCol(String name, Method getter, Method setter, boolean isId) {
        Col c = new Col(name, getter, setter);
        if(isId) {
            if(idColumn != null) throw new RuntimeException("second Id Column added");
            idColumn = c;
        } else columns.add(c);
    }

    // I expect this to only be called by test methods
    public void addCol(String name, boolean isId) {
        Col c = new Col(name);
        if(isId) idColumn = c;
        else columns.add(c);
    }

    public ArrayList<Col> getColumns() {
        return columns;
    }

    public String getTable() {
        return table;
    }

    public String getSchema() {
        return schema;
    }

    public Col getIdColumn() {
        return idColumn;
    }

    public void setIdColumn(Col idColumn) {
        this.idColumn = idColumn;
    }

    public void setTemplateStatements(VariablePrepared templateStatements) {
        this.templateStatements = templateStatements;
    }

    /**
     * Modified from
     * <url>https://stackoverflow.com/questions/521171/a-java-collection-of-value-pairs-tuples</url>
     * @author Dave Jarvis
     */
    public static class Col{

        private final String name;
        private final Method getter;
        private final Method setter;
        private final int dataType;

        public Col(String name) {
            this.name = name;
            this.getter = null;
            this.setter = null;
            this.dataType = 0;
        }

        public Col(String name, Method getter, Method setter) {
            assert name != null;
            assert getter != null;
            assert setter != null;

            this.name = name;
            this.getter = getter;
            this.setter = setter;
            this.dataType = DataTypeEnums.getInstance().getDataTypeIndex(getter.getReturnType());
        }

        public String getName() { return name; }
        public Method getGetter() { return getter; }
        public Method getSetter() { return setter; }
        public int getDataType() { return dataType; }

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
