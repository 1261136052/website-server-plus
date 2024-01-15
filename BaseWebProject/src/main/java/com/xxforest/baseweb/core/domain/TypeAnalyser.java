package com.xxforest.baseweb.core.domain;

public class TypeAnalyser {

    private String typeName ;
    private boolean baseType;
    private boolean number;
    private boolean booleanType ;

    public static TypeAnalyser create(String typeName) {
        return new TypeAnalyser().init(typeName);
    }

    private TypeAnalyser init(String typeName) {
        this.typeName = typeName ;
        String lowerCaseSimpleTypeName = typeName.toLowerCase();
        if(lowerCaseSimpleTypeName.equals("int") || lowerCaseSimpleTypeName.equals("long") || lowerCaseSimpleTypeName.equals("float") ||
                lowerCaseSimpleTypeName.equals("double") || lowerCaseSimpleTypeName.equals("byte") || lowerCaseSimpleTypeName.equals("integer")){
            this.baseType = true;
            this.number = true ;
        }else if(lowerCaseSimpleTypeName.equals("string") ){
            this.baseType = true ;
        }else if(lowerCaseSimpleTypeName.equals("boolean")){
            this.baseType = true ;
            this.booleanType = true ;
        }

        return this;
    }

    public boolean isBooleanType() {
        return booleanType;
    }

    public void setBooleanType(boolean booleanType) {
        this.booleanType = booleanType;
    }

    public String getTypeName() {
        return typeName;
    }

    public boolean isBaseType() {
        return baseType;
    }

    public boolean isNumber() {
        return number;
    }
}
