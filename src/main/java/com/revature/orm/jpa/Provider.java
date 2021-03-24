package com.revature.orm.jpa;

import com.revature.orm.OrmLogger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.ProviderUtil;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class Provider implements PersistenceProvider{
    private static Provider instance;

    private Provider(){
        OrmLogger.ormLog.debug(System.lineSeparator()+"NEW PROVIDER INITIALIZED");
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

    public EntityManagerFactory createContainerEntityManagerFactory(String pkg) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("entity-package-root",pkg);
        return createContainerEntityManagerFactory(null, map);
    }

    public EntityManagerFactory createContainerEntityManagerFactory(Map properties) {
        return createContainerEntityManagerFactory(null, properties);
    }

    @SuppressWarnings("unchecked")
    @Override
    public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo persistenceUnitInfo, Map properties) {
        try{
            return new ManagerFactory(properties);
        }catch(Exception e){
            e.printStackTrace();
            return new ManagerFactory();
        }
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
