package com.revature.orm.datatype;


import java.sql.Types;
import java.util.HashMap;

public class DataTypeEnums {
    private final HashMap<Class<?>, Integer> javaToDataType = new HashMap<>();
    private final HashMap<Integer,String> pgDataTypes = new HashMap<>();

    private static DataTypeEnums instance;

    // currently set for postgresql
    private DataTypeEnums() {
        javaToDataType.put(int.class, Types.INTEGER);
        javaToDataType.put(float.class, Types.FLOAT);
        javaToDataType.put(double.class, Types.DOUBLE);
        javaToDataType.put(String.class, Types.VARCHAR);
        javaToDataType.put(boolean.class, Types.BOOLEAN);

        pgDataTypes.put(Types.VARCHAR,"varchar");
        pgDataTypes.put(Types.BOOLEAN,"bool");
        pgDataTypes.put(Types.INTEGER,"int4");
        pgDataTypes.put(Types.FLOAT,"float4");
        pgDataTypes.put(Types.DOUBLE,"float8");
    }

    public static DataTypeEnums getInstance(){
        if(instance == null) return new DataTypeEnums();
        return instance;
    }

    public String getDataTypeString(int type){
        return pgDataTypes.get(type);
    }
    public String getDataTypeString(Class<?> javaType){
        return pgDataTypes.get(javaToDataType.get(javaType));
    }

    public int getDataTypeIndex(Class<?> javaType) {
        return javaToDataType.get(javaType);
    }
}
