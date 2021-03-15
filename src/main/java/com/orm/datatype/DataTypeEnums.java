package com.orm.datatype;


import java.util.EnumMap;

public class DataTypeEnums {
    private final EnumMap<DataTypes,String> pgDataTypes = new EnumMap<DataTypes,String>(DataTypes.class);

    private DataTypeEnums instance;

    // currently set for postgresql
    private DataTypeEnums() {
        pgDataTypes.put(DataTypes.TEXT,"text");
        pgDataTypes.put(DataTypes.BOOL,"bool");
        pgDataTypes.put(DataTypes.INT,"int4");
        pgDataTypes.put(DataTypes.FLOAT,"float8");
        pgDataTypes.put(DataTypes.SERIAL,"serial");
    }

    public DataTypeEnums getInstance(){
        if(instance == null) return new DataTypeEnums();
        return instance;
    }

    public String getDataType(DataTypes e){
        return pgDataTypes.get(e);
    }
}
