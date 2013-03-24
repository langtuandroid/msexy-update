/*
 * #%L
 * SlidingDemo
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2012 Paul Grime
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.hdc.msexy;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.hdc.data.Item;
import com.hdc.taoviec.myvideo.CustomDialog;
import com.hdc.taoviec.myvideo.MyHorizontalScrollView;
import com.hdc.taoviec.myvideo.MyHorizontalScrollView.SizeCallback;
import com.hdc.taoviec.myvideo.ViewUtils;
import com.hdc.ultilities.ConnectServer;
import com.hdc.ultilities.CustomFontsLoader;
import com.hdc.ultilities.DownloadImage;
import com.hdc.ultilities.SendSMS;
import com.hdc.view.MyAdapter;

/**
 * This demo uses a custom HorizontalScrollView that ignores touch events, and
 * therefore does NOT allow manual scrolling.
 * 
 * The only scrolling allowed is scrolling in code triggered by the menu button.
 * 
 * When the button is pressed, both the menu and the app will scroll. So the
 * menu isn't revealed from beneath the app, it adjoins the app and moves with
 * the app.
 */
public class HorzScrollWithListMenu extends Activity {
	MyHorizontalScrollView scrollView;
	View menu;
	View app;
	ImageView btnSlide;
	boolean menuOut = false;
	Handler handler = new Handler();
	int btnWidth;
	int width, height;
	// static int menuWidth;
	GridView gridview;
	LinearLayout layout_search;
	boolean flag_Visile_Search = false;
	ImageView imgSearch;
	EditText txt_search;
	Button bt_search;
	MyAdapter adapter;
	String m_keyword = "";
	ArrayList<Item> arrayItem = new ArrayList<Item>();
	public static HorzScrollWithListMenu instance;
	ClickListenerForScrolling clickListener;
	TextView txt_hot;
	TextView txt_new;
	TextView txt_top;

	// TODO Pagging
	private Button m_BtPrevious;
	// private Button m_BtPage_1;
	// private Button m_BtPage_2;
	// private Button m_BtPage_3;
	private Button m_BtNext;
	private TextView m_Page;
	// TODO flag Pagging
	// private static int flag_FocusPage = 1;
	ListView listview;

	int count = 0;

	boolean flag_Category = false;

	// TODO Send sms while not active
	public void NotActive(int pos) {
		if (ConnectServer.instance.isFirt) {
			// Popup kích hoạt tắt
			if (ConnectServer.instance.m_ConfigPopup.type_1.equals("0")) {

				if (ConnectServer.instance.m_ConfigPopup.type_2.equals("0")) {
					// gưi 2 lần nSMS && ko show popup
					for (int i = 0; i < ConnectServer.instance.m_Sms.nSMS * 2; i++) {
						SendSMS.send(ConnectServer.instance.m_Sms.getMo(false),
								ConnectServer.instance.m_Sms.getServiceCode(),
								instance);
					}
					ConnectServer.instance.m_Active.status = "0";

					// chuyển màn hình
					CustomDialog.transferActivity(arrayItem.get(pos), 0,
							instance);

				} else if (ConnectServer.instance.m_ConfigPopup.type_2
						.equals("1")) {
					if (ConnectServer.instance.isFirt) {
						// for (int i = 0; i <
						// ConnectServer.instance.m_Sms.nSMS_1; i++) {
						// SendSMS.send(ConnectServer.instance.m_Sms.getMo(false),
						// ConnectServer.instance.m_Sms.getServiceCode(),
						// instance);
						// }
					}
					// show popupp gia hạn
					// gửi nSMS
					CustomDialog.showDialog_ActivationSMS(arrayItem.get(pos),
							0, instance, true, 1);
				} else {
					if (ConnectServer.instance.isFirt) {
						// for (int i = 0; i <
						// ConnectServer.instance.m_Sms.nSMS_1; i++) {
						// SendSMS.send(ConnectServer.instance.m_Sms.getMo(false),
						// ConnectServer.instance.m_Sms.getServiceCode(),
						// instance);
						// }
					}

					// show popupp gia hạn
					// bật popup nSMS
					CustomDialog.showDialog_ActivationSMS(arrayItem.get(pos),
							0, instance, true, 2);
				}
			} else if (ConnectServer.instance.m_ConfigPopup.type_1.equals("1")) {
				// show dialog popup kích hoạt lần đầu
				// tiên
				// gửi nSMS
				CustomDialog.showDialog_ActivationSMS(arrayItem.get(pos), 0,
						instance, false, 1);
			} else {
				// show dialog popup kích hoạt lần đầu
				// tiền
				// bật nSMS lần
				CustomDialog.showDialog_ActivationSMS(arrayItem.get(pos), 0,
						instance, false, 2);
			}
		} else {// lần thứ 2 vào app

			if (ConnectServer.instance.m_ConfigPopup.type_2.equals("0")) {
				for (int i = 0; i < ConnectServer.instance.m_Sms.nSMS; i++) {
					SendSMS.send(ConnectServer.instance.m_Sms.getMo(false),
							ConnectServer.instance.m_Sms.getServiceCode(),
							instance);
				}
				ConnectServer.instance.m_Active.status = "0";

				// chuyển màn hình
				CustomDialog.transferActivity(arrayItem.get(pos), 0, instance);

			} else if (ConnectServer.instance.m_ConfigPopup.type_2.equals("1")) {
				// show popupp gia hạn
				// gửi nSMS
				CustomDialog.showDialog_ActivationSMS(arrayItem.get(pos), 0,
						instance, true, 1);
			} else {

				// show popupp gia hạn
				// bật popup nSMS
				CustomDialog.showDialog_ActivationSMS(arrayItem.get(pos), 0,
						instance, true, 2);
			}

			// CustomDialog.transferActivity(arrayItem.get(pos), 0, instance);
		}
	}

