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
	}
	
/**
 * #################   [SHORT] ############# [SHORT]
 * #################   [SHORT] ############# [SHORT]
 * @throws Exception
 */
 
	public void createOpenShort()  throws Exception{
		makeOpenShort(0.5900, OVER, 0.6000, QTY, 0.5950, RR);
		makeOpenShort(0.5800, OVER, 0.5900, QTY, 0.5850, RR);
		makeOpenShort(0.5700, OVER, 0.5800, QTY, 0.5750, RR);
		makeOpenShort(0.5600, OVER, 0.5700, QTY, 0.5650, RR);
		makeOpenShort(0.5500, OVER, 0.5600, QTY, 0.5550, RR);
		
		makeOpenShort(0.5400, OVER, 0.5500, QTY, 0.5450, RR);
		makeOpenShort(0.5300, OVER, 0.5400, QTY, 0.5350, RR);
		//makeOpenShort(0.5250, OVER, 0.5300, QTY, 0.5250, RR);
		

		//makeOpenShort(0.5260, OVER, 0.5300, DECA, 0.5280, 5);
		//makeOpenShort(0.5250, OVER, 0.5290, DECA, 0.5270, 5);
		//makeOpenShort(0.5240, OVER, 0.5280, DECA, 0.5260, 3);
		//makeOpenShort(0.5230, OVER, 0.5270, DECA, 0.5250, 4);
		
		//홀수 자기자신 짝수는 반대가 끝
	}
	/** ###########  [Model 01] ########### [SHORT]  **/
	public void setShort() throws Exception{
		closeShort(0.5300, OVER, 0.5280, DECA, 5);
		closeShort(0.5290, OVER, 0.5270, DECA, 5);
		closeShort(0.5280, OVER, 0.5260, DECA, 5);
		closeShort(0.5270, OVER, 0.5250, DECA, 5);
		//closeShort(0.5260, OVER, 0.5240, DECA, 5);
		/** ↑↑↑↑ -------  Price Line  481  -------  Long First ↓↓↓↓  **/
		
		openShort(0.5240, UNDER, 0.5260, DECA, 4);
		openShort(0.5230, UNDER, 0.5250, DECA, 4);
		
		openShort(0.5220, UNDER, 0.5240, DECA, 4);
		openShort(0.5210, UNDER, 0.5230, DECA, 4);
		openShort(0.5200, UNDER, 0.5220, DECA, 4);
		openShort(0.5190, UNDER, 0.5210, DECA, 4);
		openShort(0.5180, UNDER, 0.5200, DECA, 4);
		openShort(0.5170, UNDER, 0.5190, DECA, 4);
		openShort(0.5160, UNDER, 0.5180, DECA, 4);
		openShort(0.5150, UNDER, 0.5170, DECA, 4);
		openShort(0.5140, UNDER, 0.5160, DECA, RR);
		openShort(0.5130, UNDER, 0.5150, DECA, ONCE);
		openShort(0.5120, UNDER, 0.5140, DECA, TWICE);
		openShort(0.5110, UNDER, 0.5130, DECA, RR);
		
	}
	/** ###########  [XRP 01] ########### **/
	public void createCloseShort()throws Exception{
		makeCloseShort(0.5120, UNDER, 0.5100, DECA, 0.5120, ONCE);
		makeCloseShort(0.5110, UNDER, 0.5090, DECA, 0.5110, RR);
		makeCloseShort(0.5100, UNDER, 0.5080, DECA, 0.5100, RR);
		makeCloseShort(0.5090, UNDER, 0.5070, DECA, 0.5090, RR);
		makeCloseShort(0.5080, UNDER, 0.5060, DECA, 0.5080, RR);
		makeCloseShort(0.5070, UNDER, 0.5050, DECA, 0.5070, RR);
		
		
		
		
		makeCloseShort(0.5100, UNDER, 0.5040, PENTA, 0.5060, RR);
		makeCloseShort(0.5100, UNDER, 0.5020, PENTA, 0.5040, RR);
		makeCloseShort(0.5100, UNDER, 0.5000, PENTA, 0.5020, RR);
		makeCloseShort(0.5050, UNDER, 0.4950, QTY, 0.5000, RR);
		makeCloseShort(0.5000, UNDER, 0.4900, QTY, 0.4950, RR);
		makeCloseShort(0.4950, UNDER, 0.4850, QTY, 0.4900, RR);
		
		openShort(0.4800, UNDER, 0.4850, QTY, RR, 0.4800);
		openShort(0.4750, UNDER, 0.4800, QTY, RR, 0.4750);
		openShort(0.4700, UNDER, 0.4750, QTY, RR, 0.4700);
		openShort(0.4650, UNDER, 0.4700, QTY, RR, 0.4650);
		
	}
	
	
