package gogame;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// ----------------------------------------------------------------------
// class GameMetaInformation
// gmi is a data container to store information of a single game
// stored are only option parameters like komi and time mode
// no information on current board state is stored
// ----------------------------------------------------------------------
@SuppressWarnings("serial")
public class GameMetaInformation implements Serializable {

    public static int INVALID_INT = Integer.MAX_VALUE;
    public static float INVALID_FLOAT = Float.MAX_VALUE;
    public static long INVALID_LONG = Long.MAX_VALUE;
    public static byte INVALID_BYTE = Byte.MAX_VALUE;

    private float komi;
    private int handicap;
    private int boardSize;
    private int blackPrisoners;
    private int whitePrisoners;
    private String timeMode;
    private String whiteName;
    private String blackName;
    private String whiteRank;
    private String blackRank;
    private String result;
    private String[] dates;

    // in accordance with MoveNode's actionType
    public enum actionType {
        MOVE,
        PASS,
        RESIGN
    }

    // Constructor
    public GameMetaInformation() {
        this.timeMode = null;
        this.whiteName = null;
        this.blackName = null;
        this.whiteRank = null;
        this.blackRank = null;
        this.result = "Void";
        this.boardSize = INVALID_INT;
        this.handicap = INVALID_INT;
        this.komi = INVALID_FLOAT;
        this.dates = new String[0];
    }
    

    public void setDates(String[] inputDates) {
        this.dates = inputDates;
    }

    public String[] getDates() {
        return this.dates;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String res) {
        this.result = res;
    }

    public float getKomi() {
        return komi;
    }

    public void setKomi(float komi) {
        this.komi = komi;
    }

    public int getHandicap() {
        return handicap;
    }

    public void setHandicap(int handicap) {
        this.handicap = handicap;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public int getBlackPrisoners() {
        return blackPrisoners;
    }

    public void setBlackPrisoners(int prisoners) {
        blackPrisoners = prisoners;
    }

    public int getWhitePrisoners() {
        return whitePrisoners;
    }

    public void setWhitePrisoners(int prisoners) {
        whitePrisoners = prisoners;
    }

    public String getTimeMode() {
        return timeMode;
    }

    public void setTimeMode(String timeMode) {
        this.timeMode = timeMode;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public void setWhiteName(String whiteName) {
        this.whiteName = whiteName;
    }

    public String getBlackName() {
        return blackName;
    }

    public void setBlackName(String blackName) {
        this.blackName = blackName;
    }

    public void setWhiteRank(String whiteRank) {
        this.whiteRank = whiteRank;
    }

    public String getWhiteRank() {
        return this.whiteRank;
    }

    public void setBlackRank(String blackRank) {
        this.blackRank = blackRank;
    }

    public String getBlackRank() {
        return this.blackRank;
    }

    public int getBlackPoints() {
        return 5;
    }

    public void setBlackPoints(int points) {
        return;
    }

    public int getWhitePoints() {
        return 7;
    }

    public void setWhitePoints(int points) {
        return;
    }
 }
