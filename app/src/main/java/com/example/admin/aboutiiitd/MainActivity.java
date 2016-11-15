package com.example.admin.aboutiiitd;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button downloadBtn;
    TextView about;
    String title;
    private static final String TAG="Http";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadBtn=(Button)findViewById(R.id.downloadBtn);
        about=(TextView) findViewById(R.id.dataView);

        if(savedInstanceState!=null)
        {
            title=savedInstanceState.getString("Data");
            about.setText(title);
        }

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection(v);
            }
        });
    }

    public void checkConnection(View view){
        String url="https://www.iiitd.ac.in/about";
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            new DownloadData().execute(url);
        }
        else {
            about.setText("No Internet Connection available");
        }
    }

    private class DownloadData extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            org.jsoup.nodes.Document doc;
            try{
                doc= Jsoup.connect(params[0]).get();
                Log.d(TAG,doc.toString());
                Elements element=doc.getElementsByClass("floatright");
                title=doc.title().toString()+"\n\n"+element.toString();
                return title;
            } catch (IOException e) {
                e.printStackTrace();
                return "Invalid URL";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            about.setText(result);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("Data",title);
    }
}
