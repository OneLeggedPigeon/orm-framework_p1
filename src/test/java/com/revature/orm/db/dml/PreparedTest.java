package com.revature.orm.db.dml;

import com.revature.orm.jpa.EntityTemplate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PreparedTest {
    private static final EntityTemplate e1 = new EntityTemplate("testTable","testSchema");
    private static final EntityTemplate e2 = new EntityTemplate("testTable","testSchema");
    private static final EntityTemplate e3 = new EntityTemplate("testTable","testSchema");

    @BeforeAll
    static void beforeAll() {
        e1.addCol("col0", false);
        e1.addCol("col1",false);
        e1.addCol("col2",false);
        e1.addCol("col3",true);

        e2.addCol("col0", false);
        e2.addCol("col1",false);
        e2.addCol("col2",true);
        e2.addCol("col3",false);

        e3.addCol("col0", false);
        e3.addCol("col1",true);
        e3.addCol("col2",false);
        e3.addCol("col3",false);
    }

    @Test
    void testConstructorInsert() {
        PreparedInsert p = new PreparedInsert(e1);
        assertEquals("INSERT INTO testSchema.testTable (col0, col1, col2) VALUES (?, ?, ?) RETURNING col3", p.toString());
        p = new PreparedInsert(e2);
        assertEquals("INSERT INTO testSchema.testTable (col0, col1, col3) VALUES (?, ?, ?) RETURNING col2", p.toString());
        p = new PreparedInsert(e3);
        assertEquals("INSERT INTO testSchema.testTable (col0, col2, col3) VALUES (?, ?, ?) RETURNING col1", p.toString());
    }

    @Test
    void testConstructorUpdate() {
        PreparedUpdate p = new PreparedUpdate(e1);
        assertEquals("UPDATE testSchema.testTable SET col0 = ?, col1 = ?, col2 = ? WHERE col3 = ?", p.toString());
        p = new PreparedUpdate(e2);
        assertEquals("UPDATE testSchema.testTable SET col0 = ?, col1 = ?, col3 = ? WHERE col2 = ?", p.toString());
        p = new PreparedUpdate(e3);
        assertEquals("UPDATE testSchema.testTable SET col0 = ?, col2 = ?, col3 = ? WHERE col1 = ?", p.toString());
    }


    @Test
    void testConstructorDelete() {
        PreparedDelete p = new PreparedDelete(e1);
        assertEquals("DELETE FROM testSchema.testTable WHERE col3 = ?", p.toString());
        p = new PreparedDelete(e2);
        assertEquals("DELETE FROM testSchema.testTable WHERE col2 = ?", p.toString());
        p = new PreparedDelete(e3);
        assertEquals("DELETE FROM testSchema.testTable WHERE col1 = ?", p.toString());
    }

}