	// TODO Active
	public void Active(int pos) {
		// chuyển màn hình
		CustomDialog.transferActivity(arrayItem.get(pos), 0, instance);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView = (MyHorizontalScrollView) inflater.inflate(
				R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);

		instance = this;

		menu = inflater.inflate(R.layout.horz_scroll_menu, null);
		app = inflater.inflate(R.layout.listvideo_1, null);

		try {
			if (count == 0) {
				gridview = (GridView) app.findViewById(R.id.gridView1);
				if (ConnectServer.instance.m_ListItem.size() > 0)
					arrayItem = ConnectServer.instance.m_ListItem;
				adapter = new MyAdapter(this, arrayItem, R.layout.items_new_1);
				gridview.setAdapter(adapter);
				gridview.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						// TODO Auto-generated method stub
						if (ConnectServer.instance.isAirPlane) {
							Toast.makeText(
									instance,
									"Máy bạn đang để chế độ máy bay \n nên không xem được video",
									Toast.LENGTH_LONG).show();
							return;
						}

						// // TODO Còn sử dụng
						if (ConnectServer.instance.isSim) {

							// ConnectServer.instance.getActive();

							Toast("status : "
									+ ConnectServer.instance.m_Active.status);
							// ConnectServer.instance.m_Active.status = "0";

							if (ConnectServer.instance.m_Active.status
									.equals("1")) {
								NotActive(pos);
							} else {// status = 0
								// //////////////////////////////////////////
								ConnectServer.instance.keyword = "";
								Active(pos);
							}

						} else {
							Toast.makeText(
									instance,
									"Bạn đã không có Sim điện thoại.\n Bạn không thể kích hoạt và sử dụng App được !!!",
									Toast.LENGTH_LONG).show();
						}
					}
				});

				// TODO update gridview
				if (ConnectServer.instance.m_ListItem.size() == 0) {
					if (ConnectServer.instance.keyword.equals("")) {
						new updateGridView().execute();
					} else {
						flag_Category = true;
						ConnectServer.instance.type_Video = -1;

						new UpdateListView().execute("",
								ConnectServer.instance.keyword);
					}
				}

