package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.Booksdb;
import view.BooksView;

public class BookMain extends Application {

    @Override
    public void start(Stage primaryStage){

        Booksdb model = new Booksdb();
        BooksView root = new BooksView(model);
        primaryStage.setTitle("Book Database");

        primaryStage.setOnCloseRequest(event -> {
            try {
                model.disconnect();
                root.showAlertAndWait("Disconnected", Alert.AlertType.CONFIRMATION);
            } catch (Exception e) {}
        });

        primaryStage.setScene(new Scene(root, 500, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}