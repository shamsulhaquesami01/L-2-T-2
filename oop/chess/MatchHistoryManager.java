
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchHistoryManager {
    private static final String FILE_NAME = "match_history.txt";
    public static void saveRecord(MatchRecord record) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(record.toFileString());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving match record: " + e.getMessage());
            AlertUtil.showAlert("Save Error", "Could not save match record: " + e.getMessage());
        }
    }

    public static List<MatchRecord> loadRecords() {
        List<MatchRecord> records = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return records;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    records.add(new MatchRecord(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping malformed record: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading match records: " + e.getMessage());
            AlertUtil.showAlert("Load Error", "Could not load match history: " + e.getMessage());
        }
        Collections.sort(records, (r1, r2) -> r2.getDateTime().compareTo(r1.getDateTime()));
        return records;
    }
}