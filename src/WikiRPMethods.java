import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.*;

public class WikiRPMethods extends JFrame {

	private static final long serialVersionUID = 1L;

    public void buildUI() throws FontFormatException, IOException
    {
    	@SuppressWarnings("unused")
		Border empty;
    	Border textBorder = BorderFactory.createEmptyBorder(25,50,25,50);
    	empty = BorderFactory.createEmptyBorder();
    	JLabel title = new JLabel("WikiRP Tool");
    	Font amaranth = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Amaranth/Amaranth-Regular.ttf"));
    	Font imprima = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Imprima/Imprima-Regular.ttf"));
    	Font ribeye_marrow = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Ribeye_Marrow/RibeyeMarrow-Regular.ttf"));
    	title.setFont(new Font("amaranth", Font.PLAIN, 50));
    	title.setBorder(textBorder);
    	add(title);
    	Border imageBorder = BorderFactory.createEmptyBorder(0,50,50,50);
    	empty = BorderFactory.createEmptyBorder();
    	JLabel logo = new JLabel(new ImageIcon("assets/images/logo.png"));
    	logo.setBorder(imageBorder);
    	add(logo);
        final JTextField txt = new JTextField();
        txt.setText("Enter a Thesis Statement...");
        JButton submit = new JButton();
        submit.setText("Submit");
        add(txt);
        add(submit);

        submit.addActionListener(
        	new ActionListener(){
            	public void actionPerformed(ActionEvent e){
            		parseInput(txt.getText());
                }
            }
        );

        setLayout(new FlowLayout());
        setSize(400,450);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void parseInput(String input)
    {
    	JPanel output = new JPanel();
    	JScrollPane scrPane = new JScrollPane(output);
    	add(scrPane);
    	
    	setSize(400,800);
    	output.add(new JLabel(input));
    	setVisible(true);
    	// Study how to write a proper thesis statement
    	// Create an intent on wit.ai
    	// Make an API request to wit.ai
    	// Get the response to determine what the important data in the sentence is
    	
    	System.out.println(input);
    }
    
	public String get(String targetURL, String urlParameters)
	{
	    URL url;
	    HttpURLConnection connection = null;  
	    try {
	        // Create connection
	        url = new URL(targetURL);
	        connection = (HttpURLConnection)url.openConnection();
	        connection.setRequestMethod("GET");
	        connection.setRequestProperty("Content-Type",
	           "application/x-www-form-urlencoded");
				
	        connection.setUseCaches (false);
	        connection.setDoInput(true);
	        connection.setDoOutput(true);

	        //Send request
	        DataOutputStream wr = new DataOutputStream (
	                  connection.getOutputStream ());
	        wr.writeBytes (urlParameters);
	        wr.flush ();
	        wr.close ();

	        // Get Response
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
