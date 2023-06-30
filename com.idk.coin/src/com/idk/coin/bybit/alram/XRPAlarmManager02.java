package com.idk.coin.bybit.alram;

import java.math.BigDecimal;

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
	public CalculateModel createCalculateModel(BybitAlarmsModel parent,double price,Position buy,
	 		Position sell, Balance balance,double qty, boolean debug) {
	 return new CalculatePositionV3(parent, price, buy, sell, balance, qty, debug);
	}
	
	public static int IDLE_TIME = 3;
	public int idle_check_time = -1; //min
	public static int RESET_TIME = 60;
	public int reset_check_time = 0; //min
	public void run() {
		try {
			alarmSet();
		}catch(Exception e){
			
		}
		while(is_run) {
			try {
				Thread.sleep(1000* 60 *1);
				LOG.info(this.getSize() + "  : " + this.getClass().getName() +" 알람 리셋 : " + reset_check_time + "분, 알랑 활성체크: " + idle_check_time + "분");
				status();
				if(idle_check_time-- < 0) {
					this.printListString();
					checkAlarmIdles(10);
					idle_check_time = IDLE_TIME;
				}
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
		enableDatabase(DISABLE);
	}
	

/**
 * #################   [SHORT] ############# [SHORT]
 * #################   [SHORT] ############# [SHORT]
 * @throws Exception
 */
 
	public void createOpenShort()  throws Exception{
		
		makeOpenShort(0.4740, OVER, 0.4750, 1, 0.4740, THIRD);
		makeOpenShort(0.4730, OVER, 0.4740, 1, 0.4730, THIRD);
		makeOpenShort(0.4720, OVER, 0.4730, 1, 0.4720, THIRD);
		makeOpenShort(0.4710, OVER, 0.4720, 1, 0.4710, THIRD);
		makeOpenShort(0.4700, OVER, 0.4710, 1, 0.4700, THIRD);
		makeOpenShort(0.4690, OVER, 0.4700, 1, 0.4690, THIRD);
		
		//홀수 자신 짝수는 반대
	}
	/** ###########  [Model idwook01 XRPUSDT] ########### [SHORT]  **/
	/** ###########  [Model idwook01 XRPUSDT] ########### [SHORT]  **/
	/** ###########  [Model idwook01 XRPUSDT] ########### [SHORT]  **/
	
	public void setShort() throws Exception{
		closeShort(0.4710, OVER, 0.4700, 1, THIRD);
		
		
		/** ↑↑↑↑ -------  Price Line  481  -------  Long First ↓↓↓↓  **/
		openShort(0.4690, UNDER, 0.4700, 1, RR);
		openShort(0.4690, UNDER, 0.4700, 1, RR);
		
		
	}
	/** ###########  [XRP 01] ########### **/
	public void createCloseShort()throws Exception{
		//makeCloseShort(0.4700, UNDER, 0.4690, 1, 0.4700, RR);
		makeCloseShort(0.4690, UNDER, 0.4680, 1, 0.4690, RR);
		makeCloseShort(0.4680, UNDER, 0.4670, 1, 0.4680, RR);
		makeCloseShort(0.4670, UNDER, 0.4660, 1, 0.4670, RR);
		makeCloseShort(0.4660, UNDER, 0.4650, 1, 0.4660, RR);
		
		
		openShort(0.2600, UNDER, 0.2800, 1, RR, 0.2600);
		
	}
	
	
/**
 * #################   [LONG]      ############# [LONG][LONG]
 * #################   [LONG]      ############# [LONG][LONG]
 * @throws Exception
 */

	public void createCloseLong() throws Exception{
		//openLong(0.7200, OVER, 0.7000, 1, RR, 0.7200);
		
		//makeCloseLong(0.5900, OVER, 0.6000, 1, 0.5900, THIRD);
		//makeCloseLong(0.5700, OVER, 0.5800, 1, 0.5700, THIRD);
		//makeCloseLong(0.5500, OVER, 0.5600, 1, 0.5500, THIRD);
		//makeCloseLong(0.5300, OVER, 0.5400, 1, 0.5300, THIRD);
		//makeCloseLong(0.5100, OVER, 0.5200, 1, 0.5100, THIRD);
		//makeCloseLong(0.4900, OVER, 0.5000, 1, 0.4900, THIRD);
		//makeCloseLong(0.4700, OVER, 0.4800, 1, 0.4700, THIRD);
	}
	
	/** ###########  [Model 01] ########### [LONG] **/
	public void setLong() throws Exception{
		//openLong(0.4800, OVER, 0.4700, 1, THIRD);
		/** ↑↑↑↑ -------  Price Line 0.499 ------- short  ↓↓↓↓  **/
		//closeLong(0.4600, UNDER, 0.4800, 1, TWICE);
		
		//<-- sync//<--
	}
	/** ###########  [Model 01] ########### **/
	public void createOpenLong() throws Exception{
		
		//makeOpenLong(0.4800, UNDER, 0.4600, 1, 0.4800, RR);
		////makeOpenLong(0.4600, UNDER, 0.4400, 1, 0.4600, RR);
		//makeOpenLong(0.4400, UNDER, 0.4200, 1, 0.4400, RR);
		//makeOpenLong(0.4200, UNDER, 0.4000, 1, 0.4200, RR);
		//makeOpenLong(0.4000, UNDER, 0.3800, 1, 0.4000, RR);
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
}