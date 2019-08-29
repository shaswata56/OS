package org.shaswata56.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {

    private Socket client;
    private Scanner input = new Scanner(System.in);

    public Client(String host, int port) throws IOException {
        client = new Socket(host, port);
    }

    public void run() {
        System.out.println("Connected to "+ client.getRemoteSocketAddress());
        while (true) try {
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            String str = input.next();
            if (!str.equals("quit")) {
                out.writeUTF(str);
            } else {
                client.close();
            }
            DataInputStream in = new DataInputStream(client.getInputStream());
            System.out.println(in.readUTF());
        } catch (Exception e) {
            e.printStackTrace();
            break;
        }
    }
}
