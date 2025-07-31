public enum GameStatus {
    NORMAL,
    CHECK,
    CHECKMATE,
    STALEMATE ;
    private static GameMode selectedMode = GameMode.CLASSIC;

    public static void setSelectedMode(GameMode mode) {
        selectedMode = mode;
    }

    public static GameMode getSelectedMode() {
        return selectedMode;
    }

    }
