import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;

public class Networker {

    public String server = null;

    public static String send(String server, String pwd, String username, String msg, String recive) throws Exception {

        URL url;
        try {
            url = new URI(server).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
    
            connection.setRequestProperty("Content-Type", "application/json");
    
            String data = "{ \"serverPWD\": \"" + pwd + "\", \"username\": \"" + username + "\", \"msg\": \"" + msg + "\", \"recive\": \"" + recive + "\" }";
    
            connection.setDoOutput(true);
            connection.getOutputStream().write(data.getBytes());
    
            // Get the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
    
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            System.out.println(response);

            if (recive.equals("true")) {
                return response.toString();
            } else if (response.toString().equals("invalid password")) {
                return "no";
            } else {
                return "msg sent";
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return "failed";
        }
        
    }
    public static int alive(String website) throws Exception {
        URI uri = new URI(website);
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            return responseCode;
        } else {
            return responseCode;
        }
    }
}
