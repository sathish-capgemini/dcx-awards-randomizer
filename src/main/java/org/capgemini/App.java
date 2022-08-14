package org.capgemini;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Sathish Balan
 */
public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("Only one argument of the file path is allowed");
        }
        String filePath = args[0];
        try {
            List<String> nominees = new ArrayList<>();
            FileInputStream file = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            StreamSupport.stream(sheet.spliterator(), false)
                    .skip(1)
                    .forEach(row -> {
                        LocalDateTime completionDate = row.getCell(2).getLocalDateTimeCellValue();
                        LocalDateTime dateNow = LocalDateTime.now();
                        int completionMonth = completionDate.getMonthValue();
                        int currentMonth = dateNow.getMonthValue();
                        if (currentMonth - completionMonth == 1) {
                            Cell cell = row.getCell(6);
                            nominees.add(cell.getStringCellValue());
                        }
                    });
            List<String> distinctNominees = nominees
                    .stream()
                    .distinct()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
            Random random = new Random();
            String winner = capitalize(distinctNominees.get(random.nextInt(distinctNominees.size())));
            System.out.println("The random winner is: " + winner);
        } catch (FileNotFoundException e) {
            System.out.println("File Path incorrect, try again with proper full file path!");
        } catch (IOException e) {
            System.out.println("File Path is incorrect or File is corrupt, try again with proper full file path!");
        }
    }

    private static String capitalize(String string) {
        String[] words = string.split(" ");
        StringJoiner joiner = new StringJoiner(" ");
        for (String word : words) {
            joiner.add(StringUtils.capitalize(word));
        }
        return joiner.toString();
    }
}
