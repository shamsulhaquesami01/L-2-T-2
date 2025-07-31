import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerMatchHistoryManager {
    private static final String FILE_NAME = "match_history.txt";
    public static void savePlayerRecord(String playerName, String opponentName, String winner, String outcomeType) {
        MatchRecord record = new MatchRecord(playerName, opponentName, winner, outcomeType);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(record.toFileString());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving match record: " + e.getMessage());
            AlertUtil.showAlert("Save Error", "Could not save match record: " + e.getMessage());
        }
    }
    public static List<MatchRecord> loadPlayerRecords(String playerName) {
        List<MatchRecord> allRecords = loadAllRecords();
        List<MatchRecord> playerRecords = allRecords.stream()
                .filter(record -> record.getPlayer1Name().equals(playerName) ||
                        record.getPlayer2Name().equals(playerName))
                .collect(Collectors.toList());
        Collections.sort(playerRecords, (r1, r2) -> r2.getDateTime().compareTo(r1.getDateTime()));

        return playerRecords;
    }
    public static List<MatchRecord> loadAllRecords() {
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

        return records;
    }
    public static PlayerStats getPlayerStats(String playerName) {
        List<MatchRecord> playerRecords = loadPlayerRecords(playerName);

        int wins = 0;
        int losses = 0;
        int draws = 0;

        for (MatchRecord record : playerRecords) {
            String winner = record.getWinnerName();
            if (winner.equals("Draw") || winner.equals("DRAW")) {
                draws++;
            } else if (winner.equals(playerName)) {
                wins++;
            } else {
                losses++;
            }
        }

        return new PlayerStats(wins, losses, draws, playerRecords.size());
    }
    public static class PlayerStats {
        private final int wins;
        private final int losses;
        private final int draws;
        private final int totalGames;

        public PlayerStats(int wins, int losses, int draws, int totalGames) {
            this.wins = wins;
            this.losses = losses;
            this.draws = draws;
            this.totalGames = totalGames;
        }

        public int getWins() { return wins; }
        public int getLosses() { return losses; }
        public int getDraws() { return draws; }
        public int getTotalGames() { return totalGames; }

        public double getWinRate() {
            return totalGames > 0 ? (double) wins / totalGames * 100 : 0;
        }

        @Override
        public String toString() {
            return String.format("Games: %d | Wins: %d | Losses: %d | Draws: %d | Win Rate: %.1f%%",
                    totalGames, wins, losses, draws, getWinRate());
        }
    }
}