package com.Threading;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client extends Thread{
    public static void main(String[] args) throws Exception  {

        try
        {
            Socket clientSocket = new Socket("localhost", 4154);
            System.out.println("Client " + clientSocket.getLocalAddress() + " is active.");

            DataOutputStream messageToServer = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream replyFromServer = new DataInputStream(clientSocket.getInputStream());
            Scanner input = new Scanner(System.in);
            System.out.println(replyFromServer.readUTF());

            while(true)
            {
                System.out.println("Write Save to save data \n Write Read to read data \n Enter close to terminate connection \n");

                String userInput = input.nextLine();
                if(userInput.toLowerCase().equals("close"))
                {
                    messageToServer.writeUTF(userInput.toLowerCase());
                    messageToServer.writeUTF(userInput.toLowerCase());

                    System.out.println("Closing this connection : " + clientSocket);
                    replyFromServer.close();
                    messageToServer.close();
                    clientSocket.close();
                    System.out.println("Connection closed");
                    break;
                }


                else if(userInput.toLowerCase().equals("save"))
                {
                    messageToServer.writeUTF(userInput.toLowerCase());
                    messageToServer.writeUTF(userInput.toLowerCase());

                    System.out.println("Enter data to save: ");
                    userInput = input.nextLine();
                    messageToServer.writeUTF(userInput);
                    messageToServer.writeUTF(userInput);

                    System.out.println(replyFromServer.readUTF());
                }

                else if(userInput.toLowerCase().equals("read"))
                {
                    messageToServer.writeUTF(userInput.toLowerCase());
                    messageToServer.writeUTF(userInput.toLowerCase());
                    File file = null;
                    String clientIP = null;

                    String data = replyFromServer.readUTF();
                    if (data.equals("No file found with such connection port name from server!"))
                    {
                        System.out.println(data + "\n");

                        userInput = "close";

                        messageToServer.writeUTF(userInput.toLowerCase());
                        messageToServer.writeUTF(userInput.toLowerCase());

                        System.out.println("Closing this connection : " + clientSocket);
                        replyFromServer.close();
                        messageToServer.close();
                        clientSocket.close();
                        System.out.println("Connection closed");
                        break;
                    }
                    else
                    {
                        clientIP = clientSocket.getLocalPort() + "_" + clientSocket.getPort();
                        clientIP += ".txt";
                        file = new File(clientIP);
                        System.out.println("Data Read from Server: " + replyFromServer.readUTF());
                    }


                    try {
                        FileWriter fileWriter = new FileWriter(file);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        PrintWriter printWriter = new PrintWriter(bufferedWriter);
                        printWriter.print(data);

                        printWriter.close();
                        bufferedWriter.close();
                        fileWriter.close();
                        System.out.println("File with name: " + clientIP + " saved on client side. \n");
                    }

                    catch (Exception e) {
                        System.out.println("Cannot write file");
                    }
                }
                else
                {
                    System.out.println("Invalid input! \n");
                }



            }




        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }
}
