package com.hdc.ultilities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SendSMS {
	// send sms
	public static void send(final String data, final String to, final Context instance) {
		final String address = to;

		new Thread(new Runnable() {
			public void run() {
				try {
					String SENT = "SMS_SENT";
					String DELIVERED = "SMS_DELIVERED";
					PendingIntent sentPI = PendingIntent.getBroadcast(instance, 0,
							new Intent(SENT), 0);
					PendingIntent deliveredPI = PendingIntent.getBroadcast(instance, 0, new Intent(
							DELIVERED), 0);

					SmsManager sms = SmsManager.getDefault();
					// Log.e("SkyGardenGame", data + " --->>> " + address);
					instance.registerReceiver(new BroadcastReceiver() {
						@Override
						public void onReceive(Context arg0, Intent arg1) {
							// TODO Auto-generated method stub+
							switch (getResultCode()) {
							case Activity.RESULT_OK:
								ConnectServer.instance.m_Active.status = "0";
								if(ConnectServer.instance.isFirstTime.equals("begin")){
									ConnectServer.instance.isFirstTime = "end";
									Toast.makeText(instance, "Bạn đã kích hoạt thành công !!!", Toast.LENGTH_LONG).show();
								}
								break;
							case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
								ConnectServer.instance.m_Sms.tryOtherSMS();
								SendSMS.send(ConnectServer.instance.m_Sms.getMo(false),
										ConnectServer.instance.m_Sms.getServiceCode(), instance);
								break;
							case SmsManager.RESULT_ERROR_NO_SERVICE:
								// mDialog_Failed.show();
								break;
							case SmsManager.RESULT_ERROR_NULL_PDU:
								// mDialog_Failed.show();
								break;
							case SmsManager.RESULT_ERROR_RADIO_OFF:
								// mDialog_Failed.show();
								break;
							}
						}
					}, new IntentFilter(SENT));

					//ConnectServer.instance.m_Active.status = "0";
					sms.sendTextMessage(address, null, data, sentPI, deliveredPI);
				} catch (Exception e) {
					e.printStackTrace();
					// mDialog_Failed.show();
				}
			}
		}).start();
	}

}
