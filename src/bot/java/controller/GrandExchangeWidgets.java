package controller;

import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.script.Script;

public enum GrandExchangeWidgets {
    COLLECT_LEFT(465, 23, 2),
    COLLECT_RIGHT(465, 23, 3),
    COLLECT(465, 23),
    CLOSE(465, 22, 0),
    BOX_1(465, 7),
    BOX_2(465, 8),
    BOX_3(465, 9),
    SEARCH_RESULTS(162, 53),
    SEARCH_BAR(162, 45),
    INCREMENT_PRICE(465, 24, 53),
    DECREMENT_PRICE(465, 24, 50),
    ITEM_WINDOW(465, 24, 21);

    private int root;
    private int child1;
    private int child2;
    private int numChildren;
    private GrandExchangeWidgets(int root, int child1, int child2) {
        this.root = root;
        this.child1 = child1;
        this.child2 = child2;
        this.numChildren = 3;
    }

    private GrandExchangeWidgets(int root, int child1){
        this(root, child1, -1);
        this.numChildren = 2;
    }

    public static GrandExchangeWidgets getBox(int boxNum) {
        if(boxNum < 1 || boxNum > 3) {
            return null;
        } else {
            return GrandExchangeWidgets.valueOf("BOX_" + boxNum);
        }
    }

    public int getRoot() {
        return root;
    }

    public int getChild1() {
        return child1;
    }

    public int getChild2() {
        return child2;
    }

    public int getNumChildren() {
        return numChildren;
    }

    public RS2Widget getWidget(Script script) {
        if(numChildren == 2) {
            return script.getWidgets().get(root, child1);
        } else {
            return script.getWidgets().get(root, child1, child2);
        }
    }




}

