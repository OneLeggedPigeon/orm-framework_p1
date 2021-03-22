package com.revature.orm.jpa;

import com.revature.orm.ORMLogger;
import com.revature.orm.config.DBProperties;
import io.github.classgraph.*;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import java.lang.reflect.Method;
import java.util.*;

public class ManagerFactory implements EntityManagerFactory {
    private Map<String, Object> properties;

    private boolean closed = false;

    // list of all the classes
    private final HashMap<Class<?>,EntityTemplate> entityTemplates= new HashMap<>();

    /**
     *
     * @param properties can be null
     */
    public ManagerFactory(Map<String, Object> properties){
        this.properties = properties;
        // TODO: remove this and used commented out code that utilizes properties
        generateEntityTemplates("com.revature.zoo");
        //generateEntityTemplates(Optional.ofNullable((String) properties.get("package")).orElse(""));
    }

    /**
     * Generates templates for all of the @Entity annotated classes in package <code>pkg</code> or it's subpackages
     * @param pkg name of the root package to check
     */
    private void generateEntityTemplates(String pkg) {
        // annotations to scan for
        String entityAnnotation = "javax.persistence.Entity";
        try (ScanResult scanResult =
                     new ClassGraph()
                             .enableAllInfo()         // Scan classes, methods, fields, annotations
                             .acceptPackages(pkg)     // Scan com.xyz and subpackages (omit to scan all packages)
                             .scan()) {               // Start the scan
            for (ClassInfo entityClassInfo : scanResult.getClassesWithAnnotation(entityAnnotation)) {

                EntityTemplate template = new EntityTemplate();

                AnnotationInfoList entityAnnotationInfoList = entityClassInfo.getAnnotationInfo();
                List<AnnotationParameterValue> entityParamVals = entityAnnotationInfoList.get(entityAnnotation).getParameterValues();
                // @javax.persistence.Entity has one parameter
                String entityName = (String) entityParamVals.get(0).getValue();
                ORMLogger.ormLog.debug(entityClassInfo.getName() + " is annotated with @Entity " + entityName + " | Creating template");

                // Table
                AnnotationParameterValueList tableParamVals = entityAnnotationInfoList.get("javax.persistence.Table").getParameterValues();
                String tableName = (String) tableParamVals.getValue("name");
                String tableSchema = (String) tableParamVals.getValue("schema");
                if(tableName.isEmpty()){
                    // default
                    template.table = entityClassInfo.getSimpleName();
                } else {
                    template.table = tableName;
                }
                ORMLogger.ormLog.debug("  Template Table : " + template.table);
                if(tableSchema.isEmpty()){
                    // default
                    template.schema = DBProperties.getInstance().getSchema();
                } else {
                    template.schema = tableSchema;
                }
                ORMLogger.ormLog.debug("  Template Schema : " + template.schema);

                // Methods in Class
                HashMap<String, MethodInfo> getters = new HashMap<>();
                HashMap<String, MethodInfo> setters = new HashMap<>();
                for(MethodInfo methodInfo : entityClassInfo.getMethodInfo()){
                    String methodName = methodInfo.getName();
                    if (methodName.startsWith("set") && methodName.length() > 3) {
                        setters.put(methodInfo.getName(), methodInfo);
                    } else if (methodName.startsWith("get") && methodName.length() > 3) {
                        getters.put(methodInfo.getName(), methodInfo);
                    }
                }

                // Fields in Class
                HashMap<String, FieldInfo> fields = new HashMap<>();
                for(FieldInfo fieldInfo : entityClassInfo.getFieldInfo()){
                    fields.put(fieldInfo.getName(),fieldInfo);
                }

                // add columns and change the default column name based on annotations
                getters.forEach((getterName,getterMethodInfo) -> {
                    String name = getterName.substring(3,4).toLowerCase().concat(getterName.substring(4));
                    Method getter = getterMethodInfo.loadClassAndGetMethod();
                    String setterName = "set"+name.substring(0,1).toUpperCase().concat(name.substring(1));
                    MethodInfo setterMethodInfo = setters.get(setterName);
                    Method setter = setterMethodInfo.loadClassAndGetMethod();
                    boolean isId = false;
                    FieldInfo fieldInfo = fields.get(name);
                    // check if there is a defined column name in the annotations (getter then setter then field)
                    AnnotationInfoList gAnnotations = getterMethodInfo.getAnnotationInfo();
                    AnnotationInfoList sAnnotations = setterMethodInfo.getAnnotationInfo();
                    AnnotationInfoList fAnnotations = fieldInfo.getAnnotationInfo();
                    // Check if this is an Primary Key column
                    if(fAnnotations.get("javax.persistence.Id") != null ||
                            gAnnotations.get("javax.persistence.Id") != null ||
                            sAnnotations.get("javax.persistence.Id") != null){
                        isId = true;
                    }
                    // check Column annotation for column name
                    if(fAnnotations.get("javax.persistence.Column") != null &&
                            !fAnnotations.get("javax.persistence.Column").getParameterValues().getValue("name").equals("")){
                        name = (String) fAnnotations.get("javax.persistence.Column").getParameterValues().getValue("name");
                    } else if(gAnnotations.get("javax.persistence.Column") != null &&
                            !gAnnotations.get("javax.persistence.Column").getParameterValues().getValue("name").equals("")){
                        name = (String) gAnnotations.get("javax.persistence.Column").getParameterValues().getValue("name");
                    } else if(sAnnotations.get("javax.persistence.Column") != null &&
                            !sAnnotations.get("javax.persistence.Column").getParameterValues().getValue("name").equals("")){
                        name = (String) sAnnotations.get("javax.persistence.Column").getParameterValues().getValue("name");
                    }
                    template.addCol(name,getter,setter,isId);
                });
                entityTemplates.put(entityClassInfo.loadClass(), template);
            }
        }

        /*
        // find all the public getFoo() Methods (including inherited), but also find the fields 'foo' with annotations that match public 'getFoo()'
        ArrayList<Method> methods = new ArrayList<>(Arrays.asList(clazz.getMethods()));
        // find all the getFoo methods
        ArrayList<Manager.Col> cols = new ArrayList<>();

        HashMap<String, Method> getters = new HashMap<>();
        HashMap<String, Method> setters = new HashMap<>();
        for (Method method : methods) {
        }

        Field[] fieldArray = clazz.getDeclaredFields();
        HashMap<String, Field> fields = new HashMap<>();
        for (Field f : fieldArray){
            fields.put(f.getName(),f);
        }

        // change the default column name based on annotations
        getters.forEach((getterName,getterMethod) -> {
            String name = getterName.substring(3,4).toLowerCase().concat(getterName.substring(4));
            String setterName = "set"+name.substring(0,1).toUpperCase().concat(name.substring(1));
            Method setterMethod = setters.get(setterName);
            Field field = fields.get(name);
            // check if there is a defined column name in the annotations (getter then setter then field)
            if (getterMethod.getDeclaredAnnotation(Column.class) != null &&
                    !getterMethod.getDeclaredAnnotation(Column.class).name().isEmpty()){
                name = getterMethod.getDeclaredAnnotation(Column.class).name();
            } else if (setterMethod != null &&
                    setterMethod.getDeclaredAnnotation(Column.class) != null &&
                    !setterMethod.getDeclaredAnnotation(Column.class).name().isEmpty()){
                name = setterMethod.getDeclaredAnnotation(Column.class).name();
            } else if (field != null  &&
                    field.getDeclaredAnnotation(Column.class) != null &&
                    !field.getDeclaredAnnotation(Column.class).name().isEmpty()){
                name = field.getDeclaredAnnotation(Column.class).name();
            }
            entity.columns.add(new Manager.Col(name, getterMethod, setterMethod));
        });
        */
    }

    @Override
    public EntityManager createEntityManager() {
        if(closed) throw new IllegalStateException();
        return new Manager(this, entityTemplates, null, null);
    }

    @Override
    public EntityManager createEntityManager(Map properties) {
        if(closed) throw new IllegalStateException();
        return new Manager(this, entityTemplates, null, (HashMap<String, Object>)properties);
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType) {
        if(closed) throw new IllegalStateException();
        return new Manager(this, entityTemplates, synchronizationType, null);
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType, Map properties) {
        if(closed) throw new IllegalStateException();
        return new Manager(this, entityTemplates, synchronizationType, (HashMap<String, Object>)properties);
    }

    @Override
    public Map<String, Object> getProperties() {
        if(closed) throw new IllegalStateException();
        if (properties != null){
            return properties;
        } else {
            return new HashMap<>();
        }
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Metamodel getMetamodel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOpen() {
        return !closed;
    }

    @Override
    public void close() {
        closed = true;
    }

    @Override
    public Cache getCache() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PersistenceUnitUtil getPersistenceUnitUtil() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addNamedQuery(String s, Query query) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void addNamedEntityGraph(String s, EntityGraph<T> entityGraph) {
        throw new UnsupportedOperationException();
    }
}
