package com.Pt239.DangeonQuest;

import java.util.Random;

/**
 * Created by Артем on 11.05.2016.
 */
public class GroupOfPortals {
    public int[] x,y;
    int numOfPortals=0;
    public GroupOfPortals(int mapWidth, int mapHeight,int numOfPortals){
        this.numOfPortals=numOfPortals;
        x=new int[numOfPortals];
        y=new int[numOfPortals];
        int i =0;
        while(i<numOfPortals) {
            x[i]= new Random().nextInt(mapWidth);
            y[i]= new Random().nextInt(mapHeight);
            for(int j=0;j<i;j++){
                if(x[i]==x[j] && y[i]==y[j]){
                    i--;
                    break;
                }
            }
            i++;
        }
    }

    public int isCellHasPortal(int cellX, int cellY){
        int out =-1;
        for(int i = 0;i<numOfPortals;i++) {
            if(x[i]==cellX && y[i]==cellY){
                out = i;
                break;
            }
        }
        return out;
    }

    public int getNextPortalNumber(int portalNumber){
        if(portalNumber+1<numOfPortals){
            return portalNumber+1;
        }else{
            return 0;
        }
    }
}
