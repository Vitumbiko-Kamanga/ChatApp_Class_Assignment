package org.example.app1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatAppController {
    @FXML
    private TextArea textArea;

    @FXML
    private VBox chatPane;

    private String lastMessageTime = null;

    public ChatAppController(){

    }

    @FXML
    public void checkMessageButtonClick(ActionEvent actionEvent) throws IOException {
        Socket socket = new Socket("localhost",3000);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);

        if (lastMessageTime == null){
            writer.println("GET/ALL");
        }
        else {
            writer.println("GET/" + lastMessageTime);
        }
        writer.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line;
        int messages = 0;
        while((line = reader.readLine()) != null){
            if (line.equals("_END_")) break;

            String[] res = line.split("/");
            lastMessageTime = res[0];

            Label label1 = new Label(res[1]);
            label1.setWrapText(true);
            label1.getStyleClass().add("incoming-message");
            HBox wrapper = new HBox(label1);
            wrapper.setAlignment(Pos.CENTER_LEFT);
            chatPane.getChildren().add(wrapper);
            messages++;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(messages + " new messages");

        alert.showAndWait();
        socket.close();
    }
    @FXML
    public void sendButtonClick(ActionEvent actionEvent) {
        try {
            Socket socket = new Socket("localhost",3000);

            PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
            writer.println("POST/" + textArea.getText());
            System.out.println(textArea.getText());
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null){
                if (line.equals("_END_")) break;

                String[] res = line.split("/");
                lastMessageTime = res[0];

                Label label1 = new Label(res[1]);
                label1.getStyleClass().add("user-message");
                label1.setWrapText(true);
                HBox wrapper = new HBox(label1);
                wrapper.setAlignment(Pos.CENTER_RIGHT);
                chatPane.getChildren().add(wrapper);
            }
            textArea.clear();
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
