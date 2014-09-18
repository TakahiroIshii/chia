import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.io.DataInputStream;
import java.io.DataOutputStream;


//import json.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class ChiaService  implements Runnable
{
	//instance variables
	private Socket s;
	private DataInputStream fromClient;
	private DataOutputStream toClient;

	// this is the list of all businesses
	private List<Business> businessList;


	/*
	 * constructor
	 */
	public ChiaService(Socket socket, List<Business> bList)
	{	
		s = socket;
		businessList = bList;
	}

	/*
	 * run
	 */
	public void run()
	{
		try
		{	
			try
			{
				// build in-stream and out-stream
				fromClient = new DataInputStream(s.getInputStream());
				toClient = new DataOutputStream(s.getOutputStream());
				// start service
				startService();
			}
			finally
			{
				s.close();	
			}
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
	}
	/*
	 * This function dose all sending and receiving data.
	 * Right now, it has to follow this order of reading
	 * and sending for testing purpose. 
	 */
	public void startService() throws IOException
	{
		// first send message to client to let it know it is connected
		String msg = "You are connected\r\n"; // this is string form, \r and \n are necessary
		byte[] data = msg.getBytes("UTF-8");  // convert string to byte array using UTF8 encoding 
		toClient.write(data,0,data.length);   // write data from 0 to data.length
		toClient.flush();                     // need to flush to send

		// read client name that client is sending
		Scanner sin = new Scanner(fromClient); // make scanner with fromClient (could declare this top)
		String clientName = sin.nextLine();    // read data by up to \n
		System.out.print("Client name :" + clientName + " is connected.\n"); // print

		// send another message
		msg  = "Please send command\r\n";
		data = msg.getBytes("UTF-8");
		toClient.write(data,0,data.length);
		toClient.flush();
		System.out.print("waiting for command...\n");

		// read command from client
		String command = sin.nextLine();
		System.out.print("Command is " + command + "\n");
		int com = Integer.parseInt(command);

		// deal with command
		doCommand(com,sin);
		
		
		// expecting client says "see you"
		sin = new Scanner(fromClient); 
		String clientMsg = sin.nextLine();    
		System.out.print("Client:" + clientName + " says " + clientMsg + "\n"); // print

	}

	/*
	 * this function is for each command
	 * right now there are only two command
	 * command 0 is for direct search in names
	 * command 1 is for searching by category BUT is exactly same as 0 now
	 */

	public void doCommand(int command, Scanner s) throws IOException
	{

		String msg;
		byte[] data;
		String result;
		
		
		switch (command){
		
		// direct search
		case 0:
		{
			// tell client that server is ready to read search words
			msg  = "ready\r\n";
			data = msg.getBytes("UTF-8");
			toClient.write(data,0,data.length);
			toClient.flush();
			System.out.print("sent \"ready\"...\n");


			// get search words
			String words = s.nextLine(); 
			
			// search by words
			result = searchByWords(words);
			result = result + "\r\n";

			// send result
			System.out.print("sending result back...\n");
			data = result.getBytes("UTF-8");
			toClient.write(data,0,data.length);
			toClient.flush();

			break;
		}
		
		// category search
		case 1:
		{
			// tell client that server is ready to read search words
			msg  = "ready\r\n";
			data = msg.getBytes("UTF-8");
			toClient.write(data,0,data.length);
			toClient.flush();
			System.out.print("sent \"ready\"...\n");


			// get search category
			String category = s.nextLine();

			// search by category
			result = searchByCategory(category);
			result = result + "\r\n";

			// send info
			System.out.print("sending result back...\n");
			data = result.getBytes("UTF-8");
			toClient.write(data,0,data.length);
			toClient.flush();

			break;
		}
		}

	}

	
	/*
	 * this function searches businesses that contain w.
	 * use basic algorithm nothing special
	 */
	public String searchByWords(String w)
	{
		System.out.print("start searching...\n");
		// trimming
		w = w.toLowerCase();
		
		// this will be the result to send 
		JSONArray jBusinesses = new JSONArray();
		for (Business bis : businessList)
		{
			// trimming
			String name = bis.getName().toLowerCase(); 
			// 
			if (name.contains(w))
			{
				// if find, make JSON object and add to the list
				JSONObject jBusiness;
				jBusiness = new JSONObject(bis);
				jBusinesses.put(jBusiness);
			}
		}
		// just printing
		System.out.print("result JSON valu is:\n" + jBusinesses.toString() + "\n" + "\n");
		return jBusinesses.toString();
	}

	
	/*
	 * will be similar to searchByWords but not completed
	 * since we do not have category yet
	 */
	public String searchByCategory(String cat)
	{

		return "";

	}

}

