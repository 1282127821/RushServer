package com.game;
//package com.star.light.game;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.star.light.util.Log;
//
///**
// * 龙图
// */
//public class LongtuService {
//	/**
//	 * 1．登陆验证配置变量名
//	 */
//	String VERIFY_LOGIN = "longtu.verify_login";
//
//	/**
//	 * 3．订单查询配置变量名
//	 */
//	String VERIFY_ORDER = "longtu.verify_order";
//
//	/**
//	 * host配置变量名
//	 */
//	String SERVER_HOST = "longtu.serverHost";
//	
//	private static LongtuService longtuService = new LongtuService();
//
//	public static LongtuService getInstance() {
//		return longtuService;
//	}
//
//	/**
//	 * 登陆验证
//	 */
//	public int verifyLogin(String gameid, String channelid, String serverid, String uid, String token) {
//		String result = "";
//		try {
//			Log.debug("开始调用verifyLogin接口");
//			// 新建访问代理类对象
//			Map<String, String> dataMap = new HashMap<String, String>();
//			// 分配的游戏id
//			dataMap.put("gameid", gameid);
//			// Sdk客户端返回的channelid
//			dataMap.put("channelid", channelid);
//			// Sdk客户端返回的serverid
//			dataMap.put("serverid", serverid);
//			// Sdk客户端返回的uid
//			dataMap.put("uid", uid);
//			// Sdk客户端返回的token
//			dataMap.put("token", token);
//
//			String serverHost = SDKConfig.get(SERVER_HOST);
//			String server = SDKConfig.get(VERIFY_LOGIN);
//			String url = serverHost + server;
//			result = HttpUtil.doPost(url, dataMap, "UTF-8");
//			JSONObject jsonObject = JSON.parseObject(result);
//			return jsonObject.getInteger("code");
//		} catch (Exception e) {
//			Log.error("龙图登陆错误, gameid:" + gameid + ", channelid:" + channelid + ", serverid:" + serverid + ", uid:" + uid + ", token:" + token + ", result:" + result, e);
//		}
//		return 0;
//	}
//
//	/**
//	 * 订单查询
//	 */
//	public boolean getOrder(String gameId, String billno, String money, String goodsid) {
//		try {
//			Log.debug("开始调用verifyCharge接口");
//			// 新建访问代理类对象
//			Map<String, String> dataMap = new HashMap<String, String>();
//			// 分配的游戏id
//			dataMap.put("gameid", gameId);
//			// 订单号
//			dataMap.put("billno", billno);
//			// 金额
//			dataMap.put("money", money);
//			// 商品id
//			dataMap.put("goodsid", goodsid);
//			String serverHost = SDKConfig.get(SERVER_HOST);
//			String server = SDKConfig.get(VERIFY_ORDER);
//			String url = serverHost + server;
//			String result = HttpUtil.doPost(url, dataMap, "UTF-8");
//			if (result.equals("fail")) {
//				return false;
//			}
//		} catch (Exception e) {
//			Log.error("龙图登陆错误,gameId:" + gameId + "billno:" + billno + "money:" + money + "goodsid:" + goodsid, e);
//		}
//		return true;
//	}
//}
