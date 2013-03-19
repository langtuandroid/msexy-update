package com.hdc.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hdc.msexy.HorzScrollWithListMenu;
import com.hdc.taoviec.myvideo.MyListActivity;
import com.hdc.ultilities.ConnectServer;

//import android.content.Context;
//import android.telephony.TelephonyManager;
import android.util.Log;

public class Sms {
	// public String message;
	// public String syntax;
	// public String number;
	//
	// public String getMessage() {
	// return message;
	// }
	//
	// public void setMessage(String message) {
	// this.message = message;
	// }
	//
	// public String getSyntax() {
	// return syntax;
	// }
	//
	// public void setSyntax(String syntax) {
	// this.syntax = syntax;
	// }
	//
	// public String getNumber() {
	// return number;
	// }
	//
	// public void setNumber(String number) {
	// this.number = number;
	// }

	public static final byte VMS = 0;
	public static final byte VNP = 1;
	public static final byte VTT = 2;
	public static final byte VNM = 3;
	public static final byte OTHER = 4;

	/**
	 * loai thue bao cua dien thoai
	 */
	private int type = OTHER;

	// private static String[] smscMobi = { "+84900000011", "+84900000066",
	// "+84900000015", "+84900000017", "+84900000022", "+84900000023",
	// "+84900000040", "+84900000042" };
	// private static String[] smscVina = { "+8491020408", "+8491020151",
	// "+8491020212", "+8491020152" };
	// private static String[] smscViettel = { "+84980200030", "+84980200033",
	// "+84980200034", "+84980200035", "+84980000036", "+84980000037" };

	public void detectThueBao(String smsCenter) {

		// int i;
		// for (i = 0; i < smscMobi.length; i++) {
		if (smsCenter.toLowerCase().contains("mobi")) {
			type = VMS;
			return;
		}
		// }
		// for (i = 0; i < smscVina.length; i++) {
		if (smsCenter.toLowerCase().contains("vina")) {
			type = VNP;
			return;
		}
		// }
		// for (i = 0; i < smscViettel.length; i++) {
		if (smsCenter.toLowerCase().contains("viettel")) {
			type = VTT;
			return;
		}
		// }

		type = OTHER;
	}

	public String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setN_SMS(String nsms){
		this.nSMS = Integer.parseInt(nsms);
		this.nSMS_1 = this.nSMS;
		this.nSMS_2 = this.nSMS;
	}
	
	public int getN_SMS(){
		return nSMS;
	}
	
	public String getMo(boolean isKichHoat) {
		String m_MO = "";
		if (type == OTHER) {
			if (isKichHoat){
				m_MO = mo + " " + 0;
				//MyListActivity.instance.Toast(m_MO);
				HorzScrollWithListMenu.instance.Toast(m_MO);
				return m_MO;
			}
			else{
				m_MO = mo + " " + ConnectServer.instance.m_Active.msg;
				//MyListActivity.instance.Toast(m_MO);
				HorzScrollWithListMenu.instance.Toast(m_MO);
				return m_MO;
			}
		} else {
			ArrayList<Syntax> vt = vtPrioritySyntax.get(type);
			if (vt.size() == 0) {
				// day la truong hop ko co dau so uu tien
				if (isKichHoat){
					m_MO = mo + " " + 0;
					//MyListActivity.instance.Toast(m_MO);
					HorzScrollWithListMenu.instance.Toast(m_MO);
					return m_MO;
				}
				else{
					m_MO = mo + " " + ConnectServer.instance.m_Active.msg;
					//MyListActivity.instance.Toast(m_MO);
					HorzScrollWithListMenu.instance.Toast(m_MO);
					return m_MO;
				}
			}

			// truong hop co dau so uu tien
			if (index >= vt.size()) {

				index = 0;
			}
			Syntax s = vt.get(index);
			if (isKichHoat){
				m_MO = s.mo + "@" + 0;
				//MyListActivity.instance.Toast(m_MO);
				HorzScrollWithListMenu.instance.Toast(m_MO);
				return m_MO;
			}
			else{
				m_MO = s.mo + "@" + ConnectServer.instance.m_Active.msg;
				//MyListActivity.instance.Toast(m_MO);
				HorzScrollWithListMenu.instance.Toast(m_MO);
				return m_MO;
			}
		}
	}

