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
import java.io.IOException;


import fi.esupponen.jsonparser.StringUnit;
import fi.esupponen.jsonparser.JsonFile;


/**
 * @author      Essi Supponen [essi.supponen@cs.tamk.fi]
 * @version     2018-1218
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

    GridPane bottomPart;

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
     * Creates and opens window to import existing lists.
     *
     * Creates a new stage. Adds instruction label, textfield, import button
     * and cancel button to the stage. Cancel button just closes the window.
     * Import button reads path written to the textfield. If the file
     * doesn't exist or the file has typos, shows an Alert. If everything goes
     * accordingly, sets read file as list and updates itemTable and
     * bottonPart.
     */
    private void importWindow() {
        Stage stage = new Stage();
        stage.setTitle("Import existing file");
        
        GridPane gp = new GridPane();
        gp.setHgap(5);
        gp.setVgap(5);
        gp.setPadding(new Insets(10, 10, 10, 10));
        gp.setMinSize(300,0);

        // Sets two colums to be 50% wide
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(50f);
        gp.getColumnConstraints().add(column);
        gp.getColumnConstraints().add(column);

        Label instructionLabel = new Label("Copy full path to an existing JSON-file.");

        TextField pathField = new TextField();
        pathField.setPromptText("C:/Users/you/Documents/list.json");

        Button openButton = new Button("Open");
        openButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    JsonFile file = FileOpener.readFile(pathField.getText());
                    list = file;
                    updateItemTable();
                    updateBottom();
                    stage.close();
                } catch (IOException ioe) {
                    Alert notFoundAlert = new Alert(Alert.AlertType.WARNING);
                    notFoundAlert.setTitle("File not found!");
                    notFoundAlert.setHeaderText(null);
                    notFoundAlert.setContentText("Check that your path is correct.");
                    Optional<ButtonType> result = notFoundAlert.showAndWait();
                } catch (RuntimeException rte) {
                    Alert badFileAlert = new Alert(Alert.AlertType.WARNING);
                    badFileAlert.setTitle("Bad or missing file");
                    badFileAlert.setHeaderText(null);
                    badFileAlert.setContentText("Given file is bad. Check that it is JSON-file and every unit is in \"string\": \"string\"-format.");
                    Optional<ButtonType> result = badFileAlert.showAndWait();
                } catch (Exception ex) {
                    Alert somethingAlert = new Alert(Alert.AlertType.WARNING);
                    somethingAlert.setTitle("Something went wrong");
                    somethingAlert.setHeaderText(null);
                    somethingAlert.setContentText("Something went wrong. What exactly? Beats me. Try again, hopefully it will not happen again.");
                    Optional<ButtonType> result = somethingAlert.showAndWait();
                }
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction((e) -> stage.close());

        GridPane.setHalignment(instructionLabel, HPos.CENTER);
        GridPane.setHalignment(pathField, HPos.CENTER);
        GridPane.setHalignment(openButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);

        gp.add(instructionLabel, 0, 0, 2, 1);
        gp.add(pathField, 0, 1, 2, 1);
        gp.add(openButton, 0, 2);
        gp.add(cancelButton, 1, 2);

        Scene scene = new Scene(gp, 300, 100);
        stage.setScene(scene);
        stage.show();
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

        // resetButton in column 3, row 3
        resetButton = addResetButton();
        resetButton.setAlignment(Pos.CENTER);
        gp.add(resetButton, 2, 2);
        GridPane.setHalignment(resetButton, HPos.CENTER);

        // importButton in colum 4, row 3
        Button importButton = new Button("Import file");
        importButton.setOnAction((e) -> importWindow());
        importButton.setAlignment(Pos.CENTER);
        gp.add(importButton, 3, 2);

        return gp;
    }

    /**
     * Generates and returns itemTable.
     *
     * Creates a GridPane. Adds needed features to make it look (mostly) nice.
     *
     * @return  itemTable
     */
    private GridPane addItemTable() {
        itemTable = new GridPane();
        itemTable.setHgap(5);
        itemTable.setVgap(5);
        itemTable.setPadding(new Insets(10, 10, 10, 10));
        itemTable.setMinSize(300,0);

        ColumnConstraints columnLabels = new ColumnConstraints();
        columnLabels.setPercentWidth(40f);
        itemTable.getColumnConstraints().add(columnLabels);
        itemTable.getColumnConstraints().add(columnLabels);
        ColumnConstraints columnButtons = new ColumnConstraints();
        columnButtons.setPercentWidth(10f);
        itemTable.getColumnConstraints().add(columnButtons);
        itemTable.getColumnConstraints().add(columnButtons);

        return itemTable;
    }

    /**
     * Modifies itemTable to edit given item.
     *
     * Removes all the buttons from the itemTable. Adds textField for
     * modifying given item. Also adds Done and Cancel buttons. Done button
     * will check, if input is not empty. If empty, shows an Alert. If not
     * updates item in the list and uses updateItemTable(). Cancel button
     * just uses updateItemTable() without modifying the list.
     *
     * @param   index   index of the item to edit
     */
    public void editItemTable(int index) {
        TextField newAmountField = new TextField();
        newAmountField.setPromptText(((StringUnit)list.getUnits().get(index)).getValue());

        Button doneButton = new Button("Done");
        doneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String amount = newAmountField.getText();

                if (amount.equals("")) {
                    Alert noInputAlert = new Alert(Alert.AlertType.WARNING);
                    noInputAlert.setTitle("Empty input");
                    noInputAlert.setHeaderText(null);
                    noInputAlert.setContentText("Amount input is empty.");
                    Optional<ButtonType> result = noInputAlert.showAndWait();
                } else {
                    ((StringUnit)list.getUnits().get(index)).setValue(amount);
                    updateItemTable();
                }
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction((e) -> updateItemTable());

        for (int i = itemTable.getChildren().size() - 1; i > 4; i = i - 4) {
            itemTable.getChildren().remove(i);
            itemTable.getChildren().remove(i - 1);
        }

        itemTable.add(newAmountField, 1, index + 1);
        itemTable.add(doneButton, 2, index + 1);
        itemTable.add(cancelButton, 3, index + 1);
    }

    /**
     * Updates itemTable.
     *
     * Clears the itemTable. Adds all the labels and units from list to the
     * table. Adds Edit and Remove-buttons after every item on the list.
     */
    public void updateItemTable() {
        itemTable.getChildren().clear();

        itemTable.add(new Label("ITEM"), 0, 0);
        itemTable.add(new Label("AMOUNT"), 1, 0);
        GridPane.setHalignment(itemTable.getChildren().get(0), HPos.CENTER);
        GridPane.setHalignment(itemTable.getChildren().get(1), HPos.CENTER);

        if (list.getUnits().size() == 0) {
            itemTable.add(new Label("No items in the list."), 0, 1, 2, 1);
            GridPane.setHalignment(itemTable.getChildren().get(2), HPos.CENTER);
        } else {
            for (int i = 0; i < list.getUnits().size(); i++) {
                Label itemlabel = new Label(list.getUnits().get(i).getKey());
                itemlabel.setPrefHeight(25);

                itemTable.add(itemlabel, 0, i+1);
                itemTable.add(new Label(((StringUnit)(list.getUnits().get(i))).getValue()), 1, i+1);

                Button editButton = new Button("Edit");
                final int index = i;
                editButton.setOnAction((e) -> editItemTable(index));
                itemTable.add(editButton, 2, i+1);

                Button removeButton = new Button("Remove");
                removeButton.setOnAction(new RemoveItemListener(this, i));
                itemTable.add(removeButton, 3, i+1);
            }
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
                try {
                    list.parse();

                    Alert noInputAlert = new Alert(Alert.AlertType.INFORMATION);
                    noInputAlert.setTitle("File created");
                    noInputAlert.setHeaderText(null);
                    noInputAlert.setContentText("File " + list.getName() + ".json created!");
                    Optional<ButtonType> result = noInputAlert.showAndWait();
                } catch (Exception ex) {
                    Alert noInputAlert = new Alert(Alert.AlertType.WARNING);
                    noInputAlert.setTitle("File was not created");
                    noInputAlert.setHeaderText(null);
                    noInputAlert.setContentText("Something went wrong while parsing the file. Please check all your inputs and try again.");
                    Optional<ButtonType> result = noInputAlert.showAndWait();
                }
            }
        });

        parseButton.setMinHeight(40);
        BorderPane.setMargin(parseButton, new Insets(10,10,10,10));
        BorderPane.setAlignment(parseButton, Pos.CENTER);

        return parseButton;
    }

    /**
     * Checks if given filename contains any bad characters.
     *
     * @param   str     String to check
     * @return          is suitable for filename
     */
    private boolean suitableFileName(String str) {
        String reservedCharacters = "/\\?%*:|\"<>.";
        boolean suitable = true;

        for (int i = 0; i < str.length(); i++) {
            String c = "" + str.charAt(i);

            if (reservedCharacters.contains(c)) {
                suitable = false;
                break;
            }
        }

        return suitable;
    }

    /**
     * Modifies bottomPart to change the filename.
     *
     * Clears children from bottomPart. Adds json-label, textfield for new name
     * ok- and cancel-buttons. Ok checks if new filename is bad. If it is, shows
     * an Alert. If not, changes the name to the list and uses updateBottom().
     * Cancel button just uses updateBottom() without changing anything.
     */
    private void renameTheFile() {
        TextField newNameField = new TextField();
        newNameField.setPrefWidth(100);
        newNameField.setPromptText(list.getName());
        GridPane.setHalignment(newNameField, HPos.RIGHT);

        Label jsonLabel = new Label(".json");
        GridPane.setHalignment(jsonLabel, HPos.LEFT);

        Button okButton = new Button("OK");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String newName = newNameField.getText();

                if (newName.equals("")) {
                    Alert noInputAlert = new Alert(Alert.AlertType.WARNING);
                    noInputAlert.setTitle("Empty input");
                    noInputAlert.setHeaderText(null);
                    noInputAlert.setContentText("Input is empty.");
                    Optional<ButtonType> result = noInputAlert.showAndWait();
                } else if (!suitableFileName(newName)) {
                    Alert noInputAlert = new Alert(Alert.AlertType.WARNING);
                    noInputAlert.setTitle("Bad filename");
                    noInputAlert.setHeaderText(null);
                    noInputAlert.setContentText("Bad filename. Chekc that your filename does not include any of these characters: /\\?%*:|\"<>.");
                    Optional<ButtonType> result = noInputAlert.showAndWait();
                } else {
                    list.setName(newName);
                    updateBottom();
                }
            }
        });

        GridPane.setHalignment(okButton, HPos.RIGHT);

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction((e) -> updateBottom());
        GridPane.setHalignment(cancelButton, HPos.LEFT);

        bottomPart.getChildren().clear();

        bottomPart.add(newNameField, 1, 0, 2, 1);
        bottomPart.add(jsonLabel, 3, 0);
        bottomPart.add(okButton, 1, 1);
        bottomPart.add(cancelButton, 2, 1);
    }

    /**
     * Creates and sets Nodes in bottomPart into place.
     *
     * Clears children from bottomPart. Adds label for filename, button
     * to change filename and parse -button.
     */
    private void updateBottom() {
        bottomPart.getChildren().clear();

        Label fileNameLabel = new Label("Parsed file will be called: " + list.getName() + ".json");
        Button renameButton = new Button("Rename the file");
        renameButton.setOnAction((e) -> renameTheFile());

        bottomPart.add(fileNameLabel, 0, 0, 4, 1);
        bottomPart.add(addParseButton(), 0, 1, 2, 1);
        bottomPart.add(renameButton, 2, 1, 2, 1);

        GridPane.setHalignment(fileNameLabel, HPos.CENTER);
        GridPane.setHalignment(parseButton, HPos.RIGHT);
        GridPane.setHalignment(renameButton, HPos.LEFT);
    }

    /**
     * Generates bottomPart and makes it look nice.
     *
     * @return  bottomPart
     */
    private GridPane addBottom() {
        bottomPart = new GridPane();
        bottomPart.setHgap(5);
        bottomPart.setVgap(5);
        bottomPart.setPadding(new Insets(10, 10, 10, 10));
        bottomPart.setMinSize(300,0);

        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(25f);
        bottomPart.getColumnConstraints().add(column);
        bottomPart.getColumnConstraints().add(column);
        bottomPart.getColumnConstraints().add(column);
        bottomPart.getColumnConstraints().add(column);

        return bottomPart;
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
        bp.setBottom(addBottom());

        bp.setMinSize(0,0);

        return bp;
    }

    /**
     * Generates and shows the window.
     */
    @Override
    public void start(Stage stage) {
        list = new JsonFile("shoppinglist");

        Scene scene = new Scene(addLayout(), 640, 480);

        updateItemTable();
        updateBottom();

        stage.setScene(scene);
        stage.setTitle("Shopping list parser");
        stage.show();
    }

    /**
     * Launces the application.
     *
     * @param   args    used to launch
     */
    public static void main(String args[]){
        System.out.println("Author: Essi Supponen");

        launch(args);
    }
}