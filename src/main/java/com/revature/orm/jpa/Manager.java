package com.revature.orm.jpa;

import com.revature.orm.config.DBProperties;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import java.io.Serializable;
import java.lang.reflect.*;
import java.util.*;

public class Manager implements EntityManager {
    private final HashMap<String, Object> properties;
    private Transaction transaction;
    private final EntityManagerFactory factory;
    private final SynchronizationType sync;

    private HashMap<Object,ORMEntity> context =  new HashMap<>();
    private boolean closed = false;

    public Manager(EntityManagerFactory factory, SynchronizationType synchronizationType, HashMap<String, Object> properties){
        this.factory = factory;
        this.properties = properties;
        sync = synchronizationType;
    }

    /**
     * Make an instance managed and persistent. Annotations should go on the Getter for each column
     * @param pojo Entity instance (requires JavaBean)
     */
    @Override
    public void persist(Object pojo) {
        if(closed) throw new IllegalStateException("This Manager is Closed");
        if(transaction == null || !transaction.isActive()) throw new TransactionRequiredException("No active transaction for this Manager");

        Class<?> clazz = pojo.getClass();

        if(!(pojo instanceof Serializable) || clazz.getFields().length > 0) throw new RuntimeException(clazz+" does not follow the JavaBean standard");

        if(!clazz.isAnnotationPresent(Entity.class)) throw new IllegalArgumentException("Can't persist a non-entity");

        // TODO: only read annotations once per class, not once per object
        ORMEntity entity = new ORMEntity();
        entity.instance = pojo;

        /*
         * find all the public getFoo() Methods (including inherited), but also find the fields 'foo' with annotations that match public 'getFoo()'
         */
        ArrayList<Method> methods = new ArrayList<>(Arrays.asList(clazz.getMethods()));
        Field[] fields = clazz.getDeclaredFields();
        // find all the getFoo methods
        ArrayList<Col> cols = new ArrayList<>();

        HashMap<String, Method> getters = new HashMap<>();
        HashMap<String, Method> setters = new HashMap<>();
        for (Method method : methods) {
            String methodName = method.getName();
            if (methodName.startsWith("set") && methodName.length() > 3) {
                setters.put(method.getName(), Objects.requireNonNull(method));
            } else if (!methodName.equals("getClass") && methodName.startsWith("get") && methodName.length() > 3) {
                getters.put(method.getName(), Objects.requireNonNull(method));
            }
        }

        getters.forEach((getterName,getterMethod) -> {
            String column = getterName.substring(3,4).toLowerCase().concat(getterName.substring(4));
            String setterName = "set"+column.substring(0,1).toUpperCase().concat(column.substring(1));
            Method setterMethod = setters.get(setterName);
            // check if there is a defined column name in the annotations
            if (getterMethod.getDeclaredAnnotation(Column.class) != null &&
                    !getterMethod.getDeclaredAnnotation(Column.class).name().isEmpty()){
                column = getterMethod.getDeclaredAnnotation(Column.class).name();
            } else if (setterMethod != null &&
                    setterMethod.getDeclaredAnnotation(Column.class) != null &&
                    !setterMethod.getDeclaredAnnotation(Column.class).name().isEmpty()){
                column = setterMethod.getDeclaredAnnotation(Column.class).name();
            }
            entity.columns.add(new Col(column, getterMethod, setterMethod));
        });

        Table ann = clazz.getAnnotation(Table.class);
        if(ann == null || ann.name().isEmpty()){
            // default
            entity.table = clazz.getName().substring(clazz.getName().lastIndexOf(".")+1);
        } else {
            entity.table = ann.name();
        }
        if(ann == null || ann.schema().isEmpty()){
            // default
            entity.schema = DBProperties.getInstance().getSchema();
        } else {
            entity.schema = ann.schema();
        }

        context.put(pojo, entity);
    }

    @Override
    public <T> T merge(T t) {
        return null;
    }

    @Override
    public void remove(Object o) {

    }

    /**
     * Sets up the <code>transaction</code> if it hasn't been constructed yet
     * @return <code>transaction</code>
     */
    @Override
    public EntityTransaction getTransaction() {
        if (transaction == null){
            transaction = new Transaction();
        }
        return transaction;
    }

    @Override
    public Map<String, Object> getProperties() {
        if (properties != null){
            return properties;
        } else {
            return new HashMap<String, Object>();
        }
    }

    @Override
    public <T> T find(Class<T> aClass, Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, Map<String, Object> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType, Map<String, Object> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T getReference(Class<T> aClass, Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFlushMode(FlushModeType flushModeType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FlushModeType getFlushMode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void lock(Object o, LockModeType lockModeType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void lock(Object o, LockModeType lockModeType, Map<String, Object> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refresh(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refresh(Object o, Map<String, Object> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType, Map<String, Object> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        context.clear();
    }

    @Override
    public void detach(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LockModeType getLockMode(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setProperty(String s, Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query createQuery(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query createQuery(CriteriaUpdate criteriaUpdate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query createQuery(CriteriaDelete criteriaDelete) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> TypedQuery<T> createQuery(String s, Class<T> aClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query createNamedQuery(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String s, Class<T> aClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query createNativeQuery(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query createNativeQuery(String s, Class aClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query createNativeQuery(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, Class... classes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, String... strings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void joinTransaction() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isJoinedToTransaction() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getDelegate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        //TODO implememnt

        // once the transaciton is complete
        closed = true;
    }

    @Override
    public boolean isOpen() {
        return !closed;
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return factory;
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Metamodel getMetamodel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> aClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityGraph<?> createEntityGraph(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityGraph<?> getEntityGraph(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> aClass) {
        throw new UnsupportedOperationException();
    }

    /**
     * Read in based on Class annotations and part of a <code>Manager</code>'s persistence context, formatted for use in CRUD operations.
     * <p>The middleman between the POJO and the database row</p>
     */
    private class ORMEntity implements Serializable {
        public ArrayList<Col> columns = new ArrayList<>();
        public Object instance;
        public String table;
        public String schema;
    }

    /**
     * Modified from
     * <url>https://stackoverflow.com/questions/521171/a-java-collection-of-value-pairs-tuples</url>
     * @author Dave Jarvis
     */
    private class Col implements Serializable {

        private final String name;
        private final Method getter;
        private final Method setter;
        private final Class<?> dataType;

        public Col(String name, Method getter, Method setter) {
            assert name != null;
            assert getter != null;
            assert setter != null;

            this.name = name;
            this.getter = getter;
            this.setter = setter;
            this.dataType = getter.getReturnType();
        }

        public String getName() { return name; }
        public Object getGetter() { return getter; }
        public Method getSetter() { return setter; }
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
