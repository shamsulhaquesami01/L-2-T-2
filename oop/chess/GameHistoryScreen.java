import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class GameHistoryScreen {
    private Scene scene;
    private String playerName;

    public GameHistoryScreen(String playerName) {
        this.playerName = playerName;
        initializeUI();
    }

    private void initializeUI() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");
        VBox header = createHeader();
        root.setTop(header);
        ScrollPane historyContent = createHistoryContent();
        root.setCenter(historyContent);
        HBox bottomSection = createBottomSection();
        root.setBottom(bottomSection);

        scene = new Scene(root, 720, 720);
        scene.getStylesheets().add("file:resources/css/style_history.css");
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.getStyleClass().add("history-header");

        Label titleLabel = new Label("🏆 Game History");
        titleLabel.getStyleClass().add("history-title");

        Label playerLabel = new Label("Player: " + playerName);
        playerLabel.getStyleClass().add("history-subtitle");

        header.getChildren().addAll(titleLabel, playerLabel);
        return header;
    }

    private ScrollPane createHistoryContent() {
        VBox historyBox = new VBox(10);
        historyBox.setPadding(new Insets(20));
        historyBox.getStyleClass().add("history-content");
        List<MatchRecord> records = PlayerMatchHistoryManager.loadPlayerRecords(playerName);

        if (records.isEmpty()) {
            Label noRecordsLabel = new Label("No game history found.");
            noRecordsLabel.getStyleClass().add("no-records-label");
            historyBox.getChildren().add(noRecordsLabel);
        } else {
            VBox statsSection = createStatsSection(records);
            historyBox.getChildren().add(statsSection);
            Separator separator = new Separator();
            separator.getStyleClass().add("history-separator");
            historyBox.getChildren().add(separator);
            Label matchesLabel = new Label("Recent Matches:");
            matchesLabel.getStyleClass().add("matches-header");
            historyBox.getChildren().add(matchesLabel);

            for (MatchRecord record : records) {
                VBox matchCard = createMatchCard(record);
                historyBox.getChildren().add(matchCard);
            }
        }

        ScrollPane scrollPane = new ScrollPane(historyBox);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("history-scroll");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        return scrollPane;
    }

    private VBox createStatsSection(List<MatchRecord> records) {
        VBox statsBox = new VBox(10);
        statsBox.getStyleClass().add("stats-section");

        Label statsLabel = new Label("📈 Statistics");
        statsLabel.getStyleClass().add("stats-title");
        int totalGames = records.size();
        int wins = 0;
        int losses = 0;
        int draws = 0;

        for (MatchRecord record : records) {
            String winner = record.getWinnerName();
            if (winner.equals("Draw") || winner.equals("DRAW")) {
                draws++;
            } else if (winner.equals(playerName)) {
                wins++;
            } else {
                losses++;
            }
        }
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(10);
        statsGrid.setAlignment(Pos.CENTER);
        statsGrid.getStyleClass().add("stats-grid");
        Label totalLabel = new Label("Total Games");
        totalLabel.getStyleClass().add("stat-label");
        Label totalValue = new Label(String.valueOf(totalGames));
        totalValue.getStyleClass().add("stat-value");

        // Wins
        Label winsLabel = new Label("Wins");
        winsLabel.getStyleClass().add("stat-label");
        Label winsValue = new Label(String.valueOf(wins));
        winsValue.getStyleClass().addAll("stat-value", "wins");

        // Losses
        Label lossesLabel = new Label("Losses");
        lossesLabel.getStyleClass().add("stat-label");
        Label lossesValue = new Label(String.valueOf(losses));
        lossesValue.getStyleClass().addAll("stat-value", "losses");

        // Draws
        Label drawsLabel = new Label("Draws");
        drawsLabel.getStyleClass().add("stat-label");
        Label drawsValue = new Label(String.valueOf(draws));
        drawsValue.getStyleClass().addAll("stat-value", "draws");

        // Win Rate
        Label winRateLabel = new Label("Win Rate");
        winRateLabel.getStyleClass().add("stat-label");
        double winRate = totalGames > 0 ? (double) wins / totalGames * 100 : 0;
        Label winRateValue = new Label(String.format("%.1f%%", winRate));
        winRateValue.getStyleClass().addAll("stat-value", "win-rate");

        // Add to grid
        statsGrid.add(totalLabel, 0, 0);
        statsGrid.add(totalValue, 0, 1);
        statsGrid.add(winsLabel, 1, 0);
        statsGrid.add(winsValue, 1, 1);
        statsGrid.add(lossesLabel, 2, 0);
        statsGrid.add(lossesValue, 2, 1);
        statsGrid.add(drawsLabel, 3, 0);
        statsGrid.add(drawsValue, 3, 1);
        statsGrid.add(winRateLabel, 4, 0);
        statsGrid.add(winRateValue, 4, 1);

        statsBox.getChildren().addAll(statsLabel, statsGrid);
        return statsBox;
    }

    private VBox createMatchCard(MatchRecord record) {
        VBox matchCard = new VBox(5);
        matchCard.getStyleClass().add("match-card");

        // Match Date and Time
        Label dateLabel = new Label(record.getDateTime().format(
                java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy - HH:mm")));
        dateLabel.getStyleClass().add("match-date");

        // Players
        String opponent = record.getPlayer1Name().equals(playerName) ?
                record.getPlayer2Name() : record.getPlayer1Name();
        Label playersLabel = new Label(playerName + " vs " + opponent);
        playersLabel.getStyleClass().add("match-players");

        // Result
        String resultText = "";
        String resultClass = "";
        String winner = record.getWinnerName();

        if (winner.equals("Draw") || winner.equals("DRAW")) {
            resultText = "DRAW";
            resultClass = "result-draw";
        } else if (winner.equals(playerName)) {
            resultText = "WIN";
            resultClass = "result-win";
        } else {
            resultText = "LOSS";
            resultClass = "result-loss";
        }

        Label resultLabel = new Label(resultText);
        resultLabel.getStyleClass().addAll("match-result", resultClass);

        // Outcome details
        String outcomeText = getOutcomeDescription(record.getOutcomeType());
        Label outcomeLabel = new Label(outcomeText);
        outcomeLabel.getStyleClass().add("match-outcome");

        // Layout
        HBox resultBox = new HBox(10);
        resultBox.setAlignment(Pos.CENTER_LEFT);
        resultBox.getChildren().addAll(resultLabel, outcomeLabel);

        matchCard.getChildren().addAll(dateLabel, playersLabel, resultBox);
        return matchCard;
    }

    private String getOutcomeDescription(String outcomeType) {
        switch (outcomeType.toUpperCase()) {
            case "CHECKMATE":
                return "by Checkmate";
            case "STALEMATE":
                return "by Stalemate";
            case "TIME_OUT":
                return "by Timeout";
            case "OPPONENT_QUIT":
                return "by Opponent Quit";
            case "OPPONENT_RESIGN":
                return "by Resignation";
            case "DRAW_BY_AGREEMENT":
                return "by Agreement";
            case "RESIGN":
                return "by Resignation";
            case "QUIT":
                return "by Quit";
            default:
                return outcomeType;
        }
    }

    private HBox createBottomSection() {
        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(20));

        Button backButton = new Button("⬅️ Back to Menu");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> {
            MainMenu mainMenu = new MainMenu(playerName);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(mainMenu.getScene());
        });
        backButton.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                backButton.fire();
            }
        });

        bottomBox.getChildren().add(backButton);
        return bottomBox;
    }

    public Scene getScene() {
        return scene;
    }
}