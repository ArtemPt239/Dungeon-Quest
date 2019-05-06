package com.Pt239.DangeonQuest;

/**
 * Created by Артем on 24.04.2016.
 */
public class Player {
    Map playerMap;
    int heroX=0,heroY=0;
    int HP=1,maxNumOfHP=1;
    public static int
            WARRIOR_BITMAP=R.drawable.warrior,
            ROGUE_BITMAP=R.drawable.rogue0,
            MAGE_BITMAP=R.drawable.mage0,
            HUNTRESS_BITMAP=R.drawable.huntress0,
            PYROMANCER_BITMAP = R.drawable.pyromancer;
    int[] items = new int[9];
    public static int MAX_NUMBER_OF_ITEMS=9,
            HERO_TYPE_WARRIOR = 0,
            HERO_TYPE_ROGUE = 1,
            HERO_TYPE_MAGE = 2,
            HERO_TYPE_HUNTRESS = 3,
            HERO_TYPE_PYROMANCER=4;
    int numOfItems,heroType=HERO_TYPE_WARRIOR;
    String name="defaultNameOfPlayer";
    String[] messages = {"","","","",""};

    public Player(int mapWidth, int mapHeight, int heroType){
        this.heroType=heroType;
        playerMap = new Map(mapWidth,mapHeight);
        items[0]=Item.SWORD;
        items[1]=Item.CROSSBOW;
        items[2]=Item.ARROW;
        numOfItems=3;
        if(heroType==HERO_TYPE_HUNTRESS){
            items[3]=Item.ARROW;
            items[4]=Item.ARROW;
            numOfItems+=2;
        }
    }

    public void setMaxNumOfHP(int defaultMaxNumOfHp){
        int HPBonus=0;
        if(heroType == HERO_TYPE_WARRIOR){
            ++HPBonus;
        }
        this.maxNumOfHP=defaultMaxNumOfHp+HPBonus;

    }

    public void addItem(int item){
        items[numOfItems]=item;
        numOfItems++;
    }

    public  void removeItem(int itemId){
        for (int i=itemId;i<numOfItems-1;i++){
            items[i]=items[i+1];
        }
        numOfItems--;

    }

    public void addMessage(String message){
        messages[4]=messages[3];
        messages[3]=messages[2];
        messages[2]=messages[1];
        messages[1]=messages[0];
        messages[0]=message;
    }

    public float getFightCoefficient(){
        float coefficient=0.1f;
        Boolean hasSword = false;
        for (int i=0;i<numOfItems;i++){
            if(items[i]==Item.SWORD){
                hasSword=true;
            }
        }
        if(hasSword){
            coefficient+=0.5f;
        }
        for (int i=0;i<numOfItems;i++){
            if(items[i]==Item.MAGIC_ESSENCE){
                coefficient+=0.05f;
            }
        }
        if(heroType==HERO_TYPE_WARRIOR){
            coefficient+=0.2;
        }
        return coefficient;
    }

    static public int getHeroBitmapID(int heroType){
        int out = WARRIOR_BITMAP;
        switch (heroType){
            case 0: {out=WARRIOR_BITMAP; break;}
            case 1: {out=ROGUE_BITMAP; break;}
            case 2: {out=MAGE_BITMAP; break;}
            case 3: {out=HUNTRESS_BITMAP; break;}
            case 4: {out=PYROMANCER_BITMAP; break;}
        }
        return out;
    }

    public int hasItemOfType(int type){
        int out=-1;
        for (int i=0;i<numOfItems;i++){
            if(items[i]==type){
                out=i;
            }
        }
        return out;
    }


}
