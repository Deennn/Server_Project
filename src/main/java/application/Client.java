package application;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String userName) {
       try {
           this.socket = socket;
          this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
          this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          this.username= userName;
       } catch (IOException e ) {
           closeEverything(socket, bufferedReader, bufferedWriter);
       }
    }

    public void  sendMessage() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(username + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                if(messageToSend.equalsIgnoreCase("exit")){
                    closeEverything(socket,bufferedReader,bufferedWriter);
                    break;
                }
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    public void listenToMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        messageFromGroupChat = bufferedReader.readLine();
                        if(messageFromGroupChat == null){
                            closeEverything(socket,bufferedReader,bufferedWriter);
                            break;
                        }else{
                            System.out.println(messageFromGroupChat);
                        }
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try{
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for group chat");

        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket,username);
        client.listenToMessage();
        client.sendMessage();


    }
}
