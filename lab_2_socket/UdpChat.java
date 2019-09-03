package examples;

import java.net.*;
import java.io.*;
import java.util.*;
public class UdpChat
{
    static final String TERMINATE = "Exit";
    static String name;
    static volatile boolean finished = false;
    public static void main(String[] args)
    {
        try
        {
            Thread uiT = new Thread(new UI());
            uiT.start();
            String backup = "224.0.0.2";
            Scanner sc = new Scanner(System.in);
            InetAddress group = InetAddress.getByName(backup);
            int port = Integer.parseInt("1234");
            name = "shaswata56";
            MulticastSocket socket = new MulticastSocket(port);
            socket.setTimeToLive(255);
            socket.joinGroup(group);
            Thread t = new Thread(new ReadThread(socket,group,port));
            t.start();
            System.out.println("Start typing messages...\n");
            while(true)
            {
                String message;
                System.out.print(name +":");
                message = sc.nextLine();
                if(message.equalsIgnoreCase(UdpChat.TERMINATE))
                {
                    finished = true;
                    socket.leaveGroup(group);
                    socket.close();
                    break;
                }
                message = name + ": " + message;
                byte[] buffer = message.getBytes();
                DatagramPacket datagram = new
                        DatagramPacket(buffer,buffer.length,group,port);
                socket.send(datagram);
            }
        }
        catch(SocketException se)
        {
            System.out.println("Error creating socket");
            se.printStackTrace();
        }
        catch(IOException ie)
        {
            System.out.println("Error reading/writing from/to socket");
            ie.printStackTrace();
        }
    }
}

class ReadThread implements Runnable
{
    private MulticastSocket socket;
    private InetAddress group;
    private int port;
    private static final int MAX_LEN = 1000;
    ReadThread(MulticastSocket socket,InetAddress group,int port)
    {
        this.socket = socket;
        this.group = group;
        this.port = port;
    }

    @Override
    public void run()
    {
        while(!UdpChat.finished)
        {
            byte[] buffer = new byte[ReadThread.MAX_LEN];
            DatagramPacket datagram = new DatagramPacket(buffer,buffer.length, group, port);
            String message;
            try
            {
                socket.receive(datagram);
                message = new String(buffer,0,datagram.getLength(),"UTF-8");
                if(!message.startsWith(UdpChat.name))
                    System.out.println(message);
            }
            catch(IOException e)
            {
                System.out.println("Socket closed!");
            }
        }
    }
}