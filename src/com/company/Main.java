package com.company;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;

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

//                    output.write("BESKEDEN KOMMER HER");
  //                  update("SERVER: Besked skrevet til output");
                    //Thread.sleep(50);
                    // output.write("NY BESKED");
                    //output.flush();
                    //update("SERVER: flush");


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
           // BufferedReader input;
            //Socket socket;
            try {


              //  update("CLIENT: starting client socket on "+IP_ADDRESS);


                //socket = new Socket(serverIP, 5050);
                //socket = new Socket("localhost/127.0.0.1");
                //socket = new Socket("10.212.178.72", 5050);//fysisk s10e indstillinger

                //socket = new Socket("10.80.0.138", 5050);//fysisk s7 indstillinger

                //Test:
                //IP_ADDRESS = "192.168.50.239";
                Socket klientsocket = new Socket("10.90.17.157", PORT);//Fra emulator, indstillinger

                //update("CLIENT: client connected to "+ IP_ADDRESS);
                //input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //System.out.println(input);
                //update("CLIENT: Got inputstream");

                DataOutputStream out = new DataOutputStream(klientsocket.getOutputStream());
                out.writeUTF("hej");
                out.flush();
                out.close();
                klientsocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            boolean success = false;
            update("CLIENT: Try reading");
            while (true) {

                try {
/*                    boolean klar = input.ready();
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
                    }*/
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
            update(success ? "CLIENT: Done reading" : "ØV ikke klar");
        }//Run()
    } //class MinKlientTråd

    public static void update(String besked){
        System.out.println(besked);


    }

    static String cleanIt (String s){
        return s.replaceAll("/", "");
    }
} //Main class
