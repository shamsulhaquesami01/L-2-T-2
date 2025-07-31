import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MatchRecord {
    private String player1Name;
    private String player2Name;
    private String winnerName;
    private String outcomeType;
    private LocalDateTime dateTime;
    public MatchRecord(String player1Name, String player2Name, String winnerName, String outcomeType) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.winnerName = winnerName;
        this.outcomeType = outcomeType;
        this.dateTime = LocalDateTime.now();
    }

    public MatchRecord(String recordString) {
        String[] parts = recordString.split(",");
        if (parts.length == 5) {
            this.player1Name = parts[0];
            this.player2Name = parts[1];
            this.winnerName = parts[2];
            this.outcomeType = parts[3];
            this.dateTime = LocalDateTime.parse(parts[4], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else {
            throw new IllegalArgumentException("Invalid match record string format: " + recordString);
        }
    }

    // Getters
    public String getPlayer1Name() {
        return player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public String getOutcomeType() {
        return outcomeType;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public String toFileString() {
        return String.join(",",
                player1Name,
                player2Name,
                winnerName,
                outcomeType,
                dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
    public String getOpponentName(String playerName) {
        if (player1Name.equals(playerName)) {
            return player2Name;
        } else if (player2Name.equals(playerName)) {
            return player1Name;
        }
        return null;
    }
    public boolean didPlayerWin(String playerName) {
        return winnerName.equals(playerName);
    }

    public boolean isDraw() {
        return winnerName.equals("Draw") || winnerName.equals("DRAW");
    }

    @Override
    public String toString() {
        String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String result = winnerName.equals("Draw") ? "Draw" : winnerName + " won";
        return String.format("[%s] %s vs. %s - %s (%s)",
                formattedDate, player1Name, player2Name, result, outcomeType);
    }
}