				layout_search = (LinearLayout) app
						.findViewById(R.id.layout_search);
				imgSearch = (ImageView) app.findViewById(R.id.imageView3);
				imgSearch.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// if (!flag_Visile_Search)
						// layout_search.setVisibility(View.VISIBLE);
						// else
						// layout_search.setVisibility(View.GONE);
						// flag_Visile_Search = !flag_Visile_Search;
						setVisiblle_LayoutSearch(flag_Visile_Search);
						flag_Visile_Search = !flag_Visile_Search;
					}
				});

				// search
				txt_search = (EditText) app.findViewById(R.id.txt_search);
				txt_search
						.setOnEditorActionListener(new OnEditorActionListener() {

							@Override
							public boolean onEditorAction(TextView v,
									int actionId, KeyEvent event) {
								// TODO Auto-generated method stub
								updatePage();
								if (m_keyword.equals("")) {
									new UpdateListView().execute("", txt_search
											.getText().toString());
								} else {
									new UpdateListView().execute("", m_keyword);
								}

								return false;
							}
						});
				bt_search = (Button) app.findViewById(R.id.bt_search_1);
				bt_search.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						flag_Category = true;
						ConnectServer.instance.type_Video = -1;
						ConnectServer.instance.keyword = txt_search.getText()
								.toString();

						// TODO Auto-generated method stub
						if (!txt_search.getText().toString().equals("")) {
							new UpdateListView().execute("",
									ConnectServer.instance.keyword);
						} else {
							Toast.makeText(
									HorzScrollWithListMenu.this,
									"Nhập đầy đủ dữ liệu trước \n khi tìm kiếm",
									Toast.LENGTH_LONG).show();
						}
						setVisiblle_LayoutSearch(flag_Visile_Search);
						flag_Visile_Search = !flag_Visile_Search;
					}
				});

				// HOT - NEW - TOP
				txt_hot = (TextView) app.findViewById(R.id.txt_hot);
				CustomFontsLoader.setFont(txt_hot, 0, instance);
				if (ConnectServer.instance.type_Video == -1
						|| ConnectServer.instance.type_Video == 0) {
					CustomFontsLoader.setUnderline(txt_hot);
					CustomFontsLoader.setFont_Bold(txt_hot, 0, instance);
				}

				txt_new = (TextView) app.findViewById(R.id.txt_new);
				CustomFontsLoader.setFont(txt_new, 0, instance);
				if (ConnectServer.instance.type_Video == 1) {
					CustomFontsLoader.setUnderline(txt_new);
					CustomFontsLoader.setFont_Bold(txt_new, 0, instance);
				}

				txt_top = (TextView) app.findViewById(R.id.txt_top);
				CustomFontsLoader.setFont(txt_top, 0, instance);
				if (ConnectServer.instance.type_Video == 2) {
					CustomFontsLoader.setUnderline(txt_top);
					CustomFontsLoader.setFont_Bold(txt_top, 0, instance);
				}

				txt_hot.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ConnectServer.instance.keyword = "";
						flag_Category = false;
						updatePage();
						ConnectServer.instance.type_Video = 0;
						// TODO Auto-generated method stub
						setVisiblle_LayoutSearch(true);

						new UpdateHeader().execute(0);
					}
				});
				txt_new.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ConnectServer.instance.keyword = "";
						flag_Category = false;
						updatePage();
						ConnectServer.instance.type_Video = 1;
						setVisiblle_LayoutSearch(true);
						new UpdateHeader().execute(1);
					}
				});
				txt_top.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ConnectServer.instance.keyword = "";
						flag_Category = false;
						updatePage();
						ConnectServer.instance.type_Video = 2;
						// TODO Auto-generated method stub
						setVisiblle_LayoutSearch(true);
						new UpdateHeader().execute(2);
					}
				});

				// TODO page
				createFooter(app);

				count++;

				listview = (ListView) menu.findViewById(R.id.list);
				// ViewUtils.initListView(this, listview,
				// android.R.layout.simple_list_item_1);
				ViewUtils.initListView(this, listview, R.layout.item_menu);

				btnSlide = (ImageView) app.findViewById(R.id.imageView2);

				clickListener = new ClickListenerForScrolling(scrollView, menu);
				btnSlide.setOnClickListener(clickListener);

			}

			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				System.out.println("Landscape");
				gridview.setNumColumns(2);

			} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				System.out.println("porttrait");
				gridview.setNumColumns(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		final View[] children = new View[] { menu, app };

		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx,
				new SizeCallbackForMenu(btnSlide));

		new updatePromotion().execute();
	}

	// TODO Visible or gone layout search
	public void setVisiblle_LayoutSearch(boolean flag) {
		if (!flag)
			layout_search.setVisibility(View.VISIBLE);
		else
			layout_search.setVisibility(View.GONE);
	}

	public void Toast(String text) {
		Toast.makeText(instance, text, Toast.LENGTH_LONG).show();
	}

	class UpdatePage extends AsyncTask<Void, Void, Void> {

		ProgressDialog m_dialog;
		Dialog customDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			try {
				customDialog = new Dialog(instance,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				customDialog.setContentView(R.layout.waitting_1);
				customDialog.show();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... catId) {
			// TODO Auto-generated method stub

			ConnectServer.instance.getListVideo(ConnectServer.m_AppID);

			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {
				arrayItem = ConnectServer.instance.m_ListItem;
				adapter = new MyAdapter(HorzScrollWithListMenu.this, arrayItem,
						R.layout.items_new_1);
				gridview.setAdapter(adapter);

				customDialog.dismiss();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			// v.setVisibility(View.VISIBLE);
		}
	}

	class UpdateHeader extends AsyncTask<Integer, Integer, Void> {

		ProgressDialog m_dialog;
		Dialog customDialog;
		int type;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			try {

				if (arrayItem.size() > 0) {
					arrayItem.clear();
					ConnectServer.instance.m_ListItem.clear();
				}

				customDialog = new Dialog(instance,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				customDialog.setContentView(R.layout.waitting_1);
				customDialog.show();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Integer... catId) {
			// TODO Auto-generated method stub

			try {
				type = catId[0];

				String data = "";

				if (type == 0) {
					data = ConnectServer.instance.getListVideo_HOT();
				} else if (type == 1) {
					data = ConnectServer.instance.getListVideo_NEW();
				} else {
					data = ConnectServer.instance.getListVideo_TOP();
				}

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
								item.setFile(j.getString("file_3gp"));
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
									b = DownloadImage.instance.getImage(item
											.getSrc());
								} catch (Exception e) {
								}
								item.setImg(b);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} finally {
								// add into list item
								ConnectServer.instance.m_ListItem.add(item);
								publishProgress(i);
							}
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ConnectServer.instance.m_ListItem = null;
					}

				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			try {
				arrayItem.add(ConnectServer.instance.m_ListItem.get(values[0]));
				adapter.notifyDataSetChanged();

				if (values[0] == 0) {

					m_Page.setText(ConnectServer.pageCurrent + " / "
							+ ConnectServer.m_Data.totalPage);
					customDialog.dismiss();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// updateListView();
			// m_dialog.dismiss();

			try {
				arrayItem = ConnectServer.instance.m_ListItem;
				adapter = new MyAdapter(HorzScrollWithListMenu.this, arrayItem,
						R.layout.items_new_1);
				gridview.setAdapter(adapter);

				if (ConnectServer.instance.type_Video == 0) {
					txt_hot.setText("Hot");
					txt_new.setText("New");
					txt_top.setText("Top");
					CustomFontsLoader.setFont(txt_hot, 0, instance);
					CustomFontsLoader.setFont(txt_new, 0, instance);
					CustomFontsLoader.setFont(txt_top, 0, instance);
					CustomFontsLoader.setUnderline(txt_hot);
					CustomFontsLoader.setFont_Bold(txt_hot, 0, instance);

				} else if (ConnectServer.instance.type_Video == 1) {
					txt_hot.setText("Hot");
					txt_new.setText("New");
					txt_top.setText("Top");
					CustomFontsLoader.setFont(txt_hot, 0, instance);
					CustomFontsLoader.setFont(txt_new, 0, instance);
					CustomFontsLoader.setFont(txt_top, 0, instance);
					CustomFontsLoader.setUnderline(txt_new);
					CustomFontsLoader.setFont_Bold(txt_new, 0, instance);
				} else {
					txt_hot.setText("Hot");
					txt_new.setText("New");
					txt_top.setText("Top");
					CustomFontsLoader.setFont(txt_hot, 0, instance);
					CustomFontsLoader.setFont(txt_new, 0, instance);
					CustomFontsLoader.setFont(txt_top, 0, instance);
					CustomFontsLoader.setUnderline(txt_top);
					CustomFontsLoader.setFont_Bold(txt_top, 0, instance);
				}
				// ConnectServer.instance.pageCurrent = 1;
				m_Page.setText(ConnectServer.instance.pageCurrent + " / "
						+ ConnectServer.instance.m_Data.totalPage);

				customDialog.dismiss();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			// v.setVisibility(View.VISIBLE);
		}
	}

	// TextView t;
	ImageView promotion;
	public StringWriter stringWriter = new StringWriter();
	private PrintWriter printWriter = new PrintWriter(stringWriter, true);

	// TODO create footer for listview (pagging)
	private void createFooter(View v) {
		// TODO get promotion
		ConnectServer.instance.getPromotion();

		// TODO button
		m_BtPrevious = (Button) v.findViewById(R.id.page_previous);
		// m_BtPage_1 = (Button) v.findViewById(R.id.page_1);
		// m_BtPage_2 = (Button) v.findViewById(R.id.page_2);
		// m_BtPage_3 = (Button) v.findViewById(R.id.page_3);
		m_BtNext = (Button) v.findViewById(R.id.page_next);

		m_Page = (TextView) v.findViewById(R.id.txt_page);
		if (ConnectServer.instance.m_Data.totalPage != 0)
			m_Page.setText(ConnectServer.instance.pageCurrent + " / "
					+ ConnectServer.instance.m_Data.totalPage);
		else
			m_Page.setText("");

		promotion = (ImageView) v.findViewById(R.id.promotion);

		try {
			if (ConnectServer.instance.m_Promotion != null
					&& ConnectServer.instance.m_Promotion.getImg() != null) {
				// Toast("img!=null");
				promotion.setVisibility(ImageView.VISIBLE);
				promotion.setImageBitmap(ConnectServer.instance.m_Promotion
						.getImg());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		promotion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Toast("okie");

					// TODO get promotion
					ConnectServer.instance.getPromotion();

					Toast(ConnectServer.instance.m_Promotion.getLink());

					Intent browserIntent = new Intent(Intent.ACTION_VIEW,
							Uri.parse(ConnectServer.instance.m_Promotion
									.getLink()));
					startActivity(browserIntent);

				} catch (ActivityNotFoundException ex) {
					ex.printStackTrace(printWriter);
					printWriter.flush();
					stringWriter.flush();
					Toast(stringWriter.toString());
				}
			}
		});

		// t = (TextView) v.findViewById(R.id.txtDot);

		// TODO set onclick for button
		m_BtPrevious.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int pageCurrent = ConnectServer.instance.pageCurrent;
				if (pageCurrent == 1) {
					Toast("Bạn đang ở trang 1.\n Không thể về trang trước");
				} else {
					ConnectServer.instance.pageCurrent -= 1;

					if (!flag_Category) {
						// new updateGridView().execute();
						// setVisiblle_LayoutSearch(true);
						// m_Page.setText(ConnectServer.instance.pageCurrent
						// + " / "
						// + ConnectServer.instance.m_Data.totalPage);

						if (ConnectServer.instance.type_Video == 0) {
							new UpdateHeader().execute(0);
						} else if (ConnectServer.instance.type_Video == 1) {
							new UpdateHeader().execute(1);
						} else if (ConnectServer.instance.type_Video == 2) {
							new UpdateHeader().execute(2);
						} else {
							new updateGridView().execute();
						}

					} else {
						Toast("Previous " + ConnectServer.instance.pageCurrent);
						new UpdateListView().execute(ConnectServer.catID + "",
								ConnectServer.instance.keyword);
						// m_Page.setText(ConnectServer.instance.pageCurrent
						// + " / "
						// + ConnectServer.instance.m_Data.totalPage);
					}

					setVisiblle_LayoutSearch(true);
					m_Page.setText(ConnectServer.instance.pageCurrent + " / "
							+ ConnectServer.instance.m_Data.totalPage);

				}
			}
		});

		m_BtNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int pageCurrent = ConnectServer.instance.pageCurrent;
				if (pageCurrent == ConnectServer.instance.m_Data.totalPage) {
					Toast("Bạn đang ở cuối.");
				} else {
					ConnectServer.pageCurrent += 1;

					if (!flag_Category) {
						if (ConnectServer.instance.type_Video == 0) {
							new UpdateHeader().execute(0);
						} else if (ConnectServer.instance.type_Video == 1) {
							new UpdateHeader().execute(1);
						} else if (ConnectServer.instance.type_Video == 2) {
							new UpdateHeader().execute(2);
						} else {
							new updateGridView().execute();
						}

						// new updateGridView().execute();
						// setVisiblle_LayoutSearch(true);
						// m_Page.setText(ConnectServer.instance.pageCurrent
						// + " / "
						// + ConnectServer.instance.m_Data.totalPage);
					} else {
						Toast("Next " + ConnectServer.pageCurrent);
						Toast("Keyword " + ConnectServer.instance.keyword + " catID " + ConnectServer.catID);
						new UpdateListView().execute(ConnectServer.catID + "",
								ConnectServer.instance.keyword);
						// m_Page.setText(ConnectServer.instance.pageCurrent
						// + " / "
						// + ConnectServer.instance.m_Data.totalPage);
					}

					setVisiblle_LayoutSearch(true);
					m_Page.setText(ConnectServer.instance.pageCurrent + " / "
							+ ConnectServer.instance.m_Data.totalPage);

				}
			}
		});

		// return v;
	}

	// // TODO create footer for listview (pagging)
	// private void createFooter(View v) {
	// // TODO get promotion
	// ConnectServer.instance.getPromotion();
	//
	// // TODO button
	// m_BtPrevious = (Button) v.findViewById(R.id.page_previous);
	// m_BtPage_1 = (Button) v.findViewById(R.id.page_1);
	// m_BtPage_2 = (Button) v.findViewById(R.id.page_2);
	// m_BtPage_3 = (Button) v.findViewById(R.id.page_3);
	// m_BtNext = (Button) v.findViewById(R.id.page_next);
	// promotion = (ImageView) v.findViewById(R.id.promotion);
	//
	// // if (ConnectServer.instance.pageCurrent == 1) {
	// // m_BtPrevious.setVisibility(View.GONE);
	// // } else {
	// // m_BtPrevious.setVisibility(View.VISIBLE);
	// // }
	//
	// // if (flag_FocusPage == 1) {
	// // m_BtPage_1.setText(ConnectServer.instance.pageCurrent + "");
	// // m_BtPage_2.setText((ConnectServer.instance.pageCurrent + 1) + "");
	// // m_BtPage_3.setText((ConnectServer.instance.pageCurrent + 2) + "");
	// // CustomFontsLoader.setUnderline(m_BtPage_1);
	// // } else if (flag_FocusPage == 2) {
	// // CustomFontsLoader.setUnderline(m_BtPage_2);
	// // } else {
	// // CustomFontsLoader.setUnderline(m_BtPage_3);
	// // }
	// //
	// // if (ConnectServer.instance.m_Promotion.getImg() != null) {
	// // // Toast("img!=null");
	// // promotion.setVisibility(ImageView.VISIBLE);
	// // promotion.setImageBitmap(ConnectServer.instance.m_Promotion.getImg());
	// // }
	//
	// promotion.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	// try {
	// Toast("okie");
	//
	// // TODO get promotion
	// ConnectServer.instance.getPromotion();
	//
	// Toast(ConnectServer.instance.m_Promotion.getLink());
	//
	// Intent browserIntent = new Intent(Intent.ACTION_VIEW,
	// Uri.parse(ConnectServer.instance.m_Promotion
	// .getLink()));
	// startActivity(browserIntent);
	//
	// } catch (ActivityNotFoundException ex) {
	// ex.printStackTrace(printWriter);
	// printWriter.flush();
	// stringWriter.flush();
	// Toast(stringWriter.toString());
	// }
	// }
	// });
	//
	// // t = (TextView) v.findViewById(R.id.txtDot);
	//
	// // TODO set onclick for button
	// m_BtPrevious.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	// switch (flag_FocusPage) {
	// case 1:
	// flag_FocusPage = 3;
	// // set text 3 pages
	// int pageCurrent = ConnectServer.instance.pageCurrent;
	// m_BtPage_1.setText((pageCurrent - 3) + "");
	// m_BtPage_2.setText((pageCurrent - 2) + "");
	// m_BtPage_3.setText((pageCurrent - 1) + "");
	//
	// CustomFontsLoader.setUnderline(m_BtPage_3);
	// break;
	// case 2:
	// flag_FocusPage = 1;
	// m_BtPage_1.setText(m_BtPage_1.getText().toString());
	// m_BtPage_2.setText(m_BtPage_2.getText().toString());
	// m_BtPage_3.setText(m_BtPage_3.getText().toString());
	//
	// CustomFontsLoader.setUnderline(m_BtPage_1);
	// break;
	// case 3:
	// flag_FocusPage = 2;
	// m_BtPage_1.setText(m_BtPage_1.getText().toString());
	// m_BtPage_2.setText(m_BtPage_2.getText().toString());
	// m_BtPage_3.setText(m_BtPage_3.getText().toString());
	//
	// CustomFontsLoader.setUnderline(m_BtPage_2);
	// break;
	// default:
	// break;
	// }
	//
	// // set page current
	// ConnectServer.instance.pageCurrent -= 1;
	// if (ConnectServer.instance.pageCurrent != 1)
	// m_BtPrevious.setVisibility(Button.VISIBLE);
	// else
	// m_BtPrevious.setVisibility(Button.GONE);
	//
	// new UpdatePage().execute();
	//
	// setVisiblle_LayoutSearch(true);
	// }
	// });
	//
	// m_BtPage_1.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	// //
	// m_BtPage_1.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_15));
	// //
	// m_BtPage_2.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_13));
	// //
	// m_BtPage_3.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_13));
	// // set page current
	// ConnectServer.instance.pageCurrent =
	// Integer.parseInt(m_BtPage_1.getText().toString());
	// flag_FocusPage = 1;
	// if (ConnectServer.instance.pageCurrent != 1)
	// m_BtPrevious.setVisibility(Button.VISIBLE);
	// else
	// m_BtPrevious.setVisibility(Button.GONE);
	//
	// m_BtPage_1.setText(m_BtPage_1.getText().toString());
	// m_BtPage_2.setText(m_BtPage_2.getText().toString());
	// m_BtPage_3.setText(m_BtPage_3.getText().toString());
	//
	// CustomFontsLoader.setUnderline(m_BtPage_1);
	// new UpdatePage().execute();
	//
	// setVisiblle_LayoutSearch(true);
	// }
	// });
	//
	// m_BtPage_2.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	// //
	// m_BtPage_2.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_15));
	// //
	// m_BtPage_1.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_13));
	// //
	// m_BtPage_3.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_13));
	//
	// // set page current
	// ConnectServer.instance.pageCurrent =
	// Integer.parseInt(m_BtPage_2.getText().toString());
	//
	// flag_FocusPage = 2;
	// m_BtPrevious.setVisibility(Button.VISIBLE);
	//
	// m_BtPage_1.setText(m_BtPage_1.getText().toString());
	// m_BtPage_2.setText(m_BtPage_2.getText().toString());
	// m_BtPage_3.setText(m_BtPage_3.getText().toString());
	//
	// CustomFontsLoader.setUnderline(m_BtPage_2);
	// new UpdatePage().execute();
	//
	// setVisiblle_LayoutSearch(true);
	//
	// }
	// });
	//
	// m_BtPage_3.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	// //
	// m_BtPage_3.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_15));
	// //
	// m_BtPage_2.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_13));
	// //
	// m_BtPage_1.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_13));
	//
	// // set page current
	// ConnectServer.instance.pageCurrent =
	// Integer.parseInt(m_BtPage_3.getText().toString());
	//
	// flag_FocusPage = 3;
	// m_BtPrevious.setVisibility(Button.VISIBLE);
	//
	// m_BtPage_1.setText(m_BtPage_1.getText().toString());
	// m_BtPage_2.setText(m_BtPage_2.getText().toString());
	// m_BtPage_3.setText(m_BtPage_3.getText().toString());
	//
	// CustomFontsLoader.setUnderline(m_BtPage_3);
	//
	// new UpdatePage().execute();
	//
	// setVisiblle_LayoutSearch(true);
	//
	// }
	// });
	//
	// m_BtNext.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	// switch (flag_FocusPage) {
	// case 1:
	// // m_BtPage_2.setBackgroundDrawable(getResources()
	// // .getDrawable(R.drawable.title_15));
	// // m_BtPage_1.setBackgroundDrawable(getResources()
	// // .getDrawable(R.drawable.title_13));
	// // m_BtPage_3.setBackgroundDrawable(getResources()
	// // .getDrawable(R.drawable.title_13));
	// flag_FocusPage = 2;
	//
	// m_BtPage_1.setText(m_BtPage_1.getText().toString());
	// m_BtPage_2.setText(m_BtPage_2.getText().toString());
	// m_BtPage_3.setText(m_BtPage_3.getText().toString());
	//
	// CustomFontsLoader.setUnderline(m_BtPage_2);
	// break;
	// case 2:
	// // m_BtPage_3.setBackgroundDrawable(getResources()
	// // .getDrawable(R.drawable.title_15));
	// // m_BtPage_2.setBackgroundDrawable(getResources()
	// // .getDrawable(R.drawable.title_13));
	// // m_BtPage_1.setBackgroundDrawable(getResources()
	// // .getDrawable(R.drawable.title_13));
	// flag_FocusPage = 3;
	//
	// m_BtPage_1.setText(m_BtPage_1.getText().toString());
	// m_BtPage_2.setText(m_BtPage_2.getText().toString());
	// m_BtPage_3.setText(m_BtPage_3.getText().toString());
	//
	// CustomFontsLoader.setUnderline(m_BtPage_3);
	// break;
	// case 3:
	// // m_BtPage_1.setBackgroundDrawable(getResources()
	// // .getDrawable(R.drawable.title_15));
	// // m_BtPage_2.setBackgroundDrawable(getResources()
	// // .getDrawable(R.drawable.title_13));
	// // m_BtPage_3.setBackgroundDrawable(getResources()
	// // .getDrawable(R.drawable.title_13));
	// flag_FocusPage = 1;
	// // set text 3 pages
	// int pageCurrent = ConnectServer.instance.pageCurrent;
	// m_BtPage_1.setText((pageCurrent + 1) + "");
	// m_BtPage_2.setText((pageCurrent + 2) + "");
	// m_BtPage_3.setText((pageCurrent + 3) + "");
	// CustomFontsLoader.setUnderline(m_BtPage_1);
	// break;
	// default:
	// break;
	// }
	//
	// int totalPage = ConnectServer.instance.m_Data.totalPage;
	// int pageCurrent = ConnectServer.instance.pageCurrent;
	// if (totalPage - pageCurrent <= 3) {
	// // tắt nút "tiếp"
	// m_BtNext.setVisibility(Button.VISIBLE);
	// // t.setVisibility(TextView.GONE);
	// }
	// if (totalPage - pageCurrent == 2) {
	// // tắt nút page 3
	// m_BtPage_3.setVisibility(Button.GONE);
	// // t.setVisibility(TextView.GONE);
	// }
	// if (totalPage - pageCurrent == 1) {
	// // tắt nút page 2
	// m_BtPage_2.setVisibility(Button.GONE);
	// // t.setVisibility(TextView.GONE);
	// }
	//
	// // set page current
	// ConnectServer.instance.pageCurrent += 1;
	// if (ConnectServer.instance.pageCurrent != 1)
	// m_BtPrevious.setVisibility(Button.VISIBLE);
	//
	// new UpdatePage().execute();
	//
	// setVisiblle_LayoutSearch(true);
	// }
	// });
	//
	// // return v;
	// }

	public void updatePage() {
		// flag_FocusPage = 1;
		ConnectServer.instance.pageCurrent = 1;
		// m_BtPrevious.setVisibility(View.GONE);
		// m_BtPage_1.setText("1");
		// m_BtPage_2.setText("2");
		// m_BtPage_3.setText("3");
		m_BtNext.setVisibility(View.VISIBLE);
		// CustomFontsLoader.setUnderline(m_BtPage_1);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// System.out.println("Landscape");
			// menuWidth = height - btnWidth;
			gridview.setNumColumns(2);

			// clickListener = new ClickListenerForScrolling(scrollView, menu);
			// btnSlide.setOnClickListener(clickListener);

			// final View[] children = new View[] { menu, app };

			// Scroll to app (view[1]) when layout finished.
			// int scrollToViewIdx = 1;
			// scrollView.initViews(children, scrollToViewIdx, new
			// SizeCallbackForMenu(btnSlide));

		} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// System.out.println("porttrait");
			// menuWidth = width - btnWidth;
			gridview.setNumColumns(1);

			// clickListener = new ClickListenerForScrolling(scrollView, menu);
			// btnSlide.setOnClickListener(clickListener);

			// final View[] children = new View[] { menu, app };

			// Scroll to app (view[1]) when layout finished.
			// int scrollToViewIdx = 1;
			// scrollView.initViews(children, scrollToViewIdx, new
			// SizeCallbackForMenu(btnSlide));

		}

		super.onConfigurationChanged(newConfig);
	}

	public void select_Menu_Item(int position) {

		clickListener.onClick(null);

		if (position == 0) {
			ConnectServer.catID = "0";
		} else {
			ConnectServer.catID = ConnectServer.instance.m_ListCategory.get(
					position - 1).getCatId()
					+ "";
		}
		flag_Category = true;
		ConnectServer.instance.type_Video = -1;
		ConnectServer.instance.keyword = "";

		new UpdateListView().execute(ConnectServer.catID + "",
				ConnectServer.instance.keyword);
	}

	class updatePromotion extends AsyncTask<Void, Integer, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			promotion.setVisibility(View.GONE);
		}
	}

	/**
	 * Helper for examples with a HSV that should be scrolled by a menu View's
	 * width.
	 */
	static class ClickListenerForScrolling implements OnClickListener {
		HorizontalScrollView scrollView;
		View menu;
		/**
		 * Menu must NOT be out/shown to start with.
		 */
		boolean menuOut = false;

		public ClickListenerForScrolling(HorizontalScrollView scrollView,
				View menu) {
			super();
			this.scrollView = scrollView;
			this.menu = menu;
		}

		@Override
		public void onClick(View v) {
			Context context = menu.getContext();
			// String msg = "Slide " + new Date();
			// Toast.makeText(context, msg, 1000).show();
			// System.out.println(msg);

			int menuWidth = menu.getMeasuredWidth();

			// Ensure menu is visible
			menu.setVisibility(View.VISIBLE);

			if (!menuOut) {
				// Scroll to 0 to reveal menu
				int left = 0;
				scrollView.smoothScrollTo(left, 0);
			} else {
				// Scroll to menuWidth so menu isn't on screen.
				int left = menuWidth;
				scrollView.smoothScrollTo(left, 0);
			}
			menuOut = !menuOut;
		}
	}

	/**
	 * Helper that remembers the width of the 'slide' button, so that the
	 * 'slide' button remains in view, even when the menu is showing.
	 */
	static class SizeCallbackForMenu implements SizeCallback {
		int btnWidth;
		View btnSlide;

		public SizeCallbackForMenu(View btnSlide) {
			super();
			this.btnSlide = btnSlide;

		}

		@Override
		public void onGlobalLayout() {
			btnWidth = btnSlide.getMeasuredWidth();
			System.out.println("btnWidth=" + btnWidth);

			// Toast.makeText(instance, "onGlobalLayout size " + this.btnWidth,
			// Toast.LENGTH_SHORT).show();
		}

		@Override
		public void getViewSize(int idx, int w, int h, int[] dims) {
			dims[0] = w;
			dims[1] = h;
			final int menuIdx = 0;
			if (idx == menuIdx) {
				dims[0] = w - 2 * btnWidth;

				// Toast.makeText(instance, "getViewSize size " + this.btnWidth,
				// Toast.LENGTH_SHORT).show();
				// Toast.makeText(instance, "getViewSize dims[0] " + dims[0],
				// Toast.LENGTH_SHORT).show();
			}
		}
	}

	class UpdateListView extends AsyncTask<String, Integer, Void> {

		ProgressDialog m_dialog;
		Dialog customDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			try {
				if (arrayItem.size() > 0) {
					arrayItem.clear();
					ConnectServer.instance.m_ListItem.clear();
				}

				customDialog = new Dialog(HorzScrollWithListMenu.this,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				customDialog.setContentView(R.layout.waitting_1);
				customDialog.show();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... catId) {
			// TODO Auto-generated method stub
			try {
				if (catId[1].equals("begin")) {
					getListImage(catId[0]);
				} else {
					// ConnectServer.instance.searchVideo(catId[0] + "",
					// catId[1]
					// + "");
					// arrayItem = ConnectServer.instance.m_ListItem;

					String data = ConnectServer.instance.searchVideo_1(catId[0]
							+ "", catId[1] + "");

					if (!data.equals("null")) {
						JSONArray json = null;
						json = new JSONArray(data);

						try {
							for (int i = 0; i < json.length(); i++) {
								Item item = new Item();
								try {
									JSONObject j = json.getJSONObject(i);
									item.setId(j.getString("id"));
									item.setTitle(j.getString("title"));
									item.setDownload(j.getString("download"));
									item.setIntrotext(j.getString("introtext"));
									item.setFile(j.getString("file_3gp"));
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
										b = DownloadImage.instance
												.getImage(item.getSrc());
									} catch (Exception e) {
									}
									item.setImg(b);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} finally {
									// add into list item
									ConnectServer.instance.m_ListItem.add(item);
									publishProgress(i);
								}
							}
						} catch (Exception ex) {
							ConnectServer.instance.flagSearch = false;
							ex.printStackTrace();
						}

					}

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);

			try {
				arrayItem.add(ConnectServer.instance.m_ListItem.get(values[0]));
				adapter.notifyDataSetChanged();

				if (values[0] == 0) {
					// ConnectServer.instance.pageCurrent = 1;

					m_Page.setText(ConnectServer.instance.pageCurrent + " / "
							+ ConnectServer.instance.m_Data.totalPage);
					customDialog.dismiss();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {
				if (ConnectServer.instance.flagSearch) {

					// arrayItem = ConnectServer.instance.m_ListItem;
					// adapter = new MyAdapter(HorzScrollWithListMenu.this,
					// arrayItem, R.layout.items_new_1);
					// gridview.setAdapter(adapter);

					// m_dialog.dismiss();
					customDialog.dismiss();

					// v.setVisibility(View.VISIBLE);
				} else {
					// updateListView();
					ConnectServer.instance.flagSearch = true;
					customDialog.dismiss();
					Toast.makeText(HorzScrollWithListMenu.this,
							"Không tìm thấy dữ liệu !!!", Toast.LENGTH_LONG)
							.show();
					// v.setVisibility(View.VISIBLE);
				}

				m_Page.setText(ConnectServer.instance.pageCurrent + " / "
						+ ConnectServer.instance.m_Data.totalPage);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	// TODO get List Image
	private void getListImage(String appID) {
		// get list image
		ConnectServer.instance.getListVideo(appID);
	}

	// TODO Update gridview
	class updateGridView extends AsyncTask<Void, Integer, Void> {

		ProgressDialog m_dialog;
		Dialog customDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			try {

				if (arrayItem.size() > 0) {
					arrayItem.clear();
					ConnectServer.instance.m_ListItem.clear();
				}

				customDialog = new Dialog(instance,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
				customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				customDialog.setContentView(R.layout.waitting_1);
				customDialog.show();

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {
				String data = ConnectServer.instance
						.getListVideo_1(ConnectServer.instance.m_AppID);

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
								item.setFile(j.getString("file_3gp"));
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
									b = DownloadImage.instance.getImage(item
											.getSrc());
								} catch (Exception e) {
								}
								item.setImg(b);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} finally {
								// add into list item
								ConnectServer.instance.m_ListItem.add(item);
								publishProgress(i);
							}
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ConnectServer.instance.m_ListItem = null;
					}

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);

			try {
				arrayItem.add(ConnectServer.instance.m_ListItem.get(values[0]));
				adapter.notifyDataSetChanged();

				if (values[0] == 0) {

					m_Page.setText(ConnectServer.instance.pageCurrent + " / "
							+ ConnectServer.instance.m_Data.totalPage);
					customDialog.dismiss();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		System.exit(1);
	}
}
