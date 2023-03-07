package com.myapp.assignment.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * @author rpinninti
 * Created on 03/07/2023
 */
public class Table {
    private String name;
    private String alias;
    private String selectColumns;
    private List<Column> columns;
    private Join join;

    public Table(String name, String alias, List<Column> columns, Join join,String selectColumns) {
        this.name = name;
        this.alias = alias;
        this.columns = columns;
        this.join = join;
        this.selectColumns=selectColumns;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public Join getJoin() {
        return join;
    }

    public String generateSelectClause() {
    return selectColumns;
    }

    public String generateFromClause() {
        return name + " " + alias;
    }

    public String generateWhereClause() {
        List<String> conditions = new ArrayList<>();
        for (Column column : columns) {
            conditions.add(column.generateCondition(alias));
        }
		/*
		 * if (join != null) { conditions.add(join.getCondition()); }
		 */
        if (!conditions.isEmpty()) {
            return String.join(" AND ", conditions);
        }
        return null;
    }

	public String getSelectColumns() {
		return selectColumns;
	}

	public void setSelectColumns(String selectColumns) {
		this.selectColumns = selectColumns;
	}
}
