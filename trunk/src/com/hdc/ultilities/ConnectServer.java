package com.hdc.ultilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

import com.hdc.data.Active;
import com.hdc.data.Advertise;
import com.hdc.data.Category;
import com.hdc.data.Data;
import com.hdc.data.Item;
import com.hdc.data.Message;
import com.hdc.data.Promotion;
import com.hdc.data.Sms;

public class ConnectServer {
	public static ConnectServer instance = new ConnectServer();

	public static String HOST = "http://api.cliphot.me/";
	public final static String EMPTY = "EMPTY";
	public String REF_CODE = "10e36710a919d7f43080695cf8587e9c";
	public final static String Register = "Register.php";
	public final static String GetPhotoList = "Video.php";
	public final String PROMOTION = "Promotion.php";
	public final static String Version = "Version.php";
	public final static String Category = "Category.php";
	public final static String Search = "SearchVideo.php";
	public final static String OtherVideo = "OtherVideo.php";
	public final static String Activation = "activation_msexy_android.php";
	public final static String Update = "UpdateView.php";
	public final static String UpdateViewApp = "UpdateViewApp.php";
	public final static String Sms = "Sms.php";
	//public final static String SmsMSexy = "SmsMsexy.php";
	public final static String SmsMSexy = "syntax_msexy.php";
	
	public final static String v = "1.0";
	public final static String midp = "2.0";
	public final static String type = "photo";
	public final static String type_new = "2";
	public static String Question = "?";
	public static String And = "&";
	public static String Equals = "=";
	public static String catID = "0";
	public static int pageCurrent = 1;
	public static String m_AppID = "";
	public static String SPACE = " ";
	public String PROVIDER_ID = "0";
	public String LINK_UPDATE = "http://thegioigame.mobi/";
	public static String TOKEN = "TAoViEC@)!#2012";
	public String INFO = "info";
	public String RECORD = "30";
	// public boolean isFirstTime = false;
	public String isFirstTime = "";
	public int views = 0;
	public String MyBrand = "";
	public String MyModel = "";

	// httpClient
	static HttpClient client;
	// httpparams
	static HttpParams p;
	// list nameValuePair
	static ArrayList<NameValuePair> nameValuePair;
	// httppost
	static HttpPost httppost;
	// data
	public Promotion m_Promotion = new Promotion();
	public Advertise m_Advertise;
	public Message m_Message;
	public static Data m_Data = new Data();
	public ArrayList<Item> m_ListItem = new ArrayList<Item>();
	public ArrayList<Item> m_OtherListItem = new ArrayList<Item>();
	public Sms m_Sms = new Sms();
	public ArrayList<Category> m_ListCategory = new ArrayList<Category>();
	public Active m_Active = new Active();
	public boolean flagSearch = true;

	// contructor
	public ConnectServer() {
		// TODO Auto-generated constructor stub
	}

	// get data for appID
	public String getAppID(int w, int h) {
		String m_Info = "refCode" + Equals + REF_CODE + And + "token" + Equals + TOKEN + And
				+ "branch" + Equals + MyBrand + And + "handset" + Equals + MyModel + And + "w"
				+ Equals + w + And + "h" + Equals + h + And + "midp" + Equals + midp + And
				+ "operation_system" + Equals + "Android" + And + "type_content" + Equals
				+ type_new;

		String Info_Base64 = Base64.encode(m_Info.getBytes());

		String userID = "";

		// init httpparams
		p = new BasicHttpParams();
		p.setParameter("info", Info_Base64);
		// p.setParameter("brand", EMPTY);
		// p.setParameter("model", EMPTY);
		// p.setParameter("w", w + "");
		// p.setParameter("h", h + "");
		// p.setParameter("midp", midp);
		// p.setParameter("v", v);
		// p.setParameter("refCode", REF_CODE);
		// init httpclient
		client = new DefaultHttpClient(p);
		// init list nameValuepair
		nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("info", Info_Base64));
		// nameValuePair.add(new BasicNameValuePair("brand", EMPTY));
		// nameValuePair.add(new BasicNameValuePair("model", EMPTY));
		// nameValuePair.add(new BasicNameValuePair("w", w + ""));
		// nameValuePair.add(new BasicNameValuePair("h", h + ""));
		// nameValuePair.add(new BasicNameValuePair("midp", midp));
		// nameValuePair.add(new BasicNameValuePair("v", v));
		// nameValuePair.add(new BasicNameValuePair("refCode", REF_CODE));

