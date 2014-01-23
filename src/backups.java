
public class backups {

	// Method 2
//	System.out.println("----------");
	Object keys1 = responseObj.getJSONObject("query").getJSONObject("pages").keys().next();
//	System.out.println(keys1);


	// Method 3
	Iterator<?> keys = responseObj.getJSONObject("query").getJSONObject("pages").keys();

	while( keys.hasNext() ){
        String key = (String)keys.next();
//        System.out.println(key);
//        if( responseObj.get(key) instanceof JSONObject ){
//
//        	System.out.println(key);
//        }
    }

}
