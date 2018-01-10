package piotr.cieplinski.polsl.pl.httppostexample;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class SendRequest {

    private String baseUrl;

    //w konstruktorcze podajemy adres serwera
    public SendRequest(String url) {
        this.baseUrl = url;
    }

    //parametry: @param1 typ żądania, @param2 parametry żądania klucz=>wartość, @param3 TextView, na którym ma zostać wyświetlona odpowiedź
    public void send(String method, Map<String, String> params, TextView textView) {
        //w konstruktorze podajemy zmapowane parametry, i textView,
        //do metody execute wkładamy adres serwera i nazwę metody żądania
        new AsyncRequest(params, textView).execute(baseUrl, method);
    }

    private class AsyncRequest extends AsyncTask<String, Void, String> {
        private Map<String, String> mappedParams;
        private TextView textView;

        public AsyncRequest(Map<String, String> mappedParams, TextView textView) {
            this.mappedParams = mappedParams;
            this.textView = textView;
        }

        protected String doInBackground(String... params) {
            try {
                //ze zmapowanych parametrów tworzymy obiekt JSON
                JSONObject requestParams = new JSONObject();
                HttpURLConnection conn;
                for (Map.Entry<String, String> entry : mappedParams.entrySet()) {
                    requestParams.put(entry.getKey(), entry.getValue());
                }
                //jeśli metoda żądania = GET to pasujemy JSONa z parametrami do Stringa i wciskamy w adres URL
                if(params[1].equals("GET")) {
                    URL url = new URL(params[0] + "?" + getPostDataString(requestParams));
                    conn = (HttpURLConnection) url.openConnection();
                } else {
                    //dla pozostałych metod
                    URL url = new URL(params[0]);
                    conn = (HttpURLConnection) url.openConnection();
                    //ustawiamy parametry połączania
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod(params[1]);
                    //jeśli wysyłamy dane w ciele żądania to należy ustawić flagę DoOutput na true
                    conn.setDoOutput(true);

                    //wysyłanie danych w ciele żądania
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(requestParams));

                    //zamykanie połączeń
                    writer.flush();
                    writer.close();
                    os.close();
                }

                int responseCode = conn.getResponseCode();

                //sprawdzanie odpowiedzi i jeśli jest ok(200) to przetwarzamy odpowiedź na stringa by wyświetlić ją w textview
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode + " message : " + conn.toString());
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            textView.setText(result);
        }
    }

    private String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }

        return result.toString();
    }
}


