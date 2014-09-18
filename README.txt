To start server on your terminal:

1. Use "cd" to move to the folder where you have this file
2. Compile all .java files by typing "javac -cp json.jar *.java" on your terminal
3. Start ChiaServer by typing "./LaunchServer.sh"
*If server gets error that says “Address already in use” change port to something else.


For client side:
1. Simply go to the folder and double click ChiaClient_Aug4.xcodeproj
2. Run it on Xcode




Note:
Purpose of LaunchServer.sh is to make it easy and have specific amount of memory for the program.
If you don't use LaunchServer.sh, simply type “java -cp .:json.jar ChiaServer” after you compiled it.

Update August 3:
I added a new file call addressbook.xml and a couple new classes. I also added buildDataBase() function in ChiaServer. Basically, before the server starts, it reads data from the xml file and make a list of objects (businesses).
Also ChiaServer pass the database to ChiaService but at this moment I do not have anything to do in ChiaService with database.



Update August 4:
I added all business class to client. Now, server make a list of businesses and client sends a word to search some of them from the list. Server searches and make an array (JSONArray) of it and sends as JSON format. Client receives result as JSON format and make objects out of it.


The flow is little bit complicate:

Server:	start server and make list of business from xml file
Client:	tries to access to server

Server: for each access by Client, makes thread and starts it

Server: sends string “You are connected”
Client: sends name of client

Server: sends string “Please send command”
Client: sends command “0” (means use direct search)

Server: sends string “ready”
Client: sends words to search with

Server: with the words that client sent, search by name and make list (result)

Server: sends result as JSON format string (result is array so it’s JSONArray)
Client: reads JSON string and make an array out of it

Client: initialize business for each element

Client: sends string “see you” and disconnect
Server: receive “see you” from client (does not disconnect)
 



