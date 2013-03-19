package com.hdc.ultilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DownloadImage {
	public static DownloadImage instance = new DownloadImage();	
	public DownloadImage(){		
	}	
    public Bitmap getImage(String URL)
    {        
        Bitmap bitmap = null;
        InputStream in = null;        
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return bitmap;                
    }
	
    private static InputStream OpenHttpConnection(String urlString) 
    throws IOException
    {
        InputStream in = null;
        int response = -1;
               
        URL url = null;
        
        try{
        	url = new URL(urlString);
        }
        catch(MalformedURLException e){
        	e.printStackTrace();
        }
        
        URLConnection conn = url.openConnection();
                 
        if (!(conn instanceof HttpURLConnection))                     
            throw new IOException("Not an HTTP connection");
        
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
//            httpConn.setAllowUserInteraction(false);
//            httpConn.setInstanceFollowRedirects(true);
//            httpConn.setRequestMethod("GET");
            httpConn.setDoInput(true);
            httpConn.connect(); 

            response = httpConn.getResponseCode();                 
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();                                 
            }                     
        }
        catch (Exception ex)
        {
            throw new IOException("Error connecting");            
        }
        return in;     
    }

}
