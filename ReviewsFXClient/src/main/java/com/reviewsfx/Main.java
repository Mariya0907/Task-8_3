package com.reviewsfx;

import com.reviewsfx.controller.ReviewController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        ReviewController controller = new ReviewController();
        Scene scene = new Scene(controller.getView(), 900, 600);
        
        primaryStage.setTitle("Reviews Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

