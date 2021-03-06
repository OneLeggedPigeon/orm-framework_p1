package com.revature.zoo.service;

import com.revature.zoo.model.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.revature.orm.jpa.Provider;

//https://www.tutorialspoint.com/jpa/jpa_entity_managers.htm
public class CreateEmployee {

    public static void main( String[ ] args ) {
        //EntityManagerFactory emFactory = Provider.getInstance().createContainerEntityManagerFactory(PersistenceConfig.getInstance().getPROPERTIES());
        EntityManagerFactory emFactory = Provider.getInstance().createContainerEntityManagerFactory("com.revature.zoo");

        EntityManager entitymanager = emFactory.createEntityManager( );
        entitymanager.getTransaction( ).begin( );

        Employee employee0 = new Employee( );
        employee0.setEname( "Gopal0" );
        employee0.setSalary( 43200 );
        employee0.setDeg( "Technical Manager" );

        Employee employee1 = new Employee( );
        employee1.setEname( "Gopal1" );
        employee1.setSalary( 40000 );
        employee1.setDeg( "Technical Manager" );

        Employee employee2 = new Employee( );
        employee2.setEname( "Gopal2" );
        employee2.setSalary( 40000 );
        employee2.setDeg( "Technical Manager" );

        entitymanager.persist( employee0 );
        entitymanager.persist( employee1 );
        entitymanager.remove( employee1 );
        entitymanager.persist( employee2 );

        Employee eEdit = entitymanager.find(Employee.class, 5);
        if(eEdit != null){
            eEdit.setSalary(0);
            entitymanager.persist(eEdit);
        }

        entitymanager.getTransaction( ).commit( );


        entitymanager.close( );
        emFactory.close( );
    }
}