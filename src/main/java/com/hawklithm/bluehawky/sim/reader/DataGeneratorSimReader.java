package com.hawklithm.bluehawky.sim.reader;

import com.hawklithm.bluehawky.generator.RFIDDataGenerator;
import com.multiagent.hawklithm.davinci.exceptioin.MessageTransportException;
import com.multiagent.hawklithm.readerNet.DO.RFIDOriginalInfos;
import com.multiagent.hawklithm.readerNet.DO.SqlRFIDOriginalObject;

public class DataGeneratorSimReader extends SimReader {
	protected RFIDDataGenerator generator;

	@Override
	public void initReader() {
		generator=new RFIDDataGenerator(this);
		new Thread(generator).start();
	}

	public void dataSender(SqlRFIDOriginalObject[] data) {
		RFIDOriginalInfos pacData = new RFIDOriginalInfos();
		pacData.setInfos(data);
		pacData.setTargets(new String[] { dataTargetName });
		for (SqlRFIDOriginalObject index:pacData.getInfos()){
				index.setReaderId(readerRfid);
		}
		String msg = gson.toJson(pacData);
//		try {
//			sendRFID(msg);
			pushToNext(gson.toJson(data));
//		} catch (MessageTransportException e) {
//			e.printStackTrace();
//		}
	}

}
