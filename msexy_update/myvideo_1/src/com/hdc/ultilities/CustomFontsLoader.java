package com.hdc.ultilities;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CustomFontsLoader {
	public static final int FONT_NAME_1 =   0;
	public static final int FONT_NAME_2 =   1;
	public static final int FONT_NAME_3 =   2;

	private static final int NUM_OF_CUSTOM_FONTS = 1;

	private static boolean fontsLoaded = false;

	private static Typeface[] fonts = new Typeface[1];

	private static String[] fontPath = {
	    "fonts/Roboto-Light.ttf"
	};


	/**
	 * Returns a loaded custom font based on it's identifier. 
	 * 
	 * @param context - the current context
	 * @param fontIdentifier = the identifier of the requested font
	 * 
	 * @return Typeface object of the requested font.
	 */
	public static Typeface getTypeface(Context context, int fontIdentifier) {
	    if (!fontsLoaded) {
	        loadFonts(context);
	    }
	    return fonts[fontIdentifier];
	}


	private static void loadFonts(Context context) {
	    for (int i = 0; i < NUM_OF_CUSTOM_FONTS; i++) {
	        fonts[i] = Typeface.createFromAsset(context.getAssets(), fontPath[i]);
	    }
	    fontsLoaded = true;
	}
	
	//set font for textview
	public static void setFont(TextView v,int typeFont,Context context){
		v.setTypeface(getTypeface(context, typeFont));
	}
	
	//set underline for text view
	public static void setUnderline(TextView v){
		SpannableString str = new SpannableString(v.getText());
		str.setSpan(new UnderlineSpan(), 0, str.length(), 0);
		v.setText(str);
	}
	
	//set underline for text view
	public static void setNoUnderline(TextView v){
		
	}
	
	
}
