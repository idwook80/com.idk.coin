package com.idk.coin.bybit;

import java.util.ArrayList;

import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.ExecutionModel;
import com.idk.coin.bybit.model.MarketModel;

public class BybitExecutionManager {
	ArrayList<ExecutionModel> executions ;
	
	public BybitExecutionManager() {
		executions = new ArrayList<>();
	}
	public void addExecutionModel(ExecutionModel m) {
		synchronized (executions) {
			executions.add(m);
		}
	}
	public void removeExecutionModel(ExecutionModel m) {
		synchronized (executions) {
			executions.remove(m);
		}
	}
	public ExecutionModel getExecutionModel(String web_id) {
		synchronized (executions) {
			for(int i=0; i<executions.size(); i++) {
				ExecutionModel m = executions.get(i);
				BybitUser user =  m.getUser();
				
				if(user.getUser_id().equals(web_id)) {
					return m;
				}
			}
		}
		return null;
	}
	public ExecutionModel createExecution(BybitUser user) {
		ExecutionModel m = getExecutionModel(user.getId());
		if(m == null) {
			m = new ExecutionModel(user);
			addExecutionModel(m);
		}
		return m;
	}
	public void startAllExecutions() {
		synchronized (executions) {
			for(int i=0; i<executions.size(); i++) {
				ExecutionModel m = executions.get(i);
				m.startExcution();
			}
		}
	}
}
