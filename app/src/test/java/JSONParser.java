import android.util.Pair;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class JSONParser {

    static InputStream inputStream = null;
    static JSONObject jsonObject = null;
    static String json = "";
    int response = -1;

    // constructor
    public JSONParser(){
    }

    // function to get JSON from url by making HTTP POST or GET method
    public JSONObject makeHttpRequest(String url, String method, List<Pair<String, String>>params)
        throws IOException{
        URL url = new URL(url);
        URLConnection connection = url.openConnection();
        if (!(connection instanceof HttpURLConnection)) throw new IOException("Not an hTTP Connection");
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            httpURLConnection.setAllowUserInteraction(false);
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(getClass(params));
            writer.flush();
            writer.close();

        }
    }
}
