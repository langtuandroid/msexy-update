/*
 * #%L
 * SlidingMenuDemo
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

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hdc.taoviec.myvideo.MyHorizontalScrollView;
import com.hdc.taoviec.myvideo.MyHorizontalScrollView.SizeCallback;
import com.hdc.taoviec.myvideo.ViewUtils;
import com.hdc.ultilities.ConnectServer;
import com.hdc.view.MyAdapter;

/**
 * This demo uses a custom HorizontalScrollView that ignores touch events, and therefore does NOT allow manual scrolling.
 * 
 * The only scrolling allowed is scrolling in code triggered by the menu button.
 * 
 * When the button is pressed, both the menu and the app will scroll. So the menu isn't revealed from beneath the app, it
 * adjoins the app and moves with the app.
 */
public class HorzScrollWithListMenu extends Activity {
    MyHorizontalScrollView scrollView;
    View menu;
    View app;
    ImageView btnSlide;
    boolean menuOut = false;
    Handler handler = new Handler();
    int btnWidth;
    int width,height;
    //static int menuWidth;
    GridView gridview;
    LinearLayout layout_search;
    ImageView imgSearch;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
        setContentView(scrollView);

        //getWidth_Heigh();
        
        menu = inflater.inflate(R.layout.horz_scroll_menu, null);
        //app = inflater.inflate(R.layout.horz_scroll_app, null);
        app = inflater.inflate(R.layout.listvideo_1, null);
        //ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.tabBar);                
        
		gridview = (GridView) app.findViewById(R.id.gridView1);
		gridview.setAdapter(new MyAdapter(this,ConnectServer.instance.m_ListItem,R.layout.items_new_1));
		//gridview.setNumColumns(1);
		
		layout_search = (LinearLayout)app.findViewById(R.id.layout_search);
		imgSearch = (ImageView)app.findViewById(R.id.imageView3);
		imgSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				layout_search.setVisibility(View.VISIBLE);
			}
		});
		
		
    	if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			System.out.println("Landscape");
			gridview.setNumColumns(2);

		} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			System.out.println("porttrait");
			gridview.setNumColumns(1);
		}

        

        ListView listView = null;
//        ListView listView = (ListView) app.findViewById(R.id.list);
//        ViewUtils.initListView(this, listView, "Item ", 30, android.R.layout.simple_list_item_1);

        listView = (ListView) menu.findViewById(R.id.list);
        ViewUtils.initListView(this, listView, "Menu ", 30, android.R.layout.simple_list_item_1);

        //btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide);
        btnSlide = (ImageView)app.findViewById(R.id.imageView2);
        //btnWidth = /*btnSlide.getMeasuredWidth()*/50;
        btnSlide.setOnClickListener(new ClickListenerForScrolling(scrollView, menu));

        //menuWidth = width - btnWidth;
        
        final View[] children = new View[] { menu, app };

        // Scroll to app (view[1]) when layout finished.
        int scrollToViewIdx = 1;
        scrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));
    }
        
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//    	// TODO Auto-generated method stub
//    	if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//			System.out.println("Landscape");				    
//			menuWidth = height - btnWidth;
//		} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//			System.out.println("porttrait");
//			menuWidth = width - btnWidth;
//		}
//
//    	
//    	super.onConfigurationChanged(newConfig);
//    }

    /**
     * Helper for examples with a HSV that should be scrolled by a menu View's width.
     */
    static class ClickListenerForScrolling implements OnClickListener {
        HorizontalScrollView scrollView;
        View menu;
        /**
         * Menu must NOT be out/shown to start with.
         */
        boolean menuOut = false;

        public ClickListenerForScrolling(HorizontalScrollView scrollView, View menu) {
            super();
            this.scrollView = scrollView;
            this.menu = menu;
        }

        @Override
        public void onClick(View v) {
            Context context = menu.getContext();
            String msg = "Slide " + new Date();
            Toast.makeText(context, msg, 1000).show();
            System.out.println(msg);

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
     * Helper that remembers the width of the 'slide' button, so that the 'slide' button remains in view, even when the menu is
     * showing.
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
        }

        @Override
        public void getViewSize(int idx, int w, int h, int[] dims) {
            dims[0] = w;
            dims[1] = h;
            final int menuIdx = 0;
            if (idx == menuIdx) {
                dims[0] = w - 2*btnWidth;
            }
        }
    }
}
