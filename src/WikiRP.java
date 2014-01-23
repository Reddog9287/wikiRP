import java.awt.FontFormatException;
import java.io.IOException;

import org.json.*;

public class WikiRP {
	public static void main(String[] args) throws JSONException, FontFormatException, IOException {

		WikiRPMethods researchPaper = new WikiRPMethods();
		
		researchPaper.buildUI();

		// Get input of three @titles
		String subjOne = "Pythagorus";

		// Then dispatch an API request to Wikipedia Pages:
		// @NOTE: This should be further functionalized into a .pagesReq method
		// and a .searchReq method. Or maybe just one method, with the title,
		// and then the method (ie. search or pages) passed in. Yes I think that
		// is the best way to do that.
		String response = researchPaper.get("http://en.wikipedia.org/w/api.php","format=json&action=query&prop=revisions&rvprop=content&titles="+subjOne);

		// Parse Wiki Pages result for "*"

		String content = researchPaper.getContent(response);

		// If Wikipedia returns an error
			// Send the string to Wiki SearchSave top Wiki Search result
			// Redo API request to Wiki Pages

		if (researchPaper.errors(content))
			System.out.println("redirecting.........");
		else
			System.out.println("Moving on........");

		// Split result for refs or maybe even just plain 'ol sentences

		researchPaper.parse(content);

		// Construct three body paragraphs
		
		// Construct a citation page
		
		// -------------------------------FUTURE----IDEAS----------------------------------- //
		// * If just three things are entered, try to guess a proper thesis statement?
		// * Randomize what sentences to use in the Wikipedia result
		// * Use Natural Language Generation to construct sentences
		// * https://code.google.com/p/simplenlg/wiki/Section3
		// * Use more than one source for each body paragraph
		// * Export this to a native mac/win app with a web component

	}

}