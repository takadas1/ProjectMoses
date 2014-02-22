/*	Portion of project moses involving http get requests.
 *  Issues: Issues with text formatting after obtaining the textss
 *	Programmed by: Andrew Miller
 */

 import java.io.BufferedReader;
 import java.io.DataOutputStream;
 import java.io.InputStreamReader;
 import java.net.HttpURLConnection;
 import java.net.URL;

 public class HTTPGet	
 {
 	public static void main(String[] beans) throws Exception 
 	{
 		String url = "http://api.tumblr.com/v2/blog/thefinenine.tumblr.com/posts/text?api_key=FmuYeCbdQesF76ah7RJDMHcYUvrzKV85gWTV0HwtD7JRChh71F";
 		URL obj = new URL(url);
 		HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
 		
 		connection.setRequestMethod("GET");


 		BufferedReader in = new BufferedReader(
 			new InputStreamReader(connection.getInputStream()));
 		String inputLine;
 		StringBuffer response = new StringBuffer();

 		while((inputLine = in.readLine()) != null) 
 		{
 			response.append(inputLine);
 		}
 		in.close();

 		String result;
 		result = response.substring(response.indexOf("\"body\":"),response.length());
		result = result.substring(0 , result.indexOf("}"));
 		System.out.println(result);
 	}
 }