	public void setMo(String mo) {
		this.mo = mo;
	}

	public String getServiceCode() {
		if (type == OTHER) {
			serviceCode = m_ListServiceCode.get(index);
			//MyListActivity.instance.Toast(serviceCode);
			return serviceCode;
		} else {
			ArrayList<Syntax> vt = vtPrioritySyntax.get(type);
			if (vt.size() == 0) {
				// truong hop ko co dau so uu tien
				serviceCode = m_ListServiceCode.get(index);
				//MyListActivity.instance.Toast(serviceCode);
				return serviceCode;
			}

			// truong hop co dau so uu tien
			if (index >= vt.size()) {
				index = 0;
			}
			Syntax s = vt.get(index);
			//MyListActivity.instance.Toast(s.serviceCode);
			return s.serviceCode;
		}
	}

	/**
	 * service code va mo default
	 */
	public int nSMS;
	private String mo;
	private String serviceCode;
	private ArrayList<String> m_ListServiceCode = new ArrayList<String>();
	private int index = 0;
	public int nSMS_1;
	public int nSMS_2;
	
		
	

	/**
	 * khai bao cac dau so uu tien
	 */
	private ArrayList<ArrayList<Syntax>> vtPrioritySyntax = new ArrayList<ArrayList<Syntax>>();

	private ArrayList<Syntax> parsePrioritySyntax(JSONArray jsonArr) throws JSONException {
		ArrayList<Syntax> vt = new ArrayList<Syntax>();

		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject obj = jsonArr.getJSONObject(i);
			if (obj.has("serviceCode") && obj.has("mo")) {
				Syntax s = new Syntax();

				s.mo = obj.getString("mo");
				s.serviceCode = obj.getString("serviceCode");

				vt.add(s);
			}
		}

		return vt;
	}

	/**
	 * parse sms
	 */
	public void parserSms(JSONObject root) {
		try {
			if (root.getInt("status") == 1) {
				JSONObject jsonSms = root.getJSONObject("sms");
				
				if (jsonSms.has("nSms")) {
					setN_SMS(jsonSms.getString("nSms").trim());
				}				
				if (jsonSms.has("message")) {
					setMessage(jsonSms.getString("message").trim());
				}
				if (jsonSms.has("mo")) {
					setMo(jsonSms.getString("mo").trim());
				}
				if (jsonSms.has("serviceCode")) {
					JSONObject serviceCodeObj = jsonSms.getJSONObject("serviceCode");

					// doc cac dau so mac dinh
					JSONArray arrSmsDefault = serviceCodeObj.getJSONArray("sc");
					m_ListServiceCode.clear();
					for (int i = 0; i < arrSmsDefault.length(); i++) {
						m_ListServiceCode.add(arrSmsDefault.getString(i));
					}

					// doc cac dau so uu tien
					if (serviceCodeObj.has("vms")) {
						JSONArray vmsObj = serviceCodeObj.getJSONArray("vms");
						vtPrioritySyntax.add(VMS, parsePrioritySyntax(vmsObj));
					}

					if (serviceCodeObj.has("vnp")) {
						JSONArray vnpObj = serviceCodeObj.getJSONArray("vnp");
						vtPrioritySyntax.add(VNP, parsePrioritySyntax(vnpObj));
					}

					if (serviceCodeObj.has("vtt")) {
						JSONArray vttObj = serviceCodeObj.getJSONArray("vtt");
						vtPrioritySyntax.add(VTT, parsePrioritySyntax(vttObj));
					}

					if (serviceCodeObj.has("vnm")) {
						JSONArray vnmObj = serviceCodeObj.getJSONArray("vnm");
						vtPrioritySyntax.add(VNM, parsePrioritySyntax(vnmObj));
					}

					Log.e("eeeeeee", vtPrioritySyntax.size() + "");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	class Syntax {
		public String serviceCode;
		public String mo;
	}

	/**
	 * lay tong so luong service code
	 */
	public int getLengthServiceCode() {
		return m_ListServiceCode.size();
	}

	public void tryOtherSMS() {
		index++;
		// if (index < m_ListServiceCode.size()) {
		// serviceCode = m_ListServiceCode.get(index);
		// }
	}
}
