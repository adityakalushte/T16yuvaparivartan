package com.team16.yuvaparivartan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class SendActivity extends Activity {
	
	SQLiteDatabase sdl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		HttpParams httpParameters = new BasicHttpParams();

		//Setup timeouts
    	 HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
    	 HttpConnectionParams.setSoTimeout(httpParameters, 15000);			

    	 HttpClient httpclient = new DefaultHttpClient(httpParameters);

    	 HttpPost httppost = new HttpPost("http://adityaserver.comoj.com/yuva.php");
    	 //HttpPost httppost = new HttpPost("http://192.168.43.210/andy1.php");	//IPv4 Address for localhost

		
		sdl=openOrCreateDatabase("datatesting", MODE_PRIVATE, null);
		//String a[]={"1","2","3","4","5","6","7","8"};
		
		//sdl.execSQL("create table yuva( _id INTEGER, Name TEXT)");
		//sdl.execSQL("insert into yuva values(1,'101')");
		//sdl.execSQL("insert into yuva values(2,'102')");
		//sdl.execSQL("insert into yuva values(3,'103')");
		
		Cursor c=sdl.query("yuva", null, null, null, null, null, null);
		
		/*SimpleCursorAdapter sca=new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,
				c, new String[]{"Sapid","present"}, new int[]{android.R.id.text1,android.R.id.text2},0);
		//ArrayAdapter<String> adapt=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,a);
		final GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(sca);
	    */
		
		int id=-1;
		String name="";
		c.moveToNext();
		while(!c.isAfterLast())
		{
			id=c.getInt(0);
			name=c.getString(1);			
			Log.i("", id + name);
		
			try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("id", id+""));
		        nameValuePairs.add(new BasicNameValuePair("name", name+""));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	
		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        InputStream is = entity.getContent();
	            
	            String result="";
	          //convert response to string
	            try{
	                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	                StringBuilder sb = new StringBuilder();
	                sb.append(reader.readLine() + "\n");
	                String line="0";
	              
	                while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	                }
	                 
	                is.close();
	                result=sb.toString();
	                Log.i("JSON",result);
	                 
	            }catch(Exception e){
	                Log.e("log_tag", "Error converting result "+e.toString());
	            }
		        
		        Toast.makeText(this, "Posted sucessfully", Toast.LENGTH_SHORT).show();
		        
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    }
			
			c.moveToNext();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.send, menu);
		return true;
	}

}
