import java.awt.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.PrintException;
import javax.swing.*;
import javax.swing.border.Border;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.*;

public class WikiRPMethods extends JFrame implements Printable {

	private static String sentences = "";
	private static final long serialVersionUID = 1L;
	final JTextField txt = new JTextField();
    @SuppressWarnings("unused")
	public void buildUI() throws FontFormatException, IOException
    {
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
        
        txt.setText("Enter a Thesis Statement...");
        JButton submit = new JButton();
        submit.setText("Submit");
        add(txt);
        add(submit);

        final JCheckBox wik = new JCheckBox("Wikipedia");
        final JCheckBox ebs = new JCheckBox("EBSCOHost");
        final JCheckBox nyt = new JCheckBox("NY Times");
        final JCheckBox amz = new JCheckBox("Amzn Books");
        
        submit.addActionListener(
        	new ActionListener(){
            	public void actionPerformed(ActionEvent e){
            		try {

            			if (wik.isSelected())
            				parseInput(txt.getText(), "WIK");

            			if (ebs.isSelected())
            				parseInput(txt.getText(), "EBS");

            			if (nyt.isSelected())
            				parseInput(txt.getText(), "NYT");

            			if (amz.isSelected())
            				parseInput(txt.getText(), "AMZ");
            			
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
            }
        );

        add(wik);
        add(ebs);
        add(nyt);
        add(amz);

        setLayout(new FlowLayout());
        setSize(400,450);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void parseInput(String input, String method) throws Exception
    {
    	// Study how to write a proper thesis statement
    	// Create an intent on wit.ai
    	// Make an API request to wit.ai
    	// Get the response to determine what the important data in the sentence is
    	
    	String response = get("pages", input);
    	String content = getContent(response);
//    	System.out.println(content);
    	// Jim_Rose_(journalist) is a good, short wiki page to test with.
		if (errors(content)) {
			// this should be put into a method
			// response = get("search", input);
			// content = getContent(response);
			System.out.println("redirecting.........");
		} else
			System.out.println("Moving on........");

		String sentences = parse(content);
		
		buildPDF(sentences, input);

		txt.setText("Enter a Thesis Statement...");
    	setVisible(true);
    }
    
    public void buildPDF(String content, String thesis) throws PrintException
    {
    	try {
            BufferedWriter out = new BufferedWriter(new FileWriter("research_paper.txt"));
            out.write("Thesis: "+thesis);
            out.newLine();
            out.write("Content: "+content);
            out.close();

            add(new JLabel("Research paper successfully saved & printed."));

        } catch (IOException e) {
        	add(new JLabel("Error. No such file or directory."));
        	System.out.println(e);
        }
    	
		printPage();

    	setVisible(true);
    }
    
	public void printPage() {
		// TODO Auto-generated method stub
		PrinterJob printerJob = PrinterJob.getPrinterJob();
		WikiRPMethods print = new WikiRPMethods();
		printerJob.setPrintable(print);
        if(printerJob.printDialog()){
            try{
            	System.out.println("testing printing");
            	printerJob.print();
            }catch (PrinterException pex){
            	// oh no! the world is ending!
            	// letÕs stop everything!
            	// System.exit(0);
            }
        }
	}
	
	public int print(Graphics g, PageFormat pf, int index)
    {
        if (index > 0){
        	System.out.println("NOT Printing");
        	return Printable.NO_SUCH_PAGE;
        }
        else {
        	System.out.println("Printing");
        	drawSmiley(g, pf);
        	return Printable.PAGE_EXISTS;
        }
        
    }
	
	private void drawSmiley(Graphics g, PageFormat pf){
        int x =(int)(pf.getImageableX()+pf.getImageableWidth()/2);
        int y = (int)(pf.getImageableY()+pf.getImageableHeight()/2);
        
        FontMetrics fm = g.getFontMetrics();

    	int lineHeight = fm.getHeight();

    	int curX = 50;
    	int curY = 50;

    	String[] words = sentences.split(" ");

    	for (String word : words)
    	{
    		// Find out thw width of the word.
    		int wordWidth = fm.stringWidth(word + " ");

    		// If text exceeds the width, then move to next line.
    		if (curX + wordWidth >= 50 + x)
    		{
    			curY += lineHeight;
    			curX = 50;
    		}

    		g.drawString(word, curX, curY);

    		// Move over to the right for next word.
    		curX += wordWidth;
    	}
    }

	public String get(String method, String input)
	{
		String urlParameters;
	    URL url;
	    HttpURLConnection connection = null;  
	    try {
	    	url = new URL("http://en.wikipedia.org/w/api.php");
	    	if (method == "search") {
	    		urlParameters = "format=json&action=query&list=search&srprop=timestamp&srsearch="+input;
	    	} else if (method == "pages") {
	    		urlParameters = "format=json&action=query&prop=revisions&rvprop=content&titles="+input;
	    	} else
	    		return "It appears that you have entered an invalid input";

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
	
	public String parse(String content)
	{
		String[] parts = content.split("\\r?\\n"); // this can probably be way more specific
//		String sentences = "";
//		System.out.println(parts[0]);
//		System.out.println("==================---------------------===================");
		for (int i=0;i<parts.length;i++)
		{
			// REgex mannnn
//			System.out.println("=======================================================================================");
			if (parts[i].contains("==")){
//				System.out.println(parts[i]);
			}
			else if (parts[i].contains("{{cite")){
				sentences += parts[i];
//				System.out.println(parts[i]);
				// THIS IS OFFENSIVE
			}
			else if (parts[i].contains("{{")){}
			else if (parts[i].contains("}}")){}
			else if (parts[i].contains("|")){
//				System.out.println(parts[i]);
			}
			else if (parts[i].contains("#")){
//				System.out.println(parts[i]);
			}
			else if (parts[i].contains("Category:")){
//				System.out.println(parts[i]);
			}
			else
			{
				sentences += parts[i];
			}
		}
		System.out.print(sentences);
		return sentences;
	}
}