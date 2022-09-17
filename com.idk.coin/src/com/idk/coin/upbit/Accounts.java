package com.idk.coin.upbit;

import java.util.ArrayList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Accounts {
	ArrayList<Account> accounts;
	
	public Accounts() {
		accounts = new ArrayList<Account>();
	}
	public void addAccount(JsonObject jobj) {
		String currency = jobj.get("currency").getAsString();
		String unit_currency = jobj.get("unit_currency").getAsString();
		Account a = getAccount(currency);
		if(a == null) {
			 a = new Account(currency, unit_currency);
			 addAccount(a);
		} 
		a.setJsonObject(jobj);
		
	}
	public void addAccount(Account account) {
		synchronized(accounts) {
			accounts.add(account);
		}
	}
	public void removeAccount(Account account) {
		synchronized(accounts) {
			accounts.remove(account);
		}
	}
	public void removeAccount(String currency) {
		Account account = getAccount(currency);
		removeAccount(account);
	}
	public Account getAccount(String currency) {
		synchronized(accounts) {
			for(Account a : accounts) {
				if(a.getCurrency().equals(currency)){
					return a;
				}
			}
		}
		return null;
	}
	
	public Account[] getAccounts() {
		return accounts.toArray(new Account[0]);
	}
}
