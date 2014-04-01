package com.hawklithm.bluehawky.generator;

import java.util.Date;
import java.util.Random;

import com.hawklithm.bluehawky.sim.reader.DataGeneratorSimReader;
import com.multiagent.hawklithm.readerNet.DO.SqlRFIDOriginalObject;

public class RFIDDataGenerator implements Runnable {

	private Random random = new Random();
	private int dataNumberPerTerm = 10;
	private DataGeneratorSimReader reader;
	private int[] staticRFID=new int[]{1000001,1000002,1000003,1000004,1000005,1000006,1000007,1000008,1000009,1000010};
	
	public RFIDDataGenerator(DataGeneratorSimReader simReader){
		reader=simReader;
	}

	@Override
	public void run() {
		int counter = 1;
		while ((counter--) > 0) {
			SqlRFIDOriginalObject[] datas = new SqlRFIDOriginalObject[dataNumberPerTerm];
			for (int i = 0; i < dataNumberPerTerm; i++) {
				datas[i] = new SqlRFIDOriginalObject();
				datas[i].setDate(new Date());
				datas[i].setRfid(staticRFID[i%10]);
//				datas[i].setRfid(random.nextInt(1000000)+1000000);
			}
			reader.dataSender(datas);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public int getDataNumberPerTerm() {
		return dataNumberPerTerm;
	}

	public void setDataNumberPerTerm(int dataNumberPerTerm) {
		this.dataNumberPerTerm = dataNumberPerTerm;
	}

}
