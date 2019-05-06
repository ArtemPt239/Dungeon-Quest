package com.Pt239.DangeonQuest;

import java.util.Stack;
import java.util.Vector;

/**
 * Created by Артем on 19.04.2016.
 */
public class Cell {
    public Boolean isDiscovered = false;
    int specialObject = 0;
    public static int
            OBJECT_TYPE_NO_OBJECT=0,
            OBJECT_TYPE_NPC_SHOP=1,
            OBJECT_TYPE_HORN_OF_TREASURES=2,
            OBJECT_TYPE_NPC_CLASS_CHANGER=3,
            OBJECT_TYPE_HEALING_FOUNTAIN=4;


    Stack<Integer> droppedItems = new Stack<Integer>();
    Boolean hasMonster = false;
    static int ID_MONSTER_BITMAP = R.drawable.teemo0,
            ID_HEALING_FOUNTAIN_BITMAP = R.drawable.fountain
                    ;

    public Cell (){}

    public Cell (Cell cell){
        this.isDiscovered=cell.isDiscovered;
        this.hasMonster = cell.hasMonster;
        for(int i=0;i<cell.droppedItems.size();i++){
            this.droppedItems.push(cell.droppedItems.get(i));
        }
    }
}
