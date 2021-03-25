package com.revature.demo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="tablename2",schema="p_1_2")
public class DemoEntity2 implements Serializable {
    @Id
    @Column
    private int id;
    @Column
    private double dub;
    @Column
    private boolean bool;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDub() {
        return dub;
    }

    public void setDub(double dub) {
        this.dub = dub;
    }

    public boolean getBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public String toString(){
        return "id: "+id+" double: "+dub+" boolean: "+bool;
    }
}