package kz.sabyrzhan.directorylister;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;

public class SearchWindowController {
    @FXML
    protected TableView<FolderData> tableView;

    @FXML
    protected TableColumn<FolderData,String> folderNameColumn;

    @FXML
    protected TextField searchTextField;

    @FXML
    protected Label resultLabel;

    private final List<FolderData> folderDataList;

    public SearchWindowController(List<String> folderNames) {
        this.folderDataList = folderNames.stream().map(FolderData::new).toList();
    }


    /*
    These links from StackOverflow helped to implement matched search result highlighting:
    - https://stackoverflow.com/questions/23818097/highlight-text-in-tableview-with-textflow
    - https://stackoverflow.com/questions/26906810/javafx-tableview-with-highlighted-text
    */
    @FXML
    void initialize(){
        folderNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableView.getItems().setAll(folderDataList);
        folderNameColumn.prefWidthProperty().bind(tableView.widthProperty().subtract(20));
        searchTextField.textProperty().addListener(this::onSearchFieldChange);
        folderNameColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    setStyle("");
                } else {
                    setGraphic(null);
                    TextFlow value = buildTextFlow(item, searchTextField.getText(), 20);
                    if (value == null) {
                        setText(item);
                        setTextFill(Color.BLACK);
                        setStyle("");
                        setContentDisplay(ContentDisplay.TEXT_ONLY);
                    } else {
                        setGraphic(value);
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    }
                }
            }
        });
        updateResultInfo(folderDataList.size());
    }

    private TextFlow buildTextFlow(String text, String filter, double height) {
        if(filter.trim().isEmpty()) {
            return null;
        }

        String textLower = text.toLowerCase();
        filter = filter.trim().toLowerCase();

        var result = new TextFlow();
        String[] filters = filter.split(" ");
        int start = 0;
        for(String f : filters) {
            int index = textLower.indexOf(f, start);
            if (index < 0) {
                return null;
            }

            result.getChildren().add(new Text(text.substring(start, index)));
            Text e = new Text(text.substring(index, index + f.length()));
            e.setFill(Color.BLUE);
            result.getChildren().add(e);
            start = index + f.length();
        }

        result.getChildren().add(new Text(text.substring(start)));
        result.setPrefHeight(height);

        return result;
    }

    @FXML
    public void onSearchFieldChange(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        int foundItemsSize;
        if (newValue.trim().isEmpty()) {
            tableView.getItems().setAll(folderDataList);
            foundItemsSize = folderDataList.size();
        } else {
            String[] terms = newValue.trim().toLowerCase().split(" ");
            List<FolderData> filteredData = folderDataList.stream()
                    .filter(d -> {
                        String textLower = d.name.trim().toLowerCase();
                        int start = 0;
                        for(String f : terms) {
                            int index = textLower.indexOf(f, start);
                            if (index < 0) {
                                return false;
                            }
                            start = index + f.length();
                        }

                        return true;
                    })
                    .sorted().toList();
            tableView.getItems().setAll(filteredData);
            foundItemsSize = filteredData.size();
        }
        updateResultInfo(foundItemsSize);
    }

    private void updateResultInfo(int totalItemsFound) {
        resultLabel.textProperty().setValue("Total items found: " + totalItemsFound);
    }

    public record FolderData(String name) implements Comparable<FolderData> {
        public String getName() {
            return name;
        }

        @Override
        public int compareTo(FolderData o) {
            return name.compareTo(o.name);
        }
    }
}