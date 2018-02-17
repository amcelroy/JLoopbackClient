import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class LoopbackClient {
	
	String m_IP = null;
	int m_Port = 0;
	
	public LoopbackClient(String ip, int port){
		m_IP = ip;
		m_Port = port;
	}

	public String sendRequest(String request) throws UnknownHostException, IOException, InterruptedException{
		Socket s = new Socket(m_IP, m_Port);
		
		s.getOutputStream().write(request.getBytes());
		
		InputStreamReader br = new InputStreamReader(s.getInputStream());
		
		String json = "";
		
		boolean stop = false;
		
		while(stop == false){
			char[] buff = new char[8192];
			int read = br.read(buff, 0, buff.length);
			if(read == -1){
				break;
			}
			String b = new String(buff);
			if(b.contains("***ENDJSON***")){
				int lastOf = b.lastIndexOf("***ENDJSON***");
				b = b.substring(0, lastOf);
				stop = true;
			}
			json += b;
		}
		
		s.close();
		
		return json;
	}
	
	public String formatJSON(String v, String k){
		String json = "";
		
		json = "{ \"" + v + "\" : \"" + k + "\" } \r\n";  
		
		return json;
	}
	
	public void parse(String response) throws ParseException{
		JSONParser p = new JSONParser();
		Object o = p.parse(response);
		
		JSONObject a = (JSONObject)o;
		
		if(a.containsKey("frame")){
			String width = (String) a.get("width");
			String height = (String) a.get("height");
			String data = (String) a.get("data");
		}		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoopbackClient c = new LoopbackClient("127.0.0.1", 32768);
		
		String req = c.formatJSON("frame", "");
		try {
			String response = c.sendRequest(req);
			
			c.parse(response);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
