package com.revature.zoo.model;

// https://www.tutorialspoint.com/jpa/jpa_entity_managers.htm
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class Employee implements Serializable {

    @Id
    @Column(name="employee_id")
    private int eid;

    @Column(name="name")
    private String ename;
    private double salary;
    @Column(name="degree")
    private String deg;

    public Employee(int eid, String ename, double salary, String deg) {
        super( );
        this.eid = eid;
        this.ename = ename;
        this.salary = salary;
        this.deg = deg;
    }

    public Employee( ) {
        super();
    }

    public int getEid( ) {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public String getEname( ) {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public double getSalary( ) {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getDeg( ) {
        return deg;
    }

    public void setDeg(String deg) {
        this.deg = deg;
    }

    @Override
    public String toString() {
        return "Employee [eid=" + eid + ", ename=" + ename + ", salary=" + salary + ", deg=" + deg + "]";
    }
}