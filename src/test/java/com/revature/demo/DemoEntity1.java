package com.revature.demo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="tablename1")
public class DemoEntity1 implements Serializable {
    @Id
    private int id;
    private float f;
    private double d;
    private boolean b;
    private String s;
}