package org.example.app1;

import org.example.chatapp.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.time.LocalDateTime;

public class MessageServer {
    private ServerSocket serverSocket;
    private MessageStore<Message> messageStore;
    private int port;

    public MessageServer(int port) {
        this.port = port;
        this.messageStore = new MessageStore<>();
    }
    public void start() throws IOException{
        System.out.println("Sever started...");
        this.serverSocket = new ServerSocket(port);
        while(true){
            Socket socket = serverSocket.accept();
            System.out.println("Connection received ...");

            // reading the content sent from the client
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String command  = reader.readLine();
            String[] commands = command.split("/");

            // writing to the client
            PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
            switch (commands[0]){
                case "GET":
                    if (commands[1].equals("ALL")){
                        for (Message message : messageStore.getAllMessages()){
                            writer.println(message.getDate().toString() + "/" + message.getText());
                        }
                        writer.println("_END_");
                    }
                    else {
                        for (Message message : messageStore.getAllMessagesFromStartDate(commands[1])){
                            writer.println(message.getDate().toString() + "/" + message.getText());
                        }
                        writer.println("_END_");
                    }
                    break;
                case "POST":
                    Message message = new Message(LocalDateTime.now(),commands[1]);
                    messageStore.addMessage(message);
                    writer.println(message.getDate().toString() + "/" + message.getText());
                    writer.println("_END_");
                    break;
                default:
                    System.out.println("Error");
            }
            System.out.println("Response sent");

        }
    }

    public static void main(String[] args) throws IOException {
        MessageServer server = new MessageServer(3000);
        server.start();
    }
}
