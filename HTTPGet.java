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
 		String url = "http://api.tumblr.com/v2/blog/project-moses.tumblr.com/posts/text?api_key=FmuYeCbdQesF76ah7RJDMHcYUvrzKV85gWTV0HwtD7JRChh71F";
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

 		int wordCounter=0;
 		String reader = response.toString();
 		String result, finalResult="";
 		while(wordCounter<300)
 		{
 			if(reader.contains("\"body\":"))
 			{
 				result = reader.substring(reader.indexOf("\"body\":"),reader.length());
 				reader = reader.substring(reader.indexOf("\"body\":")+6,reader.length());
 				result = result.substring(result.indexOf("\\u003E")+6 , result.indexOf("}"));
				result = result.substring(0 , result.indexOf("\\u003C"));
				result = result.replace("&#8217;","'");
 			}
 			else
 			{
 				break;
 			}
			wordCounter += result.length();
			finalResult = finalResult + result + "\n";
 		}
 		System.out.println(finalResult);
 	}
 }
