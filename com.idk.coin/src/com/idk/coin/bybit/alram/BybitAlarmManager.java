package com.idk.coin.bybit.alram;

import java.util.ArrayList;

import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.model.AlarmManager;

public class BybitAlarmManager {
	ArrayList<AlarmManager> list ;
	
	public BybitAlarmManager() {
		init();
	}
	public void init() {
		list = new ArrayList<AlarmManager>();
	}
	public void addAlarmManager(AlarmManager a) {
		synchronized(list) {
			list.add(a);
		}
	}
	public void removeAlarmManager(AlarmManager a) {
		synchronized(list) {
			list.remove(a);
		}
	}
	
	public AlarmManager getAlarmManager(String symbol, String web_id) {
		synchronized(list) {
			for(AlarmManager a : list) {
				if(symbol.equals(a.symbol)) {
					BybitUser user = a.getUser();
					if(user.getId().equals(web_id)) return a;
				}
			}
		}
		return null;
	}
	public void startAllAlarms() {
		synchronized(list) {
			for(AlarmManager a : list) {
				a.startAlarmManager();
			}
		}
	}
}
