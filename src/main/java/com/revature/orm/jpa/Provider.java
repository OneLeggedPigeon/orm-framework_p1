package com.revature.orm.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.ProviderUtil;
import java.util.Map;

public class Provider implements PersistenceProvider {
    private static Provider instance;

    private Provider(){
    }

    public static Provider getInstance(){
        if (instance == null){
            return instance = new Provider();
        } else {
            return instance;
        }
    }

    public EntityManagerFactory createContainerEntityManagerFactory() {
        return createContainerEntityManagerFactory(null, null);
    }

    @Override
    public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo persistenceUnitInfo, Map properties) {
        return new ManagerFactory(properties);
    }

    @Override
    public void generateSchema(PersistenceUnitInfo persistenceUnitInfo, Map map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean generateSchema(String s, Map map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProviderUtil getProviderUtil() {
        throw new UnsupportedOperationException();
    }
}
