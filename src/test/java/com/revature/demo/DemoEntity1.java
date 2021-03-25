package com.revature.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table
public class DemoEntity1 implements Serializable {
    @Id
    @Column
    private int id;
    private float f;
    private String s;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name="floaterson")
    public float getF() {
        return f;
    }

    public void setF(float f) {
        this.f = f;
    }

    @Column
    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String toString(){
        return "id: "+id+" float: "+f+" String: "+s;
    }
}