package com.example.buildingmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    Spot[][] grid;
    ArrayList<Spot> openSet=new ArrayList<Spot>();
    ArrayList<Spot> closedSet=new ArrayList<Spot>();
    Spot start;
    Spot end;
    boolean nosolution=false;
    boolean keepgoing=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        grid=new Spot[5][5];
        for(int i1=0;i1<grid.length;i1++){
            for(int j1=0;j1<grid[0].length;j1++){
                 grid[i1][j1]=new Spot(i1,j1,false);
            }
        }
        for(int i2=0;i2<grid.length;i2++){
            for(int j2=0;j2<grid[0].length;j2++){
                grid[i2][j2].addNeighbors(grid);
            }
        }
        start=grid[0][0];
        end=grid[4][4];


    }

    public void pathFinding(Spot start,Spot end,Spot[][] grid){
        ArrayList<Spot> path=new ArrayList<Spot>();
        openSet.add(start);
        while(keepgoing){
        if(!openSet.isEmpty()){
            int winner=0;
            for(int x=0;x<openSet.size();x++){
                 if(openSet.get(x).getF()<openSet.get(winner).getF()){
                     winner=x;
                 }
            }
            Spot current=openSet.get(winner);
            if(current==end){
                Spot tempS=current;
                while (tempS!=null){
                    path.add(tempS.previous);
                    tempS=tempS.previous;
                }
                keepgoing=false;
                //done!!
            }
            openSet.remove(current);
            closedSet.add(current);

            ArrayList<Spot> neighbors=current.neighbors;

            for(int i3=0;i3<neighbors.size();i3++){
                Spot neighbor=neighbors.get(i3);
                if(!closedSet.contains(neighbor)&&(!neighbor.wall)){
                    double x=1;
                    if (Math.abs(current.i-neighbor.i)+Math.abs(current.j-neighbor.j)==2){
                        x=Math.pow(2,1/2);
                    }
                    double tempG=current.g+x;
                    if(openSet.contains(neighbor)){
                        if(tempG<neighbor.g){
                            neighbor.g=tempG;
                        }
                        else {
                            neighbor.g=tempG;
                            openSet.add(neighbor);
                        }
                    }

                }

                neighbor.h=heuristic(neighbor,end);
                neighbor.f=neighbor.g+neighbor.h;
                neighbor.previous=current;
            }
            //we can keep going
        }
        else {
            Toast.makeText(this, "no solution", Toast.LENGTH_SHORT).show();
            nosolution=true;
            //no solution
        }

    }}
    public double heuristic(Spot n,Spot end){
      return Math.sqrt(Math.pow((n.i-end.i),2)+Math.pow((n.j-end.j),2));
    }
}
