package com.revature.zoo.service;

import com.revature.orm.config.PersistenceConfig;
import com.revature.zoo.model.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.revature.orm.jpa.Provider;

//https://www.tutorialspoint.com/jpa/jpa_entity_managers.htm
public class CreateEmployee {

    public static void main( String[ ] args ) {
        EntityManagerFactory emFactory = Provider.getInstance().createContainerEntityManagerFactory(PersistenceConfig.getInstance().getPROPERTIES());

        EntityManager entitymanager = emFactory.createEntityManager( );
        entitymanager.getTransaction( ).begin( );

        Employee employee0 = new Employee( );
        employee0.setEname( "Gopal0" );
        employee0.setSalary( 40000 );
        employee0.setDeg( "Technical Manager" );

        Employee employee1 = new Employee( );
        employee1.setEname( "Gopal1" );
        employee1.setSalary( 40000 );
        employee1.setDeg( "Technical Manager" );

        Employee employee2 = new Employee( );
        employee2.setEid( 2 );
        employee2.setEname( "Gopal2" );
        employee2.setSalary( 40000 );
        employee2.setDeg( "Technical Manager" );

        entitymanager.remove( employee0 );
        entitymanager.persist( employee1 );
        entitymanager.remove( employee1 );
        entitymanager.remove( employee2 );
        entitymanager.persist( employee0 );

        System.out.println(entitymanager.find(Employee.class, 5));
        entitymanager.getTransaction( ).commit( );

        entitymanager.close( );
        emFactory.close( );
    }
    // Should create a table 'employee'
    // with columns 'Eid' 'Ename' 'Salary' 'Deg'
}