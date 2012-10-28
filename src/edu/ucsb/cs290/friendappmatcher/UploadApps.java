package edu.ucsb.cs290.friendappmatcher;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class UploadApps extends Activity {
	private static final String URL_STRING = "https://derp.our.url";
	private ListView l;
	private List<String> appNames;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_apps);
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        appNames = new ArrayList<String>(packages.size());
        for (ApplicationInfo applicationInfo : packages) {
        	if(!applicationInfo.packageName.startsWith("com.android")) {
        		appNames.add(applicationInfo.packageName);
        	}
		}
        l = (ListView) findViewById(R.id.listView1);
        l.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        l.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, appNames));
    }

    public void submit(View v) {
    	SparseBooleanArray s = l.getCheckedItemPositions();
    	StringBuilder sb = new StringBuilder();
    	for(int i = 0;i<s.size();i++) {
    		if(s.get(i)) {
    			sb.append(appNames.get(i)).append("\n");
    		}
    	}
    	try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("apps", sb.toString()));
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL_STRING);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));

            httpclient.execute(httppost);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	Toast.makeText(this, "Posted apps!", Toast.LENGTH_SHORT).show();
    	finish();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_upload_apps, menu);
        return true;
    }
}
