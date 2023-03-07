package com.myapp.assignment.model;
/**
 * @author rpinninti
 * Created on 03/07/2023
 */
public class Join {
    private String type;
    private String tableName;
    private String alias;
    private String condition;

    public Join(String type, String tableName, String alias, String condition) {
        this.type = type;
        this.tableName = tableName;
        this.alias = alias;
        this.condition = condition;
    }

    public String getType() {
        return type;
    }

    public String getTableName() {
        return tableName;
    }

    public String getAlias() {
        return alias;
    }

    public String getCondition() {
        return condition;
    }
}
