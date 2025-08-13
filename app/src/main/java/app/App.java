package app;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/index.fxml"));
        Scene scene = new Scene(loader.load()); // let FXML size apply
        stage.setTitle("Dataset Processing System");   
        stage.setScene(scene);
        stage.show();
    
    }

    public static void main(String[] args) {
        launch(args);
    }
}