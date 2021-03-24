package com.revature.orm.jpa;

import com.revature.orm.OrmLogger;
import com.revature.orm.config.PersistenceConfig;
import com.revature.orm.db.Prepared;
import com.revature.orm.db.StatementService;
import com.revature.orm.db.connection.ConnectionSession;
import com.revature.orm.db.ddl.*;
import com.revature.orm.db.dml.*;

import javax.persistence.EntityTransaction;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Transaction implements EntityTransaction {
    private boolean active;
    private final HashMap<Object, EntityTemplate> templates = new HashMap<>();
    private final HashMap<Object, ContextType> types = new HashMap<>();
    private final Manager manager;


    Transaction(Manager manager){
        this.manager = manager;
        active = false;
    }

    public void remove(Object pojo){
        templates.remove(pojo);
        types.remove(pojo);
    }

    public void add(Object pojo, EntityTemplate template, ContextType contextType){
        if(contextType.equals(ContextType.READ)) throw new InvalidParameterException("Should be getting PERSIST or REMOVE, not READ");
        templates.put(pojo, template);
        types.put(pojo, contextType);
    }

    @Override
    public void begin() {
        active = true;
    }

    /**
     * Write to the Database
     */
    @Override
    public void commit() {
        if(!isActive()) throw new IllegalStateException("Transaction wasn't active");

        ConnectionSession ses = new ConnectionSession();
        Connection conn = ses.getActiveConnection();
        for(HashMap.Entry<Object,EntityTemplate> tem : templates.entrySet()){
            Object pojo = tem.getKey();
            EntityTemplate template = tem.getValue();
            ContextType type = types.get(pojo);
            int id = 0;
            try {
                id = (Integer) template.getIdColumn().getGetter().invoke(pojo);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            // check the state of the database
            boolean tableExists = TableManager.tableExists(conn, template.getTable(), new PreparedTableSelect(template.getSchema()));
            boolean rowExists = false;
            if(id > 0) rowExists = StatementService.idExists(conn,template.getTable(),template.getSchema(),template.getIdColumn().getName(), id);
            // check if the existing table matches the template
//            boolean tableMatches;

            // set the up to two statements that can be committed based on the database state
            // ORDER IS IMPORTANT, that's why I'm manually assigning to an array
            Prepared[] statements = new Prepared[2];
            switch (type){
                case PERSIST:
                    for(Prepared p :template.getStatements(type)){
                        Class<?> clazz = p.getClass();
                        if(clazz.equals(PreparedTableCreate.class) && !tableExists && PersistenceConfig.getInstance().getPropertyByKey("create-tables").equals("true")){
                            statements[0]=p;
                            tableExists = true;
//                            tableMatches = true;
                        } else if(clazz.equals(PreparedTableAlter.class) &&
                                tableExists &&
//                                tableMatches &&
                                PersistenceConfig.getInstance().getPropertyByKey("alter-tables").equals("true")){
                            //TODO statements[0]=p;
                        } else if(clazz.equals(PreparedUpdate.class) && tableExists && rowExists){
                            statements[1]=p;
                        } else if(clazz.equals(PreparedInsert.class) && tableExists && !rowExists){
                            statements[1]=p;
                        }
                    }
                    break;
                case REMOVE:
                    for(Prepared p :template.getStatements(type)){
                        Class<?> clazz = p.getClass();
                        if(clazz.equals(PreparedDelete.class) && tableExists && rowExists){
                            statements[0]=p;
                        }
                    }
                    break;
                default:
                    throw new RuntimeException("Wrong Type Somehow");
            }
            if(!tableExists && statements[0] == null) OrmLogger.ormLog.debug("!!!Can't create table with current settings!!!");
            for(Prepared sql : statements){
                if(sql != null){
                    try {
                        Class<?> clazz = sql.getClass();
                        PreparedStatement ps = sql.getPreparedStatement(conn);
                        int count = ps.getParameterMetaData().getParameterCount();
                        if(sql.isQuery()){
                            if (clazz.equals(PreparedInsert.class)) {
                                // INSERT
                                for(int i = 1; i <= count; i++){
                                    EntityTemplate.Col c = template.getColumns().get(i-1);
                                    ps.setObject(i, c.getGetter().invoke(pojo), c.getDataType());
                                }
                                EntityTemplate.Col idColumn = template.getIdColumn();
                                ResultSet rs = ps.executeQuery();
                                if(rs.next()) idColumn.getSetter().invoke(pojo,rs.getInt(idColumn.getName()));
                                else throw new SQLException("Couldn't get back the new serial Id from INSERT");
                            }
                        } else {
                            if(count > 0){
                                // UPDATE REMOVE
                                for(int i = 1; i <= count - 1; i++){
                                    EntityTemplate.Col c = template.getColumns().get(i-1);
                                    ps.setObject(i, c.getGetter().invoke(pojo), c.getDataType());
                                }
                                ps.setObject(count, template.getIdColumn().getGetter().invoke(pojo));
                            }
                            ps.executeUpdate();
                        }
                        OrmLogger.ormLog.debug("Executed: "+ps.toString());
                        ps.close();
                    } catch (SQLException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        ses.close();
        templates.clear();
        types.clear();
        manager.getContext().clear();
        active = false;
    }

    @Override
    public void rollback() {

    }

    @Override
    public void setRollbackOnly() {

    }

    @Override
    public boolean getRollbackOnly() {
        return false;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
