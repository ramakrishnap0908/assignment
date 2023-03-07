package com.myapp.assignment.model;

import java.util.List;

import com.myapp.assignment.util.StringUtil;
/**
 * @author rpinninti
 * Created on 03/07/2023
 */
public class Column {
    private String fieldName;
    private String operator;
    private String fieldValue;
   // private String selectColumns;
    public Column(String fieldName, String operator, String fieldValue) {
        this.fieldName = fieldName;
        this.operator = operator;
        this.fieldValue = fieldValue;
    }

	public String getFieldName() {
        return fieldName;
    }

    public String getOperator() {
        return operator;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public String generateCondition(String alias) {
        String comparison = alias + "." + fieldName + " ";
        switch (operator) {
            case "IN":
                List<String> quotedValues = StringUtil.quoteWords(fieldValue);
                comparison += "IN (" + String.join(",", quotedValues) + ")";
                break;
            case "LIKE":
            	 if (fieldValue.toString().contains("%")) {
                     throw new IllegalArgumentException("Field value cannot contain '%'");
                 }
                comparison += "LIKE '%" + fieldValue + "%'";
                break;
            default:
                comparison += operator + " '" + fieldValue + "'";
                break;
        }
        return comparison;
    }
}
