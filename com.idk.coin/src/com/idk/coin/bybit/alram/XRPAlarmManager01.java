package com.idk.coin.bybit.alram;

import java.math.BigDecimal;

import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.BybitAlarmsModel;

public class XRPAlarmManager01 extends BybitAlarmsModel {
	public static double  HALF		= 0.0;
	public static double  QUATER	= 0.0;
	public static double  PENTA		= 0.0;
	public static double  DECA		= 0.0;
	
	
	public XRPAlarmManager01(String symbol, BybitUser user) throws Exception{
		super(symbol, user);
	}
	public XRPAlarmManager01(String symbol, String web_id, String web_pw) throws Exception{
		super(symbol, web_id, web_pw);
	}
	public void userSet() throws Exception{
		//String default_qty = user.getDefault_qty(); // DB 미적용으로 사용불가
		String default_qty = null;
		
		if(default_qty == null) default_qty = "100";
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
		
		enableDatabase();
	}
	
/**
 * #################   [SHORT] ############# [SHORT]
 * #################   [SHORT] ############# [SHORT]
 * @throws Exception
 */
 
	public void createOpenShort()  throws Exception{
		
		makeOpenShort(0.6450, OVER, 0.6500, HALF, 0.6450, THIRD);
		makeOpenShort(0.6350, OVER, 0.6400, HALF, 0.6350, THIRD);
		makeOpenShort(0.6250, OVER, 0.6300, HALF, 0.6250, THIRD);
		makeOpenShort(0.6150, OVER, 0.6200, HALF, 0.6150, THIRD);
		makeOpenShort(0.6050, OVER, 0.6100, HALF, 0.6050, THIRD);
		
		makeOpenShort(0.5950, OVER, 0.6000, HALF, 0.5950, THIRD);
		makeOpenShort(0.5900, OVER, 0.5950, HALF, 0.5900, THIRD);
		makeOpenShort(0.5850, OVER, 0.5900, HALF, 0.5850, THIRD);
		makeOpenShort(0.5800, OVER, 0.5850, HALF, 0.5800, THIRD);
		makeOpenShort(0.5750, OVER, 0.5800, HALF, 0.5750, THIRD);
		makeOpenShort(0.5700, OVER, 0.5750, HALF, 0.5700, THIRD);
		makeOpenShort(0.5650, OVER, 0.5700, HALF, 0.5650, THIRD);
		makeOpenShort(0.5600, OVER, 0.5650, HALF, 0.5600, THIRD);
		makeOpenShort(0.5550, OVER, 0.5600, HALF, 0.5550, THIRD);
		
		makeOpenShort(0.5500, OVER, 0.5550, QTY, 0.5500, THIRD);
		makeOpenShort(0.5450, OVER, 0.5500, QTY, 0.5450, THIRD);
		makeOpenShort(0.5400, OVER, 0.5450, QTY, 0.5400, THIRD);
		makeOpenShort(0.5350, OVER, 0.5400, QTY, 0.5350, THIRD);
		makeOpenShort(0.5300, OVER, 0.5350, QTY, 0.5300, THIRD);
		makeOpenShort(0.5250, OVER, 0.5300, QTY, 0.5250, THIRD);
		makeOpenShort(0.5200, OVER, 0.5250, QTY, 0.5200, THIRD);
		makeOpenShort(0.5150, OVER, 0.5200, QTY, 0.5150, THIRD);
		makeOpenShort(0.5100, OVER, 0.5150, QTY, 0.5100, THIRD);
		
		makeOpenShort(0.5050, OVER, 0.5100, QTY, 0.5000, THIRD);
		makeOpenShort(0.5000, OVER, 0.5050, QTY, 0.4950, THIRD);
		makeOpenShort(0.4950, OVER, 0.5000, QTY, 0.4900, THIRD);
		makeOpenShort(0.4900, OVER, 0.4950, QTY, 0.4850, THIRD);
		//makeOpenShort(0.4850, OVER, 0.4900, QTY, 0.4800, THIRD);
		//makeOpenShort(0.4800, OVER, 0.4850, QTY, 0.4750, THIRD);
		//홀수 자신 짝수는 반대
	}
	/** ###########  [Model idwook01 XRPUSDT] ########### [SHORT]  **/
	/** ###########  [Model idwook01 XRPUSDT] ########### [SHORT]  **/
	/** ###########  [Model idwook01 XRPUSDT] ########### [SHORT]  **/
	
	public void setShort() throws Exception{
		closeShort(0.4900, OVER, 0.4800, QTY, THIRD);
		/** ↑↑↑↑ -------  Price Line  481  -------  Long First ↓↓↓↓  **/
		
		openShort(0.4750, UNDER, 0.4850, QTY, RR);
		//openShort(0.4900, UNDER, 0.5000, QTY, RR);
	
		

		//<-- sync//<--
	}
	/** ###########  [XRP 01] ########### **/
	public void createCloseShort()throws Exception{
		
		//openShort(0.5000, UNDER, 0.5100, QTY, RR, 0.4950);
		openShort(0.4700, UNDER, 0.4800, QTY, RR, 0.4700);
		openShort(0.4600, UNDER, 0.4700, QTY, RR, 0.4600);
		openShort(0.4500, UNDER, 0.4600, QTY, RR, 0.4500);
		openShort(0.4400, UNDER, 0.4500, QTY, RR, 0.4400);
		
		openShort(0.4300, UNDER, 0.4400, QTY, RR, 0.4300);
		openShort(0.4200, UNDER, 0.4300, QTY, RR, 0.4200);
		openShort(0.4100, UNDER, 0.4200, QTY, RR, 0.4100);
		openShort(0.4000, UNDER, 0.4100, QTY, RR, 0.4000);
		openShort(0.3900, UNDER, 0.4000, QTY, RR, 0.3900);
		
	}
	
	
/**
 * #################   [LONG]      ############# [LONG][LONG]
 * #################   [LONG]      ############# [LONG][LONG]
 * @throws Exception
 */

