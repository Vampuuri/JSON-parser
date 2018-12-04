package fi.esupponen.shoppinglist;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.event.*;
import java.util.Optional;


import fi.esupponen.jsonparser.StringUnit;
import fi.esupponen.jsonparser.JsonFile;


/**
 * @author      Essi Supponen [essi.supponen@cs.tamk.fi]
 * @version     2018-1204
 * @since       2018-1201
 */
public class Gui extends Application {

    /**
     * Button to parse the list into json-file.
     */
    Button parseButton;

    /**
     * Button to add an item to the list.
     */
    Button addButton;

    /**
     * Button to reset the list.
     */
    Button resetButton;

    /**
     * TextField for item input.
     */
    TextField itemTextField;

    /**
     * TextField for amount input.
     */
    TextField amountTextField;

    /**
     * Label for item.
     */
    Label itemLabel;

    /**
     * Label for amount.
     */
    Label amountLabel;

    /**
     * Table for all items in list.
     */
    GridPane itemTable;

    /**
     * JsonFile to save all the information.
     */
    JsonFile list;

    /**
     * Generates and returns button for adding an item to the list.
     *
     * Generates a new button. Adds event for button push. Reads given inputs
     * and adds it to the list. If neither of inputs are missing, shows an
     * alert. If the given key (item) is already in list, shows an alert.
     *
     * @return  addButton
     */
    private Button addAddButton() {
        Button add = new Button("ADD TO THE LIST");

        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String itemString = itemTextField.getText();
                String amountString = amountTextField.getText();

                if (itemString.equals("") || amountString.equals("")) {
                    // If either of the inputs are empty, shows an alert.
                    Alert noInputAlert = new Alert(Alert.AlertType.WARNING);
                    noInputAlert.setTitle("Empty input");
                    noInputAlert.setHeaderText(null);
                    noInputAlert.setContentText("Item and/or Amount input is empty.");
                    Optional<ButtonType> result = noInputAlert.showAndWait();
                } else if (list.alreadyUsed(itemString)) {
                    // If key is already used, shows and alert.
                    Alert keyUsedAlert = new Alert(Alert.AlertType.WARNING);
                    keyUsedAlert.setTitle("Key already used");
                    keyUsedAlert.setHeaderText(null);
                    keyUsedAlert.setContentText("Item key is already used. Please use another Item.");
                    Optional<ButtonType> result = keyUsedAlert.showAndWait();
                } else {
                    // If nothing is wrong, adds the item to the list and
                    // clears the TextFields.
                    list.add(itemString, amountString);

                    itemTextField.clear();
                    amountTextField.clear();
                    itemTextField.setPromptText("Item to buy");
                    amountTextField.setPromptText("Amount");

                    updateItemTable();
                }
            }
        });

        return add;
    }

    /**
     * Generates and returns button for resetting the list.
     * 
     * Generates a button. Adds ation to the button push. Clears list and
     * updates the itemTable.
     *
     * @return  resetButton
     */
    private Button addResetButton() {
        Button reset = new Button("Reset list");

        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                list.clear();
                updateItemTable();
            }
        });

        return reset;
    }

    /**
     * Generates and returns the whole top of the border layout.
     *
     * Creates a gridpane. Adds all the needed features to make the layout look
     * (mostly) nice. Adds itemLabel, itemTextField, amountLabel,
     * amountTextField, addButton and resetButton.
     *
     * @return  top part to the border layout
     */
    private GridPane addTop() {
        GridPane gp = new GridPane();
        gp.setHgap(5);
        gp.setVgap(5);
        gp.setPadding(new Insets(10, 10, 10, 10));
        gp.setMinSize(300,0);

        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(25f);
        gp.getColumnConstraints().add(column);
        gp.getColumnConstraints().add(column);
        gp.getColumnConstraints().add(column);
        gp.getColumnConstraints().add(column);

        // itemLabel in column 1, row 1
        itemLabel = new Label("Item: ");
        itemLabel.setMaxWidth(Double.MAX_VALUE);
        gp.add(itemLabel, 0, 0);

        // itemTextField in column 2-4, row 1
        itemTextField = new TextField();
        itemTextField.setPromptText("Item to buy");
        itemTextField.setMaxWidth(Double.MAX_VALUE);
        gp.add(itemTextField, 1, 0, 3, 1);

        // amountLabel int column 1, row 2
        amountLabel = new Label("Amount: ");
        amountLabel.setMaxWidth(Double.MAX_VALUE);
        gp.add(amountLabel, 0, 1);

        // amountTextField in column 2-4, row 2
        amountTextField = new TextField();
        amountTextField.setPromptText("Amount");
        amountTextField.setMaxWidth(Double.MAX_VALUE);
        gp.add(amountTextField, 1, 1, 3, 1);

        // addButton in column 1-2, row 3
        addButton = addAddButton();
        gp.add(addButton, 0, 2, 2, 1);
        GridPane.setHalignment(addButton, HPos.CENTER);

        // resetButton in column 3-4, row 3
        resetButton = addResetButton();
        resetButton.setAlignment(Pos.CENTER);
        gp.add(resetButton, 2, 2, 2, 1);
        GridPane.setHalignment(resetButton, HPos.CENTER);

        return gp;
    }

    /**
     * Generates and returns itemTable.
     *
     * Creates a GridPane. Adds needed features to make it look (mostly) nice.
     * Adds ITEM and AMOUNT labels.
     *
     * @return  itemTable
     */
    private GridPane addItemTable() {
        itemTable = new GridPane();
        itemTable.setHgap(5);
        itemTable.setVgap(5);
        itemTable.setPadding(new Insets(10, 100, 10, 100));
        itemTable.setMinSize(300,0);

        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(50f);
        itemTable.getColumnConstraints().add(column);
        itemTable.getColumnConstraints().add(column);

        itemTable.add(new Label("ITEM"), 0, 0);
        itemTable.add(new Label("AMOUNT"), 1, 0);

        GridPane.setHalignment(itemTable.getChildren().get(0), HPos.CENTER);
        GridPane.setHalignment(itemTable.getChildren().get(1), HPos.CENTER);

        return itemTable;
    }

    /**
     * Updates itemTable.
     *
     * Clears the itemTable. Adds all the labels and units from list to the
     * table.
     */
    public void updateItemTable() {
        itemTable.getChildren().clear();

        itemTable.add(new Label("ITEM"), 0, 0);
        itemTable.add(new Label("AMOUNT"), 1, 0);
        GridPane.setHalignment(itemTable.getChildren().get(0), HPos.CENTER);
        GridPane.setHalignment(itemTable.getChildren().get(1), HPos.CENTER);

        for (int i = 0; i < list.getUnits().size(); i++) {
            itemTable.add(new Label(list.getUnits().get(i).getKey()), 0, i+1);
            itemTable.add(new Label(((StringUnit)(list.getUnits().get(i))).getValue()), 1, i+1);
        }
    }

    /**
     * Generates and returns parseButton.
     *
     * Creates a new button. When button is pressed parses the file. Shows an
     * alert window to inform the user.
     *
     * @return  parseButton
     */
    private Button addParseButton() {
        parseButton = new Button("PARSE FILE");
        parseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                list.parse();

                Alert noInputAlert = new Alert(Alert.AlertType.INFORMATION);
                noInputAlert.setTitle("File created");
                noInputAlert.setHeaderText(null);
                noInputAlert.setContentText("File shoppinglist.json created!");
                Optional<ButtonType> result = noInputAlert.showAndWait();
            }
        });

        parseButton.setMinHeight(40);
        BorderPane.setMargin(parseButton, new Insets(10,10,10,10));
        BorderPane.setAlignment(parseButton, Pos.CENTER);

        return parseButton;
    }

    /**
     * Generates and returns the layout.
     *
     * Uses BorderPane. Adds right items to the right places.
     *
     * @return  layout
     */
    private BorderPane addLayout() {
        BorderPane bp = new BorderPane();

        bp.setTop(addTop());
        bp.setCenter(addItemTable());
        bp.setBottom(addParseButton());

        bp.setMinSize(0,0);

        return bp;
    }

    /**
     * Generates and shows the window.
     */
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(addLayout(), 640, 480);

        list = new JsonFile("shoppinglist");

        updateItemTable();

        stage.setScene(scene);
        stage.setTitle("Shopping list parser");
        stage.show();
    }

    /**
     * Launces the application.
     */
    public static void main(String args[]){
        launch(args);
    }
}