package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardController {
    public Button btn;
    public AnchorPane pane;
    public javafx.scene.control.Label lblTimes;

    public void initialize(){
        calculateTime();
    }

    private void calculateTime() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.ZERO,
                actionEvent -> lblTimes.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
        ), new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    public void customerButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/CustomerForm.fxml"))));
        stage.setTitle("Customer Form");
    }

    public void itemButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/ItemForm.fxml"))));
        stage.setTitle("Item Form");
    }

    public void PlaceOrderButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/PlaceOrderForm.fxml"))));
        stage.setTitle("PlaceOrder Form");
    }

    public void OrderButtoOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/OrderTables.fxml"))));
        stage.setTitle("Orders Form");
    }
}
