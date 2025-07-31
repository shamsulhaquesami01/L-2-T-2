import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import javafx.scene.media.AudioClip;

public class GameScreen {
    private Scene scene;
    private ChessGame logic;
    private Tile[][] tiles = new Tile[8][8];
    private Tile selectedTile = null;
    private Label turnLabel = new Label();
    private Label statusLabel = new Label();
    private Label gameStatusLabel = new Label();
    private NetworkMain network;
    private List<Tile> highlightedTiles = new ArrayList<>();
    private boolean isBlackPlayer;
    private boolean gameOver = false;
    private GameMode gameMode;
    private int promotion=0;

    // --- Timer specific fields ---
    private Label whiteTimerLabel = new Label("White Time: --:--");
    private Label blackTimerLabel = new Label("Black Time: --:--");
    public static long whiteTimeRemainingMillis;
    public static long blackTimeRemainingMillis;
    private Timeline gameTimer;
    private final long INITIAL_TIME_SECONDS = 300;
    private String localPlayerName;
    private String opponentPlayerName;
    private final AudioClip clickSound = new AudioClip(getClass().getResource("/sound/click.wav").toString());
    private final AudioClip moveSound = new AudioClip(getClass().getResource("/sound/move.wav").toString());

    // Player panel components
    private VBox topPlayerPanel;
    private VBox bottomPlayerPanel;
    private Label topPlayerName;
    private Label topPlayerStatus;
    private Label bottomPlayerName;
    private Label bottomPlayerStatus;
    private ImageView topPlayerIcon;
    private ImageView bottomPlayerIcon;
    private String name;

    // Move history components
    private VBox moveHistoryBox;
    private ScrollPane moveHistoryScrollPane;
    private List<String> moveHistory = new ArrayList<>();
    private int moveNumber = 1;


