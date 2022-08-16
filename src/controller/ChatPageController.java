package controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatPageController extends Thread {
    public AnchorPane ChatPageContext;
    public JFXTextField txtMessage;
    public JFXTextArea txtAreaAllText;
    public Label lblName;

    BufferedReader reader;
    PrintWriter writer;
    Socket socket;

    public void initialize(){
        lblName.setText(LogingPageController.username);
        try {
            socket = new Socket("localhost", 5000);
            System.out.println("Socket is connected with server successfully...!");
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer=new PrintWriter(socket.getOutputStream(),true);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }  
        
    }
    @Override
    public void run(){
        try {
            while (true) {

                String msg = reader.readLine();
                String[] tokens = msg.split(" ");
                String cmd = tokens[0];
                System.out.println(cmd);
                StringBuilder fullMsg = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fullMsg.append(tokens[i]);
                }
                System.out.println(fullMsg);

                System.out.println("cmd="+cmd+"-----"+"UserName"+lblName.getText());
                if(!cmd.equalsIgnoreCase(lblName.getText()+":")){
                    txtAreaAllText.appendText(msg + "\n");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(){
        String msg = txtMessage.getText();
        writer.println(lblName.getText() + ": " + txtMessage.getText());
        txtMessage.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        txtAreaAllText.appendText("Me: " + msg + "\n");
        txtMessage.clear();
        if(msg.equalsIgnoreCase("Bye")||(msg.equalsIgnoreCase("Logout"))) {
            System.exit(0);
        }
    }

    public void btnSendOnAction(ActionEvent actionEvent) {
        send();
    }
}
