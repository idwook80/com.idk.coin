package com.idk.coin.bybit.alram;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.idk.coin.bybit.account.PositionRest;
import com.idk.coin.bybit.account.WalletRest;
import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.Balance;
import com.idk.coin.bybit.model.BybitAlarmsModel;
import com.idk.coin.bybit.model.Position;

public class XRPAlarmManager02 extends BybitAlarmsModel {
	public static double  HALF		= 0.0;
	public static double  QUATER	= 0.0;
	public static double  PENTA		= 0.0;
	public static double  DECA		= 0.0;
	
	
	public XRPAlarmManager02(String symbol, BybitUser user) throws Exception{
		super(symbol, user);
	}
	public XRPAlarmManager02(String symbol, String web_id, String web_pw) throws Exception{
		super(symbol, web_id, web_pw);
	}
	public void userSet() throws Exception{
		//String default_qty = user.getDefault_qty(); // DB 미적용으로 사용불가
		String default_qty = null;
		
		if(default_qty == null) default_qty = "10";
		setDefault_qty(Double.valueOf(default_qty));
		LOSS_TRIGGER_QTY= 1;
	    MIN_PROFIT		= 0.0020;
	    BigDecimal temp = new BigDecimal(String.valueOf(default_qty));
		HALF 			= temp.divide(new BigDecimal("2")).doubleValue();
		QUATER 			= temp.divide(new BigDecimal("4")).doubleValue();
		PENTA 			= temp.divide(new BigDecimal("5")).doubleValue();
		DECA 			= temp.divide(new BigDecimal("10")).doubleValue();
		
	
	}
	public void run() {
		while(is_run) {
			try {
				this.printListString();
				LOG.info(this.getSize() + "  : " + this.getClass().getName());
				ArrayList<Position> ps = PositionRest.getActiveMyPosition(user.getApi_key(),user.getApi_secret(), symbol);
				Balance balance 		 =   WalletRest.getWalletBalance(user.getApi_key(),user.getApi_secret(), "USDT");
				Position buy =  Position.getPosition(ps, symbol, "Buy");
				Position sell = Position.getPosition(ps, symbol, "Sell");
				LOG.info(balance.toString());
				LOG.info(buy.toString());
				LOG.info(sell.toString());
				
				LOG.info("현재가:["+current_price + "],잔고 : [" + String.format("%.2f",balance.getEquity())  + "]"
						+","+ "SHORT : [" + sell.getEntry_price() + "]," + String.format("%.2f",sell.getSize()) + "주"
						+","+ "LONG : ["+ buy.getEntry_price() +"],"+ String.format("%.2f",buy.getSize()) + "주"); 
			}catch(Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000* 60 * 5);
			}catch(Exception e) {
				
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
		
		enableDatabase();
		this.printListString();
		//clearAlarmDatabase();
		//loadAlarmDatabase();
		//registerAlarmDatabase();
		
		
	
	}
	

/**
 * #################   [SHORT] ############# [SHORT]
 * #################   [SHORT] ############# [SHORT]
 * @throws Exception
 */
 
	public void createOpenShort()  throws Exception{
		
		makeOpenShort(0.5270, OVER, 0.5290, 1, 0.5270, THIRD);
		makeOpenShort(0.5250, OVER, 0.5270, 1, 0.5250, THIRD);
		makeOpenShort(0.5230, OVER, 0.5250, 1, 0.5230, THIRD);
		makeOpenShort(0.5210, OVER, 0.5230, 1, 0.5210, THIRD);
		makeOpenShort(0.5190, OVER, 0.5210, 1, 0.5190, THIRD);
		makeOpenShort(0.5170, OVER, 0.5190, 1, 0.5170, THIRD);
		makeOpenShort(0.5150, OVER, 0.5170, 1, 0.5150, THIRD);
		
		makeOpenShort(0.527, OVER, 0.529, 1, 0.527, 5);
		makeOpenShort(0.525, OVER, 0.527, 1, 0.525, 5);
		makeOpenShort(0.523, OVER, 0.525, 1, 0.523, 5);
		makeOpenShort(0.521, OVER, 0.523, 1, 0.521, 5);
		makeOpenShort(0.519, OVER, 0.521, 1, 0.519, 5);
		makeOpenShort(0.517, OVER, 0.519, 1, 0.517, 5);
		makeOpenShort(0.515, OVER, 0.517, 1, 0.515, 5);
		
		//홀수 자신 짝수는 반대
	}
	/** ###########  [Model idwook01 XRPUSDT] ########### [SHORT]  **/
	/** ###########  [Model idwook01 XRPUSDT] ########### [SHORT]  **/
	/** ###########  [Model idwook01 XRPUSDT] ########### [SHORT]  **/
	
	public void setShort() throws Exception{
		//closeShort(0.5170, OVER, 0.5150, 1, THIRD);
		closeShort(0.5150, OVER, 0.5130, 1, THIRD);
		closeShort(0.5130, OVER, 0.5110, 1, THIRD);
		
		
		/** ↑↑↑↑ -------  Price Line  481  -------  Long First ↓↓↓↓  **/
		openShort(0.5090, UNDER, 0.5110, 1, RR);
		openShort(0.5070, UNDER, 0.5090, 1, RR);
		openShort(0.5050, UNDER, 0.5070, 1, RR);
		
	}
	/** ###########  [XRP 01] ########### **/
	public void createCloseShort()throws Exception{
		makeCloseShort(0.5050, UNDER, 0.5030, 1, 0.5050, RR);
		makeCloseShort(0.5030, UNDER, 0.5010, 1, 0.5030, RR);
		
		makeCloseShort(0.5010, UNDER, 0.4990, 1, 0.5010, RR);
		makeCloseShort(0.4990, UNDER, 0.4970, 1, 0.4990, RR);
		makeCloseShort(0.4970, UNDER, 0.4950, 1, 0.4970, RR);
		makeCloseShort(0.4950, UNDER, 0.4930, 1, 0.4950, RR);
		makeCloseShort(0.4930, UNDER, 0.4910, 1, 0.4930, RR);
		
		openShort(0.4890, UNDER, 0.4910, 1, RR, 0.4890);
		
	}
	
	
/**
 * #################   [LONG]      ############# [LONG][LONG]
 * #################   [LONG]      ############# [LONG][LONG]
 * @throws Exception
 */

	public void createCloseLong() throws Exception{
		openLong(0.5320, OVER, 0.5300, 1, RR, 0.5320);
		
		makeCloseLong(0.5280, OVER, 0.5300, 1, 0.5280, RR);
		makeCloseLong(0.5260, OVER, 0.5280, 1, 0.5260, RR);
		makeCloseLong(0.5240, OVER, 0.5260, 1, 0.5240, RR);
		makeCloseLong(0.5220, OVER, 0.5240, 1, 0.5220, RR);
		makeCloseLong(0.5200, OVER, 0.5220, 1, 0.5200, RR);
		
		makeCloseLong(0.5180, OVER, 0.5200, 1, 0.5180, THIRD);
		makeCloseLong(0.5160, OVER, 0.5180, 1, 0.5160, THIRD);
		makeCloseLong(0.5140, OVER, 0.5160, 1, 0.5140, THIRD);
	}
	
	/** ###########  [Model 01] ########### [LONG] **/
	public void setLong() throws Exception{
		//openLong(0.5160, OVER, 0.5140, 1, THIRD);
		openLong(0.5140, OVER, 0.5120, 1, THIRD);
		openLong(0.5120, OVER, 0.5100, 1, THIRD);
		/** ↑↑↑↑ -------  Price Line 0.499 ------- short  ↓↓↓↓  **/
		closeLong(0.5080, UNDER, 0.5100, 1, TWICE);
		closeLong(0.5060, UNDER, 0.5080, 1, TWICE);
		
		//<-- sync//<--
	}
	/** ###########  [Model 01] ########### **/
	public void createOpenLong() throws Exception{
		makeOpenLong(0.5060, UNDER, 0.5040, 1, 0.5060, THIRD);
		makeOpenLong(0.5040, UNDER, 0.5020, 1, 0.5040, THIRD);
		makeOpenLong(0.5020, UNDER, 0.5000, 1, 0.5020, THIRD);
		
		makeOpenLong(0.5000, UNDER, 0.4980, 1, 0.5000, THIRD);
		makeOpenLong(0.4980, UNDER, 0.4960, 1, 0.4980, THIRD);
		makeOpenLong(0.4960, UNDER, 0.4940, 1, 0.4960, THIRD);
		makeOpenLong(0.4940, UNDER, 0.4920, 1, 0.4940, THIRD);
		makeOpenLong(0.4920, UNDER, 0.4900, 1, 0.4920, THIRD);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}