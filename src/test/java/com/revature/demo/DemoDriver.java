package com.revature.demo;

import com.revature.orm.config.PersistenceConfig;
import com.revature.orm.jpa.Provider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Scanner;

public class DemoDriver {
    public static void main(String[] args) {
        // SETUP
        EntityManagerFactory emFactory = Provider.getInstance().createContainerEntityManagerFactory(PersistenceConfig.getInstance().getPROPERTIES());
        EntityManager eM1 = emFactory.createEntityManager();
        EntityManager eM2 = emFactory.createEntityManager();
        Scanner scan = new Scanner(System.in);

        eM1.getTransaction().begin();
        eM2.getTransaction().begin();

        DemoEntity1 e11 = new DemoEntity1();
        DemoEntity1 e12 = new DemoEntity1();
        e11.setF(12);
        e11.setS("Jonny Tornado");
        e12.setF(5);
        e12.setS("Tonny Jornado");

        DemoEntity1 e13 = new DemoEntity1();
        DemoEntity1 e14 = new DemoEntity1();
        e13.setF(47);
        e13.setS("Ronaldo McDonaldo");
        e14.setF(-5);
        e14.setS("Blast Hardcheese");

        DemoEntity2 e21 = new DemoEntity2();
        DemoEntity2 e22 = new DemoEntity2();
        e21.setBool(true);
        e21.setDub(3.14);
        e22.setBool(false);
        e22.setDub(1.34);




        // INSERT
        eM1.persist(e11);
        eM1.persist(e12);

        eM2.persist(e21);
        eM2.persist(e22);

        eM1.getTransaction().commit();
        eM2.getTransaction().commit();

        System.out.println("insert committing");
        scan.nextLine();




        // FIND
        // we don't necessarily need to call begin here, as find() isn't part of the transaction
        // it does pause the calling thread until the previous commits are finished, which is useful for actually find()ing the rows committed
        eM1.getTransaction().begin();
        eM2.getTransaction().begin();

        System.out.println("e11: "+e11);
        System.out.println("e12: "+e12);
        System.out.println("e21: "+e21);
        System.out.println("e22: "+e22);
        System.out.println();

        // we can generate the object from the database, useful when there is no local instance
        // note that e11, and e21 now have a set id != 0 from the previous commit
        DemoEntity1 eMod11 = eM1.find(DemoEntity1.class,e11.getId());
        DemoEntity2 eMod21 = eM2.find(DemoEntity2.class,e21.getId());

        System.out.println("eMod11: "+eMod11);
        System.out.println("eMod21: "+eMod21);
        System.out.println();

        eMod11.setS(eMod11.getS().concat(" and this"));
        eMod21.setDub(eMod21.getDub()+20.5);

        System.out.println("eMod11: "+eMod11);
        System.out.println("eMod21: "+eMod21);
        scan.nextLine();




        // UPDATE
        // we have the option of querying both tables with one Manager, the Managers are not assigned to a specific table except by convention
        eM1.persist(eMod11);
        eM1.persist(eMod21);

        eM1.getTransaction().commit();

        System.out.println("update committing");
        scan.nextLine();




        // REMOVE
        eM1.getTransaction().begin();
        // eM2 already called begin() earlier and hasn't committed
        // the transaction is still active
        // it will only check the database at commit time, which means eM1's changes are accounted for

        // both of these work because e21's id was returned and set during the INSERT commit
        eM1.remove(eMod11);
        eM2.remove(e21);

        // add a third and fourth row to table1

        eM1.persist(e13);
        eM1.persist(e14);

        // except actually don't add e13
        eM1.remove(e13);

        // and also remove whatever is in row two
        DemoEntity1 removeId = new DemoEntity1();
        removeId.setId(2);
        eM1.remove(removeId);

        eM1.getTransaction().commit();
        eM2.getTransaction().commit();

        // there should now be only 1 element in each table
        System.out.println("removal committing");
        eM1.close();
        eM2.close();
    }
}
