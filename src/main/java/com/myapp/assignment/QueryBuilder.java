package com.myapp.assignment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.assignment.model.Column;
import com.myapp.assignment.model.Join;
import com.myapp.assignment.model.Table;

public class QueryBuilder {
	


	private static final String REQUEST_FILE_NAME = "request.json";
	
	private static final String DELIMITER_COMMA_SPACE = ", ";
	private static final String FIELD_SPACE = " ";
	private static final String CONDITION_AND = " AND ";
	private static final String CONDITION_WHERE = " WHERE ";
	private static final String CONDITION_FROM = " FROM ";
	private static final String CONDITION_SELECT = "SELECT ";
	private static final String CONDITION_ON = " ON ";
	private static final String FIELD_JOIN_CONDITION = "joinCondition";
	private static final String FIELD_JOIN_TYPE = "joinType";
	private static final String FIELD_FIELDVALUE = "fieldValue";
	private static final String FIELD_OPERATOR = "operator";
	private static final String FIELD_FIELDNAME = "fieldName";
	private static final String FIELD_COLUMNS = "columns";
	private static final String FIELD_ALIAS = "alias";
	private static final String FIELD_SELECT_COLUMNS = "selectColumns";

	private static final String FIELD_TABLE_NAME = "name";
	private static final String FIELD_TABLES = "tables";

	public static void main(String[] args) throws IOException {
	    String query = generateQueryFromJsonFile(REQUEST_FILE_NAME);
	    System.out.println(query);
	}

	private static String generateQueryFromJsonFile(String fileName) throws IOException {
	    ObjectMapper mapper = new ObjectMapper();
	    InputStream inputStream = QueryBuilder.class.getClassLoader().getResourceAsStream(fileName);
	    JsonNode rootNode = mapper.readTree(inputStream);
	    List<Table> tables = buildTablesFromJsonNode(rootNode);
	    return generateQueryFromTables(tables);
	}

	private static List<Table> buildTablesFromJsonNode(JsonNode rootNode) {
	    List<Table> tables = new ArrayList<>();
	    for (JsonNode tableNode : rootNode.get(FIELD_TABLES)) {
	        String tableName = tableNode.get(FIELD_TABLE_NAME).asText();
	        String alias = tableNode.get(FIELD_ALIAS).asText();
	        String selectColumns = tableNode.get(FIELD_SELECT_COLUMNS).asText();
	        List<Column> columns = buildColumnsFromJsonNode(tableNode.get(FIELD_COLUMNS));
	        Join join = buildJoinFromJsonNode(tableNode);
	        tables.add(new Table(tableName, alias, columns, join,selectColumns));
	    }
	    return tables;
	}

	private static List<Column> buildColumnsFromJsonNode(JsonNode columnsNode) {
	    List<Column> columns = new ArrayList<>();
	    for (JsonNode columnNode : columnsNode) {
	        String fieldName = columnNode.get(FIELD_FIELDNAME).asText();
	        String operator = columnNode.get(FIELD_OPERATOR).asText();
	        if (!isValidOperator(operator)) {
	            throw new IllegalArgumentException("Invalid operator: " + operator);
	        }
	        String fieldValue = columnNode.get(FIELD_FIELDVALUE).asText();
	        columns.add(new Column(fieldName, operator, fieldValue));
	    }
	    return columns;
	}

	private static Join buildJoinFromJsonNode(JsonNode tableNode) {
	    JsonNode joinTypeNode = tableNode.get(FIELD_JOIN_TYPE);
	    if (joinTypeNode != null) {
	        String joinType = joinTypeNode.asText();
	        String joinCondition = tableNode.get(FIELD_JOIN_CONDITION).asText();
	        return new Join(joinType, tableNode.get(FIELD_TABLE_NAME).asText(), tableNode.get(FIELD_ALIAS).asText(), joinCondition);
	    }
	    return null;
	}

	private static String generateQueryFromTables(List<Table> tables) {
	    List<String> selectClauses = new ArrayList<>();
	    List<String> fromClauses = new ArrayList<>();
	    List<String> joinCondition = new ArrayList<>();
	    List<String> whereCondition = new ArrayList<>();

	    for (Table table : tables) {
	        selectClauses.add(table.generateSelectClause());
	        if(table.getJoin()==null) {
	        	fromClauses.add(table.generateFromClause());
	        }
	        String whereClause = table.generateWhereClause();
	        if (whereClause != null) {
	            whereCondition.add(whereClause);
	        }

	        Join join = table.getJoin();
	        if (join != null) {
	            joinCondition.add(join.getType() + FIELD_SPACE + join.getTableName() + FIELD_SPACE +  join.getAlias() + FIELD_SPACE + CONDITION_ON + join.getCondition());
	        }
	    }

	    StringBuilder sb = new StringBuilder();
	    sb.append(CONDITION_SELECT);
	    sb.append(String.join(DELIMITER_COMMA_SPACE, selectClauses));
	    sb.append(CONDITION_FROM);
	    sb.append(String.join(DELIMITER_COMMA_SPACE, fromClauses));
	    if (!joinCondition.isEmpty()) {
	        sb.append(FIELD_SPACE);
	        sb.append(String.join(FIELD_SPACE, joinCondition));
	    }
	    if (!whereCondition.isEmpty()) {
	        sb.append(CONDITION_WHERE);
	        sb.append(String.join(CONDITION_AND, whereCondition));
	    }

	    return sb.toString();
	}
	
	
	private static boolean isValidOperator(String operator) {
	    switch (operator) {
	        case "IN":
	        case "LIKE":
	        case "=":
	        case "<>":
	        case "<":
	        case ">":
	        case "<=":
	        case ">=":
	        case "BETWEEN":
	            return true;
	        default:
	            return false;
	    }
	}
}
