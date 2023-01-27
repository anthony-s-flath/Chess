package gui;

public class Settings {

    // what color pieces will be set up on top of board
    public String topColor = "BLACK";
    // should game follow order of turns
    public boolean turnOrder = true;
    // color of first turn if turnOrder is true
    public String firstTurn = "WHITE";

    public void setFirstTurn(String inFirstTurn) {
        firstTurn = inFirstTurn.toUpperCase();
    }

    public void setTopColor(String inTopColor) {
        topColor = inTopColor.toUpperCase();
    }

    public void setTurnOrder(boolean inTurnOrder) {
        turnOrder = inTurnOrder;
    }

    public String getTopColor() {
        return topColor;
    }

    public boolean getTurnOrder() {
        return turnOrder;
    }

    public String getFirstTurn() {
        return firstTurn;
    }

}
