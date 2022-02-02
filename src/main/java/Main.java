import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Main {
    private static final String JSON_FILE_NAME = "data.json";
    private static final String CSV_FILE_NAME = "data.csv";

    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        List<Employee> list = parseCSV(columnMapping, CSV_FILE_NAME);
        String json = listToJson(list);
        System.out.println(json);
        createJsonFile(json, JSON_FILE_NAME);
    }

    private static List<Employee> parseCSV(String[] column, String fileName) {
        List<Employee> result = null;
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(column);
            CsvToBean<Employee> csvData = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            result = csvData.parse();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(list, listType);
    }

    private static void createJsonFile(String data, String fileOutputName) {
        try (FileWriter fileWriter = new FileWriter(fileOutputName)) {
            fileWriter.write(data);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