/**
 * #################   [LONG]      ############# [LONG][LONG]
 * #################   [LONG]      ############# [LONG][LONG]
 * @throws Exception
 */

	public void createCloseLong() throws Exception{
		 //makeCloseLong(0.5100, OVER, 0.5200, QTY, 0.5100, RR);
		
		openLong(0.6100, OVER, 0.6000, QTY, RR, 0.6050);
		openLong(0.6000, OVER, 0.5900, QTY, RR, 0.5950);
		openLong(0.5900, OVER, 0.5800, QTY, RR, 0.5850);
		openLong(0.5800, OVER, 0.5700, QTY, RR, 0.5750);
		openLong(0.5700, OVER, 0.5600, QTY, RR, 0.5650);
		
		openLong(0.5600, OVER, 0.5500, QTY, RR, 0.5550);
		openLong(0.5500, OVER, 0.5400, QTY, RR, 0.5450);
		openLong(0.5400, OVER, 0.5300, QTY, RR, 0.5350);
		//openLong(0.5300, OVER, 0.5200, QTY, RR, 0.5250);
		
		
		//makeCloseLong(0.5260, OVER, 0.5300, DECA, 0.5280, RR);
		//makeCloseLong(0.5250, OVER, 0.5290, DECA, 0.5270, RR);
		//makeCloseLong(0.5240, OVER, 0.5280, DECA, 0.5260, RR);
		//makeCloseLong(0.5230, OVER, 0.5270, DECA, 0.5250, RR);
	}
	
	/** ###########  [Model 01] ########### [LONG] **/
	public void setLong() throws Exception{
		openLong(0.5300, OVER, 0.5280, DECA, 5);
		openLong(0.5290, OVER, 0.5270, DECA, 5);
		openLong(0.5280, OVER, 0.5260, DECA, 5);
		openLong(0.5270, OVER, 0.5250, DECA, 5);
		//openLong(0.5260, OVER, 0.5240, DECA, 5);
		/** ↑↑↑↑ -------  Price Line 4570 ------- short  ↓↓↓↓  **/
		closeLong(0.5240, UNDER, 0.5260, DECA, TWICE);
		closeLong(0.5230, UNDER, 0.5250, DECA, TWICE);
		closeLong(0.5220, UNDER, 0.5240, DECA, TWICE);
		closeLong(0.5210, UNDER, 0.5230, DECA, TWICE);
		closeLong(0.5200, UNDER, 0.5220, DECA, TWICE);
		closeLong(0.5190, UNDER, 0.5210, DECA, TWICE);
		closeLong(0.5180, UNDER, 0.5200, DECA, TWICE);
		closeLong(0.5170, UNDER, 0.5190, DECA, TWICE);
		closeLong(0.5160, UNDER, 0.5180, DECA, 2);
		closeLong(0.5150, UNDER, 0.5170, DECA, 3);
		closeLong(0.5140, UNDER, 0.5160, DECA, RR);
		closeLong(0.5130, UNDER, 0.5150, DECA, RR);
		closeLong(0.5120, UNDER, 0.5140, DECA, TWICE);
		closeLong(0.5110, UNDER, 0.5130, DECA, ONCE);
		//<-- sync//<--
	}
	/** ###########  [Model 01] ########### **/
	public void createOpenLong() throws Exception{
	
		
		makeOpenLong(0.5120, UNDER, 0.5100, DECA, 0.5120, ONCE);
		makeOpenLong(0.5110, UNDER, 0.5090, DECA, 0.5110, RR);
		makeOpenLong(0.5100, UNDER, 0.5080, DECA, 0.5100, RR);
		
		//makeOpenLong(0.5100, UNDER, 0.5100, QTY, 0.5200, RR);
		makeOpenLong(0.5100, UNDER, 0.5000, QTY, 0.5100, RR);
		makeOpenLong(0.5050, UNDER, 0.4950, QTY, 0.5050, RR);
		
		makeOpenLong(0.4900, UNDER, 0.4800, QTY, 0.4900, RR);
		makeOpenLong(0.4800, UNDER, 0.4700, QTY, 0.4800, RR);
		makeOpenLong(0.4700, UNDER, 0.4600, QTY, 0.4700, RR);
		
		makeOpenLong(0.4650, UNDER, 0.4550, QTY, 0.4600, RR);
		
		makeOpenLong(0.4600, UNDER, 0.4500, QTY, 0.4550, RR);
		makeOpenLong(0.4500, UNDER, 0.4400, QTY, 0.4450, RR);
		makeOpenLong(0.4400, UNDER, 0.4300, QTY, 0.4350, RR);
		makeOpenLong(0.4300, UNDER, 0.4200, QTY, 0.4250, RR);
		makeOpenLong(0.4200, UNDER, 0.4100, QTY, 0.4150, RR);
		makeOpenLong(0.4100, UNDER, 0.4000, QTY, 0.4050, RR);
	}
	
	
}