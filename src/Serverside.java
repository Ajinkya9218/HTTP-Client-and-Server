
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;



public class Serverside {

	public static void main(String[] args) throws NumberFormatException, IOException {
		int port;
		boolean sprint = true;
		
		if(args.length != 1){
			System.out.println("arguments passed are wrong");
			System.out.println("Usage: Serverside <port>");
		}else{
			
			// create a new socket with port number
			port = Integer.parseInt(args[0]);
			ServerSocket sock = new ServerSocket(port);	
			
			
			//Multithreaded serverside: To allow multiple clients to connect to the server
			
			
			while(sprint){
				
				
				
				
				System.out.println("Serverside waiting to connect..");
				Socket first = sock.accept();
				ClientHandler client = new ClientHandler(first);
				Thread tin = new Thread(client);
				tin.start();
				System.out.println("Type STOP to shut down the server or continue to continue listening ");
				BufferedReader buffread = new BufferedReader(new InputStreamReader(System.in));
				if(buffread.readLine().equalsIgnoreCase("STOP")){
					PrintStream pr = new PrintStream(first.getOutputStream());
					pr.println("close");
					System.out.println("Clients closed");
					System.out.println("Stopping Serverside");
					sock.close();
					sprint = false;
				}
			}
			
		}
	}
}

class ClientHandler implements Runnable{
	Socket second;

	ClientHandler(Socket s){
		this.second = s;
	}
	
	@Override
	public void run() {
		String demand, command, name_of_file;
		String[] dem = new String[2];
		int step =0;                                          
		
		try{
			
			//create i/p and o/p streams to read from and write to the client
			
			
			InputStreamReader neat = new InputStreamReader(second.getInputStream());
			BufferedReader Rar = new BufferedReader(neat);
			PrintStream Sar = new PrintStream(second.getOutputStream());
			System.out.println("Serverside is connected to new Client.");
			
			//read request from clientside
			
			demand = Rar.readLine();
			while(!demand.isEmpty()){
				System.out.println("Executing serverside "+demand);
				if(step == 0){
					dem = demand.split(" ");
					step++;
				}
				demand = Rar.readLine();
			}
						
			command = dem[0];
			name_of_file = dem[1].split("/")[1];
			File file = new File(name_of_file);
			
			
														//GET implementation
			if(command.equalsIgnoreCase("get")){
				if(!file.exists()){
					Sar.println(" 404 Not Found\n");
					Sar.println("close");
				}else{
														//Read the file contents and send the client
					Sar.println(" 200 OK\n");
					FileReader sack = new FileReader(file);
					BufferedReader Rar2 = new BufferedReader(sack);
					
					String set;
					while((set = Rar2.readLine())!=null){
						Sar.println(set);
					}
					Sar.println("close"); 											//close the client connection
					System.out.println("Serverside: GET command completed");
				}
			}
			
													//PUT implementation
			else if(command.equalsIgnoreCase("put")){
				
				try{
					byte[] buffer = new byte[1024];
				    InputStream none = second.getInputStream();
				    FileOutputStream dock = new FileOutputStream("D:\\"+name_of_file);
				    BufferedOutputStream bos = new BufferedOutputStream(dock);
				    int sick = none.read(buffer, 0, buffer.length);
				    bos.write(buffer, 0, sick);
				    if(sick !=0 ){
				    	Sar.println(" 200 OK File Created\n");
				    }
				    Sar.println("close"); //close the client connection
				    bos.close();
				    System.out.println("Serverside: PUT command executed");
				}catch(FileNotFoundException ex){
					Sar.println("HTTP/ 404 Not Found\n");
				}
										
			}
		}catch(IOException ex){
			System.out.println("problem in connection");
		}
	}
	
}


		

