package com.idk.coin.db.upbit;

import java.util.HashMap;
import java.util.Iterator;

public abstract class DaoModel {

	public String getInsertQuery(HashMap map) {
		Iterator<String> it = map.keySet().iterator();
		StringBuffer keyBuffer = new StringBuffer("(");
		StringBuffer valueBuffer = new StringBuffer("(");
		boolean isFirst = true;
		while(it.hasNext()) {
			String key = it.next();
			Object value = map.get(key);
			if(!isFirst) {
				keyBuffer.append(",");
				valueBuffer.append(",");
			}
			keyBuffer.append("`" + key + "`");
			valueBuffer.append("'" + value + "'");
			isFirst = false;
		}
		keyBuffer.append(")");
		valueBuffer.append(")");
		return keyBuffer.toString() + " VALUES " + valueBuffer.toString();
	}
	public String getUpdateQuery(HashMap map) {
		Iterator<String> it = map.keySet().iterator();
		StringBuffer queryBuffer= new StringBuffer("");
		boolean isFirst = true;
		while(it.hasNext()) {
			String key = it.next();
			Object value = map.get(key);
			if(!isFirst) {
				queryBuffer.append(",");
			}
			queryBuffer.append("`"+key+"` = '" + value + "'");
			isFirst = false;
		}
		return queryBuffer.toString();
	}
}
