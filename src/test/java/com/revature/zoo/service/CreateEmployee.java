package com.revature.zoo.service;

import com.revature.zoo.model.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.revature.orm.jpa.RevaturePersistenceProvider;

//https://www.tutorialspoint.com/jpa/jpa_entity_managers.htm
public class CreateEmployee {

    public static void main( String[ ] args ) {
        EntityManagerFactory emFactory = RevaturePersistenceProvider.getInstance().createEntityManagerFactory();

        EntityManager entitymanager = emFactory.createEntityManager( );
        entitymanager.getTransaction( ).begin( );

        Employee employee = new Employee( );
        employee.setEid( 1201 );
        employee.setEname( "Gopal" );
        employee.setSalary( 40000 );
        employee.setDeg( "Technical Manager" );

        entitymanager.persist( employee );
        entitymanager.getTransaction( ).commit( );

        entitymanager.close( );
        emFactory.close( );
    }
    // Should create a table 'employee'
    // with columns 'Eid' 'Ename' 'Salary' 'Deg'
}