package com.Threading;

import java.io.*;
import java.net.Socket;

public class ManageClientThread implements Runnable {

     Socket socket;
     DataInputStream dataInputStream;
     DataOutputStream dataOutputStream;


    public ManageClientThread(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        this.socket = socket;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;

    }

    @Override
    public void run() {
        String input, output;
        String clientIP = this.socket.getPort() + ".txt";
        File file = new File(clientIP);



        while (true)
        {
            try {

                input = dataInputStream.readUTF();
                if(input.toLowerCase().equals("close"))
                {
                    System.out.println("Closing this connection : " + this.socket);
                    this.socket.close();
                    Server.connectionCount--;
                    System.out.println("Connection closed");

                    System.out.println("Active connections: " + Server.connectionCount);
                    break;
                }
                else
                {
                    if(input.toLowerCase().equals("save"))
                    {
                        try {

                            input = dataInputStream.readUTF();
                            FileWriter fileWriter = new FileWriter(file);
                            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                            PrintWriter printWriter = new PrintWriter(bufferedWriter);
                            printWriter.print(input);

                            printWriter.close();
                            bufferedWriter.close();
                            fileWriter.close();
                            dataOutputStream.writeUTF("Information saved for client: " + this.socket.getRemoteSocketAddress()+ "\n\n");
                        }

                        catch (Exception e) {
                            System.out.println("Cannot write file");
                        }
                    }
                    else if(input.toLowerCase().equals("read"))
                    {
                        try {

                            FileReader fileReader = new FileReader(file);
                            BufferedReader bufferedReader = new BufferedReader(fileReader);
                            output = bufferedReader.readLine();
                            if(output == null)
                            {
                                dataOutputStream.writeUTF("No Information found for client: " + this.socket.getRemoteSocketAddress());
                                dataOutputStream.writeUTF("No Information found for client: " + this.socket.getRemoteSocketAddress());
                            }
                            else
                            {
                                dataOutputStream.writeUTF("Information for client: " + this.socket.getRemoteSocketAddress() + "\n" + output );
                                dataOutputStream.writeUTF("Information for client: " + this.socket.getRemoteSocketAddress() + "\n" + output );
                            }


                        } catch (Exception e) {
                            System.out.println("No file found with such connection port name from server!");
                            dataOutputStream.writeUTF("No file found with such connection port name from server!");
                            System.out.println("Closing this connection : " + this.socket);
                            this.socket.close();
                            Server.connectionCount--;
                            System.out.println("Connection closed");

                            System.out.println("Active connections: " + Server.connectionCount);
                            break;
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
