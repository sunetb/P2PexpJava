package com.company;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;

public class Main {
    //static String serverIP;
    //Button startKlient, startServer;
    //TextView ipInfo;

    //IP DTU DELL 192.168.50.239

    static String info  = "LOG: \n";

    static final int PORT = 4444;

    static String IP_ADDRESS = "10.90.17.157";

    //Debug state
    static boolean useLocalhost = false; //overrules useAutoIP
    static boolean useAutoIP = true;

    //Other state
    static boolean retryClient = false;

    static ArrayList adresser = new ArrayList();


    // String phoneModel = Build.MODEL;


    public static void main(String[] args) {


        Thread serverTråd = new Thread(new MinServerTråd());
       // serverTråd.start();


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Thread klientTråd = new Thread(new MinKlientTråd());
        klientTråd.start();


        //TODO:
        // ny knap som lukker klienten og starter en ny
        //Både skriv og læs på klient og server - pakkes i metoder!! Kræver det Handler?



    }//main







    static class MinServerTråd implements Runnable {
        @Override
        public void run() {
   /*
            try {
                IP_ADDRESS = getLocalIpAddress();
                update("SERVER: Automatic SERVER IP: " + IP_ADDRESS);
                //Galaxy s10e IOT 10.90.17.158
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            */

  //          while (true) {
                try {

                   // update("SERVER: starting serversocket");
                    ServerSocket serverSocket = new ServerSocket(PORT);

                  //  update("SERVER: start listening..");
                    Socket klientSocket = serverSocket.accept();
                    update("SERVER connection accepted");
                    DataInputStream instream = new DataInputStream(klientSocket.getInputStream());
                    //PrintWriter output = new PrintWriter(outstream, true);
                    String str = (String)  instream.readUTF();
                    System.out.println("Server siger: " +str);
                    serverSocket.close();

                } catch (
                        IOException e) {
                    update("oops!!");
                    throw new RuntimeException(e);


                }
                //update("SERVER (later): Automatic SERVER IP: " + IP_ADDRESS);
   //         }
        }
    }    //class MinServerTråd

    private static String getLocalIpAddress() throws UnknownHostException {
        Enumeration e = null;
        try {
            e = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        InetAddress i = null;
        while(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                i = (InetAddress) ee.nextElement();
                System.out.println(i.getHostAddress());
                update("getLocalIpAddress() gave "+ i.getHostAddress());
                adresser.add(i.getHostAddress());
            }
        }


        return (i == null) ? "fejl" : adresser.get(2).toString(); //If we want to get te last address (localhost)
    }
    static class MinKlientTråd  implements Runnable {
        @Override
        public void run() {

            try {
                Scanner input = new Scanner(System.in);
                update("Please write ip of server (Type 'c' to use hardcoded: 10.90.17.181) ");
                String ip = input.nextLine();
                if (ip.equalsIgnoreCase("c"))
                    ip = "10.90.17.181";

                update("CLIENT: starting client socket ");
                Socket klientsocket = new Socket(ip, 4444);//Fra emulator, indstillinger

                update("CLIENT: client connected ");

                DataInputStream instream = new DataInputStream(klientsocket.getInputStream());
                DataOutputStream out = new DataOutputStream(klientsocket.getOutputStream());
                update("CLIENT: made outputstream");
                boolean carryOn = true;
                while(carryOn) {

                    update("Type message (Enter sends the message)");
                    String besked = input.nextLine();
                    out.writeUTF(besked);
                    //update("CLIENT: wrote to outputstream");

                    out.flush();
                    //update("CLIENT: flushed");
                    String messageFromServer = instream.readUTF();
                    update("Server says: " +messageFromServer);
                    carryOn = !besked.equalsIgnoreCase("bye");
                }
                input.close();
                update("CLIENT: closed Scanner");
                instream.close();
                update("CLIENT: closed inputstream");
                out.close();
                update("CLIENT: closed outputstream");
                klientsocket.close();
                update("CLIENT: closed socket");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

 /*           boolean success = false;
            update("CLIENT: Try reading");
            while (true) {

                try {
                    boolean klar = input.ready();
                    //try hard
                    if (retryClient)
                        klar = true;
                    if (!klar ){
                        update("not ready");
                        //retry();
                        retryClient = true;
                        break;
                    }
                    else{
                        if(!retryClient) update("Ready to read");
                        else update("Force read. Ready now? "+ input.ready());
                    }
                    final String message = input.readLine();
                    System.out.println(message);
                    if (message != null) {
                        //    MainActivity.this.runOnUiThread(new Runnable() {
                        //      @Override
                        //    public void run() {
                        update("CLIENT: SUCCESS!!! Server sent me this: " + message + " ");
                        success = true;
                        //  }
                        // });
                    }
                    else break;

                } catch (
                        IOException e) {
                    update("CLIENT: oops ioexception!!");
                    throw new RuntimeException(e);
                }
                update("end loop");
            }
            update(success ? "CLIENT: Done reading" : "ØV ikke klar");*/
        }//Run()
    } //class MinKlientTråd

    public static void update(String besked){
        System.out.println(besked);


    }


} //Main class
