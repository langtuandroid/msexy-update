package com.hdc.ultilities;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.hdc.taoviec.myvideo.MyVideoActivity;

public class Image {

	// resize bitmap
	public static Bitmap BitmapResize(Bitmap bitmap, float newWidth,
			float newHeight) {

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// calculate the scale - in this case = 0.4f
		float scaleWidth = (float) (newWidth / width);
		float scaleHeight = (float) (newHeight / height);

		// createa matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);

		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);

		return resizedBitmap;
	}

	// get bitmap from asset;
	public static Bitmap createImage(String fileName, int flag) {
		InputStream in = null;
		Bitmap bitmap = null;
		try {

			fileName += ".png";
			in = MyVideoActivity.instance.assets.open(fileName);
			bitmap = BitmapFactory.decodeStream(in);
			if (flag == 0) {
				if (MyVideoActivity.instance.width != 240
						&& MyVideoActivity.instance.height != 320) {
					bitmap = BitmapResize(bitmap,
							MyVideoActivity.instance.width,
							MyVideoActivity.instance.height);
				}
			}else{
					bitmap = BitmapResize(bitmap,
							MyVideoActivity.instance.width,
							bitmap.getHeight()*3);
			}
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load bitmap from asset '"
					+ fileName + "'");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return bitmap;
	}

}