    public GameScreen(NetworkMain net, GameMode mode, String name) {
        this.network = net;
        network.setGameScreen(this);
        this.gameMode = mode;
        this.name = name;
        this.logic = new ChessGame(mode);
        this.isBlackPlayer = (net.getMyColor() == PieceColor.BLACK);
        this.localPlayerName = name;
        this.opponentPlayerName = net.getOpponentName();

        logic.setCurrentTurn(PieceColor.WHITE);

        // Initialize board UI
        GridPane board = new GridPane();
        board.setAlignment(Pos.CENTER);
        initializeBoardUI(board);
        setupUIComponents(board);

        // Check initial game status
        updateGameStatus();
        startMessageListener();

        // --- Timer setup for Quick Chess ---
        if (gameMode == GameMode.QUICK_CHESS) {
            whiteTimeRemainingMillis = TimeUnit.SECONDS.toMillis(INITIAL_TIME_SECONDS);
            blackTimeRemainingMillis = TimeUnit.SECONDS.toMillis(INITIAL_TIME_SECONDS);
            updateTimerLabels(); // Initialize labels

            gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                if (!gameOver) {
                    if (logic.getCurrentTurn() == PieceColor.WHITE) {
                        whiteTimeRemainingMillis -= 1000;
                    } else {
                        blackTimeRemainingMillis -= 1000;
                    }
                    updateTimerLabels();

                    if (whiteTimeRemainingMillis <= 0) {
                        Platform.runLater(() -> handleTimeOut(PieceColor.WHITE));
                    } else if (blackTimeRemainingMillis <= 0) {
                        Platform.runLater(() -> handleTimeOut(PieceColor.BLACK));
                    }
                }
            }));
            gameTimer.setCycleCount(Animation.INDEFINITE);
            gameTimer.play(); // Start the timer immediately
            System.out.println("Quick Chess mode activated. Timer started.");
        }
    }

    private void initializeBoardUI(GridPane board) {
        Piece[][] gameBoard = logic.getBoard();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int displayRow = row;
                int displayCol = col;

                if (isBlackPlayer) {
                    displayRow = 7 - row;
                    displayCol = 7 - col;
                }

                tiles[row][col] = new Tile(row, col, 70, this::handleTileClickInternal);
                board.add(tiles[row][col], displayCol, displayRow);

                if (gameBoard[row][col] != null) {
                    tiles[row][col].setPiece(gameBoard[row][col].getImage());
                }
            }
        }
    }

    private void createPlayerPanels() {
        topPlayerIcon = new ImageView();
        bottomPlayerIcon = new ImageView();
        try {

            topPlayerIcon.setImage(new Image("file:resources/img/profile_you.png"));
            bottomPlayerIcon.setImage(new Image("file:resources/img/profile_you.png"));
            topPlayerIcon.setFitWidth(30);
            topPlayerIcon.setFitHeight(30);
            bottomPlayerIcon.setFitWidth(30);
            bottomPlayerIcon.setFitHeight(30);
            topPlayerIcon.setPreserveRatio(true);
            bottomPlayerIcon.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Player icons not found, using default styling");
        }

        topPlayerName = new Label();
        topPlayerStatus = new Label();
        bottomPlayerName = new Label();
        bottomPlayerStatus = new Label();


        topPlayerName.getStyleClass().add("player-name");
        topPlayerStatus.getStyleClass().add("move-status");
        bottomPlayerName.getStyleClass().add("player-name");
        bottomPlayerStatus.getStyleClass().add("move-status");


        topPlayerPanel = new VBox(5);
        topPlayerPanel.getStyleClass().add("player-panel");
        topPlayerPanel.setAlignment(Pos.TOP_LEFT);

        bottomPlayerPanel = new VBox(5);
        bottomPlayerPanel.getStyleClass().add("player-panel");
        bottomPlayerPanel.setAlignment(Pos.BOTTOM_LEFT);


        HBox topPlayerInfo = new HBox(8);
        topPlayerInfo.setAlignment(Pos.TOP_LEFT);
        topPlayerInfo.getChildren().addAll(topPlayerIcon, new VBox(2, topPlayerName, topPlayerStatus));
        topPlayerPanel.getChildren().add(topPlayerInfo);

        HBox bottomPlayerInfo = new HBox(8);
        bottomPlayerInfo.setAlignment(Pos.BOTTOM_LEFT);
        bottomPlayerInfo.getChildren().addAll(bottomPlayerIcon, new VBox(2, bottomPlayerName, bottomPlayerStatus));
        bottomPlayerPanel.getChildren().add(bottomPlayerInfo);


        if (isBlackPlayer) {
            // Black player
            topPlayerName.setText(opponentPlayerName + " (White)");
            bottomPlayerName.setText(localPlayerName + " (Black)");
        } else {
            // White player
            topPlayerName.setText(opponentPlayerName + " (Black)");
            bottomPlayerName.setText(localPlayerName + " (White)");
        }

        updatePlayerStatus();
    }

    private void updatePlayerStatus() {
        Platform.runLater(() -> {
            if (gameOver) {
                topPlayerStatus.setText("Game Over");
                bottomPlayerStatus.setText("Game Over");
                return;
            }

            boolean isMyTurn = network.isMyTurn();
            PieceColor currentTurn = logic.getCurrentTurn();

            if (isBlackPlayer) {
                // Black player
                if (currentTurn == PieceColor.WHITE) {
                    topPlayerStatus.setText(isMyTurn ? "Waiting..." : "Your Turn");
                    bottomPlayerStatus.setText(isMyTurn ? "Your Turn" : "Waiting...");
                } else {
                    topPlayerStatus.setText(isMyTurn ? "Waiting..." : "Your Turn");
                    bottomPlayerStatus.setText(isMyTurn ? "Your Turn" : "Waiting...");
                }
            } else {
                // White player
                if (currentTurn == PieceColor.WHITE) {
                    topPlayerStatus.setText(isMyTurn ? "Waiting..." : "Your Turn");
                    bottomPlayerStatus.setText(isMyTurn ? "Your Turn" : "Waiting...");
                } else {
                    topPlayerStatus.setText(isMyTurn ? "Waiting..." : "Your Turn");
                    bottomPlayerStatus.setText(isMyTurn ? "Your Turn" : "Waiting...");
                }
            }
        });
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


        StackPane boardContainer = new StackPane();
        boardContainer.getChildren().add(board);


        StackPane.setAlignment(topPlayerPanel, Pos.TOP_LEFT);
        StackPane.setAlignment(bottomPlayerPanel, Pos.BOTTOM_LEFT);
        StackPane.setMargin(topPlayerPanel, new Insets(10));
        StackPane.setMargin(bottomPlayerPanel, new Insets(10));



        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));
        rightPanel.setAlignment(Pos.CENTER_LEFT);
        rightPanel.getStyleClass().add("game-panel");
        boardContainer.getChildren().addAll(topPlayerPanel, bottomPlayerPanel);
        turnLabel.getStyleClass().addAll("game-label", "turn-label");
        statusLabel.getStyleClass().addAll("game-label", "status-label");


        whiteTimerLabel.getStyleClass().add("timer-label");
        blackTimerLabel.getStyleClass().add("timer-label");

        Button restartButton = new Button("Resign");
        restartButton.setOnAction(e -> restartGame());
        restartButton.getStyleClass().add("game-button");

        Button returnToMenuButton = new Button("Return to Menu");
        returnToMenuButton.setOnAction(e -> returnToMenu());
        returnToMenuButton.getStyleClass().add("game-button");

        Button drawButton=new Button("Offer Draw");
        drawButton.setOnAction(e->offerDraw());
        drawButton.getStyleClass().add("game-button");

        if (isBlackPlayer) {
            rightPanel.getChildren().addAll(blackTimerLabel, whiteTimerLabel, turnLabel, statusLabel,
                    moveHistoryScrollPane, restartButton, returnToMenuButton, drawButton);
        } else {
            rightPanel.getChildren().addAll(whiteTimerLabel, blackTimerLabel, turnLabel, statusLabel,
                    moveHistoryScrollPane, restartButton, returnToMenuButton, drawButton);
        }

        gameContent.setCenter(boardContainer);
        gameContent.setRight(rightPanel);
        StackPane root = new StackPane(gameContent);
        scene = new Scene(root, 1300, 720);
        scene.getStylesheets().add("file:resources/css/style_ingame.css");
    }

    private void showGameOverDialog(String title, String message, String styleClass) {
        Platform.runLater(() -> {
            StackPane overlay = new StackPane();
            overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
            overlay.setPrefSize(scene.getWidth(), scene.getHeight());

            VBox dialogBox = new VBox(20);
            dialogBox.setAlignment(Pos.CENTER);
            dialogBox.getStyleClass().add("game-over-dialog");
            dialogBox.setStyle("-fx-background-color: rgba(112, 78, 34, 0.85);" +
                    "-fx-padding: 40; " +
                    "-fx-border-color: #ecf0f1; " +
                    "-fx-border-width: 3; " +
                    "-fx-border-radius: 15; " +
                    "-fx-background-radius: 15; " +
                    " -fx-effect: dropshadow(gaussian, black, 2, 0.5, 0.0, 0.0);");

            Label iconLabel = new Label("♔");
            iconLabel.setStyle("-fx-font-size: 90px; -fx-text-fill: #000000; -fx-font-weight: bold;");

            // Title
            Label titleLabel = new Label(title);
            titleLabel.getStyleClass().add("game-over-title");
            titleLabel.setStyle("-fx-font-size: 28px;  -fx-font-family: 'Inter'; sans-serif;-fx-font-weight: bold; " +
                    "-fx-text-fill: #ecf0f1; -fx-text-alignment: center;");

            // Message
            Label messageLabel = new Label(message);
            messageLabel.getStyleClass().add("game-over-message");
            messageLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #ffffff; " +
                    "-fx-text-alignment: center; -fx-wrap-text: true;");

            // Buttons
            HBox buttonBox = new HBox(15);
            buttonBox.setAlignment(Pos.CENTER);

            Button newGameButton = new Button("New Game");
            Button mainMenuButton = new Button("Main Menu");

            Stream.of(newGameButton, mainMenuButton).forEach(btn -> {
                btn.setPrefWidth(130);
                btn.setPrefHeight(45);
                btn.setStyle("-fx-background-color: rgba(2, 8, 5, 0.7);"
                        + "-fx-text-fill: #ffffff;"
                        + "-fx-font-weight: bold;"
                        + "-fx-font-size: 14px;"
                        + "-fx-padding: 8 20;"
                        + "-fx-background-radius: 12;"
                        + "-fx-border-color: rgb(160, 122, 75);"
                        + "-fx-border-width: 1.2px;"
                        + "-fx-border-radius: 12;"
                        + "-fx-cursor: hand;"
                        + "-fx-pref-width: 130px;"
                        + "-fx-pref-height: 46px;"
                );

// HOVER
                btn.setOnMouseEntered(e -> btn.setStyle(
                        "-fx-background-color: rgba(160, 122, 75, 0.2);"
                                + "-fx-text-fill: white;"
                                + "-fx-font-weight: bold;"
                                + "-fx-font-size: 14px;"
                                + "-fx-padding: 8 20;"
                                + "-fx-background-radius: 12;"
                                + "-fx-border-color: white;"
                                + "-fx-border-width: 1.2px;"
                                + "-fx-border-radius: 12;"
                                + "-fx-cursor: hand;"
                                + "-fx-effect: dropshadow(gaussian, rgba(160, 122, 75, 0.8), 8, 0.8, 0, 2);"
                                + "-fx-pref-width: 130px;"
                                + "-fx-pref-height: 46px;"
                ));

// EXIT HOVER
                btn.setOnMouseExited(e -> btn.setStyle(
                        "-fx-background-color: rgba(2, 8, 5, 0.7);"
                                + "-fx-text-fill: #ffffff;"
                                + "-fx-font-weight: bold;"
                                + "-fx-font-size: 14px;"
                                + "-fx-padding: 8 20;"
                                + "-fx-background-radius: 12;"
                                + "-fx-border-color: rgb(160, 122, 75);"
                                + "-fx-border-width: 1.2px;"
                                + "-fx-border-radius: 12;"
                                + "-fx-cursor: hand;"
                                + "-fx-pref-width: 130px;"
                                + "-fx-pref-height: 46px;"
                ));

// PRESSED EFFECT (optional for mouse press)
                btn.setOnMousePressed(e -> btn.setStyle(
                        "-fx-background-color: #2E7D32;"
                                + "-fx-text-fill: #ffffff;"
                                + "-fx-font-weight: bold;"
                                + "-fx-font-size: 14px;"
                                + "-fx-padding: 8 20;"
                                + "-fx-background-radius: 12;"
                                + "-fx-border-color: white;"
                                + "-fx-border-width: 1.2px;"
                                + "-fx-border-radius: 12;"
                                + "-fx-cursor: hand;"
                                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 2, 0, 0, 1);"
                                + "-fx-scale-x: 0.98;"
                                + "-fx-scale-y: 0.98;"
                                + "-fx-pref-width: 130px;"
                                + "-fx-pref-height: 46px;"
                ));

// RELEASE restores to hover or normal (optional)
                btn.setOnMouseReleased(e -> btn.fireEvent(new javafx.scene.input.MouseEvent(
                        javafx.scene.input.MouseEvent.MOUSE_EXITED, 0, 0, 0, 0,
                        javafx.scene.input.MouseButton.PRIMARY, 1,
                        false, false, false, false, true,
                        false, false, true, false, false, null
                )));

            });

            newGameButton.setOnAction(e -> {
                ((StackPane) scene.getRoot()).getChildren().remove(overlay);
                restartGame();
            });

            mainMenuButton.setOnAction(e -> {
                ((StackPane) scene.getRoot()).getChildren().remove(overlay);
                returnToMenu();
            });

            buttonBox.getChildren().addAll(newGameButton, mainMenuButton);
            dialogBox.getChildren().addAll(iconLabel, titleLabel, messageLabel, buttonBox);
            overlay.getChildren().add(dialogBox);

            ((StackPane) scene.getRoot()).getChildren().add(overlay);
        });
    }

    void offerDraw() {
        try {
            System.out.println("Offering draw to opponent...");
            network.sendDrawOffer();
        } catch (IOException e) {
            Platform.runLater(() -> {
                AlertUtil.showAlert("Network Error", "Failed to send draw offer: " + e.getMessage());
            });
            e.printStackTrace();
        }
    }

    void showDrawBox() {
        Platform.runLater(() -> {
            StackPane overlay = new StackPane();
            overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
            overlay.setPrefSize(scene.getWidth(), scene.getHeight());

            VBox box = new VBox(15);
            box.setAlignment(Pos.CENTER);
            box.setStyle("-fx-background-color: white; -fx-padding: 30; "
                    + "-fx-border-color: #333; -fx-border-width: 3; "
                    + "-fx-border-radius: 10; -fx-background-radius: 10; "
                    + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 0);");

            Label prompt = new Label(opponentPlayerName + " offered a draw. Do you accept?");
            prompt.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");

            HBox buttonBox = new HBox(20);
            buttonBox.setAlignment(Pos.CENTER);

            Button yesButton = new Button("Accept");
            Button noButton = new Button("Decline");

            Stream.of(yesButton, noButton).forEach(btn -> {
                btn.setPrefWidth(100);
                btn.setPrefHeight(40);
                btn.setStyle("-fx-font-size: 14px;");
                btn.getStyleClass().add("menu-button");
            });

            yesButton.setOnAction(e -> {
                ((StackPane) scene.getRoot()).getChildren().remove(overlay);

                new Thread(() -> {
                    try {
                        System.out.println("Draw accepted by player");
                        network.sendDrawAcceptance();
                        Platform.runLater(() -> {
                            handleDrawAccepted();
                        });
                    } catch (Exception ex) {
                        Platform.runLater(() -> {
                            AlertUtil.showAlert("Network Error", "Failed to send draw acceptance: " + ex.getMessage());
                        });
                        ex.printStackTrace();
                    }
                }).start();
            });

            noButton.setOnAction(e -> {
                ((StackPane) scene.getRoot()).getChildren().remove(overlay);
                new Thread(() -> {
                    try {
                        System.out.println("Draw rejected by player");
                        network.sendDrawRejection();
                    } catch (IOException ex) {
                        Platform.runLater(() -> {
                            AlertUtil.showAlert("Network Error", "Failed to send draw rejection: " + ex.getMessage());
                        });
                        ex.printStackTrace();
                    }
                }).start();
            });

            buttonBox.getChildren().addAll(yesButton, noButton);
            box.getChildren().addAll(prompt, buttonBox);
            overlay.getChildren().add(box);

            ((StackPane) scene.getRoot()).getChildren().add(overlay);
        });
    }

    public void handleDrawAccepted() {
        Platform.runLater(() -> {
            gameOver = true;
            if (gameTimer != null) gameTimer.stop();

            statusLabel.setText("GAME_OVER: DRAW");
            turnLabel.setText("Game Over");
            saveMatchRecord(localPlayerName, opponentPlayerName, "Draw", "DRAW_BY_AGREEMENT");

            updatePlayerStatus();
            showGameOverDialog("Game Over", "Draw by mutual agreement", "draw");

            System.out.println("Draw accepted - game ended");
        });
    }

    public void handleDrawRejected() {
        Platform.runLater(() -> {
            System.out.println("Draw offer was rejected by " + opponentPlayerName);
        });
    }

    private void handleTileClickInternal(Tile clickedTile) {
        if (gameOver || !network.isMyTurn()) {
            return;
        }

        int clickedRow = clickedTile.getRow();
        int clickedCol = clickedTile.getCol();
        Piece clickedPiece = logic.getBoard()[clickedRow][clickedCol];

        if (selectedTile == null) {
            // No tile selected
            if (clickedPiece != null && clickedPiece.getColor() == network.getMyColor()) {
                selectedTile = clickedTile;
                highlightValidMoves(selectedTile.getRow(), selectedTile.getCol());
            }
            clickSound.play();
        } else {
            // A tile is already selected
            if (clickedTile == selectedTile) {
                clearHighlights();
                selectedTile = null;
                return;
            }

            int srcRow = selectedTile.getRow();
            int srcCol = selectedTile.getCol();
            int destRow = clickedRow;
            int destCol = clickedCol;
            if (logic.movePiece(srcRow, srcCol, destRow, destCol,0)) {
                Piece moved=logic.getBoard()[destRow][destCol];
                if(moved instanceof Pawn &&
                        (destRow==0 || destRow==7))
                {
                    showPromotionChooser(srcRow,srcCol,destRow,destCol,moved.getColor());
                    return;
                }
                boolean isWhiteMove = (logic.getCurrentTurn() == PieceColor.BLACK); // Current turn switches after move
                addMoveToHistory(srcRow, srcCol, destRow, destCol, isWhiteMove);

                clearHighlights();
                updateBoardUI();
                moveSound.play();
                network.setMyTurn(false);
                updateGameStatus();
                promotion=0;
                try {
                    network.sendMove(srcRow, srcCol, destRow, destCol,promotion);
                } catch (IOException e) {
                    AlertUtil.showAlert("Network Error", "Failed to send move: " + e.getMessage());
                    e.printStackTrace();
                }
                selectedTile = null;
            } else {
                clearHighlights();
                if (clickedPiece != null && clickedPiece.getColor() == network.getMyColor()) {
                    selectedTile = clickedTile;
                    highlightValidMoves(selectedTile.getRow(), selectedTile.getCol());
                } else {
                    selectedTile = null;
                }
            }
        }
    }

    private void showPromotionChooser(int srcRow,int srcCol,int row, int col, PieceColor color) {
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
                promotePawn(srcRow,srcCol,row, col, color, btn.getText().substring(2));
                ((StackPane) scene.getRoot()).getChildren().remove(glass);
            });
        });

        box.getChildren().addAll(titleLabel, q, r, b, k);
        glass.getChildren().add(box);
        ((StackPane) scene.getRoot()).getChildren().add(glass);
    }

    private void promotePawn(int srcRow,int srcCol,int row, int col, PieceColor color, String choice) {
        Piece newPiece;
        switch (choice) {
            case "Rook"   ->
            {
                newPiece = new Rook(color == PieceColor.WHITE);
                promotion=2;
            }
            case "Bishop" -> {
                promotion=3;
                newPiece = new Bishop(color == PieceColor.WHITE);
            }
            case "Knight" -> {
                promotion=4;
                newPiece = new Knight(color == PieceColor.WHITE);
            }
            default       -> {
                promotion=1;
                newPiece = new Queen(color == PieceColor.WHITE);
            }
        }
        logic.getBoard()[row][col] = newPiece;
        tiles[row][col].setPiece(newPiece.getImage());
        updateGameStatus();
        sendAndContinue(srcRow, srcCol, row, col, promotion);
    }

    private void sendAndContinue(int sr,int sc,int dr,int dc,int promo) {
        boolean isWhiteMove = (logic.getCurrentTurn() == PieceColor.BLACK);
        addMoveToHistory(sr, sc, dr, dc, isWhiteMove);

        clearHighlights();
        updateBoardUI();
        network.setMyTurn(false);
        updateGameStatus();
        try {
            network.sendMove(sr, sc, dr, dc, promo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        selectedTile = null;
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

    private void updateTurnLabel() {
        Platform.runLater(() -> {
            if (gameOver) {
                turnLabel.setText("Game Over");
                turnLabel.getStyleClass().setAll("game-label", "turn-label", "game-over");
                return;
            }

            if (network.isMyTurn()) {
                turnLabel.getStyleClass().setAll("game-label", "turn-label", "your-turn");
            } else {
                turnLabel.getStyleClass().setAll("game-label", "turn-label", "opponent-turn");
            }
        });
    }

    private void updateTimerLabels() {
        Platform.runLater(() -> {
            whiteTimerLabel.setText("White Time: " + formatTime(whiteTimeRemainingMillis));
            blackTimerLabel.setText("Black Time: " + formatTime(blackTimeRemainingMillis));
        });
    }

    private String formatTime(long millis) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        long minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void saveMatchRecord(String player1, String player2, String winner, String outcome) {
        PlayerMatchHistoryManager.savePlayerRecord(player1, player2, winner, outcome);
    }

    private void updateGameStatus() {
        Platform.runLater(() -> {
            GameStatus status = logic.getGameStatus();
            statusLabel.setText("Game Status: " + status.toString());

            if (status == GameStatus.CHECKMATE || status == GameStatus.STALEMATE) {
                gameOver = true;
                if (gameTimer != null) gameTimer.stop();

                String winner = "";
                String outcome = "";
                String dialogTitle = "Game Over";
                String dialogMessage = "";

                if (status == GameStatus.CHECKMATE) {
                    if (logic.getCurrentTurn() == PieceColor.WHITE) {
                        winner = isBlackPlayer ? localPlayerName : opponentPlayerName;
                    } else {
                        winner = isBlackPlayer ? opponentPlayerName : localPlayerName;
                    }
                    outcome = "CHECKMATE";
                    dialogMessage = winner + " wins by Checkmate!";
                } else {
                    winner = "Draw";
                    outcome = "STALEMATE";
                    dialogMessage = "Stalemate - It's a Draw!";
                }

                saveMatchRecord(localPlayerName, opponentPlayerName, winner, outcome);
                showGameOverDialog(dialogTitle, dialogMessage, outcome.toLowerCase());

                try {
                    network.sendGameOverNotification(status.toString(), logic.getCurrentTurn());
                } catch (IOException e) {
                    System.err.println("Failed to send game over notification: " + e.getMessage());
                }
            } else if (status == GameStatus.CHECK) {
                statusLabel.setText("Game Status: CHECK!");
            } else {
                statusLabel.setText("Game Status: " + status.toString());
            }
            updateTurnLabel();
            updatePlayerStatus();
        });
    }

    private void waitForOpponentMove() {
        new Thread(() -> {
            try {
                while (!gameOver) {
                    String messageType = network.receiveMessage();

                    if (messageType == null) {
                        Thread.sleep(100);
                        continue;
                    }

                    if (messageType.equals("MOVE")) {
                        if (!network.isMyTurn()) {
                            int[] move = network.getLastMove();
                            if (move != null) {
                                Platform.runLater(() -> {
                                    logic.movePiece(move[0], move[1], move[2], move[3], move[4]);

                                    // Add opponent's move to history
                                    boolean isWhiteMove = (logic.getCurrentTurn() == PieceColor.WHITE);
                                    addMoveToHistory(move[0], move[1], move[2], move[3], !isWhiteMove);

                                    updateBoardUI();
                                    moveSound.play();
                                    network.setMyTurn(true);
                                    updateGameStatus();
                                });
                                break;
                            }
                        }
                    } else if (messageType.equals("DRAW_OFFER")) {
                        Platform.runLater(() -> {
                            showDrawBox();
                        });
                    } else if (messageType.equals("DRAW_ACCEPT")) {
                        Platform.runLater(() -> {
                            handleDrawAccepted();
                        });
                        break;
                    } else if (messageType.equals("DRAW_REJECT")) {
                        Platform.runLater(() -> {
                            handleDrawRejected();
                        });
                    } else if (messageType.startsWith("GAME_OVER:")) {
                        String[] parts = messageType.split(":");
                        if (parts.length >= 2) {
                            Platform.runLater(() -> {
                                if (parts[1].equals("DRAW")) {
                                    handleDrawAccepted();
                                } else {
                                    handleGameOverNotification(parts[1], logic.getCurrentTurn());
                                }
                            });
                        }
                        break;
                    }
                }
            } catch (NetworkMain.GameOverException e) {
                Platform.runLater(() -> handleGameOverNotification(e.getGameOverType(), e.getLoser()));
            } catch (Exception e) {
                if (!gameOver) {
                    Platform.runLater(() -> {
                        AlertUtil.showAlert("Network Error", "Connection error: " + e.getMessage());
                    });
                    System.err.println("Error in waitForOpponentMove: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void startMessageListener() {
        new Thread(() -> {
            try {
                while (!gameOver) {
                    String messageType = network.receiveMessage();

                    if (messageType == null) {
                        Thread.sleep(100);
                        continue;
                    }
                    if (messageType.equals("DRAW_OFFER")) {
                        Platform.runLater(() -> {
                            showDrawBox();
                        });
                    } else if (messageType.equals("DRAW_ACCEPT")) {
                        Platform.runLater(() -> {
                            handleDrawAccepted();
                        });
                        break;
                    } else if (messageType.equals("DRAW_REJECT")) {
                        Platform.runLater(() -> {
                            handleDrawRejected();
                        });
                    } else if (messageType.equals("MOVE")) {
                        if (!network.isMyTurn()) {
                            int[] move = network.getLastMove();
                            if (move != null) {
                                Platform.runLater(() -> {
                                    logic.movePiece(move[0], move[1], move[2], move[3], move[4]);
                                    boolean isWhiteMove = (logic.getCurrentTurn() == PieceColor.WHITE);
                                    addMoveToHistory(move[0], move[1], move[2], move[3], !isWhiteMove);

                                    updateBoardUI();
                                    moveSound.play();
                                    network.setMyTurn(true);
                                    updateGameStatus();
                                });
                            }
                        }
                    } else if (messageType.startsWith("GAME_OVER:")) {
                        String[] parts = messageType.split(":");
                        if (parts.length >= 2) {
                            Platform.runLater(() -> {
                                if (parts[1].equals("DRAW")) {
                                    handleDrawAccepted();
                                } else {
                                    handleGameOverNotification(parts[1], logic.getCurrentTurn());
                                }
                            });
                        }
                        break;
                    }
                }
            } catch (NetworkMain.GameOverException e) {
                Platform.runLater(() -> handleGameOverNotification(e.getGameOverType(), e.getLoser()));
            } catch (Exception e) {
                if (!gameOver) {
                    Platform.runLater(() -> {
                        AlertUtil.showAlert("Network Error", "Connection error: " + e.getMessage());
                    });
                    System.err.println("Error in message listener: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void handleGameOverNotification(String type, PieceColor loser) {
        gameOver = true;
        if (gameTimer != null) gameTimer.stop();

        Platform.runLater(() -> {
            String winner = "";
            String outcome = type;
            String dialogTitle = "Game Over";
            String dialogMessage = "";

            if (type.equals("CHECKMATE")) {
                if(loser==PieceColor.WHITE){
                    winner = isBlackPlayer ? localPlayerName : opponentPlayerName;
                } else {
                    winner = isBlackPlayer ? opponentPlayerName : localPlayerName;
                }
                dialogMessage = winner + " wins by Checkmate!";
            } else if (type.equals("STALEMATE")) {
                winner = "Draw";
                dialogMessage = "Stalemate - It's a Draw!";
            } else if (type.equals("TIME_OUT")) {
                if(loser==PieceColor.WHITE){
                    winner = isBlackPlayer ? localPlayerName : opponentPlayerName;
                } else {
                    winner = isBlackPlayer ? opponentPlayerName : localPlayerName;
                }
                outcome = "TIME_OUT";
                dialogMessage = winner + " wins by Time Out!";
            } else if (type.equals("QUIT")) {
                if(loser==PieceColor.WHITE){
                    winner = isBlackPlayer ? localPlayerName : opponentPlayerName;
                } else {
                    winner = isBlackPlayer ? opponentPlayerName : localPlayerName;
                }
                outcome = "OPPONENT_QUIT";
                dialogMessage = winner + " wins by opponent quit!";
            }
            else if(type.equals("RESIGN"))
            {
                if(loser==PieceColor.WHITE){
                    winner = isBlackPlayer ? localPlayerName : opponentPlayerName;
                } else {
                    winner = isBlackPlayer ? opponentPlayerName : localPlayerName;
                }
                outcome = "OPPONENT_RESIGN";
                dialogMessage = winner+" wins by opponent Resign!";
            }
            saveMatchRecord(localPlayerName, opponentPlayerName, winner, outcome);

            showGameOverDialog(dialogTitle, dialogMessage, outcome.toLowerCase());

            updateTurnLabel();
            updatePlayerStatus();
        });
    }

    private void handleTimeOut(PieceColor timedOutPlayer) {
        if (gameOver) return;

        gameOver = true;
        if (gameTimer != null) gameTimer.stop();

        Platform.runLater(() -> {
            String winner;
            if(timedOutPlayer==PieceColor.WHITE){
                winner = isBlackPlayer ? localPlayerName : opponentPlayerName;
            } else {
                winner = isBlackPlayer ? opponentPlayerName : localPlayerName;
            }

            String dialogMessage = winner + " wins by Time Out!";
            statusLabel.setText("");
            turnLabel.setText("Game Ended");
            saveMatchRecord(localPlayerName, opponentPlayerName, winner, "TIME_OUT");
            showGameOverDialog("Game Over", dialogMessage, "timeout");

            try {
                network.sendGameOverNotification("TIME_OUT", timedOutPlayer);
            } catch (IOException e) {
                System.err.println("Failed to send time out notification: " + e.getMessage());
            }

            updatePlayerStatus();
        });
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
                            highlightedTiles.add(tiles[r][c]);
                        }
                    }
                }
            }
        }
    }

    private void clearHighlights() {
        for (Tile t : highlightedTiles) {
            t.removeHighlight();
        }
        highlightedTiles.clear();
    }

    private void restartGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        clearHighlights();
        selectedTile = null;

        if (!gameOver) {
            try {
                new Thread(()->
                {
                    try {
                        network.sendGameOverNotification("RESIGN", network.getMyColor());
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
                ).start();
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
        Platform.runLater(()->
        {
            ConnectionScreen connectionScreen = new ConnectionScreen(name);
            Main.instance.setScene(connectionScreen.getScene());
        });

    }

    private void returnToMenu() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        clearHighlights();
        selectedTile = null;

        if (!gameOver) {
            try {
                new Thread(()->
                {
                    try {
                        network.sendGameOverNotification("QUIT", network.getMyColor());
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
                ).start();
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
        Platform.runLater(()->
        {
            ConnectionScreen connectionScreen = new ConnectionScreen(name);
            Main.instance.setScene(connectionScreen.getScene());
        });
    }

    public Scene getScene() {
        return scene;
    }
}