package kz.sabyrzhan.directorylister;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Arrays;
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

    @FXML
    void initialize(){
        folderNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableView.getItems().setAll(folderDataList);
        folderNameColumn.prefWidthProperty().bind(tableView.widthProperty().subtract(20));
        searchTextField.textProperty().addListener(this::onSearchFieldChange);
        updateResultInfo(folderDataList.size());
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
                    .filter(d -> Arrays.stream(terms).allMatch(m -> d.name.toLowerCase().contains(m)))
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