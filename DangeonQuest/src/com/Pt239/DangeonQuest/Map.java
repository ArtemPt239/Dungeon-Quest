package com.Pt239.DangeonQuest;

/**
 * Created by Артем on 19.04.2016.
 */
public class Map {
    int width,height;
    Cell[][] cells;
    Wall[][] wallsHorizontal,wallsVertical;
    int necropolisX=0, necropolisY=0;
    GroupOfPortals portals;


    public Map(int width,int height){
        this.width=width;
        this.height=height;
        cells = new Cell[width][height];
        wallsHorizontal = new Wall[height+1][width];  // ["уровень"][координата]
        wallsVertical = new Wall[width+1][height];
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                cells[i][j]= new Cell();
            }
        }
        for(int i=0;i<height+1;i++){
            for(int j=0;j<width;j++){
                wallsHorizontal[i][j]= new Wall();
            }
        }
        for(int i=0;i<width+1;i++){
            for(int j=0;j<height;j++){
                wallsVertical[i][j]= new Wall();
            }
        }

    }

    public void generatePortals(int numOfPortals){
        portals = new GroupOfPortals(width,height,numOfPortals);
    }
}
