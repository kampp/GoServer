package gogame;

import java.io.Serializable;
import java.util.ArrayList;

// ----------------------------------------------------------------------
// class MoveNode
//
// contains the representation of a move placed on the board
// consists of move data as well as members for defining a tree structure
// to store multiple branches in one game
// ----------------------------------------------------------------------
@SuppressWarnings("serial")
public class MoveNode implements Serializable {
    private ArrayList<MoveNode> children;
    private MoveNode parent;

    // defines what type of action is used: 0 = set, 1 = pass, 2 = resign
    private GameMetaInformation.actionType actionType;
    private boolean isBlacksMove;
    private boolean isPrisoner;
    private int[] position;
    private String comment;
    private long currentTime;
    private byte currentOtPeriods;
    
    // generate root move node
    public MoveNode() {
    	//TODO
    }
    
    // generates a new MoveNode by the given JSON-String
    // the string is recieved through the POST:[..]/play method
    public MoveNode(String jsonStr, boolean isBlacksMove) {
    	//TODO
    }


    // add child returns the index of the newly inserted child node
    public int addChild(MoveNode childInput) {
        this.children.add(childInput);
        return this.children.indexOf(childInput);
    }

    // removes the specified child from the children. Returns true if child existed and
    // false if it did not
    public boolean removeChild(MoveNode childToRemove) {
        return this.children.remove(childToRemove);
    }

    public GameMetaInformation.actionType getActionType() {
        return actionType;
    }

    public ArrayList<MoveNode> getChildren() {
        return children;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public boolean isBlacksMove() {
        return isBlacksMove;
    }

    public void setPrisoner() {
        isPrisoner = true;
    }

    public void unsetPrisoner() {
        isPrisoner = false;
    }

    public boolean isPrisoner() {
        return isPrisoner;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public MoveNode getParent() {
        return parent;
    }

    public long getTime() {
        return currentTime;
    }

    public void setTime(long time) {
        this.currentTime = time;
    }

    public byte getOtPeriods() {
        return currentOtPeriods;
    }

    public void setOtPeriods(byte otPeriods) {
        this.currentOtPeriods = otPeriods;
    }
}
