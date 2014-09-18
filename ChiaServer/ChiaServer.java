import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
import javax.xml.crypto.Data;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
 
import org.jdom2.*;
import org.jdom2.input.DOMBuilder;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.StAXEventBuilder;
import org.jdom2.input.StAXStreamBuilder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class ChiaServer
{
	
	// list of all businesses database
	private static List<Business> businessList;
	
	
	public static void main(String[] args) throws IOException
	{
		// first initialize list
		businessList = new ArrayList<Business>();
		// this method will make database
		buildDataBase ();
		
		//make socket for server with port 8080
		ServerSocket server = new ServerSocket(8080);
		System.out.print("\n\n\nWaiting for clients to connect...\n");

		// this wile loop catches clients and start service for each
		while (true)
		{	
			Socket s;
			s = server.accept();
			System.out.print("A client is approaching...\n");
			
			System.out.print("Starting service...\n");
			ChiaService service = new ChiaService(s, businessList);
			Thread t = new Thread(service); // make thread for each client
			t.start();
		}
	}
	
	/*
	 * this method is pretty much the same as an example you can find at
	 * http://www.journaldev.com/1206/jdom-parser-read-xml-file-to-object-in-java
	 * 
	 * so more information go to this web site
	 */
	public static void buildDataBase (){
		// file name should contains location too
		// but since this xml file is in the same file
		// just its name is fine
		final String fileName = "/Users/Takai/Desktop/addressbook.xml";
		
        org.jdom2.Document jdomDoc;
        
        try {
            //we can create JDOM Document from DOM, SAX and STAX Parser Builder classes
            jdomDoc = useDOMParser(fileName);
            Element root = jdomDoc.getRootElement();
            List<Element> addressBookElement = root.getChildren("restaurant");
            
            for (Element resElement : addressBookElement) {
                Restaurant res = new Restaurant();
                res.setId(Integer.parseInt(resElement.getAttributeValue("id")));
                res.setName(resElement.getChildText("storeName"));
                res.setPhoneNumber(resElement.getChildText("phonenumber"));
                res.setStNumber(resElement.getChildText("stNumber"));
                res.setStName(resElement.getChildText("stName"));
                res.setPostalcode(resElement.getChildText("postalcode"));
                businessList.add(res);
            }
            
            //lets print Business list information for testing
            /*
            for (Business res : businessList)
                System.out.println(res);
            */
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	
	//Get JDOM document from DOM Parser
    private static org.jdom2.Document useDOMParser(String fileName)
            throws ParserConfigurationException, SAXException, IOException {
        //creating DOM Document
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File(fileName));
        DOMBuilder domBuilder = new DOMBuilder();
        return domBuilder.build(doc);
 
    }
}
