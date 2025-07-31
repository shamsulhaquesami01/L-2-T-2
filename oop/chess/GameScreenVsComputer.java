import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GameScreenVsComputer {
    private final AudioClip clickSound = new AudioClip(getClass().getResource("/sound/click.wav").toString());
    private final AudioClip moveSound = new AudioClip(getClass().getResource("/sound/move.wav").toString());
    private Scene scene;
    private ChessGame logic;
    private Tile[][] tiles = new Tile[8][8];
    private Tile selectedTile = null;
    private Label turnLabel = new Label();
    private Label statusLabel = new Label();
    private Label gameStatusLabel = new Label();
    private GameMode gameMode;
    private boolean gameOver = false;
    private ComputerDifficulty difficulty;
    private int promotion=0;


    private VBox playerTopPanel;
    private VBox playerBottomPanel;
    private Label topPlayerLabel;
    private Label bottomPlayerLabel;
    private Label topPlayerStatus;
    private Label bottomPlayerStatus;
    private ImageView topPlayerImage;
    private ImageView bottomPlayerImage;
    private String name;

    private VBox moveHistoryBox;
    private ScrollPane moveHistoryScrollPane;
    private List<String> moveHistory = new ArrayList<>();
    private int moveNumber = 1;

    public GameScreenVsComputer(GameMode mode, ComputerDifficulty difficulty, String name) {
        this.gameMode = mode;
        this.name = name;
        this.difficulty = difficulty;
        this.logic = new ChessGame(mode);

        if (difficulty == ComputerDifficulty.ADVANCED) {
            ChessAI.MAX_TIME_MS = 50000;
        } else if (difficulty == ComputerDifficulty.INTERMEDIATE) {
            ChessAI.MAX_TIME_MS = 35000;
        } else {
            ChessAI.MAX_TIME_MS = 13000;
        }

        GridPane board = new GridPane();
        board.setAlignment(Pos.CENTER);
        initializeBoardUI(board);
        setupUIComponents(board);
        updateGameStatus();
    }

    private void initializeBoardUI(GridPane board) {
        Piece[][] gameBoard = logic.getBoard();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                tiles[row][col] = new Tile(row, col, 70, this::handleTileClickInternal);
                board.add(tiles[row][col], col, row);

                if (gameBoard[row][col] != null) {
                    tiles[row][col].setPiece(gameBoard[row][col].getImage());
                }
            }
        }
    }

    private void createMoveHistoryPanel() {
        moveHistoryBox = new VBox(2);
        moveHistoryBox.getStyleClass().add("move-history-box");

        moveHistoryScrollPane = new ScrollPane(moveHistoryBox);
        moveHistoryScrollPane.getStyleClass().add("move-history-scroll");
        moveHistoryScrollPane.setFitToWidth(true);
        moveHistoryScrollPane.setMaxHeight(200);
        moveHistoryScrollPane.setMinHeight(200);
        moveHistoryScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        moveHistoryScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }


    private void addMoveToHistory(int srcRow, int srcCol, int destRow, int destCol, boolean isWhiteMove) {
        String move = convertToChessNotation(srcRow, srcCol, destRow, destCol);

        if (isWhiteMove) {
            moveHistory.add(moveNumber + ". " + move);
        } else {

            if (!moveHistory.isEmpty()) {
                String lastMove = moveHistory.get(moveHistory.size() - 1);
                moveHistory.set(moveHistory.size() - 1, lastMove + " " + move);
                moveNumber++;
            }
        }

        updateMoveHistoryDisplay();
    }


    private void updateMoveHistoryDisplay() {
        Platform.runLater(() -> {
            moveHistoryBox.getChildren().clear();

            Label historyTitle = new Label("Move History");
            historyTitle.getStyleClass().add("move-history-title");
            moveHistoryBox.getChildren().add(historyTitle);

            for (String move : moveHistory) {
                Label moveLabel = new Label(move);
                moveLabel.getStyleClass().add("move-history-item");
                moveHistoryBox.getChildren().add(moveLabel);
            }


            moveHistoryScrollPane.setVvalue(1.0);
        });
    }

    private String convertToChessNotation(int srcRow, int srcCol, int destRow, int destCol) {
        char srcFile = (char) ('a' + srcCol);
        char destFile = (char) ('a' + destCol);
        int srcRank = 8 - srcRow;
        int destRank = 8 - destRow;

        return srcFile + "" + srcRank + "-" + destFile + "" + destRank;
    }

    private void setupUIComponents(GridPane board) {
        BorderPane gameContent = new BorderPane();
        gameContent.getStyleClass().add("root");
        createPlayerPanels();
        createMoveHistoryPanel();
        VBox rightPanel = new VBox(20);
        rightPanel.setPadding(new Insets(20, 20, 20, 20));
        rightPanel.setAlignment(Pos.CENTER_LEFT);
        rightPanel.getStyleClass().add("game-panel");

        statusLabel.getStyleClass().addAll("game-label", "status-label");
        gameStatusLabel.getStyleClass().addAll("game-label", "game-status-label");


        Button returnToMenuButton = new Button("❌ Exit");
        returnToMenuButton.setOnAction(e -> {
            ConnectionScreen menu = new ConnectionScreen(name);
            Main.instance.setScene(menu.getScene());
        });
        returnToMenuButton.getStyleClass().add("exit-button");


        rightPanel.getChildren().addAll(statusLabel, gameStatusLabel, moveHistoryScrollPane, returnToMenuButton);
        rightPanel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-background-radius: 10px;");


        VBox boardContainer = new VBox(10);
        boardContainer.setAlignment(Pos.CENTER);
        boardContainer.getChildren().addAll(playerTopPanel, board, playerBottomPanel);


        gameContent.setCenter(boardContainer);
        gameContent.setRight(rightPanel);

        StackPane root = new StackPane(gameContent);
        scene = new Scene(root, 1300, 720); // Increased width to accommodate move history
        scene.getStylesheets().add("file:resources/css/style_ingame.css");
    }

    private void createPlayerPanels() {

        try {

            Image aiImage = new Image(getClass().getResourceAsStream("/img/profile_ai.png"));
            topPlayerImage = new ImageView(aiImage);
            topPlayerImage.setFitWidth(40);
            topPlayerImage.setFitHeight(40);
            topPlayerImage.setPreserveRatio(true);
        } catch (Exception e) {

            topPlayerImage = new ImageView();
            topPlayerImage.setFitWidth(40);
            topPlayerImage.setFitHeight(40);
            topPlayerImage.setStyle("-fx-background-color: #444; -fx-border-color: #666;");
        }

        try {

            Image humanImage = new Image(getClass().getResourceAsStream("/img/profile_you.png"));
            bottomPlayerImage = new ImageView(humanImage);
            bottomPlayerImage.setFitWidth(40);
            bottomPlayerImage.setFitHeight(40);
            bottomPlayerImage.setPreserveRatio(true);
        } catch (Exception e) {

            bottomPlayerImage = new ImageView();
            bottomPlayerImage.setFitWidth(40);
            bottomPlayerImage.setFitHeight(40);
            bottomPlayerImage.setStyle("-fx-background-color: #444; -fx-border-color: #666;");
        }


        topPlayerLabel = new Label("Black (AI)");
        topPlayerLabel.getStyleClass().add("player-name");

        topPlayerStatus = new Label("Thinking...");
        topPlayerStatus.getStyleClass().add("move-status");

        bottomPlayerLabel = new Label(name);
        bottomPlayerLabel.getStyleClass().add("player-name");

        bottomPlayerStatus = new Label("Your Turn");
        bottomPlayerStatus.getStyleClass().add("move-status");


        HBox topPlayerInfo = new HBox(10);
        topPlayerInfo.setAlignment(Pos.CENTER_LEFT);
        VBox topPlayerText = new VBox(2);
        topPlayerText.getChildren().addAll(topPlayerLabel, topPlayerStatus);
        topPlayerInfo.getChildren().addAll(topPlayerImage, topPlayerText);

        playerTopPanel = new VBox();
        playerTopPanel.getChildren().add(topPlayerInfo);
        playerTopPanel.getStyleClass().add("player-panel");
        playerTopPanel.setAlignment(Pos.CENTER_LEFT);


        HBox bottomPlayerInfo = new HBox(10);
        bottomPlayerInfo.setAlignment(Pos.CENTER_LEFT);
        VBox bottomPlayerText = new VBox(2);
        bottomPlayerText.getChildren().addAll(bottomPlayerLabel, bottomPlayerStatus);
        bottomPlayerInfo.getChildren().addAll(bottomPlayerImage, bottomPlayerText);

        playerBottomPanel = new VBox();
        playerBottomPanel.getChildren().add(bottomPlayerInfo);
        playerBottomPanel.getStyleClass().add("player-panel");
        playerBottomPanel.setAlignment(Pos.CENTER_LEFT);
    }

    private void handleTileClickInternal(Tile clickedTile) {
        if (gameOver || logic.getCurrentTurn() != PieceColor.WHITE) return;

        int clickedRow = clickedTile.getRow();
        int clickedCol = clickedTile.getCol();
        Piece clickedPiece = logic.getBoard()[clickedRow][clickedCol];

        if (selectedTile == null) {
            if (clickedPiece != null && clickedPiece.getColor() == PieceColor.WHITE) {
                selectedTile = clickedTile;
                highlightValidMoves(clickedTile.getRow(), clickedTile.getCol());
            }
            clickSound.play();
        } else {
            if (clickedTile == selectedTile) {
                // Clicked the same tile again - deselect
                clearHighlights();
                selectedTile = null;
                return;
            }
            int srcRow = selectedTile.getRow();
            int srcCol = selectedTile.getCol();
            int destRow = clickedRow;
            int destCol = clickedCol;

            if (logic.movePiece(srcRow, srcCol, destRow, destCol, 0)) {
                Piece moved = logic.getBoard()[destRow][destCol];
                if (moved instanceof Pawn && (destRow == 0)) {
                    showPromotionChooser(srcRow, srcCol, destRow, destCol, moved.getColor());
                    return;
                }
                addMoveToHistory(srcRow, srcCol, destRow, destCol, true);

                clearHighlights();
                updateBoardUI();
                moveSound.play();
                updateGameStatus();
                selectedTile = null;

                if (!gameOver) {
                    new Thread(this::makeAIMove).start();
                }
            } else {
                clearHighlights();
                if (clickedPiece != null && clickedPiece.getColor() == PieceColor.WHITE) {
                    selectedTile = clickedTile;
                    highlightValidMoves(clickedTile.getRow(), clickedTile.getCol());
                } else {
                    selectedTile = null;
                }
            }
        }
    }

    private void PromotionAI() {
        updateAIpawn(logic);
    }

    private void updateAIpawn(ChessGame logic) {
        for (int i = 0; i < 8; i++) {
            if (logic.getBoard()[7][i] instanceof Pawn) {
                logic.getBoard()[7][i] = null;
                Piece newPiece = ChessAI.consultAboutNewPiece(logic, 7, i);
                logic.getBoard()[7][i] = newPiece;

                int finalI = i;
                Platform.runLater(() -> {
                    if (tiles[7][finalI] != null) {
                        tiles[7][finalI].setPiece(null);
                        tiles[7][finalI].setPiece(newPiece.getImage());
                    } else {
                        System.out.println("Tile at [7][" + finalI + "] is null!");
                    }
                });
            }
        }
    }

    private void showPromotionChooser(int srcRow, int srcCol, int row, int col, PieceColor color) {
        StackPane glass = new StackPane();
        glass.setStyle("-fx-background-color: rgba(0,0,0,0.6);");

        glass.setPrefSize(scene.getWidth(), scene.getHeight());
        glass.setMinSize(scene.getWidth(), scene.getHeight());
        glass.setMaxSize(scene.getWidth(), scene.getHeight());

        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: white; -fx-padding: 30; "
                + "-fx-border-color: #333; -fx-border-width: 3; "
                + "-fx-border-radius: 10; -fx-background-radius: 10; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 0);");

        Label titleLabel = new Label("Promote Pawn to:");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Button q = new Button("♛ Queen");
        Button r = new Button("♜ Rook");
        Button b = new Button("♝ Bishop");
        Button k = new Button("♞ Knight");

        Stream.of(q, r, b, k).forEach(btn -> {
            btn.setMaxWidth(200);
            btn.setMinWidth(200);
            btn.setPrefHeight(40);
            btn.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");
            btn.setOnAction(e -> {
                promotePawn(srcRow, srcCol, row, col, color, btn.getText().substring(2));
                ((StackPane) scene.getRoot()).getChildren().remove(glass);
            });
        });

        box.getChildren().addAll(titleLabel, q, r, b, k);
        glass.getChildren().add(box);
        ((StackPane) scene.getRoot()).getChildren().add(glass);
    }

    private void promotePawn(int srcRow, int srcCol, int row, int col, PieceColor color, String choice) {
        Piece newPiece;
        switch (choice) {
            case "Rook" -> {
                newPiece = new Rook(color == PieceColor.WHITE);
                promotion = 2;
            }
            case "Bishop" -> {
                promotion = 3;
                newPiece = new Bishop(color == PieceColor.WHITE);
            }
            case "Knight" -> {
                promotion = 4;
                newPiece = new Knight(color == PieceColor.WHITE);
            }
            default -> {
                promotion = 1;
                newPiece = new Queen(color == PieceColor.WHITE);
            }
        }
        logic.getBoard()[srcRow][srcCol] = null;
        logic.getBoard()[row][col] = newPiece;
        tiles[srcRow][srcCol].setPiece(null);
        tiles[row][col].setPiece(newPiece.getImage());


        addMoveToHistory(srcRow, srcCol, row, col, true);

        clearHighlights();
        selectedTile = null;
        updateGameStatus();
        if (!gameOver) {
            new Thread(this::makeAIMove).start();
        }
    }

    private void makeAIMove() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }

        Move bestMove;
        if (difficulty == ComputerDifficulty.ADVANCED) {
            bestMove = ChessAI.findBestMove(logic, 7);
        } else if (difficulty == ComputerDifficulty.INTERMEDIATE) {
            bestMove = ChessAI.findBestMove(logic, 5);
        } else {
            bestMove = ChessAI.findBestMove(logic, 3);
        }

        if (bestMove != null) {
            int srcRow = bestMove.srcRow;
            int srcCol = bestMove.srcCol;
            int destRow = bestMove.destRow;
            int destCol = bestMove.destCol;

            logic.movePiece(srcRow, srcCol, destRow, destCol, 0);

            Platform.runLater(() -> {
                addMoveToHistory(srcRow, srcCol, destRow, destCol, false);
                updateBoardUI();
                moveSound.play();
                updateGameStatus();
            });
        }
        System.out.println("Best given");

        for (int i = 0; i <= 7; i++) {
            if (logic.getBoard()[7][i] != null)
                System.out.println(logic.getBoard()[7][i].getClass().getSimpleName() + logic.getBoard()[7][i].getColor());
            if (logic.getBoard()[7][i] instanceof Pawn) {
                System.out.println("In Gamescreen");
                PromotionAI();
            }
        }
    }

    private void highlightValidMoves(int srcRow, int srcCol) {
        clearHighlights();
        Piece piece = logic.getBoard()[srcRow][srcCol];
        if (piece == null) return;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (piece.isValidMove(srcRow, srcCol, r, c, logic.getBoard())) {
                    Piece destPiece = logic.getBoard()[r][c];
                    if (destPiece == null || destPiece.getColor() != piece.getColor()) {
                        if (!logic.wouldMoveResultInCheck(srcRow, srcCol, r, c, piece.getColor())) {
                            tiles[r][c].highlight();
                        }
                    }
                }
            }
        }
    }

    private void clearHighlights() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                tiles[r][c].removeHighlight();
            }
        }
    }

    private void updateBoardUI() {
        Piece[][] gameBoard = logic.getBoard();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                tiles[r][c].setPiece(null);
                if (gameBoard[r][c] != null) {
                    tiles[r][c].setPiece(gameBoard[r][c].getImage());
                }
            }
        }
    }

    private void updateGameStatus() {
        GameStatus status = logic.getGameStatus();
        statusLabel.setText("Game Status: " + status.toString());

        if (status == GameStatus.CHECKMATE || status == GameStatus.STALEMATE) {
            gameOver = true;
            if (status == GameStatus.CHECKMATE) {
                String winner = logic.getCurrentTurn() == PieceColor.WHITE ? "Black (AI)" : name;
                gameStatusLabel.setText(winner + " wins by Checkmate!");
                gameStatusLabel.getStyleClass().add("checkmate");
            } else {
                gameStatusLabel.setText("Stalemate (Draw)");
                gameStatusLabel.getStyleClass().add("stalemate");
            }

            // Update player status to show game over
            topPlayerStatus.setText("Game Over");
            bottomPlayerStatus.setText("Game Over");
        } else if (status == GameStatus.CHECK) {
            gameStatusLabel.setText("CHECK!");
            gameStatusLabel.getStyleClass().add("check");
        } else {
            gameStatusLabel.setText("");
        }
        if (!gameOver) {
            if (logic.getCurrentTurn() == PieceColor.WHITE) {
                bottomPlayerStatus.setText("Your Turn");
                topPlayerStatus.setText("Waiting...");
                bottomPlayerStatus.setStyle("-fx-text-fill: #00ff00; -fx-font-weight: bold;");
                topPlayerStatus.setStyle("-fx-text-fill: #9fd0ff; -fx-font-weight: normal;");
            } else {
                bottomPlayerStatus.setText("Waiting...");
                topPlayerStatus.setText("AI Thinking...");
                bottomPlayerStatus.setStyle("-fx-text-fill: #9fd0ff; -fx-font-weight: normal;");
                topPlayerStatus.setStyle("-fx-text-fill: #00ff00; -fx-font-weight: bold;");
            }
        }
    }

    public Scene getScene() {
        return scene;
    }
}