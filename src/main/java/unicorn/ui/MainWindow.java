package unicorn.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import unicorn.Unicorn;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Unicorn unicorn;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.png"));
    private final Image unicornImage = new Image(this.getClass().getResourceAsStream("/images/Unicorn.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Supplies the command processor used by this window.
     *
     * @param unicorn command processor for user input
     */
    public void setUnicorn(Unicorn unicorn) {
        this.unicorn = unicorn;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank()) {
            return;
        }
        String response = unicorn.getResponse(input);
        String commandType = unicorn.getCommandType();
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getUnicornDialog(response, unicornImage, commandType)
        );
        userInput.clear();
        if (input.equals("bye")) {
            Platform.exit();
        }
    }
}
