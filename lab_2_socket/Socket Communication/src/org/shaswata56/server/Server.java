package org.shaswata56.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Server extends Thread {
    private ServerSocket sock;
    private Scanner s = new Scanner(System.in);
    private String buff;

    public Server(int port) {
        try {
            sock = new ServerSocket(port);
            sock.setSoTimeout(300000);
        } catch (Exception e) {
            System.out.println("Port is being used by another process!\nTry another..");
        }
    }

    public void run() {
        try {
            System.out.println(Inet4Address.getLocalHost() +" is listening on "+ sock.getLocalPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Socket server = null;
        try {
            server = sock.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert server != null;
        System.out.println("Connected to "+ server.getRemoteSocketAddress());
        while (true) {
            try {
                DataInputStream in = new DataInputStream(server.getInputStream());
                System.out.println(in.readUTF());

                buff = s.next();
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                if(!buff.equals("quit")){
                    out.writeUTF(buff);
                } else {
                    server.close();
                }
            } catch (SocketTimeoutException se) {
                System.out.println("Connection timed out!\nRetry!!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
