package cn.zhouyafeng.itchat4j.demo.demo1;

import cn.zhouyafeng.itchat4j.Wechat;
import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.beans.RecommendInfo;
import cn.zhouyafeng.itchat4j.core.Core;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;
import cn.zhouyafeng.itchat4j.utils.tools.DownloadTools;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 处理消息只是打印出来
 * 
 * @author https://github.com/yaphone
 * @date 创建时间：2017年4月25日 上午12:18:09
 * @version 1.0
 *
 */
public class MyDemo implements IMsgHandlerFace {
	Logger LOG = Logger.getLogger(MyDemo.class);

	// 这里是需要发送的文件的路径,分为pic/voice/viedo/media四个文件夹
	String wechatFileDir = "/Users/krison/Desktop/wechat";

	@Override
	public String textMsgHandle(BaseMsg msg) {

		System.out.println("消息类型 : "+msg.getType());
		System.out.println("消息内容 : "+msg.getText());

		if(msg.isGroupMsg()){
			//群消息
			List<JSONObject> groupList = WechatTools.getGroupList();
//			System.out.println(JSON.toJSONString(groupList));
			for(JSONObject group : groupList){
				if(msg.getToUserName().equals(group.getString("UserName"))){
					System.out.println("群名称："+group.getString("NickName"));
					break;
				}
			}
			JSONArray memberListByGroupId = WechatTools.getMemberListByGroupId(msg.getToUserName());
			for(Object jsonObject : memberListByGroupId){
				JSONObject job = (JSONObject)jsonObject;
				if(msg.getFromUserName().equals(job.getString("UserName"))){
					System.out.println("消息发送人："+job.getString("NickName"));
					break;
				}
			}
		}else{
			//个人消息
			List<JSONObject> contactList = WechatTools.getContactList();
			for(JSONObject job : contactList){
				if(msg.getToUserName().equals(job.getString("UserName"))){
					System.out.println("个人消息，消息接收人："+job.getString("NickName"));
					break;
				}
			}
		}

		return null;
	}

	@Override
	public String picMsgHandle(BaseMsg msg) {
		// 这里使用收到图片的时间作为文件名
		String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
		// 调用此方法来保存图片
		String picPath = wechatFileDir + File.separator + "pic" + File.separator + fileName + ".jpg";
		// 保存图片的路径
		DownloadTools.getDownloadFn(msg, MsgTypeEnum.PIC.getType(), picPath);
		return "图片保存成功";
	}

	@Override
	public String voiceMsgHandle(BaseMsg msg) {
		String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
		String voicePath = wechatFileDir + File.separator + "voice" + File.separator + fileName + ".mp3";
		DownloadTools.getDownloadFn(msg, MsgTypeEnum.VOICE.getType(), voicePath);
		return "声音保存成功";
	}

	@Override
	public String viedoMsgHandle(BaseMsg msg) {
		String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
		String viedoPath = wechatFileDir + File.separator + "viedo" + File.separator + fileName + ".mp4";
		DownloadTools.getDownloadFn(msg, MsgTypeEnum.VIEDO.getType(), viedoPath);
		return "视频保存成功";
	}

	@Override
	public String mediaMsgHandle(BaseMsg msg) {
		String fileName = msg.getFileName();
		// 这里是需要保存收到的文件路径，文件可以是任何格式如PDF，WORD，EXCEL等。
		String filePath = wechatFileDir + File.separator + "media" + File.separator + fileName;
		DownloadTools.getDownloadFn(msg, MsgTypeEnum.MEDIA.getType(), filePath);
		return "文件保存成功";
	}

	@Override
	public String nameCardMsgHandle(BaseMsg msg) {
		return "收到名片消息";
	}

	@Override
	public void sysMsgHandle(BaseMsg msg) { // 收到系统消息
		String text = msg.getContent();
		LOG.info(text);
	}

	@Override
	public String verifyAddFriendMsgHandle(BaseMsg msg) {
		return null;
	}

}