	public void createCloseLong() throws Exception{
		 //makeCloseLong(0.5100, OVER, 0.5200, QTY, 0.5100, RR);
		openLong(0.6600, OVER, 0.6500, QTY, RR, 0.6550);
		openLong(0.6500, OVER, 0.6400, QTY, RR, 0.6450);
		openLong(0.6400, OVER, 0.6300, QTY, RR, 0.6350);
		openLong(0.6300, OVER, 0.6200, QTY, RR, 0.6250);
		openLong(0.6200, OVER, 0.6100, QTY, RR, 0.6150);
		
		openLong(0.6100, OVER, 0.6000, QTY, RR, 0.6050);
		openLong(0.6000, OVER, 0.5900, QTY, RR, 0.5950);
		openLong(0.5900, OVER, 0.5800, QTY, RR, 0.5850);
		openLong(0.5800, OVER, 0.5700, QTY, RR, 0.5750);
		openLong(0.5700, OVER, 0.5600, QTY, RR, 0.5650);
		
		openLong(0.5600, OVER, 0.5500, QTY, RR, 0.5550);
		openLong(0.5500, OVER, 0.5400, QTY, RR, 0.5450);
		openLong(0.5400, OVER, 0.5300, QTY, RR, 0.5350);
		openLong(0.5300, OVER, 0.5200, QTY, RR, 0.5250);
		openLong(0.5200, OVER, 0.5100, QTY, RR, 0.5150);
		openLong(0.5100, OVER, 0.5000, QTY, RR, 0.5050);
		
		makeCloseLong(0.5000, OVER, 0.5050, QTY, 0.4975, RR);
		makeCloseLong(0.4950, OVER, 0.5000, QTY, 0.4950, RR);
		makeCloseLong(0.4925, OVER, 0.4975, QTY, 0.4925, RR);
		makeCloseLong(0.4900, OVER, 0.4950, QTY, 0.4900, RR);
		makeCloseLong(0.4875, OVER, 0.4925, QTY, 0.4875, RR);
		//makeCloseLong(0.4850, OVER, 0.4900, QTY, 0.4850, RR);
		//makeCloseLong(0.4825, OVER, 0.4875, QTY, 0.4825, RR);
		//makeCloseLong(0.4800, OVER, 0.4850, QTY, 0.4800, RR);
	}
	
	/** ###########  [Model 01] ########### [LONG] **/
	public void setLong() throws Exception{
		
		openLong(0.4900, OVER, 0.4850, QTY, RR);
		openLong(0.4875, OVER, 0.4825, QTY, RR);
		//openLong(0.4850, OVER, 0.4800, QTY, RR);
		//openLong(0.4825, OVER, 0.4775, QTY, RR);
		/** ↑↑↑↑ -------  Price Line 0.499 ------- short  ↓↓↓↓  **/
		closeLong(0.4800, UNDER, 0.4850, QTY, THIRD); 
		closeLong(0.4775, UNDER, 0.4825, QTY, THIRD); 
		
		//closeLong(0.4750, UNDER, 0.4800, QTY, THIRD); 
		//<-- sync//<--
	}
	/** ###########  [Model 01] ########### **/
	public void createOpenLong() throws Exception{
		
		//makeOpenLong(0.4800, UNDER, 0.4750, QTY, 0.4800, THIRD);
		
		makeOpenLong(0.4800, UNDER, 0.4750, QTY, 0.4800, THIRD);
		makeOpenLong(0.4775, UNDER, 0.4725, QTY, 0.4775, THIRD);
		makeOpenLong(0.4750, UNDER, 0.4700, QTY, 0.4750, THIRD);
		makeOpenLong(0.4725, UNDER, 0.4675, QTY, 0.4725, THIRD);
		makeOpenLong(0.4700, UNDER, 0.4650, QTY, 0.4700, THIRD);
		makeOpenLong(0.4675, UNDER, 0.4625, QTY, 0.4675, THIRD);
		makeOpenLong(0.4650, UNDER, 0.4600, QTY, 0.4650, THIRD);
		makeOpenLong(0.4625, UNDER, 0.4575, QTY, 0.4625, THIRD);
		makeOpenLong(0.4600, UNDER, 0.4550, QTY, 0.4600, THIRD);
		makeOpenLong(0.4575, UNDER, 0.4525, QTY, 0.4575, THIRD);
		makeOpenLong(0.4550, UNDER, 0.4500, QTY, 0.4550, THIRD);
		
		makeOpenLong(0.4550, UNDER, 0.4450, HALF, 0.4500, THIRD);
		makeOpenLong(0.4500, UNDER, 0.4400, HALF, 0.4450, THIRD);
		makeOpenLong(0.4450, UNDER, 0.4350, HALF, 0.4400, THIRD);
		makeOpenLong(0.4400, UNDER, 0.4300, HALF, 0.4350, THIRD);
		makeOpenLong(0.4350, UNDER, 0.4250, HALF, 0.4300, THIRD);
		makeOpenLong(0.4300, UNDER, 0.4200, HALF, 0.4250, THIRD);
		makeOpenLong(0.4250, UNDER, 0.4150, HALF, 0.4200, THIRD);
		makeOpenLong(0.4200, UNDER, 0.4100, HALF, 0.4150, THIRD);
		makeOpenLong(0.4150, UNDER, 0.4050, HALF, 0.4100, THIRD);
		makeOpenLong(0.4100, UNDER, 0.4000, HALF, 0.4050, THIRD);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}