package com.idk.coin.bybit.alram;

import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.BybitAlarmsModel;

public class AlarmManager02 extends BybitAlarmsModel {
	public AlarmManager02(String symbol, BybitUser user) throws Exception{
		super(symbol, user);
	}
	public AlarmManager02(String symbol, String web_id, String web_pw) throws Exception{
		super(symbol, web_id, web_pw);
	}
	public void userSet() throws Exception{
		String default_qty = user.getDefault_qty();
		if(default_qty == null) default_qty = "0.05";
		setDefault_qty(Double.valueOf(default_qty));
		LOSS_TRIGGER_QTY= 0.001;
	    MIN_PROFIT		= 50;
	  
	}
	public void run() {
		while(is_run) {
			try {
				Thread.sleep(1000* 60 * 10);
				LOG.info(this.getSize() + "  : " + this.getClass().getName());
				this.printListString();
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	public void alarmSet() throws Exception{
		createOpenShort();
		setShort();
		createCloseShort();
		
		createCloseLong();
		setLong();
		createOpenLong();
	}
	
/**
 * #################   [SHORT] #############
 * @throws Exception
 */
	public void createOpenShort()  throws Exception{
		shortStopLoss(23000, QTY2);
		
		makeOpenShort(22710, OVER, 22910, QTY, 22810, RR);
		makeOpenShort(22510, OVER, 22710, QTY, 22610, RR);
		makeOpenShort(22310, OVER, 22510, QTY, 22410, RR);
		makeOpenShort(22110, OVER, 22310, QTY, 22210, RR);
		makeOpenShort(21910, OVER, 22110, QTY, 22010, RR);
		shortStopLoss(22750, QTY3);
		shortStopLoss(22500, QTY3);
		shortStopLoss(22250, QTY3);
		shortStopLoss(22000, QTY3);
		
		
		shortStopLoss(21750, QTY2);
		shortStopLoss(21500, QTY2);
		shortStopLoss(21250, QTY);
		shortStopLoss(21000, QTY);
		
		makeOpenShort(21710, OVER, 21910, QTY, 21810, RR);
		makeOpenShort(21510, OVER, 21710, QTY, 21610, RR);
		makeOpenShort(21310, OVER, 21510, QTY, 21410, RR);
		makeOpenShort(21110, OVER, 21310, QTY, 21210, RR);
		makeOpenShort(20910, OVER, 21110, QTY, 21010, RR);
		
		
		shortStopLoss(20750, QTY2);
		shortStopLoss(20500, QTY2);
		shortStopLoss(20250, QTY2);
		shortStopLoss(20000, QTY2);
		
		makeOpenShort(20710, OVER, 20910, QTY, 20710, RR);
		makeOpenShort(20510, OVER, 20710, QTY, 20510, RR);
		makeOpenShort(20310, OVER, 20510, QTY, 20310, RR);
		makeOpenShort(20110, OVER, 20310, QTY, 20110, RR);
		makeOpenShort(19910, OVER, 20110, QTY, 19910, RR);
		
		shortStopLoss(19750, QTY);
		shortStopLoss(19500, QTY);
		shortStopLoss(19250, QTY);
		shortStopLoss(19000, QTY);
		
		makeOpenShort(19710, OVER, 19910, QTY, 19710, RR);
		makeOpenShort(19510, OVER, 19710, QTY, 19510, RR);
		makeOpenShort(19310, OVER, 19510, QTY, 19310, RR);
		makeOpenShort(19110, OVER, 19310, QTY, 19110, RR);
		makeOpenShort(18910, OVER, 19110, QTY, 18910, RR);
		
		shortStopLoss(18750, QTY);
		shortStopLoss(18500, QTY);
		shortStopLoss(18250, QTY);
		shortStopLoss(18000, QTY);
		
		makeOpenShort(18710, OVER, 18910, QTY, 18710, RR);
		makeOpenShort(18510, OVER, 18710, QTY, 18510, RR);
		makeOpenShort(18310, OVER, 18510, QTY, 18310, RR);
		makeOpenShort(18110, OVER, 18310, QTY, 18110, RR);
		makeOpenShort(17910, OVER, 18110, QTY, 17910, RR);
		
		shortStopLoss(17750, QTY2);
		shortStopLoss(17500, QTY2);
		//shortStopLoss(17250, QTY);
		//shortStopLoss(17000, QTY);
		
		makeOpenShort(17710, OVER, 17910, QTY, 17710, RR);
		makeOpenShort(17510, OVER, 17710, QTY, 17510, RR);
		makeOpenShort(17310, OVER, 17510, QTY, 17310, RR);
		makeOpenShort(17110, OVER, 17310, QTY, 17110, RR);
		makeOpenShort(16910, OVER, 17110, QTY, 16910, RR);
		
		makeOpenShort(16710, OVER, 16910, QTY, 16710, RR);
		
	}
	
	/** ###########  [Model 02] ########### [SHORT]  **/
	public void setShort() throws Exception{
		addCloseShort(16710, OVER, 16510, QTY, RR);
		/** ↑↑↑↑ -------  Price Line 16621 0.75 -------  Long First ↓↓↓↓  **/
		addOpenShort(16310, UNDER, 16510, QTY, RR);
	}
	/** ###########  [Model 02] ########### **/
	public void createCloseShort()throws Exception{
		
		makeCloseShort(16310, UNDER, 16110, QTY, 16310, RR);
		
		//makeCloseShort(16110, UNDER, 15910, QTY, 16110, RR);
		makeCloseShort(15910, UNDER, 15710, QTY, 15910, RR);
		//makeCloseShort(15710, UNDER, 15510, QTY, 15710, RR);
		makeCloseShort(15510, UNDER, 15310, QTY, 15510, RR);
		//makeCloseShort(15310, UNDER, 15110, QTY, 15310, RR);
		
		makeCloseShort(15110, UNDER, 14910, QTY, 15010, RR);
		//makeCloseShort(14910, UNDER, 14710, QTY, 14810, RR);
		makeCloseShort(14710, UNDER, 14510, QTY, 14610, RR);
		//makeCloseShort(14510, UNDER, 14310, QTY, 14410, RR);
		makeCloseShort(14310, UNDER, 14110, QTY, 14210, RR);
		
		//makeCloseShort(14110, UNDER, 13910, QTY, 14010, RR);
		makeCloseShort(13910, UNDER, 13710, QTY, 13810, RR);
		//makeCloseShort(13710, UNDER, 13510, QTY, 13610, RR);
		makeCloseShort(13510, UNDER, 13310, QTY, 13410, RR);
		//makeCloseShort(13310, UNDER, 13110, QTY, 13210, RR);
		
		makeCloseShort(13110, UNDER, 12910, QTY, 13010, RR);
		//makeCloseShort(12910, UNDER, 12710, QTY, 12810, RR);
		makeCloseShort(12710, UNDER, 12510, QTY, 12610, RR);
		//makeCloseShort(12510, UNDER, 12310, QTY, 12410, RR);
		makeCloseShort(12310, UNDER, 12110, QTY, 12210, RR);
		
		//makeCloseShort(12110, UNDER, 11910, QTY, 12010, RR);
		makeCloseShort(11910, UNDER, 11710, QTY, 11810, RR);
		//makeCloseShort(11710, UNDER, 11510, QTY, 11610, RR);
		makeCloseShort(11510, UNDER, 11310, QTY, 11410, RR);
		//makeCloseShort(11310, UNDER, 11110, QTY, 11210, RR);
		
		makeCloseShort(11110, UNDER, 10910, QTY, 10010, RR);
		//makeCloseShort(10910, UNDER, 10710, QTY, 10810, RR);
		makeCloseShort(10710, UNDER, 10510, QTY, 10610, RR);
		//makeCloseShort(10510, UNDER, 10310, QTY, 10410, RR);
		makeCloseShort(10310, UNDER, 10110, QTY, 10210, RR);
	}

	
	
	
	
	
/**
 * #################   [LONG]      ############# [LONG][LONG]
 * @throws Exception
 */
	public void createCloseLong() throws Exception{
		
		
		//makeCloseLong(22810, OVER, 23010, QTY, 22910, RR);
		makeCloseLong(22610, OVER, 22810, QTY, 22710, RR);
		//makeCloseLong(22410, OVER, 22610, QTY, 22510, RR);
		makeCloseLong(22210, OVER, 22410, QTY, 22310, RR);
		//makeCloseLong(22010, OVER, 22210, QTY, 22110, RR);
		
		makeCloseLong(21810, OVER, 22010, QTY, 21810, RR);
		//makeCloseLong(21610, OVER, 21810, QTY, 21610, RR);
		makeCloseLong(21410, OVER, 21610, QTY, 21410, RR);
		//makeCloseLong(21210, OVER, 21410, QTY, 21210, RR);
		makeCloseLong(21010, OVER, 21210, QTY, 21010, RR);
		
		//makeCloseLong(20810, OVER, 21010, QTY, 20810, RR);
		makeCloseLong(20610, OVER, 20810, QTY, 20610, RR);
		//makeCloseLong(20410, OVER, 20610, QTY, 20410, RR);
		makeCloseLong(20210, OVER, 20410, QTY, 20210, RR);
		//makeCloseLong(20010, OVER, 20210, QTY, 20010, RR);
		
		makeCloseLong(19810, OVER, 20010, QTY, 19810, RR);
		//makeCloseLong(19610, OVER, 19810, QTY, 19610, RR);
		makeCloseLong(19410, OVER, 19610, QTY, 19410, RR);
		//makeCloseLong(19210, OVER, 19410, QTY, 19210, RR);
		makeCloseLong(19010, OVER, 19210, QTY, 19010, RR);
		
		//makeCloseLong(18810, OVER, 19010, QTY, 18810, RR);
		makeCloseLong(18610, OVER, 18810, QTY, 18610, RR);
		//makeCloseLong(18410, OVER, 18610, QTY, 18410, RR);
		makeCloseLong(18210, OVER, 18410, QTY, 18210, RR);
		//makeCloseLong(18010, OVER, 18210, QTY, 18010, RR);
		
		makeCloseLong(17810, OVER, 18010, QTY, 17810, RR);
		//makeCloseLong(17610, OVER, 17810, QTY, 17610, RR);
		makeCloseLong(17410, OVER, 17610, QTY, 17410, RR);
		//makeCloseLong(17210, OVER, 17410, QTY, 17210, RR);
		makeCloseLong(17010, OVER, 17210, QTY, 17010, RR);
		
		makeCloseLong(16810, OVER, 17010, QTY, 16810, RR);
		makeCloseLong(16610, OVER, 16810, QTY, 16610, RR);
	}
	
	/** ###########  [Model 02] ########### [LONG] **/
	public void setLong() throws Exception{
		
		//addOpenLong(16610, OVER, 16410, QTY, RR);
		
		/** ↑↑↑↑ -------  Price Line 16602 0.7 ------- short  ↓↓↓↓  **/
		
		addCloseLong(16410, UNDER, 16610, QTY, RR);
		addCloseLong(16210, UNDER, 16410, QTY, RR);
		
	}
	/** ###########  [Model 02] ########### **/
	public void createOpenLong() throws Exception{
		
		//makeOpenLong(16410, UNDER, 16210, QTY, 16410, RR);
		makeOpenLong(16210, UNDER, 16010, QTY, 16210, RR);
		
		//longStopLoss(15750, QTY);
		longStopLoss(15500, QTY);
		longStopLoss(15250, QTY);
		longStopLoss(15000, QTY);
		
		makeOpenLong(16010, UNDER, 15810, QTY, 16010, RR);
		makeOpenLong(15810, UNDER, 15610, QTY, 15810, RR);
		//makeOpenLong(15610, UNDER, 15410, QTY, 15610, RR);
		makeOpenLong(15410, UNDER, 15210, QTY, 15410, RR);
		makeOpenLong(15210, UNDER, 15010, QTY, 15210, RR);
		
		longStopLoss(14750, QTY2);
		longStopLoss(14500, QTY2);
		longStopLoss(14250, QTY);
		longStopLoss(14000, QTY);
		
		makeOpenLong(15010, UNDER, 14810, QTY, 15010, RR);
		makeOpenLong(14810, UNDER, 14610, QTY, 14810, RR);
		//makeOpenLong(14610, UNDER, 14410, QTY, 14610, RR);
		makeOpenLong(14410, UNDER, 14210, QTY, 14410, RR);
		makeOpenLong(14210, UNDER, 14010, QTY, 14210, RR);
		
		longStopLoss(13750, QTY);
		longStopLoss(13500, QTY2);
		longStopLoss(13250, QTY2);
		longStopLoss(13000, QTY);
		
		makeOpenLong(14010, UNDER, 13810, QTY, 14010, RR);
		makeOpenLong(13810, UNDER, 13610, QTY, 13810, RR);
		//makeOpenLong(13610, UNDER, 13410, QTY, 13610, RR);
		makeOpenLong(13410, UNDER, 13210, QTY, 13410, RR);
		makeOpenLong(13210, UNDER, 13010, QTY, 13210, RR);
		
		longStopLoss(12750, QTY);
		longStopLoss(12500, QTY2);
		longStopLoss(12250, QTY2);
		longStopLoss(12000, QTY);
		
		makeOpenLong(13010, UNDER, 12810, QTY, 13010, RR);
		makeOpenLong(12810, UNDER, 12610, QTY, 12810, RR);
		//makeOpenLong(12610, UNDER, 12410, QTY, 12610, RR);
		makeOpenLong(12410, UNDER, 12210, QTY, 12410, RR);
		makeOpenLong(12210, UNDER, 12010, QTY, 12210, RR);
		
		longStopLoss(11750, QTY);
		longStopLoss(11500, QTY2);
		longStopLoss(11250, QTY2);
		longStopLoss(11000, QTY);
		
		makeOpenLong(12010, UNDER, 11810, QTY, 12010, RR);
		makeOpenLong(11810, UNDER, 11610, QTY, 11810, RR);
		//makeOpenLong(11610, UNDER, 11410, QTY, 11610, RR);
		makeOpenLong(11410, UNDER, 11210, QTY, 11410, RR);
		makeOpenLong(11210, UNDER, 11010, QTY, 11210, RR);
		
		longStopLoss(10750, QTY);
		longStopLoss(10500, QTY);
		longStopLoss(10250, QTY);
		longStopLoss(10000, QTY);
		
		makeOpenLong(11010, UNDER, 10810, QTY, 11010, RR);
		makeOpenLong(10810, UNDER, 10610, QTY, 10810, RR);
		//makeOpenLong(10610, UNDER, 10410, QTY, 10610, RR);
		makeOpenLong(10410, UNDER, 10210, QTY, 10410, RR);
		makeOpenLong(10210, UNDER, 10010, QTY, 10210, RR);
		
		
		
		
		
	}
	
	
}
