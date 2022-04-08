<%@ page language="java" contentType="application/json; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="javax.sql.DataSource"%>
<%@ page import="org.springframework.beans.factory.annotation.Value"%>
<%@ page import="org.springframework.jndi.JndiTemplate"%>
<%@ page import="org.springframework.jndi.JndiObjectFactoryBean"%>
<%@ page import="org.springframework.jdbc.core.JdbcTemplate"%>
<%@ page import="org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup" %>
<%@ page import="org.springframework.jdbc.support.rowset.SqlRowSet" %>
<%@ page import="org.springframework.jdbc.support.rowset.SqlRowSetMetaData" %>
<%@ page import="org.springframework.web.util.HtmlUtils" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%!
	private String jndiName = "jdbc/app";
	private JdbcTemplate jdbcTemplate;
	private ObjectMapper objectMapper = new ObjectMapper();

	public void init() throws ServletException {
		
		JndiDataSourceLookup jndiDataSourceLookup = new JndiDataSourceLookup();
		jndiDataSourceLookup.setResourceRef(true);
		
		DataSource dataSource = jndiDataSourceLookup.getDataSource(jndiName);
		
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public Map<String, Object> select(final String sql, final String[] params) {
		
		List<String[]> resultList = new ArrayList<String[]>();
		
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, null);
		SqlRowSetMetaData sqlRowSetMetaData = sqlRowSet.getMetaData();
		
		final String[] columnNames = sqlRowSetMetaData.getColumnNames();
		final int columnSize = columnNames.length; 
		
		while(sqlRowSet.next()) {
			
			String[] columns = new String[columnSize];
			
			for(int i=0; i<columnSize; i++) {
				columns[i] = sqlRowSet.getString(i + 1);
			}
			
			resultList.add(columns);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("columnNames", columnNames);
		result.put("result", resultList);
		
		return result;
	}
	
%>
<%
	Map<String, Object> data = objectMapper.readValue(request.getInputStream(), Map.class);
	
	String sql = (String) data.get("query");
	String[] params = (String[]) data.get("params");
	
	System.out.println(sql);
	
//	sql = HtmlUtils.htmlUnescape(sql);
	
	String result = "";
	
	if(!StringUtils.isEmpty(sql)) {
		response.getWriter().write( objectMapper.writeValueAsString(select(sql, params)) );
	}

%>