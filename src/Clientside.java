
import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.*;


public class Clientside {

	public static void main(String[] args) {
		String  name_of_file,command,reply,hostname;
		int port;
		
		if(args.length != 4){
			System.out.println("arguments passed are wrong");
			System.out.println("Usage: myClient <hostname> <port> <command> <filename>");
		}else{
			
			
			hostname = args[0];
			port = Integer.parseInt(args[1]);
			command = args[2];
			name_of_file = args[3];
			
			try {
																//create a socket and connect to the server
				Socket clientSock = new Socket(hostname,port);
				System.out.println("Clientside connection is established with "+hostname+"!!");
				
														//defining input and output streams to read from and write to server
				PrintStream Sar = new PrintStream(clientSock.getOutputStream());
				
				InputStreamReader neat = new InputStreamReader(clientSock.getInputStream());
				BufferedReader Rar = new BufferedReader(neat);
				
												//GET implementation
				if(command.equalsIgnoreCase("get")){
					
					
					
					System.out.println("Clientside = HTTP GET request sent to server "+hostname);
					Sar.println("GET /"+name_of_file+" HTTP/1.1\n");
					
					//Take the server's reply and display
					
					reply = Rar.readLine();
					while(!reply.isEmpty() || reply != null){
						System.out.println("Server: "+reply);
						if(reply.equalsIgnoreCase("close")){
							break;
						}
						reply = Rar.readLine();
					}
					System.out.println("Client is stopping now");
					clientSock.close();
				}
				
														//PUT implementation
				else if(command.equalsIgnoreCase("put")){
					
					System.out.println("Clientside = HTTP PUT request sent to server "+hostname);
					Sar.println("PUT /"+name_of_file+" HTTP\n");
					
					//send contents of file to server	
					
					File NEWFILE = new File(name_of_file);
					byte[] buffer = new byte[(int) NEWFILE.length()];
				    BufferedInputStream ta = new BufferedInputStream(new FileInputStream(NEWFILE));
				    ta.read(buffer, 0, buffer.length);
				    OutputStream output = clientSock.getOutputStream();
				    output.write(buffer, 0, buffer.length);
				    output.flush();
				    
				    
					
					//read the server's reply and display
				    
					reply = Rar.readLine();
					while(!reply.isEmpty() || reply != null){
						System.out.println("Serverside = "+reply);
						if(reply.equalsIgnoreCase("close")){
							break;
						}
						reply = Rar.readLine();
					}
					System.out.println("Clientside is closing now");
					clientSock.close();
				}
				

				
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}
		}
	}

}

