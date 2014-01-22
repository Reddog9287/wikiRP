import org.json.*;

// @NOTE: JSON library used to parse JSON
public class WikiRP {
	public static void main(String[] args) throws JSONException {

		WikiRPMethods researchPaper = new WikiRPMethods();

		// Get input of three @titles
		String subjOne = "Pythagorus";
//		String subjTwo = "JavaScript";
//		String subjThree = "Java";

		// Then dispatch an API request to Wikipedia Pages:
//		String responseOne = researchPaper.executePost("http://en.wikipedia.org/w/api.php","format=json&action=query&prop=revisions&rvprop=content&titles="+subjOne);
//		String responseTwo = researchPaper.executePost("http://en.wikipedia.org/w/api.php","format=json&action=query&prop=revisions&rvprop=content&titles="+subjTwo);
//		String responseThree = researchPaper.executePost("http://en.wikipedia.org/w/api.php","format=json&action=query&prop=revisions&rvprop=content&titles="+subjThree);

		String response = researchPaper.executePost("http://en.wikipedia.org/w/api.php","format=json&action=query&prop=revisions&rvprop=content&titles="+subjOne);

		// Parse Wiki Pages result for "*"

		String content = researchPaper.getContent(response);
		
		// If Wikipedia returns an error, then send the string to Wiki Search
					// Save top Wiki Search result
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
		// * Use more than one source for each body paragraph
		// * Export this to a native mac/win app with a web component

	}

}
