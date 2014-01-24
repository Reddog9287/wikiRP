import java.awt.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.swing.*;
import javax.swing.border.Border;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.*;

import com.pdfjet.*;
import com.pdfjet.Point;

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

        submit.addActionListener(
        	new ActionListener(){
            	public void actionPerformed(ActionEvent e){
            		try {
						parseInput(txt.getText());
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

        setLayout(new FlowLayout());
        setSize(400,450);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void parseInput(String input) throws Exception
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

//            Print test = new Print(thesis);
            // ????????? WHAT THE HELL

//            PrinterJob job = PrinterJob.getPrinterJob();
////            job.setPrintable();
//
//            boolean doPrint = job.printDialog();
//            if (doPrint) {
//                try {
//                    job.print();
//                } catch (PrinterException e) {
//                	System.out.println(e);
//                    // The job did not successfully
//                    // complete
//                }
//            }
        } catch (IOException e) {
        	add(new JLabel("Error. No such file or directory."));
        	System.out.println(e);
        }
    	
		printPage();

    	setVisible(true);
//    	
//    	
//    	 FileOutputStream fos = new FileOutputStream("research_paper.pdf");
//    	 PDF pdf = new PDF(fos);
//
//    	 pdf.setTitle("Research Paper");
//         pdf.setAuthor("WikiRP Tool");
//
//         Page page = new Page(pdf, Letter.PORTRAIT);
//         TextColumn column = new TextColumn();
//         column.setLineBetweenParagraphs(true);
//         column.setLineSpacing(1.0);
//         
//         Paragraph p1 = new Paragraph();
//         p1.setAlignment(Align.LEFT);
//         p1.add(new TextLine(null, thesis));
//         
//         Paragraph p2 = new Paragraph();
//         p2.add(new TextLine(null, content));
//         
//         column.addParagraph(p1);
//         column.addParagraph(p2);
//         column.setPosition(90, 300);
//         column.setSize(470, 100);
//         column.drawOn(page);
//         pdf.flush();
//         fos.close();
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
//            	System.exit(0);
            }
        }
		
//		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
//	    pras.add(new Copies(1));
//	    PrintService pss[] = PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.GIF, pras);
//	    if (pss.length == 0)
//	      throw new RuntimeException("No printer services available.");
//	    PrintService ps = pss[0];
//	    System.out.println("Printing to " + ps);
//	    DocPrintJob job = ps.createPrintJob();
//	    FileInputStream fin = new FileInputStream("research_paper.txt");
//	    Doc doc = new SimpleDoc(fin, DocFlavor.INPUT_STREAM.GIF, null);
//	    job.print(doc, pras);
//	    fin.close();
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
//        g.drawOval(x-50,y-50,100,100);
//        g.drawString(sentences, 50, 50);
//        g.fillOval(x-25,y-20,10,10);
//        g.fillOval(x+20,y-20,10,10);
//        g.drawArc(x-27,y+10,55,30,180,180);
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
//In_Rainbows_%E2%80%93_From_the_Basement
				sentences += parts[i];
			}
//			System.out.println(parts[i]);
		}
		System.out.print(sentences);
		return sentences;
//		System.out.println(parts[0]);

//		for (int i=0;i<parts.length;i++)
//		{
//			if (parts[i].contains("<ref>"))
//			{
//				String[] ref = parts[i].split("<ref>");
////				System.out.println(ref[1]+"\r\n");
//			}
//			else 
//				System.out.println("response contains no <ref>s");
//		}
	}
}
