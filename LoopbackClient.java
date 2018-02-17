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

	public String sendRequest(String request, String termSequence) throws UnknownHostException, IOException, InterruptedException{
		Socket s = new Socket(m_IP, m_Port);
		
		s.getOutputStream().write(request.getBytes());
		
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		
		String json = "";
		
		boolean stop = false;
		
		while(stop == false){
			char[] buff = new char[32768];
			int read = br.read(buff, 0, buff.length);
			if(read == -1){
				break;
			}
			String b = new String(buff);
			if(b.contains(termSequence)){
				int lastOf = b.lastIndexOf(termSequence);
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
	
	public void parse(String response) {
		throw new UnsupportedOperationException("Function not implimented");
//		if(response.substring(0, 10).contains("frame:")){
//			response = response.substring(6);
//		}else{
//			JSONParser p = new JSONParser();
//			Object o = p.parse(response);
//			
//			JSONObject a = (JSONObject)o;
//
//		}
//		
	
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoopbackClient c = new LoopbackClient("127.0.0.1", 32768);
		
		String req = c.formatJSON("frame", "");
		try {
			String response = c.sendRequest(req, "</transmission>");
			
			c.parse(response);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
