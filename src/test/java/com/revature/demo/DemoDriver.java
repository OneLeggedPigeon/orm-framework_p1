package com.revature.demo;

import com.revature.orm.config.PersistenceConfig;
import com.revature.orm.jpa.Provider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class DemoDriver {
    public static void main(String[] args) {
        // SETUP
        EntityManagerFactory emFactory = Provider.getInstance().createContainerEntityManagerFactory(PersistenceConfig.getInstance().getPROPERTIES());
        EntityManager entityManager1 = emFactory.createEntityManager();
        EntityManager entityManager2 = emFactory.createEntityManager();

        entityManager1.getTransaction().begin();
        entityManager2.getTransaction().begin();

        // PERSIST

        // REMOVE

        // FIND


    }
}
