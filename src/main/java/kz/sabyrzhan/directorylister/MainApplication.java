package kz.sabyrzhan.directorylister;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String whiteListFileName = getParameters().getRaw().get(0);
        ReportGenerator.generate(whiteListFileName);
        List<String> folderNames = readFile();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("search-view.fxml"));
        fxmlLoader.setController(new SearchWindowController(folderNames));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Search items");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static List<String> readFile() {
        List<String> folderNames = new ArrayList<>();
        try (var stream = Files.lines(Paths.get(ReportGenerator.OUTPUT_FILE_NAME))) {
            stream.forEach(folderNames::add);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return folderNames;
    }
}