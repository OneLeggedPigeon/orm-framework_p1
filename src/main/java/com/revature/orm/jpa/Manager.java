package com.revature.orm.jpa;

import com.revature.orm.OrmLogger;
import com.revature.orm.db.connection.ConnectionSession;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Manager implements EntityManager {
    private final HashMap<String, Object> properties;
    private Transaction transaction;
    private final EntityManagerFactory factory;
    @SuppressWarnings("FieldCanBeLocal")
    private final SynchronizationType sync;
    // Association between classes and EntityTemplates, used for generating context
    private final HashMap<Class<?>,EntityTemplate> entityTemplates;

    public HashMap<Object, EntityTemplate> getContext() {
        return context;
    }

    private final HashMap<Object,EntityTemplate> context =  new HashMap<>();
    private boolean closed = false;

    public Manager(EntityManagerFactory factory, HashMap<Class<?>,EntityTemplate> templates, SynchronizationType synchronizationType, HashMap<String, Object> properties){
        this.factory = factory;
        this.properties = properties;
        sync = synchronizationType;
        entityTemplates = templates;
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
        if(clazz.getFields().length > 0) throw new RuntimeException(clazz+" does not follow the JavaBean standard");

        EntityTemplate template = entityTemplates.get(clazz);

        if(!clazz.isAnnotationPresent(Entity.class)) throw new IllegalArgumentException(clazz+" is not an @Entity and cannot be persisted");
        if(template == null) throw new RuntimeException("Something went wrong with the entity template loader");

        context.put(pojo, template);

        ((Transaction) getTransaction()).add(pojo, template, ContextType.PERSIST);
    }

    @Override
    public void remove(Object pojo) {
        if(closed) throw new IllegalStateException("This Manager is Closed");
        if(transaction == null || !transaction.isActive()) throw new TransactionRequiredException("No active transaction for this Manager");
        Class<?> clazz = pojo.getClass();
        if(clazz.getFields().length > 0) throw new RuntimeException(clazz+" does not follow the JavaBean standard");

        if(context.containsKey(pojo)){
            // simply remove the object if it's been persisted and hasn't been committed
            context.remove(pojo);
            ((Transaction) getTransaction()).remove(pojo);
        } else {
            // add new remove statement
            EntityTemplate template = entityTemplates.get(clazz);

            if(!clazz.isAnnotationPresent(Entity.class)) throw new IllegalArgumentException(clazz+" is not an @Entity and cannot be removed");
            if(template == null) throw new RuntimeException("Something went wrong with the entity template loader");

            context.put(pojo, template);

            ((Transaction) getTransaction()).add(pojo, template, ContextType.REMOVE);
        }
    }

    @Override
    public <T> T merge(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets up the <code>transaction</code> if it hasn't been constructed yet
     * @return <code>transaction</code>
     */
    @Override
    public EntityTransaction getTransaction() {
        if (transaction == null){
            transaction = new Transaction(this);
        }
        return transaction;
    }

    @Override
    public Map<String, Object> getProperties() {
        if (properties != null){
            return properties;
        } else {
            return new HashMap<>();
        }
    }

    /**
     *
     * @param aClass Class Type
     * @param o Primary Key
     * @param <T> Class Type
     * @return and instance of the class with values taken from the matching database entry, or null if no such object exists
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T find(Class<T> aClass, Object o) {
        T result = null;
        for(Constructor<?> c : aClass.getConstructors()){
            if (c.getParameterCount() == 0){
                try {
                    result = (T) c.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if(result == null) {
            OrmLogger.ormLog.debug("Could not reflect constructor for "+aClass);
            return null;
        }
        try {
            EntityTemplate template = entityTemplates.get(aClass);
            Connection conn = new ConnectionSession().getActiveConnection();
            PreparedStatement ps = conn.prepareStatement(template.getStatements(ContextType.READ).get(0).toString());
            ps.setInt(1,(Integer) o);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                template.getIdColumn().getSetter().invoke(result, rs.getInt(template.getIdColumn().getName()));
                for(EntityTemplate.Col column : template.getColumns()){
                    column.getSetter().invoke(result, rs.getObject(column.getName()));
                }
            } else {
                throw new SQLException("Row Data wasn't present for "+aClass+" | "+o);
            }
            conn.close();
        } catch (SQLException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            OrmLogger.ormLog.debug("Problem while finding "+aClass+" | "+o);
            return null;
        }
        return result;
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
        transaction.shutDown();
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
}
