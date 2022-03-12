package kz.sabyrzhan;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Main {
    public static class WhiteListItem {
        private int level;
        private String rootFolder;

        public WhiteListItem next(String subfolder) {
            var whiteListItem = new WhiteListItem();
            whiteListItem.level = level;
            whiteListItem.rootFolder = rootFolder + "/" + subfolder;

            return whiteListItem;
        }
    }
    public static void main(String[] args) {
        String whileListFile = args[0];
        List<WhiteListItem> whiteListItems = new ArrayList<>();
        try(var scanner = new Scanner(new FileInputStream(whileListFile))) {
            while(scanner.hasNextLine()) {
                String[] tokens = scanner.nextLine().trim().split(",");
                var item = new WhiteListItem();
                item.level = Integer.parseInt(tokens[0]);
                item.rootFolder = tokens[1];
                whiteListItems.add(item);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        String resultFile = "result.txt";
        var allFiles = new ArrayList<String>();
        for(var whiteListItem : whiteListItems) {
            readFolderContentRecursively(whiteListItem, allFiles, 1);
            allFiles.sort(Comparator.naturalOrder());
            StringBuilder stringBuilder = new StringBuilder();
            for(String folder: allFiles) {
                stringBuilder.append(folder).append("\n");
            }
            String contentToWrite = stringBuilder.toString();
            try(var fileOutputStream = new FileOutputStream(resultFile);
                PrintWriter writer = new PrintWriter(fileOutputStream)) {
                writer.print(contentToWrite);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Finished!");
    }

    private static void readFolderContentRecursively(WhiteListItem item, List<String> items, int currentLevel) {
        if (currentLevel > item.level) {
            return;
        }

        var rootPath = Paths.get(item.rootFolder);
        Predicate<Path> isDirPredicate = Files::isDirectory;
        Predicate<Path> isNotTheSameDir = pathItem -> !pathItem.equals(rootPath);

        try (Stream<Path> paths = Files.walk(rootPath, 1)) {
            paths.filter(Files::isDirectory).filter(isDirPredicate.and(isNotTheSameDir)).forEach(pathItem -> {
                var folder = pathItem.getFileName().toString();
                if (currentLevel == item.level) {
                    items.add(folder + " (" + item.rootFolder + ")");
                }
                readFolderContentRecursively(item.next(folder), items, currentLevel + 1);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
