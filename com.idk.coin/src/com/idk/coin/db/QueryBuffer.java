package com.idk.coin.db;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class QueryBuffer {
	public final static int INT = 0;
	public final static int STR = 1;
	
	StringBuffer intoBuffer = null;
	StringBuffer valueBuffer = null;
	StringBuffer setBuffer = null;
	StringBuffer whereBuffer = null;
	StringBuffer selectBuffer = null;
	StringBuffer orderBuffer = null;
	StringBuffer groupBuffer = null;
	StringBuffer limitBuffer = null;
	StringBuffer orBuffer = null;
	StringBuffer andBuffer = null;
	
	String db	= null;
	String table = null;
	
	public QueryBuffer(){};
	
	public QueryBuffer(String db,String table){
		setDb(db);
		setTable(table);
	}
	public void setTable(String table){
		this.table = table;
	}
	public void setDb(String db){
		this.db = db;
	}
	
	private String getMethodValue(String key,Object obj) throws Exception {
		Class clazz  = obj.getClass();
		String methodName = "get"+key.toUpperCase().substring(0,1) + key.substring(1);
		Method method = clazz.getMethod(methodName, null);
		return (String)method.invoke(obj, null);
	}
	public void intoValueDate(String into,Object obj) throws Exception {
		String value  = getMethodValue(into,obj);
			
		if(value == null || value.length()==0)  intoValueInt(into, "NULL");
		else intoValueStr(into,value);
	}
	public void intoValueInt(String into,Object obj) throws Exception{
		intoValueInt(into,getMethodValue(into,obj));
	}
	public void intoValueStr(String into,Object obj) throws Exception{
		intoValueStr(into,getMethodValue(into,obj));
	}
	
	public void intoValueStr(String into,String value){
		intoValue(into,STR,value);
	}
	public void intoValueInt(String into,String value){
		if(value == null || value.equals("")) value = "0";
		intoValue(into,INT,value);
	}
	private void intoValue(String into,int type,String value){
		setInto(into);
		setIntoValue(type,value);
	}
	public void setInto(String into){
		if(intoBuffer == null) intoBuffer = new StringBuffer();
		if(intoBuffer.length()>0) intoBuffer.append(",");
		intoBuffer.append(into);
	}
	public void setIntoValue(int type,String value){
		if(valueBuffer == null) valueBuffer = new StringBuffer();
		if(valueBuffer.length()>0) valueBuffer.append(",");
		switch(type){
		case INT : valueBuffer.append(value); break;
		case STR : valueBuffer.append("'" + (value != null ? value : "") + "'"); break;
		default : valueBuffer.append("'" + (value != null ? value : "") + "'"); break;
		}
	}
	public String getInsert(){
		return "INSERT INTO " + (db != null ? (db + "." ) : "") + table 
		+ "(" + (intoBuffer != null ? intoBuffer.toString() : "") + ") VALUES (" + (valueBuffer != null ?  valueBuffer.toString() : "") + ")";
	}
	public String getReplace(){
		return "REPLACE INTO " + (db != null ? (db + "." ) : "") + table 
				+ "(" + (intoBuffer != null ? intoBuffer.toString() : "") + ") VALUES (" + (valueBuffer != null ?  valueBuffer.toString() : "") + ")";
	}
	
	public void setValueDate(String key,String date) throws Exception {
		if(date == null) setValueInt(key, "NULL");
		else if(date.trim().length() == 0) setValueInt(key, "NULL");
		else setValueStr(key, date);
	}
	public void setValueDate(String key,Object obj) throws Exception {
		String value = getMethodValue(key, obj);
		setValueDate(key,value);
	}
	public void setValueStr(String key,Object obj) throws Exception {
		setValueStr(key,getMethodValue(key, obj));
	}
	public void setValueInt(String key,Object obj) throws Exception {
		setValueInt(key, getMethodValue(key, obj));
	}
	public void setValueInt(String key,String value){
		if(value == null || value.equals("")) return;
		setValue(key, INT, value);
	}
	public void setValueStr(String key,String value){
		setValue(key, STR, value);
	}
	public void setValue(String key,int type,String value){
		if(setBuffer == null) setBuffer = new StringBuffer();
		if(setBuffer.length() > 0) setBuffer.append(",");
		setBuffer.append(key + " = "); 
		switch(type){
		case INT : setBuffer.append(value); break;
		case STR : setBuffer.append("'" + (value != null ? value : "") + "'"); break;
		default : setBuffer.append("'" + (value != null ? value : "") + "'"); break;
		}
	}
	public String getUpdate(){
		return "UPDATE " + (db != null ? (db + "." ) : "") + table + " SET " + setBuffer.toString() 
			+  (whereBuffer != null  ? " WHERE " + whereBuffer.toString() : "");
	}
	
	
	public void setWhere(String where){
		if(whereBuffer == null) whereBuffer = new StringBuffer();
		
		if(where.toUpperCase().indexOf("WHERE") > 0) {
			where = where.substring(6);
		}
		whereBuffer.append(where);
	}
	public void setAndWhere(String where){
		if(whereBuffer == null) whereBuffer = new StringBuffer();
		if(whereBuffer.length() > 0) whereBuffer.append(" AND ");
		whereBuffer.append(where);
	}
	public void setOrWhere(String where){
		if(whereBuffer == null) whereBuffer = new StringBuffer();
		if(whereBuffer.length() > 0) whereBuffer.append(" OR ");
		whereBuffer.append(where);
	}
	
	public void addWhereIntAnd(String key, String value){
		addWhere(getWhereKeyValue(key, value, INT), "AND");
	}
	public void addWhereIntOr(String key, String value){
		addWhere(getWhereKeyValue(key, value, INT), "OR");
	}
	public void addWhereInt(String key,String value){
		addWhere(getWhereKeyValue(key, value, INT), null);
	}
	public void addWhereStr(String key,String value){
		addWhere(getWhereKeyValue(key, value, STR), null);
	}
	public void addWhereStrAnd(String key, String value){
		addWhere(getWhereKeyValue(key, value, STR), "AND");
	}
	public void addWhereStrOr(String key, String value){
		addWhere(getWhereKeyValue(key, value, STR), "OR");
	}
	
	public void addWhere(String where, String merge){
		if(whereBuffer == null) whereBuffer = new StringBuffer();
		if(whereBuffer.length() > 0) {
			if(merge == null) whereBuffer.append(" , ");
			else whereBuffer.append(" "+merge+" ");
		}
		whereBuffer.append(" " + where+ " ");
	}
	private String getWhereKeyValue(String key, String value, int type){
		String str = "";
		switch(type){
		case INT : return key + " = " + value;
		default  : return key + " = '" +value+"'";
		}
	}
	public String getDelete(){
		return getDelete(null);
	}
	public String getDelete(String prefix){
		return "DELETE "+ (prefix !=null ? prefix : "")+ " FROM " +  (db != null ? (db + "." ) : "") + table 
			+ (whereBuffer != null ? " WHERE " + whereBuffer.toString()  : "");
	}
	
	public void setSelect(Object... args){
		for(int i=0; i<args.length; i++){
			setSelect(args[i].toString());
		}
	}
	public void setSelect(String key){
		if(selectBuffer == null) selectBuffer = new StringBuffer();
		if(selectBuffer.length() > 0) selectBuffer.append(",");
		selectBuffer.append(key);
	}
	public void setOrder(String key,String sort){
		setOrder(key + " "+sort);
	}
	public void setOrder(String orderby){
		if(orderby != null){
			if(orderBuffer != null){
				orderBuffer.append(" , ");
				orderBuffer.append(orderby);
			}else {
				orderBuffer = new StringBuffer(" "+orderby);
			}
		}
	}
	public String getOrder(){
		if(orderBuffer != null) return orderBuffer.toString();
		return null;
	}
	
	public void setLimit(int start,int offset){
		limitBuffer = new StringBuffer("");
		limitBuffer.append(" " + start+ " , " + offset);
	}
	public void setGroupBy(String group){
		groupBuffer = new StringBuffer();
		groupBuffer.append(" "+group +" ");
	}
	public String getSelect(){
		return "SELECT " + selectBuffer.toString() 
				+ " FROM "+  (db != null ? (db + "." ) : "") + table 
				+  (whereBuffer != null  ? " WHERE " + whereBuffer.toString() : "")
				+  (groupBuffer != null  ? " GROUP BY " + groupBuffer.toString() : "")
				+  (orderBuffer != null ? " ORDER BY " + orderBuffer.toString() : "")
				+  (limitBuffer != null ? " LIMIT " + limitBuffer.toString() : "");
	}
	
	public void setSelectObject(HashMap row,Object obj) throws Exception{
		Class clazz  	= obj.getClass();
		String[] keys 	= selectBuffer.toString().split(",");
		
		for(int i=0; i<keys.length; i++){
			String key  = keys[i].trim();
			if(key.indexOf(".")>0) key = key.substring(key.indexOf(".")+1);
			if(key != null && key.length()>0){
				try{
					String methodName 		= "set"+key.toUpperCase().substring(0,1) + key.substring(1);
					Method method 			= clazz.getMethod(methodName, String.class);
					Object value 			= row.get(key);
					
					if(value == null || value.equals("")) value = row.get(key.toUpperCase());
					
					if(method != null && value != null){
						method.invoke(obj, value.toString());
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	public String getSelectBuffer(){
		return selectBuffer != null ? selectBuffer.toString() : "";
	}
	public String getWhereBuffer(){
		return whereBuffer != null ? " WHERE " + whereBuffer.toString() : "";
	}
	public String getOrderBuffer(){
		return orderBuffer != null ? " ORDER BY " + orderBuffer.toString() : "";
	}
	public String getLimitBuffer(){
		return limitBuffer != null ? " LIMIT " + limitBuffer.toString() : "";
	}
	
	public String[] getFieldName(Class o){
		Field[] fields = o.getDeclaredFields();
		String fieldNames[] = new String[fields.length];
		for(int i=0; i<fields.length; i++){
			fieldNames[i] = fields[i].getName();
		}
		return fieldNames;
	}
	
	public void setOr(String or){
		orBuffer = null;
		addOr(or);
	}
	public void addOr(String or){
		if(or != null && !or.trim().equals("")){
			if(orBuffer != null) orBuffer.append(" OR "+or);
			else {
				orBuffer = new StringBuffer(" "+or);
			}
		}
	}
	public String getOrBuffer(){
		if(orBuffer != null) return orBuffer.toString()+" ";
		return null;
	}
	public String clearOrBuffer(){
		String ret = getOrBuffer();
		orBuffer = null;
		return ret;
	}
	public void setAnd(String and){
		andBuffer = null;
		addAnd(and);
	}
	public void addAnd(String and){
		if(and != null && !and.trim().equals("")){
			if(andBuffer != null) andBuffer.append(" AND "+and);
			else {
				andBuffer = new StringBuffer(" "+and);
			}
		}
	}
	public String getAndBuffer(){
		if(andBuffer != null) return andBuffer.toString() + " " ;
		return null;
	}
}
