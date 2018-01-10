package piotr.cieplinski.polsl.pl.httppostexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    public final static String URL = "EXTRA_URL";
    public final static String METHOD = "EXTRA_METHOD";
    public final static String PARAMS = "EXTRA_PARAMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        SendRequest sendRequest = new SendRequest(intent.getStringExtra(URL));
        sendRequest.send(intent.getStringExtra(METHOD), (Map<String, String>)intent.getSerializableExtra(PARAMS), (TextView) findViewById(R.id.textView_result));
    }
}
