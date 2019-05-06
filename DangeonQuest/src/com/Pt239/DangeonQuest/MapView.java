package com.Pt239.DangeonQuest;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Switch;

/**
 * Created by Артем on 19.04.2016.
 */
public class MapView extends View {
    Map map = new Map(0,0);
    private float oldx=0,oldy=0,scale=1;
    public  float dx=0,dy=0;
    int numOfSelectedPlayer=0,maxNumOfPlayers=0;
    Player[] players;

    Bitmap wallBitmap,floorBitmap,necropolisFloorBitmap,exitBitmap,portalBitmap;
    public MapView(Context context, Player[] players) {
        super(context);
        wallBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.wall0);
        floorBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.floor0);
        necropolisFloorBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.necropolisfloor0);
        exitBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.door0);
        portalBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.portal0);

        maxNumOfPlayers=players.length;
        this.players = new Player[maxNumOfPlayers+1];
        for(int i=0;i<maxNumOfPlayers;i++){
            this.players[i]=players[i];
        }
        this.players[maxNumOfPlayers] = new Player(1,1,Player.HERO_TYPE_WARRIOR);

        super.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    dx +=1.5*(event.getX() - oldx);
                    dy +=1.5*(event.getY() - oldy);
                    oldx = event.getX();
                    oldy = event.getY();
                    invalidate();
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    oldx = event.getX();
                    oldy = event.getY();
                }




                return true;
            }
        });
    }

    public void drawMap(Map map){
        this.map=map;
        invalidate();
    }

    public void setPlayerNum(int playerNum){
        if (playerNum!=-1) {
            this.numOfSelectedPlayer=playerNum;
            this.map = players[playerNum].playerMap;
        } else {
            this.numOfSelectedPlayer=maxNumOfPlayers;
            this.map = players[numOfSelectedPlayer].playerMap;
        }
        invalidate();
    }

    public float getScale(){return scale;}

    public void setScale(float scale){
        this.scale=scale;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.scale(scale,scale);
        //ориентация изображения по центру экрана
        float baseDX =0 , baseDY = 0;
        if(players[numOfSelectedPlayer]!=null){
            baseDX=-(players[numOfSelectedPlayer].heroX*4+2.5f)*floorBitmap.getWidth()+getWidth()/2;
            baseDY=-(players[numOfSelectedPlayer].heroY*4+2.5f)*floorBitmap.getHeight()+getHeight()/2;
        }

        canvas.translate(dx+baseDX,dy+baseDY);

        //cells
        for (int y = 0;y<map.height;y++) {
            for (int x = 0; x < map.width; x++) {
                if (map.cells[x][y].isDiscovered) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            canvas.drawBitmap(floorBitmap, floorBitmap.getWidth() * (x * 4 + 1 + i), floorBitmap.getHeight() * (y * 4 + 1 + j), new Paint());
                            //КОСТЫЛЬ!!!
                            //вместо высоты-ширины неплохо бы вставить 128 пикселей, но почемуто он переводит их в 85 пикселей. ХЗ почему
                        }
                    }
                }
            }
        }
        //necropolis
        if (map.cells[map.necropolisX][map.necropolisY].isDiscovered) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    canvas.drawBitmap(necropolisFloorBitmap, necropolisFloorBitmap.getWidth() * (map.necropolisX * 4 + 1 + i),
                            necropolisFloorBitmap.getHeight() * (map.necropolisY * 4 + 1 + j), new Paint());
                }
            }
        }
        //walls horizontal
        for (int level = 0;level<map.height+1;level++) {
            for (int x = 0; x < map.width; x++) {
                switch (map.wallsHorizontal[level][x].state) {
                    case 1:{
                        //corridor
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (x * 4 + 1), floorBitmap.getHeight() * (level * 4), new Paint());
                        canvas.drawBitmap(floorBitmap, floorBitmap.getWidth() * (x * 4 + 2), floorBitmap.getHeight() * (level * 4), new Paint());
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (x * 4 + 3), floorBitmap.getHeight() * (level * 4), new Paint());
                        break;
                    }
                    case 2: {
                        //wall indestructible
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (x * 4 + 1), floorBitmap.getHeight() * (level * 4), new Paint());
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (x * 4 + 2), floorBitmap.getHeight() * (level * 4), new Paint());
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (x * 4 + 3), floorBitmap.getHeight() * (level * 4), new Paint());
                        break;
                    }
                    case 3: {
                        //wall destructible
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (x * 4 + 1), floorBitmap.getHeight() * (level * 4), new Paint());
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (x * 4 + 2), floorBitmap.getHeight() * (level * 4), new Paint());
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (x * 4 + 3), floorBitmap.getHeight() * (level * 4), new Paint());
                        break;
                    }
                    case 4: {
                        //exit
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (x * 4 + 1), floorBitmap.getHeight() * (level * 4), new Paint());
                        canvas.drawBitmap(exitBitmap, floorBitmap.getWidth() * (x * 4 + 2), floorBitmap.getHeight() * (level * 4), new Paint());
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (x * 4 + 3), floorBitmap.getHeight() * (level * 4), new Paint());
                        break;
                    }
                }
            }
        }
        //walls vertical
        for (int level = 0;level<map.width+1;level++) {
            for (int y = 0; y < map.height; y++) {
                switch (map.wallsVertical[level][y].state) {
                    case 1:{
                        //corridor
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (level * 4 ), floorBitmap.getHeight() * (y * 4 + 1), new Paint());
                        canvas.drawBitmap(floorBitmap, floorBitmap.getWidth() * (level * 4 ), floorBitmap.getHeight() * (y * 4 + 2), new Paint());
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (level * 4 ), floorBitmap.getHeight() * (y * 4 + 3), new Paint());
                        break;
                    }
                    case 2: {
                        //wall indestructible
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (level * 4 ), floorBitmap.getHeight() * (y * 4 + 1), new Paint());
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (level * 4 ), floorBitmap.getHeight() * (y * 4 + 2), new Paint());
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (level * 4 ), floorBitmap.getHeight() * (y * 4 + 3), new Paint());
                        break;
                    }
                    case 3: {
                        //wall destructible
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (level * 4 ), floorBitmap.getHeight() * (y * 4 + 1), new Paint());
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (level * 4 ), floorBitmap.getHeight() * (y * 4 + 2), new Paint());
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (level * 4 ), floorBitmap.getHeight() * (y * 4 + 3), new Paint());
                        break;
                    }
                    case 4: {
                        //exit
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (level * 4 ), floorBitmap.getHeight() * (y * 4 + 1), new Paint());
                        canvas.drawBitmap(exitBitmap, floorBitmap.getWidth() * (level * 4 ), floorBitmap.getHeight() * (y * 4 + 2), new Paint());
                        canvas.drawBitmap(wallBitmap, floorBitmap.getWidth() * (level * 4 ), floorBitmap.getHeight() * (y * 4 + 3), new Paint());
                        break;
                    }
                }
            }
        }

        // items
        for (int y = 0;y<map.height;y++) {
            for (int x = 0; x < map.width; x++) {
                if (map.cells[x][y].isDiscovered) {
                    if (!map.cells[x][y].droppedItems.empty()){
                        Bitmap itemBitmap;
                        switch(map.cells[x][y].droppedItems.peek()){
                            case 0:{
                                itemBitmap = BitmapFactory.decodeResource(getContext().getResources(),Item.ID_TREASURE_BITMAP);
                                break;
                            }
                            case 1:{
                                itemBitmap = BitmapFactory.decodeResource(getContext().getResources(),Item.ID_TREASURE_BITMAP);
                                break;
                            }
                            case 2:{
                                itemBitmap = BitmapFactory.decodeResource(getContext().getResources(),Item.ID_KEY_BITMAP);
                                break;
                            }
                            case 3:{
                                itemBitmap = BitmapFactory.decodeResource(getContext().getResources(),Item.ID_SWORD_BITMAP);
                                break;
                            }
                            case 4:{
                                itemBitmap = BitmapFactory.decodeResource(getContext().getResources(),Item.ID_CROSSBOW_BITMAP);
                                break;
                            }
                            case 5:{
                                itemBitmap = BitmapFactory.decodeResource(getContext().getResources(),Item.ID_ARROW_BITMAP);
                                break;
                            }
                            case 7:{
                                itemBitmap = BitmapFactory.decodeResource(getContext().getResources(),Item.ID_ESSENCE_BITMAP);
                                break;
                            }
                            case 8:{
                                itemBitmap = BitmapFactory.decodeResource(getContext().getResources(),Item.ID_PEACE_OF_MAP_BITMAP);
                                break;
                            }
                            default:{
                                itemBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_launcher);
                            }

                        }
                        canvas.drawBitmap(itemBitmap, itemBitmap.getWidth() * (x * 4 + 3), itemBitmap.getHeight() * (y * 4 + 3), new Paint());
                    }
                }
            }
        }

        //portals
        if (players[numOfSelectedPlayer]!=null){
            if (players[numOfSelectedPlayer].playerMap.portals!=null){
                for (int y = 0;y<map.height;y++) {
                    for (int x = 0; x < map.width; x++) {
                        if (map.cells[x][y].isDiscovered && map.portals.isCellHasPortal(x,y)!=-1) {
                            canvas.drawBitmap(portalBitmap, portalBitmap.getWidth() * (x * 4 + 1), portalBitmap.getHeight() * (y * 4 + 3), new Paint());
                        }
                    }
                }
            }
        }

        //player hero
        if (players[numOfSelectedPlayer]!=null){
            Bitmap heroBitmap = BitmapFactory.decodeResource(getContext().getResources()
                    ,players[numOfSelectedPlayer].getHeroBitmapID(players[numOfSelectedPlayer].heroType));
            canvas.drawBitmap(heroBitmap, heroBitmap.getWidth() * (players[numOfSelectedPlayer].heroX * 4 + 2),
                    heroBitmap.getHeight() * (players[numOfSelectedPlayer].heroY * 4 + 2), new Paint());

        }
        //opponents heroes
        int numOfDrawnHeroes = 0;
        if (players[numOfSelectedPlayer]!=null) {
            for(int i=0;i<maxNumOfPlayers;i++){
                if((i!=numOfSelectedPlayer)&&(players[i].heroX==players[numOfSelectedPlayer].heroX)&&(players[i].heroY==players[numOfSelectedPlayer].heroY)){
                    Bitmap heroBitmap = BitmapFactory.decodeResource(getContext().getResources()
                            ,players[i].getHeroBitmapID(players[i].heroType));
                    switch (numOfDrawnHeroes){
                        case 0:{
                            canvas.drawBitmap(heroBitmap, heroBitmap.getWidth() * (players[numOfSelectedPlayer].heroX * 4 + 3),
                                    heroBitmap.getHeight() * (players[numOfSelectedPlayer].heroY * 4 + 1), new Paint());
                            break;
                        }
                        case 1:{
                            canvas.drawBitmap(heroBitmap, heroBitmap.getWidth() * (players[numOfSelectedPlayer].heroX * 4 + 2),
                                    heroBitmap.getHeight() * (players[numOfSelectedPlayer].heroY * 4 + 1), new Paint());
                            break;
                        }
                        case 2:{
                            canvas.drawBitmap(heroBitmap, heroBitmap.getWidth() * (players[numOfSelectedPlayer].heroX * 4 + 3),
                                    heroBitmap.getHeight() * (players[numOfSelectedPlayer].heroY * 4 + 2), new Paint());
                            break;
                        }
                    }
                    numOfDrawnHeroes++;
                }
            }
        }
        //monsters
        for (int x = 0; x < map.width; x++) {
            for (int y = 0; y < map.height; y++) {
                if (map.cells[x][y].hasMonster){
                    Bitmap monsterBitmap =  BitmapFactory.decodeResource(getContext().getResources(),Cell.ID_MONSTER_BITMAP);
                    canvas.drawBitmap(monsterBitmap, monsterBitmap.getWidth() * (x * 4 + 3),
                            monsterBitmap.getHeight() * (y * 4 + 1), new Paint());
                }
            }
        }

        //special objects
        for (int x = 0; x < map.width; x++) {
            for (int y = 0; y < map.height; y++) {
                if (map.cells[x][y].specialObject==Cell.OBJECT_TYPE_HEALING_FOUNTAIN){
                    Bitmap monsterBitmap =  BitmapFactory.decodeResource(getContext().getResources(),Cell.ID_HEALING_FOUNTAIN_BITMAP);
                    canvas.drawBitmap(monsterBitmap, monsterBitmap.getWidth() * (x * 4 + 1),
                            monsterBitmap.getHeight() * (y * 4 + 1), new Paint());
                }
            }
        }

        // smoke
        if(players[numOfSelectedPlayer]!=null) {
            for (int x = -1; x <= map.width; x++) {
                for (int y = -1; y <= map.height; y++) {
                    if (!(x == players[numOfSelectedPlayer].heroX && y == players[numOfSelectedPlayer].heroY)) {
                        Paint paint = new Paint();
                        paint.setColor(Color.argb(128, 0, 0, 0));
                        canvas.drawRect((float) (x*4 + 0.5) * wallBitmap.getWidth(), (float) (y*4 + 0.5) * wallBitmap.getHeight(),
                                (float) (x*4 + 4.5) * wallBitmap.getWidth(), (float) (y*4 + 4.5) * wallBitmap.getHeight(), paint);
                    }
                }
            }
        }




    }
}
