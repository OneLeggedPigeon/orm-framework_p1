package com.revature.orm.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.ProviderUtil;
import java.util.Map;

public class RevaturePersistenceProvider implements PersistenceProvider {
    private static RevaturePersistenceProvider instance;

    private RevaturePersistenceProvider(){}

    public static RevaturePersistenceProvider getInstance(){
        if (instance == null){
            return new RevaturePersistenceProvider();
        } else {
            return instance;
        }
    }

    public EntityManagerFactory createEntityManagerFactory() {
        return createEntityManagerFactory(null, null);
    }

    // TODO
    @Override
    public EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map properties) {
        if (properties == null){
            return new RevatureEntityManagerFactory();
        } else {
            return new RevatureEntityManagerFactory(properties);
        }
    }

    // TODO
    @Override
    public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo persistenceUnitInfo, Map map) {
        return null;
    }

    // TODO
    @Override
    public void generateSchema(PersistenceUnitInfo persistenceUnitInfo, Map map) {

    }

    // TODO
    @Override
    public boolean generateSchema(String s, Map map) {
        return false;
    }

    // TODO
    @Override
    public ProviderUtil getProviderUtil() {
        return null;
    }
}
