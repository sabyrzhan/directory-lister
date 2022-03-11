package kz.sabyrzhan;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        int level = Integer.parseInt(args[0]);
        String path = args[1];
        String resultFile = "result.txt";
        var allFiles = new ArrayList<String>();
        readFolderContentRecursively(path, allFiles, 1, level);
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
        System.out.println("Finished!");
    }

    private static void readFolderContentRecursively(String path, List<String> items, int currentLevel, int targetLevel) {
        if (currentLevel > targetLevel) {
            return;
        }

        var rootPath = Paths.get(path);
        Predicate<Path> isDirPredicate = Files::isDirectory;
        Predicate<Path> isNotTheSameDir = pathItem -> !pathItem.equals(rootPath);

        try (Stream<Path> paths = Files.walk(rootPath, 1)) {
            paths.filter(Files::isDirectory).filter(isDirPredicate.and(isNotTheSameDir)).forEach(pathItem -> {
                var folder = pathItem.getFileName().toString();
                items.add(folder);
                readFolderContentRecursively(path + "/" + folder, items, currentLevel + 1, targetLevel);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
