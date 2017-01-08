package gogame;

import java.io.Serializable;
import java.util.ArrayList;

// ----------------------------------------------------------------------
// class RunningGame
// author Felix Wisser
//
// this class contains the internal representation of a running game
// excluding meta parameters like board size, komi, HC ...
// ----------------------------------------------------------------------
@SuppressWarnings("serial")
public class RunningGame implements Serializable{
    private GameMetaInformation gmi;
    private MoveNode rootNode;
    private ArrayList<Integer> mainTreeIndices;      // contains indices to the current node in use, to get nodes from other branches, alter this array


    public RunningGame(GameMetaInformation gmi)
    {
        this.gmi = gmi;
        rootNode = new MoveNode(gmi.getHandicap() > 0);
        mainTreeIndices = new ArrayList<Integer>();
    }
    // ----------------------------------------------------------------------
    // function getCurrentNode()
    //
    // returns the last added Node from the whole tree. this is the node,
    // to which the next played is attached
    // ----------------------------------------------------------------------
    public MoveNode getCurrentNode() {

        MoveNode currentlyDeepest = rootNode;

        for (int i = 0; i < mainTreeIndices.size(); i++) {
            currentlyDeepest = currentlyDeepest.getChildren().get(mainTreeIndices.get(i));
        }

        return currentlyDeepest;
    }

    // ----------------------------------------------------------------------
    // function getSpecificNode()
    //
    // returns a moveNode according to the given array list, which contains
    // tree-indices like the treeIndices-member of this class
    //
    // is used to get other branches of the tree and moves, that are not at
    // the bottom layer of the move-tree
    // ----------------------------------------------------------------------
    public MoveNode getSpecificNode(ArrayList<Integer> indexArray) {

        MoveNode currentlyDeepest = rootNode;

        for (int i = 0; i < indexArray.size(); i++) {
            currentlyDeepest = currentlyDeepest.getChildren().get(indexArray.get(i));
        }

        return currentlyDeepest;
    }

    // ----------------------------------------------------------------------
    // function getRootNode()
    //
    // returns the root node of the current game
    // ----------------------------------------------------------------------
    public MoveNode getRootNode() {
        return this.rootNode;
    }

    // ----------------------------------------------------------------------
    // function playMove()
    //
    // creates a new moveNode and attaches it to the main tree-branch
    //
    // to add a move to different branch of the tree, use recordMove()
    // ----------------------------------------------------------------------
    public void playMove(GameMetaInformation.actionType actionType, int[] position, long time, byte otPeriods) {

        MoveNode currentNode = this.getCurrentNode();
        MoveNode thisMoveNode = new MoveNode(actionType, !currentNode.isBlacksMove(), position, currentNode, time, otPeriods); // negate color to signal the other play is at turn
        this.addIndexToMainTree(currentNode.addChild(thisMoveNode)); // add child node and index

    }

    // ----------------------------------------------------------------------
    // function recordMove()
    //
    // creates a new moveNode and attaches it to the parent node passed in
    // the arguments. This creates a new branch in the sgf.
    // If the specified parent node has no children, the new move is attached
    // to the main tree-branch.
    // ----------------------------------------------------------------------
    public int recordMove(GameMetaInformation.actionType actionType, int[] position, ArrayList<Integer> indices, long time, byte otPeriods, String comment) {

        MoveNode parentNode = getSpecificNode(indices);
        MoveNode thisMoveNode = new MoveNode(actionType, !parentNode.isBlacksMove(), position, parentNode, time, otPeriods, comment);
        return parentNode.addChild(thisMoveNode);
    }


    public int recordMove(GameMetaInformation.actionType actionType, int[] position, ArrayList<Integer> indices, long time, byte otPeriods) {
        MoveNode parentNode = getSpecificNode(indices);
        MoveNode thisMoveNode = new MoveNode(actionType, !parentNode.isBlacksMove(), position, parentNode, time, otPeriods);
        return parentNode.addChild(thisMoveNode);
    }

    public int recordMove(GameMetaInformation.actionType actionType, int[] position, ArrayList<Integer> indices, String comment) {
        MoveNode parentNode = getSpecificNode(indices);
        MoveNode thisMoveNode = new MoveNode(actionType, !parentNode.isBlacksMove(), position, parentNode, comment);
        return parentNode.addChild(thisMoveNode);
    }

    public int recordMove(GameMetaInformation.actionType actionType, int[] position, ArrayList<Integer> indices) {
        MoveNode parentNode = getSpecificNode(indices);
        MoveNode thisMoveNode = new MoveNode(actionType, !parentNode.isBlacksMove(), position, parentNode);
        return parentNode.addChild(thisMoveNode);
    }

    // ----------------------------------------------------------------------
    // function addIndexToMainTree()
    //
    // append given index to the main game tree
    // ----------------------------------------------------------------------
    public void addIndexToMainTree(int index) {
        mainTreeIndices.add(index);
    }

    public void updateMainTreeIndices() {
        MoveNode mn = getRootNode();
        this.mainTreeIndices = new ArrayList<Integer>();
        while (mn.getChildren().size() != 0) {
            addIndexToMainTree(0);
            mn = getCurrentNode();
        }
    }

    // ----------------------------------------------------------------------
    // function takeLastMoveBack()
    //
    // deletes the last node in the main tree-branch
    //
    // to delete from a different branch of the tree, use deleteRecordedMove() TODO implement
    // ----------------------------------------------------------------------
    public void takeLastMoveBack() {

        MoveNode lastAddedMove = this.getCurrentNode();
        MoveNode parentNode = lastAddedMove.getParent();

        parentNode.removeChild(lastAddedMove);
        mainTreeIndices.remove(mainTreeIndices.size()-1); // size-1 is the index of the last element in the list

    }

    // ----------------------------------------------------------------------
    // function setAsPrisoner(ArrayList<Integer> indices)
    //
    // sets the specified node as a prisoner, meaning that it will be removed
    // from the board it is currently placed on
    // ----------------------------------------------------------------------
    public void setAsPrisoner(ArrayList<Integer> indices) {
        getSpecificNode(indices).setPrisoner();
    }

    public GameMetaInformation getGameMetaInformation() {
        return gmi;
    }

    public ArrayList<Integer> getMainTreeIndices() {
        return mainTreeIndices;
    }


}
