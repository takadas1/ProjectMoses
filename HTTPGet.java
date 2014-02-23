/* Portion of Project Moses involving http get requests.
 * Issues: Need to create switch for tags.
 * Programmed by: Andrew Preuss && Andrew Miller
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPGet{

	// Pulls down the twenty most recent text posts from a particular blog,
	// Using wordCounter variable can return less than twenty posts.
	// @return result a single massive string containing the text from all posts.
	// @param urlName the name of the blog to be pulled down. 
	public static String getPostsByBlog(String urlName)throws Exception{
		// Uses the urlName to get the tumblr blog from which posts will be extracted from. 
		String url = "http://api.tumblr.com/v2/blog/"+urlName+"/posts/text?api_key=FmuYeCbdQesF76ah7RJDMHcYUvrzKV85gWTV0HwtD7JRChh71F";
		URL obj = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
		connection.setRequestMethod("GET");

		//Reads in the JSON objects as a single unformatted string.
		BufferedReader in = new BufferedReader(
			new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while((inputLine = in.readLine()) != null){
			response.append(inputLine);
		}
		in.close();	//Closes the connection.

		// wordCounter is used to limit how many posts/words we want to pull down.
		// It counts each character in the posts including spaces and punctuation.
		int wordCounter=0;
		String reader = response.toString();
		String result, finalResult="";
		// This while loop formats the JSON string into a regular plaintext string.
		while(wordCounter<300){
			if(reader.contains("\"body\":")){
				result = reader.substring(reader.indexOf("\"body\":"),reader.length());
				reader = reader.substring(reader.indexOf("\"body\":")+6,reader.length());
				result = result.substring(result.indexOf("\\u003E")+6 , result.indexOf("}"));
				result = result.substring(0 , result.indexOf("\\u003C"));
				result = result.replace("&#8217;","'");
		}
		else{
			break;
		}

		wordCounter += result.length();
		finalResult = finalResult + result + "\n";
		}
	return finalResult;
	}

	// This method pulls down posts by their tags as opposed to all the posts from one blog
	// @param tag is the tag of all the posts that will be pulled.
	// @return result a single massive string containing the text from all posts.
	public static String getPostsByTag(String tag)throws Exception{
		// Uses the urlName to get the tumblr blog from which posts will be extracted from. 
		String url = "http://api.tumblr.com/v2/tagged?tag="+tag;
		URL obj = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
		connection.setRequestMethod("GET");

		//Reads in the JSON objects as a single unformatted string.
		BufferedReader in = new BufferedReader(
			new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while((inputLine = in.readLine()) != null){
			response.append(inputLine);
		}
		in.close();	//Closes the connection.

		// wordCounter is used to limit how many posts/words we want to pull down.
		// It counts each character in the posts including spaces and punctuation.
		int wordCounter=0;
		String reader = response.toString();
		String result, finalResult="";
		// This while loop formats the JSON string into a regular plaintext string.
		while(wordCounter<300){
			if(reader.contains("\"body\":")){
				result = reader.substring(reader.indexOf("\"body\":"),reader.length());
				reader = reader.substring(reader.indexOf("\"body\":")+6,reader.length());
				result = result.substring(result.indexOf("\\u003E")+6 , result.indexOf("}"));
				result = result.substring(0 , result.indexOf("\\u003C"));
				result = result.replace("&#8217;","'");
		}
		else{
			break;
		}

		wordCounter += result.length();
		finalResult = finalResult + result + "\n";
		}
	return finalResult;
	}	

}
