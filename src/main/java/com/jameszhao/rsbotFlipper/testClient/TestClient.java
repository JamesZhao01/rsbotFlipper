package com.jameszhao.rsbotFlipper.testClient;

import com.jameszhao.rsbotFlipper.server.data.DataHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TestClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port)throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(String msg) throws IOException{
        out.println(msg);
        String resp = in.readLine();
        return resp;
    }

    public void stopConnection()throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void main(String[] args) throws IOException{
//        TestClient c = new TestClient();
//        c.startConnection("127.0.0.1", 8080);
//
//        Item i = Item.newBuilder().setId(12).setName("asdf").build();
//
//        System.out.println(i.toByteArray());
//        Item t = Item.newBuilder().mergeFrom(i.toByteArray()).build();
//        System.out.println(t.getId());
//        System.out.println(t.getName());

//        String response = c.sendMessage(i.toString());
//
//
//        System.out.println(response);
       DataHandler handler = new DataHandler();


    }

}
