package com.Threading;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static int connectionCount = 0;
    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws Exception{

            Socket connectionSocket = null;


            ServerSocket serverSocket = new ServerSocket(4154);
            while(true) {
                if(connectionCount <= 0)
                {
                    System.out.println("Waiting for clients on port " + serverSocket.getLocalPort());
                }

                try {
                   connectionSocket = serverSocket.accept();
                }
                catch(Exception e)
                {
                    connectionSocket.close();
                    e.printStackTrace();
                }



                if(connectionSocket != null)
                {
                    System.out.println("Got connection from: " + connectionSocket.getRemoteSocketAddress());
                    connectionCount++;
                    System.out.println("Active connections: " + connectionCount);

                    DataOutputStream dataOutputStream = new DataOutputStream(connectionSocket.getOutputStream());
                    DataInputStream dataInputStream = new DataInputStream(connectionSocket.getInputStream());
                    dataOutputStream.writeUTF("Welcome to the server: " + serverSocket.getLocalSocketAddress());


                    if(dataInputStream.readUTF().toLowerCase().equals("close"))
                    {
                        Server.connectionCount--;
                        System.out.println("Active connections: " + connectionCount);
                    }

                    ManageClientThread thread = new ManageClientThread(connectionSocket, dataInputStream, dataOutputStream);
                    executorService.execute(thread);
                }


            }

    }


}



