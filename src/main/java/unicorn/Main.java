package unicorn;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import unicorn.ui.MainWindow;

/**
 * Displays the Unicorn graphical user interface using FXML.
 */
public class Main extends Application {
    private final Unicorn unicorn = new Unicorn();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
        AnchorPane mainLayout = fxmlLoader.load();
        MainWindow mainWindow = fxmlLoader.getController();
        mainWindow.setUnicorn(unicorn);

        stage.setScene(new Scene(mainLayout));
        stage.setTitle("Unicorn");
        stage.show();
    }
}
