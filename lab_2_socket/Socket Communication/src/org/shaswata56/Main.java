package org.shaswata56;

import org.shaswata56.client.Client;
import org.shaswata56.gchat.GroupChat;
import org.shaswata56.server.Server;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.print("1. Group Chat (UDP)\n2. P2P Chat (TCP)\n:");
        int choice = input.nextInt();
        if(choice == 1) {
            args = new String[2];
            input.nextLine();
            System.out.print("Enter IP: ");
            args[0] = input.nextLine();
            System.out.print("Enter Port: ");
            args[1] = input.nextLine();
            GroupChat.main(args);
        } else if(choice == 2) {
            System.out.print("Enter 1, if you want to host server and waiting for client\nEnter 2, if you have an ip address for connect to a server\n:");
            choice = input.nextInt();
            if(choice == 1) {
                System.out.print("Enter port number you wish to use: ");
                int port = input.nextInt();
                Thread t = new Server(port);
                t.start();
            } else if(choice == 2) {
                System.out.print("Enter IP : ");
                String host = input.next();
                System.out.print("Enter PORT : ");
                int port = input.nextInt();
                Thread t = new Client(host, port);
                t.start();
            }
        } else {
            main(args);
        }
    }
}
