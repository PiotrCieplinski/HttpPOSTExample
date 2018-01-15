package piotr.cieplinski.polsl.pl.httppostexample;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    //Bazowy adres serwera
    //private static final String BASE_URL = "http://192.168.0.157/webapi/index.php/";
    private static final String BASE_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickSend(View view) {
        String url = getTextFromEditText("editText_url");
        String method = ((Spinner) findViewById(R.id.spinner_method)).getSelectedItem().toString();
        Map<String, String> params = new HashMap<>();
        for(int i = 1; i <= 5; i++) {
            String key = getTextFromEditText("key"+i);
            if(key != "" && key != null) {
                String value = getTextFromEditText("value"+i);
                if(key != "" && key != null) {
                    params.put(key, value);
                }
            }
        }

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(ResultActivity.URL, url);
        intent.putExtra(ResultActivity.METHOD, method);
        intent.putExtra(ResultActivity.PARAMS, (Serializable) params);
        startActivity(intent);
    }

    private String getTextFromEditText(String name) {
        int resID = getResources().getIdentifier(name, "id", getPackageName());
        return ((EditText) findViewById(resID)).getText().toString();
    }
}
