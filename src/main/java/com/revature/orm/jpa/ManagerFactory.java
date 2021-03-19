package com.revature.orm.jpa;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import java.util.HashMap;
import java.util.Map;

public class ManagerFactory implements EntityManagerFactory {
    private Map<String, Object> properties;

    private boolean closed = false;

    public ManagerFactory(){

    }

    public ManagerFactory(Map<String, Object> properties){
        this.properties = properties;
    }

    @Override
    public EntityManager createEntityManager() {
        return new Manager(this, null, null);
    }

    @Override
    public EntityManager createEntityManager(Map properties) {
        return new Manager(this, null, (HashMap<String, Object>)properties);
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType) {
        return new Manager(this, synchronizationType, null);
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType, Map properties) {
        return new Manager(this, synchronizationType, (HashMap<String, Object>)properties);
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
    public CriteriaBuilder getCriteriaBuilder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Metamodel getMetamodel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOpen() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        //TODO implememnt
        closed = true;
    }

    @Override
    public Cache getCache() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PersistenceUnitUtil getPersistenceUnitUtil() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addNamedQuery(String s, Query query) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void addNamedEntityGraph(String s, EntityGraph<T> entityGraph) {
        throw new UnsupportedOperationException();
    }
}
