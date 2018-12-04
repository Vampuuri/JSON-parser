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
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.HPos;

import fi.esupponen.jsonparser.StringUnit;
import fi.esupponen.jsonparser.JsonFile;


/**
 * @author      Essi Supponen <essi.supponen@cs.tamk.fi>
 * @version     2018-1201
 * @since       2018-1201
 */
public class Gui extends Application {
    Button parseButton;
    Button addButton;
    Button resetButton;

    TextField itemTextField;
    TextField amountTextField;

    Label itemLabel;
    Label amountLabel;

    GridPane itemTable;

    JsonFile list;

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
        addButton = new Button("ADD TO THE LIST");
        gp.add(addButton, 0, 2, 2, 1);
        GridPane.setHalignment(addButton, HPos.CENTER);

        // resetButton in column 3-4, row 3
        resetButton = new Button("Reset list");
        resetButton.setAlignment(Pos.CENTER);
        gp.add(resetButton, 2, 2, 2, 1);
        GridPane.setHalignment(resetButton, HPos.CENTER);

        return gp;
    }

    private GridPane addItemTable() {
        itemTable = new GridPane();
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

        for (int i = 0; i < itemTable.getChildren().size(); i++) {
            ((Region)(itemTable.getChildren().get(i))).setPadding(new Insets(3,3,3,3));
        }

        itemTable.setStyle("-fx-background-color: white; -fx-grid-lines-visible: true");

        return itemTable;
    }

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

        for (int i = 0; i < itemTable.getChildren().size(); i++) {
            ((Region)(itemTable.getChildren().get(i))).setPadding(new Insets(3,3,3,3));
        }
    }

    private Button addParseButton() {
        parseButton = new Button("PARSE FILE");

        parseButton.setMinHeight(40);
        BorderPane.setMargin(parseButton, new Insets(10,10,10,10));
        BorderPane.setAlignment(parseButton, Pos.CENTER);

        return parseButton;
    }

    private BorderPane addLayout() {
        BorderPane bp = new BorderPane();

        bp.setTop(addTop());
        bp.setCenter(addItemTable());
        bp.setBottom(addParseButton());

        bp.setMinSize(0,0);

        return bp;
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(addLayout(), 640, 480);

        list = new JsonFile("shoppinglist");
        list.add(new StringUnit("omenoita", "viisi"));
        list.add(new StringUnit("puolukoita", "kilo"));
        list.add(new StringUnit("jauhoja", "pussi"));

        updateItemTable();

        stage.setScene(scene);
        stage.setTitle("Shopping list parser");
        stage.show();
    }

    public static void main(String args[]){
        launch(args);
    }
}