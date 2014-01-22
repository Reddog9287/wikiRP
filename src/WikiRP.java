import java.util.Iterator;

import org.json.*;

// @NOTE: JSON library used to parse JSON
public class WikiRP {
	public static void main(String[] args) throws JSONException {
		WikiRPMethods researchPaper = new WikiRPMethods();

		String title = "Pythagoras";
		String response = researchPaper.executePost("http://en.wikipedia.org/w/api.php","format=json&action=query&prop=revisions&rvprop=content&titles="+title);
		JSONObject responseObj = new JSONObject(response);

		// Method 1
		String pageid = (String) responseObj.getJSONObject("query").getJSONObject("pages").names().get(0);
//		System.out.println(pageid);


		// Method 2
//		System.out.println("----------");
		Object keys1 = responseObj.getJSONObject("query").getJSONObject("pages").keys().next();
//		System.out.println(keys1);


		// Method 3
		Iterator<?> keys = responseObj.getJSONObject("query").getJSONObject("pages").keys();

		while( keys.hasNext() ){
            String key = (String)keys.next();
//            System.out.println(key);
//            if( responseObj.get(key) instanceof JSONObject ){
//
//            	System.out.println(key);
//            }
        }


//		System.out.println(responseObj.getJSONObject("query").getJSONObject("pages").getJSONObject(pageid).getJSONArray("revisions").getJSONObject(0).get("*"));
		String content = (String) responseObj.getJSONObject("query").getJSONObject("pages").getJSONObject(pageid).getJSONArray("revisions").getJSONObject(0).get("*");
		String[] parts = content.split("<\\/ref>");
		String[] refs = null;

		for (int i=0;i<parts.length;i++) {
			if (parts[i].contains("<ref>")) {
				String[] ref = parts[i].split("<ref>");
				System.out.println(ref[1]+"\r\n");
			} else {
			    
			}
//			System.out.println(parts[i]);
		}
		
//		refs = parts[0].split("<ref>");
		
//		System.out.println(refs[1]);
//		
//		for (int i=0; i<parts.length; i++) {
//			refs = parts[i].split("<ref>");
//		}

//		System.out.println(parts[0]);

//		System.out.println(researchPaper.executePost("http://en.wikipedia.org/w/api.php","format=json&action=query&titles=Main%20Page&prop=revisions&rvprop=content"));
	}

}
