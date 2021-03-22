package com.revature.orm.jpa;

import com.revature.orm.OrmLogger;
import com.revature.orm.config.DatabaseProperties;
import com.revature.orm.config.PersistenceConfig;
import com.revature.orm.db.StatementService;
import io.github.classgraph.*;
import org.apache.commons.lang.time.StopWatch;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ManagerFactory implements EntityManagerFactory {
    private final Map<String, Object> properties;

    private boolean closed = false;

    // list of all the classes
    private final HashMap<Class<?>,EntityTemplate> entityTemplates= new HashMap<>();

    public ManagerFactory(){
        this(new HashMap<String, Object>());
    }

    public ManagerFactory(Map<String, Object> properties){
        this.properties = properties;
        // TODO: remove this and used commented out code that utilizes properties
        generateEntityTemplates(Optional.ofNullable((String) properties.get("entity-package-root")).orElse(PersistenceConfig.getInstance().getPropertyByKey("entity-package-root")));
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

                AtomicBoolean hasId = new AtomicBoolean(false);

                AnnotationInfoList entityAnnotationInfoList = entityClassInfo.getAnnotationInfo();
                List<AnnotationParameterValue> entityParamVals = entityAnnotationInfoList.get(entityAnnotation).getParameterValues();
                // @javax.persistence.Entity has one parameter
                String entityName = (String) entityParamVals.get(0).getValue();
                OrmLogger.ormLog.debug(entityClassInfo.getName() + " is annotated with @Entity " + entityName + " | Creating template");

                // Table
                String tableName = "";
                String tableSchema = "";
                //Use Defaults if no @Table Annotation
                if (entityAnnotationInfoList.get("javax.persistence.Table") != null) {
                    AnnotationParameterValueList tableParamVals = entityAnnotationInfoList.get("javax.persistence.Table").getParameterValues();
                    tableName = ((String) tableParamVals.getValue("name")).toLowerCase();
                    tableSchema = ((String) tableParamVals.getValue("schema")).toLowerCase();
                }
                if(tableName.isEmpty()){
                    // default
                    tableName = entityClassInfo.getSimpleName().toLowerCase();
                }
                if(tableSchema.isEmpty()){
                    // default
                    tableSchema = DatabaseProperties.getInstance().getSchema().toLowerCase();
                }
                OrmLogger.ormLog.debug(
                        "  Template Table  : " + tableName + System.lineSeparator() +
                        "  Template Schema : " + tableSchema);

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

                EntityTemplate template = new EntityTemplate(tableName,tableSchema);

                // add columns and change the default column properties based on annotations
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
                        if(hasId.get()) throw new RuntimeException("Please only annotate a single int with @Id");
                        if(!getter.getReturnType().equals(int.class)) throw new RuntimeException("Please only annotate an int with @Id");
                        isId = true;
                        hasId.set(true);
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

                if(!hasId.get()) throw new RuntimeException("no @Id was defined for "+template);
                new Thread(new SqlRunnable(template)).start();
                entityTemplates.put(entityClassInfo.loadClass(), template);
            }
        }
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

    /**
     * add the sql statements to a <code>EntityTemplate</code> in a separate thread
     */
    private class SqlRunnable implements Runnable {
        private final EntityTemplate template;

        public SqlRunnable(EntityTemplate template){
            this.template = template;
        }

        @Override
        public void run() {
            OrmLogger.ormLog.debug(template.getTable() + ": new Thread running to generate SQL");
            StopWatch s = new StopWatch();
            s.start();
            template.setTemplateStatements(StatementService.generateSql(template));
            s.stop();
            OrmLogger.ormLog.debug(template.getTable() + ": thread completed in " + s.getTime());
        }
    }
}
