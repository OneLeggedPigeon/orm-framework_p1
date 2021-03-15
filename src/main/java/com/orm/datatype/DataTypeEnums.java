package com.orm.datatype;


import java.util.EnumMap;

public class DataTypeEnums {
    private final EnumMap<DataType,String> pgDataTypes = new EnumMap<DataType,String>(DataType.class);

    private static DataTypeEnums instance;

    // currently set for postgresql
    private DataTypeEnums() {
        pgDataTypes.put(DataType.TEXT,"text");
        pgDataTypes.put(DataType.BOOL,"bool");
        pgDataTypes.put(DataType.INT,"int4");
        pgDataTypes.put(DataType.FLOAT,"float8");
        pgDataTypes.put(DataType.SERIAL,"serial");
    }

    public static DataTypeEnums getInstance(){
        if(instance == null) return new DataTypeEnums();
        return instance;
    }

    public String getDataType(DataType e){
        return pgDataTypes.get(e);
    }
}
