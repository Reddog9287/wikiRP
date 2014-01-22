import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.*;

public class WikiRPMethods {
	public String executePost(String targetURL, String urlParameters)
	  {
	    URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      url = new URL(targetURL);
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("GET");
	      connection.setRequestProperty("Content-Type", 
	           "application/x-www-form-urlencoded");
				
	      connection.setRequestProperty("Content-Length", "" + 
	               Integer.toString(urlParameters.getBytes().length));
	      connection.setRequestProperty("Content-Language", "en-US");  
				
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      //Send request
	      DataOutputStream wr = new DataOutputStream (
	                  connection.getOutputStream ());
	      wr.writeBytes (urlParameters);
	      wr.flush ();
	      wr.close ();

	      //Get Response
	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      return response.toString();

	    } catch (Exception e) {

	      e.printStackTrace();
	      return null;

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	  }
	
	public Boolean errors(String content) {
		if (content.contains("#Redirect") || content.contains("#REDIRECT")) {
			String redirect = content.split("\\]\\]|\\[\\[")[1];
			System.out.println(redirect);
			return true;
		}
		else 
			return false;
	}
	
	public String getContent(String response) throws JSONException
	{
		JSONObject responseObj = new JSONObject(response);
		String pageid = (String) responseObj.getJSONObject("query").getJSONObject("pages").names().get(0);

		String content = (String) responseObj.getJSONObject("query").getJSONObject("pages").getJSONObject(pageid).getJSONArray("revisions").getJSONObject(0).get("*");

		return content;
	}
	
	public void parse(String content)
	{
		String[] parts = content.split("<\\/ref>");

		for (int i=0;i<parts.length;i++)
		{
			if (parts[i].contains("<ref>"))
			{
				String[] ref = parts[i].split("<ref>");
				System.out.println(ref[1]+"\r\n");
			}
			else
				System.out.println("response contains no <ref>s");
		}
	}
}
