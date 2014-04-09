package com.hawklithm.bluehawky.sim.reader;

import com.google.gson.Gson;
import com.hawklithm.bluehawky.client.NettyClient;
import com.multiagent.hawklithm.davinci.exceptioin.MessageTransportException;
import com.multiagent.hawklithm.global.pusher.IMessagePusher;
import com.multiagent.hawklithm.global.register.IRegisterManager;
import com.multiagent.hawklithm.readerNet.DO.RFIDOriginalInfos;
import com.multiagent.hawklithm.readerNet.DO.SqlRFIDOriginalObject;
import com.multiagent.hawklithm.shadowsong.DO.Warden;
import com.multiagent.hawklithm.shadowsong.DO.WardenMessage;
import com.multiagent.hawklithm.shadowsong.manager.WardenManager;

public class SimReader {
	protected IRegisterManager register;
	/**
	 * 读卡器名称
	 */
	protected String readerName;
	protected NettyClient nettyClient;
	/**
	 * 接收数据的读卡器模块的RFID
	 */
	protected String dataTargetName;
	
	protected int timeDelay=5000;
	
	protected Integer readerRfid;
	/**
	 * 接收本模拟读卡器数据的目标读卡器名称
	 */
	protected String readerTargetName;
	/**
	 * 数据类型,用于标记监听的信息类型
	 */
	protected String[] wardenedMessageKinds;
	protected String wardenedMessageKindToNext;
	protected IMessagePusher<WardenMessage> pusher;
	protected Gson gson=new Gson();
	public static final String 
			MESSAGE_GATE_OVER = "message_gate_over",
			MESSAGE_SORTING_ENTER="message_sorting_enter",
			MESSAGE_SORTING_START="message_sorting_start",
			MESSAGE_SORTING_END="message_sorting_end",
			MESSAGE_SORTTING_OVER = "message_sortting_over",
			MESSAGE_CLEANANDDISINFECT_ENTER="message_cleananddisinfect_enter",
			MESSAGE_CLEANANDDISINFECT_START="message_cleananddisinfect_start",
			MESSAGE_CLEANANDDISINFECT_END="message_cleananddisinfect_end",
			MESSAGE_CLEANANDDISINFECT_OVER = "message_cleananddisinfect_over",
			MESSAGE_PACKAGING_ENTER = "message_packaging_enter",
			MESSAGE_PACKAGING_START = "message_packaging_start",
			MESSAGE_PACKAGING_END = "message_packaging_end",
			MESSAGE_PACKAGING_OVER = "message_packaging_over",
			MESSAGE_SECONDARYDISINFECT_ENTER = "message_secondarydisinfect_enter",
			MESSAGE_SECONDARYDISINFECT_START = "message_secondarydisinfect_start",
			MESSAGE_SECONDARYDISINFECT_END = "message_secondarydisinfect_end",
			MESSAGE_SECONDARYDISINFECT_OVER = "message_secondarydisinfect_over",
			MESSAGE_STERILESTORAGE_ENTER = "message_sterilestorage_enter",
			MESSAGE_STERILESTORAGE_START = "message_sterilestorage_start",
			MESSAGE_STERILESTORAGE_END = "message_sterilestorage_end",
			MESSAGE_STERILESTORAGE_OVER = "message_sterilestorage_over";

	public void initReader() {
		System.out.println("readerName:"+readerName+", kinds: "+gson.toJson(wardenedMessageKinds));
		if (!register.regist(new Warden(readerName, wardenedMessageKinds){

			@Override
			public void asynchronizedProcess(String message) {
				System.out.println("asynchronizedProcess: "+readerName+", "+message);
				SqlRFIDOriginalObject[] data=gson.fromJson(message, SqlRFIDOriginalObject[].class);
				RFIDOriginalInfos	pacData=new RFIDOriginalInfos();
				pacData.setInfos(data);
				pacData.setTargets(new String[]{dataTargetName});
				for (SqlRFIDOriginalObject index:pacData.getInfos()){
					index.setReaderId(readerRfid);
				}
				String msg=gson.toJson(pacData);
				try {
//					sendRFID(msg);
					Thread.sleep(timeDelay);
					pushToNext(message);
//				} catch (MessageTransportException e) {
//					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		})){
			System.out.println("simReader 注册失败,name: "+readerName+" ,kinds"+gson.toJson(wardenedMessageKinds));
		}
		System.out.println("register: "+gson.toJson(((WardenManager)register).getMap()));
	}
	public void sendRFID(String infos) throws MessageTransportException{
		nettyClient.sendMessage(infos);
		System.out.println("sendRFID["+readerName+"]send [ "+infos+" ] to reader module");
	}
	public void pushToNext(String infos){
		WardenMessage message=new WardenMessage();
		message.setNote(infos);
		message.setKind(wardenedMessageKindToNext);
		message.setTarget(readerTargetName);
		pusher.push(message);
	}

	public IRegisterManager getRegister() {
		return register;
	}

	public void setRegister(IRegisterManager register) {
		this.register = register;
	}


	public String getReaderName() {
		return readerName;
	}

	public void setReaderName(String readerName) {
		this.readerName = readerName;
	}

	public String[] getWardenedMessageKinds() {
		return wardenedMessageKinds;
	}

	public void setWardenedMessageKinds(String[] wardenedMessageKinds) {
		this.wardenedMessageKinds = wardenedMessageKinds;
	}
	public NettyClient getNettyClient() {
		return nettyClient;
	}
	public void setNettyClient(NettyClient nettyClient) {
		this.nettyClient = nettyClient;
	}
	public IMessagePusher<WardenMessage> getPusher() {
		return pusher;
	}
	public void setPusher(IMessagePusher<WardenMessage> pusher) {
		this.pusher = pusher;
	}
	public String getWardenedMessageKindToNext() {
		return wardenedMessageKindToNext;
	}
	public void setWardenedMessageKindToNext(String wardenedMessageKindToNext) {
		this.wardenedMessageKindToNext = wardenedMessageKindToNext;
	}
	public String getReaderTargetName() {
		return readerTargetName;
	}
	public void setReaderTargetName(String readerTargetName) {
		this.readerTargetName = readerTargetName;
	}
	public String getDataTargetName() {
		return dataTargetName;
	}
	public void setDataTargetName(String dataTargetName) {
		this.dataTargetName = dataTargetName;
	}
	public Integer getReaderRfid() {
		return readerRfid;
	}
	public void setReaderRfid(Integer readerRfid) {
		this.readerRfid = readerRfid;
	}
	public int getTimeDelay() {
		return timeDelay;
	}
	public void setTimeDelay(int timeDelay) {
		this.timeDelay = timeDelay;
	}

}