		// init httppost
		// String url = HOST + Register + Question + "brand" + Equals + EMPTY
		// + And + "model" + Equals + EMPTY + And + "w" + Equals + w + And
		// + "h" + Equals + h + And + "midp" + Equals + midp + And + "v"
		// + Equals + v + And + "refCode" + Equals + REF_CODE;
		String url = HOST + Register + Question + INFO + Equals + Info_Base64;

		httppost = new HttpPost(url);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			userID = client.execute(httppost, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// slip string
		// String[] s = userID.split("#");
		String res = "";
		try {
			JSONObject json = new JSONObject(userID);
			String status = json.getString("status");
			res = json.getString("res");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

	// get data for appID
	public void updateViewApp(int w, int h) {
		String m_Info = "appId" + Equals + m_AppID + And + "token" + Equals + TOKEN + And
				+ "branch" + Equals + MyBrand + And + "handset" + Equals + MyModel + And + "w"
				+ Equals + w + And + "h" + Equals + h + And + "midp" + Equals + midp + And
				+ "operation_system" + Equals + "Android" + And + "type_content" + Equals
				+ type_new;

		String Info_Base64 = Base64.encode(m_Info.getBytes());

		String userID = "";

		// init httpparams
		p = new BasicHttpParams();
		p.setParameter("info", Info_Base64);
		// init httpclient
		client = new DefaultHttpClient(p);
		// init list nameValuepair
		nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("info", Info_Base64));

		String url = HOST + UpdateViewApp + Question + INFO + Equals + Info_Base64;

		httppost = new HttpPost(url);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			userID = client.execute(httppost, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// slip string
		// String[] s = userID.split("#");
		String res = "";
		try {
			JSONObject json = new JSONObject(userID);
			String status = json.getString("status");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// get data for version
	public int getVersion() {
		String m_Info = "token" + Equals + TOKEN + And + "type" + Equals + type_new + And + "v"
				+ Equals + v;

		String Info_Base64 = Base64.encode(m_Info.getBytes());

		String m_version = "";

		// init httpparams
		p = new BasicHttpParams();
		p.setParameter("info", Info_Base64);
		// p.setParameter("type", type);
		// p.setParameter("v", v);
		// p.setParameter("refCode", REF_CODE);

		// init httpclient
		client = new DefaultHttpClient(p);
		// init list nameValuepair
		nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("info", Info_Base64));
		// nameValuePair.add(new BasicNameValuePair("type", type));
		// nameValuePair.add(new BasicNameValuePair("v", v));
		// nameValuePair.add(new BasicNameValuePair("refCode", REF_CODE));

		// init httppost
		// String url = HOST + Version + Question + "type" + Equals + type + And
		// + "v" + Equals + v + And + "refCode" + Equals + REF_CODE;
		String url = HOST + Version + Question + "info" + Equals + Info_Base64;

		httppost = new HttpPost(url);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			m_version = client.execute(httppost, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// jsonObject
		try {
			JSONObject j = new JSONObject(m_version);
			m_version = j.getString("status");
			// j = new JSONObject(m_version);
			// m_version = j.getString("message");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return Integer.parseInt(m_version.trim());
	}

	// get List data
	public void getListVideo(String userID) {
		String m_Info = "token" + Equals + TOKEN + And + "catId" + Equals + catID + And + "p"
				+ Equals + pageCurrent;// + And + "record" + Equals + RECORD;

		String Info_Base64 = Base64.encode(m_Info.getBytes());

		String data = "";

		// init httpparams
		p = new BasicHttpParams();
		p.setParameter("info", Info_Base64);
		// p.setParameter("catid", PROVIDER_ID);
		// p.setParameter("p", Integer.toString(pageCurrent));
		// p.setParameter("app", userID);
		// p.setParameter("refCode", REF_CODE);
		// init httpclient
		client = new DefaultHttpClient(p);
		// init list nameValuepair
		nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("info", Info_Base64));
		// nameValuePair.add(new BasicNameValuePair("catid", PROVIDER_ID));
		// nameValuePair.add(new BasicNameValuePair("p", Integer
		// .toString(pageCurrent)));
		// nameValuePair.add(new BasicNameValuePair("app", userID));
		// nameValuePair.add(new BasicNameValuePair("refCode", REF_CODE));
		// init httppost
		// String url = HOST + GetPhotoList + Question + "catid" + Equals
		// + PROVIDER_ID + And + "p" + Equals
		// + Integer.toString(pageCurrent) + And + "app" + Equals + userID
		// + And + "refCode" + Equals + REF_CODE;
		String url = HOST + GetPhotoList + Question + "info" + Equals + Info_Base64;

		httppost = new HttpPost(url);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			data = client.execute(httppost, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// jsonObject
		try {
			JSONObject j = new JSONObject(data);
			// try {
			m_Promotion = getDataPromotion(j.getString("promotion"));
			// m_Advertise = getAdvertise(j.getString("ads"));
			// } catch (JSONException e2) {
			//
			// }

			// m_Message = getMessage(j.getString("status"));
			m_Data = getData(j.getString("data"));

			j = new JSONObject(j.getString("data"));
			ArrayList<Item> aa = getListItem(j.getString("item"));
			if (aa != null)
				m_ListItem = aa;
			// m_Sms = getSms(j.getString("sms"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// get List data
	public void getPromotion() {
		String m_Info = "token" + Equals + TOKEN + And + "type=2" + And + "refCode" + Equals
				+ REF_CODE + And + "os_type=Android";

		String Info_Base64 = Base64.encode(m_Info.getBytes());

		String data = "";

		// init httpparams
		p = new BasicHttpParams();
		p.setParameter("info", Info_Base64);
		// p.setParameter("catid", PROVIDER_ID);
		// p.setParameter("p", Integer.toString(pageCurrent));
		// p.setParameter("app", userID);
		// p.setParameter("refCode", REF_CODE);
		// init httpclient
		client = new DefaultHttpClient(p);
		// init list nameValuepair
		nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("info", Info_Base64));
		// nameValuePair.add(new BasicNameValuePair("catid", PROVIDER_ID));
		// nameValuePair.add(new BasicNameValuePair("p", Integer
		// .toString(pageCurrent)));
		// nameValuePair.add(new BasicNameValuePair("app", userID));
		// nameValuePair.add(new BasicNameValuePair("refCode", REF_CODE));
		// init httppost
		// String url = HOST + GetPhotoList + Question + "catid" + Equals
		// + PROVIDER_ID + And + "p" + Equals
		// + Integer.toString(pageCurrent) + And + "app" + Equals + userID
		// + And + "refCode" + Equals + REF_CODE;
		String url = HOST + PROMOTION + Question + "info" + Equals + Info_Base64;

		httppost = new HttpPost(url);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			data = client.execute(httppost, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// jsonObject
		try {
			JSONObject j = new JSONObject(data);
			j = new JSONObject(j.getString("promotion"));
			try {
				m_Promotion.setLink(j.getString("link"));
				m_Promotion.setSrc(j.getString("src"));
				if (!m_Promotion.getSrc().equals("")) {
					Bitmap b = null;
					b = DownloadImage.instance.getImage(m_Promotion
							.getSrc());
					m_Promotion.setImg(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// // get List data
	public void updateView(String itemId) {
		String m_Info = "token" + Equals + TOKEN + And + "dataID" + Equals + itemId + And + "type"
				+ Equals + type_new;// + And + "record" + Equals + RECORD;

		String Info_Base64 = Base64.encode(m_Info.getBytes());

		String data = "";

		// init httpparams
		p = new BasicHttpParams();
		p.setParameter("info", Info_Base64);
		// p.setParameter("catid", PROVIDER_ID);
		// p.setParameter("p", Integer.toString(pageCurrent));
		// p.setParameter("app", userID);
		// p.setParameter("refCode", REF_CODE);
		// init httpclient
		client = new DefaultHttpClient(p);
		// init list nameValuepair
		nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("info", Info_Base64));
		// nameValuePair.add(new BasicNameValuePair("catid", PROVIDER_ID));
		// nameValuePair.add(new BasicNameValuePair("p", Integer
		// .toString(pageCurrent)));
		// nameValuePair.add(new BasicNameValuePair("app", userID));
		// nameValuePair.add(new BasicNameValuePair("refCode", REF_CODE));
		// init httppost
		// String url = HOST + GetPhotoList + Question + "catid" + Equals
		// + PROVIDER_ID + And + "p" + Equals
		// + Integer.toString(pageCurrent) + And + "app" + Equals + userID
		// + And + "refCode" + Equals + REF_CODE;
		String url = HOST + Update + Question + "info" + Equals + Info_Base64;

		httppost = new HttpPost(url);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			data = client.execute(httppost, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		views++;

		// // jsonObject
		// try {
		// JSONObject j = new JSONObject(data);
		// // try {
		// m_Promotion = getDataPromotion(j.getString("promotion"));
		// // m_Advertise = getAdvertise(j.getString("ads"));
		// // } catch (JSONException e2) {
		// //
		// // }
		//
		// // m_Message = getMessage(j.getString("status"));
		// m_Data = getData(j.getString("data"));
		//
		// j = new JSONObject(j.getString("data"));
		// ArrayList<Item> aa = getListItem(j.getString("item"));
		// if (aa != null)
		// m_ListItem = aa;
		// // m_Sms = getSms(j.getString("sms"));
		// } catch (JSONException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
	}

	// get data promotion
	public static Promotion getDataPromotion(String data) {
		Promotion p1 = new Promotion();
		try {
			JSONObject json = new JSONObject(data);
			// p1.setId(json.getString("id"));
			// p1.setTitle(json.getString("title"));

			p1.setSrc(json.getString("src"));
			p1.setLink(json.getString("link"));

			Bitmap b = null;
			try {
				b = DownloadImage.instance.getImage(p1.getSrc());
			} catch (Exception e) {
				b = null;
			}
			p1.setImg(b);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p1;
	}

	// get data "Advertise"
	public static Advertise getAdvertise(String data) {
		Advertise m = new Advertise();

		try {
			JSONObject json = new JSONObject(data);
			m.setMessage(json.getString("message"));
			m.setImg(json.getString("img"));
			m.setTime_out(Integer.parseInt(json.getString("time_out").trim()));
			json = new JSONObject(json.getString("action"));
			m.setType(json.getString("type"));
			m.setUrl(json.getString("url"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return m;
	}

	// get data status
	public static Message getMessage(String data) {
		Message m = new Message();

		try {
			JSONObject json = new JSONObject(data);
			m.setMessage(json.getString("message"));
			m.setCode(json.getString("code"));
			m.setDate(json.getString("date"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return m;
	}

	// get data
	public static Data getData(String data) {
		Data p1 = new Data();
		try {
			JSONObject json = new JSONObject(data);
			p1.setType(json.getString("type"));
			p1.setTotalPage(Integer.parseInt(json.getString("totalPage")));
			json = new JSONObject(json.getString("action"));
			p1.setTypeAction(json.getString("type"));
			p1.setUrl(json.getString("url"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p1;
	}

	// get data sms
	// public static Sms getSms(String data) {
	// Sms m = new Sms();
	//
	// try {
	// JSONObject json = new JSONObject(data);
	// m.setMessage(json.getString("message"));
	// m.setSyntax(json.getString("syntax"));
	// m.setNumber(json.getString("number"));
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return m;
	// }

	// get list Item
	public static ArrayList<Item> getListItem(String data) {
		ArrayList<Item> aa = new ArrayList<Item>();
		if (!data.equals("null")) {
			JSONArray json = null;
			try {
				json = new JSONArray(data);

				for (int i = 0; i < json.length(); i++) {
					Item item = new Item();
					try {
						JSONObject j = json.getJSONObject(i);
						item.setId(j.getString("id"));
						item.setTitle(j.getString("title"));
						item.setDownload(j.getString("download"));
						item.setIntrotext(j.getString("introtext"));
						// item.setFile(j.getString("file_3gp"));
						// item.setFile_3gp(j.getString("file_3gp"));
						item.setFile(j.getString("file_3gp"));
						//item.setFile(j.getString("file_mp4"));
						item.setSrc(j.getString("src"));
						String duration = "";
						try {
							duration = j.getString("duration");
						} catch (Exception ex) {
							duration = "";
						}
						item.setDuration(duration);
						Bitmap b = null;
						try {
							b = DownloadImage.instance.getImage(item.getSrc());
						} catch (Exception e) {
						}
						item.setImg(b);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						// add into list item
						aa.add(item);
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				aa = null;
			}

		}
		return aa;
	}

	// get list Category
	public static ArrayList<Category> getListCategory(String data) {
		ArrayList<Category> aa = new ArrayList<Category>();

		JSONArray json = null;
		try {
			json = new JSONArray(data);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < json.length(); i++) {
			Category item = new Category();
			try {
				JSONObject j = json.getJSONObject(i);
				item.setCatId(Integer.parseInt(j.getString("id").trim()));
				item.setName(j.getString("name"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// add into list item
				aa.add(item);
			}
		}

		return aa;
	}

	// TODO get array category
	public void getCategory() {
		String m_Info = "token" + Equals + TOKEN + And + "type" + Equals + type_new;

		String Info_Base64 = Base64.encode(m_Info.getBytes());

		String data = "";

		// init httpparams
		p = new BasicHttpParams();
		p.setParameter("info", Info_Base64);

		client = new DefaultHttpClient(p);
		// init list nameValuepair
		nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("info", Info_Base64));

		String url = HOST + Category + Question + "info" + Equals + Info_Base64;

		httppost = new HttpPost(url);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			data = client.execute(httppost, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// jsonObject
		try {
			JSONObject j = new JSONObject(data);
			j = new JSONObject(j.getString("data"));
			m_ListCategory = getListCategory(j.getString("item"));

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// search video
	public void searchVideo(String catId, String keyword) {
		String m_Info = "token" + Equals + TOKEN + And + "catId" + Equals + catId + And + "keyword"
				+ Equals + keyword + And + "p" + Equals + pageCurrent;// + And +
																		// "record"
																		// +
																		// Equals
																		// +
																		// RECORD;

		String Info_Base64 = Base64.encode(m_Info.getBytes());

		String data = "";

		// init httpparams
		p = new BasicHttpParams();
		p.setParameter("info", Info_Base64);
		// p.setParameter("catid", PROVIDER_ID);
		// p.setParameter("p", Integer.toString(pageCurrent));
		// p.setParameter("app", userID);
		// p.setParameter("refCode", REF_CODE);
		// init httpclient
		client = new DefaultHttpClient(p);
		// init list nameValuepair
		nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("info", Info_Base64));
		// nameValuePair.add(new BasicNameValuePair("catid", PROVIDER_ID));
		// nameValuePair.add(new BasicNameValuePair("p", Integer
		// .toString(pageCurrent)));
		// nameValuePair.add(new BasicNameValuePair("app", userID));
		// nameValuePair.add(new BasicNameValuePair("refCode", REF_CODE));
		// init httppost
		// String url = HOST + GetPhotoList + Question + "catid" + Equals
		// + PROVIDER_ID + And + "p" + Equals
		// + Integer.toString(pageCurrent) + And + "app" + Equals + userID
		// + And + "refCode" + Equals + REF_CODE;
		String url = HOST + Search + Question + "info" + Equals + Info_Base64;

		httppost = new HttpPost(url);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			data = client.execute(httppost, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// jsonObject
		try {
			JSONObject j = new JSONObject(data);
			// try {
			// m_Promotion = getDataPromotion(j.getString("promotion"));
			// m_Advertise = getAdvertise(j.getString("ads"));
			// } catch (JSONException e2) {
			//
			// }

			// m_Message = getMessage(j.getString("status"));
			// m_Data = getData(j.getString("data"));

			j = new JSONObject(j.getString("data"));
			ArrayList<Item> aa = getListItem(j.getString("item"));
			if (aa != null && aa.size() > 0)
				m_ListItem = aa;
			else {
				flagSearch = false;
			}
			// m_Sms = getSms(j.getString("sms"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// get Other List data
	public void getOtherListVideo(String videoId) {
		String m_Info = "token" + Equals + TOKEN + And + "videoId" + Equals + videoId;

		String Info_Base64 = Base64.encode(m_Info.getBytes());

		String data = "";

		// init httpparams
		p = new BasicHttpParams();
		p.setParameter("info", Info_Base64);
		// init httpclient
		client = new DefaultHttpClient(p);
		// init list nameValuepair
		nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("info", Info_Base64));

		String url = HOST + OtherVideo + Question + "info" + Equals + Info_Base64;

		httppost = new HttpPost(url);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			data = client.execute(httppost, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// jsonObject
		try {
			JSONObject j = new JSONObject(data);
			// m_Data = getData(j.getString("data"));

			j = new JSONObject(j.getString("data"));
			ArrayList<Item> aa = getListItem(j.getString("item"));
			if (aa != null)
				m_OtherListItem = aa;

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// TODO get Active
	public void getActive() {
		String m_Info = "token" + Equals + TOKEN + And + "appId" + Equals + m_AppID;

		String Info_Base64 = Base64.encode(m_Info.getBytes());

		String userID = "";

		// init httpparams
		p = new BasicHttpParams();
		p.setParameter("info", Info_Base64);
		// init httpclient
		client = new DefaultHttpClient(p);
		// init list nameValuepair
		nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("info", Info_Base64));

		String url = HOST + Activation + Question + INFO + Equals + Info_Base64;

		httppost = new HttpPost(url);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			userID = client.execute(httppost, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// slip string
		// String[] s = userID.split("#");
		String res = "";
		try {
			JSONObject json = new JSONObject(userID);
			m_Active.status = json.getString("status");
			m_Active.msg = json.getString("msg");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// TODO get SMS
	public void getSMS() {
		String m_Info = "token" + Equals + "TAoViEC@)!#2013" + And + "appId" + Equals + m_AppID + And
				+ "refCode" + Equals + REF_CODE /*+ And + "type" + Equals + type_new*/;

		String Info_Base64 = Base64.encode(m_Info.getBytes());

		String userID = "";

		// init httpparams
		p = new BasicHttpParams();
		p.setParameter("info", Info_Base64);
		// init httpclient
		client = new DefaultHttpClient(p);
		// init list nameValuepair
		nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("info", Info_Base64));

		String url = HOST + SmsMSexy + Question + INFO + Equals + Info_Base64;

		httppost = new HttpPost(url);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			userID = client.execute(httppost, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// slip string
		// String[] s = userID.split("#");
//		String res = "";
		try {
			JSONObject json = new JSONObject(userID);
			//json = new JSONObject(json.getString("sms"));
			m_Sms.parserSms(json);
			/*
			m_Sms.mo = json.getString("mo");
			m_Sms.message = json.getString("message");
			JSONArray array = new JSONArray(json.getString("serviceCode"));
			for(int i = 0 ; i < array.length();i++){
				m_Sms.m_ListServiceCode.add(array.getString(i));
			}
			m_Sms.index = 0;
			m_Sms.serviceCode = m_Sms.m_ListServiceCode.get(m_Sms.index);
			*/
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
