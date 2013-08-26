package com.xteam.war3.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class YoukuUrlUtils {
	
	public static String getYoukuRealUrl(String url) throws JSONException {
		// 目前只支持 地址 带有 v_show 的网页
		String htmlUrl = "http://v.youku.com/v_show/id_XNTU0MTE1NTg4.html";// 视频页面地址
		// 截取 id f17333950o1p0
		String htmlUrlId = htmlUrl.substring(htmlUrl.lastIndexOf("/") + 4, htmlUrl.length() - 5);
		// 取得返回的 json数据
		String json = getInputHtml("http://v.youku.com/player/getPlayList/VideoIDS/" + htmlUrlId);

		if (json != null) {
			JSONObject jsobj = new JSONObject(json);
			JSONArray jsonarr = jsobj.getJSONArray("data");
			JSONObject obj1 = jsonarr.getJSONObject(0);
			double seed = obj1.getDouble("seed");
			JSONObject obj2 = obj1.getJSONObject("streamfileids");
			String mp4id = null;
			String flvid = null;
			String format = null;
			try {
				mp4id = obj2.getString("mp4");
				format = "mp4";
			} catch (JSONException e) {
				Log.e("owen", "没有MP4格式");
				try {
					flvid = obj2.getString("flv");
					format = "flv";
				} catch (JSONException e1) {
					Log.e("owen", "没有FLV格式");
					return null;
				}
				return null;
			}
			//
			String realfileid = null;
			if (format.equals("mp4")) {
				realfileid = getFileID(mp4id, seed);
			} else {
				realfileid = getFileID(flvid, seed);
			}
			String idLeft = realfileid.substring(0, 8);
			String idRight = realfileid.substring(10);
			//
			String sid = genSid();
			JSONObject obj3 = obj1.getJSONObject("segs");
			JSONArray mp4arr = obj3.getJSONArray(format);
			String result = null;
			for (int i = 0; i < mp4arr.length(); i++) {
				JSONObject o = mp4arr.getJSONObject(i);
				String k = o.getString("k");
				result = "http://f.youku.com/player/getFlvPath/sid/" + sid + "_" + String.format("%1$02X", i) + "/st/"
						+ format + "/fileid/" + idLeft + String.format("%1$02X", i) + idRight + "?K=" + k;
				Log.e("owen", "URL: " + result);
			}
			return result;
		}
		return null;
	}

	private static String getFileID(String fileid, double seed) {
		String mixed = getFileIDMixString(seed);
		String[] ids = fileid.split("\\*");
		StringBuilder realId = new StringBuilder();
		int idx;
		for (int i = 0; i < ids.length; i++) {
			idx = Integer.parseInt(ids[i]);
			realId.append(mixed.charAt(idx));
		}
		return realId.toString();
	}

	public static String genSid() {
		int i1 = (int) (1000 + Math.floor(Math.random() * 999));
		int i2 = (int) (1000 + Math.floor(Math.random() * 9000));
		return System.currentTimeMillis() + "" + i1 + "" + i2;
	}

	private static String getFileIDMixString(double seed) {
		StringBuilder mixed = new StringBuilder();
		StringBuilder source = new StringBuilder(
				"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ/\\:._-1234567890");
		int index, len = source.length();
		for (int i = 0; i < len; ++i) {
			seed = (seed * 211 + 30031) % 65536;
			index = (int) Math.floor(seed / 65536 * source.length());
			mixed.append(source.charAt(index));
			source.deleteCharAt(index);
		}
		return mixed.toString();
	}

	public static String getInputHtml(String urlStr) {
		URL url = null;
		try {
			url = new URL(urlStr);
			HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
			httpsURLConnection.setRequestMethod("GET");
			httpsURLConnection.setConnectTimeout(30 * 1000);
			if (httpsURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// 通过输入流获取网络图片
				InputStream inputStream = httpsURLConnection.getInputStream();
				byte[] data = readInputStream(inputStream);
				inputStream.close();
				return new String(data, "UTF-8");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public static byte[] readInputStream(InputStream inputStream) throws Exception {
		// 定义一个输出流向内存输出数据
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// 定义一个缓冲区
		byte[] buffer = new byte[1024];
		// 读取数据长度
		int len = 0;
		// 当取得完数据后会返回一个-1
		while ((len = inputStream.read(buffer)) != -1) {
			// 把缓冲区的数据 写到输出流里面
			byteArrayOutputStream.write(buffer, 0, len);
		}
		byteArrayOutputStream.close();
		// 得到数据后返回
		return byteArrayOutputStream.toByteArray();
	}

}
