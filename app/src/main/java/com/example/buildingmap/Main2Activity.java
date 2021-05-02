package com.example.buildingmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.Stack;

public class Main2Activity extends AppCompatActivity {

    final int N=0;
    final int NE=1;
    final int E=2;
    final int SE=3;
    final int S=4;
    final int SW=5;
    final int W=6;
    final int NW=7;

    Spot[][] grid1;
    Spot start;
    Spot end;

    Bitmap b;

    ImageView imageView2;
    TextView tv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        imageView2=(ImageView) findViewById(R.id.imageView2);
        tv=(TextView) findViewById(R.id.textView);

        b=createImage(50,50, Color.YELLOW);

        grid1=new Spot[5][5];
        for(int i1=0;i1<grid1.length;i1++){
            for(int j1=0;j1<grid1[0].length;j1++){
                 grid1[i1][j1]=new Spot(i1,j1,false);
            }
        }
        for(int i2=0;i2<grid1.length;i2++){
            for(int j2=0;j2<grid1[0].length;j2++){
                grid1[i2][j2].addNeighbors(grid1);
            }
        }
        grid1[1][0].wall=true;
        grid1[1][2].wall=true;
        grid1[3][1].wall=true;
        grid1[2][4].wall=true;
        grid1[1][4].wall=true;
        grid1[4][3].wall=true;
        start=grid1[0][0];
        end=grid1[4][1];


    }

    public static Bitmap createImage(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
        return bitmap;
    }

    public void visual(Spot[][] gridd,Bitmap b){
        for(int i1 = 0; i1<50; i1++){
            for(int j1=0;j1<50;j1++){
                if(gridd[i1][j1].wall=true){
                    b.setPixel(i1,j1,Color.BLACK);
                }
            }
        }
    }

    public ArrayList<Spot> pathFinding(Spot start, Spot end, Spot[][] grid){
        b=createImage(50,50, Color.YELLOW);
       // visual(grid,b);
        ArrayList<Spot> path=new ArrayList<Spot>();
        ArrayList<Spot> openSet=new ArrayList<Spot>();
        ArrayList<Spot> closedSet=new ArrayList<Spot>();
        boolean nosolution=false;
        boolean keepgoing=true;
        int winner=0;

        openSet.add(start);
        while(keepgoing){
        if(!openSet.isEmpty()){
            for(int x=0;x<openSet.size();x++){
                 if(openSet.get(x).getF()<openSet.get(winner).getF()){
                     winner=x;
                 }
            }
            Spot current=openSet.get(winner);
            if(current==end){
                Spot tempS=current;
               int e=0;
               //for(int y=0;y<10;y++) {
                   while (tempS != null) {
                       e++;
                       tv.setText(e + "");
                       path.add(tempS);
                       b.setPixel(tempS.i, tempS.j, Color.RED);
                       tempS = tempS.previous;
                       //
                   }
              // }
                Toast.makeText(Main2Activity.this, "DONE!", Toast.LENGTH_SHORT).show();
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

                    }
                    else {
                        neighbor.g=tempG;
                        openSet.add(neighbor);
                    }

                    neighbor.h=heuristic(neighbor,end);
                    neighbor.f=neighbor.g+neighbor.h;
                    neighbor.previous=current;

                }


            }
            //we can keep going
        }
        else {
            Toast.makeText(this, "no solution", Toast.LENGTH_SHORT).show();
            nosolution=true;
            keepgoing=false;
            //no solution
        }

       }
    return path;
    }

    public double heuristic(Spot n,Spot end){
      return Math.sqrt(Math.pow((n.i-end.i),2)+Math.pow((n.j-end.j),2));
    }


    public Stack<Vector> vectorPath(ArrayList<Spot> path){
        ArrayList<Vector> vPath=new ArrayList<Vector>();
        Spot current,peek;
        int currentD=0;
        int temp;
        int steps=0;
        while (!path.isEmpty()){
            current=path.remove(0);
            if (!path.isEmpty()){
            peek=path.get(0);
            temp=getDirection(current.i,current.j,peek.i,peek.j);
            if(temp!=currentD){
                vPath.add(new Vector(currentD,steps));
                currentD=temp;
                steps=1;
            }
            else {
                steps++;
            }
            }
        }
        return listToStack(vPath);
    }

    /**
     * this method gets two spot's coordinations and returns the relvative direction.
     * @param x0
     * @param y0
     * @param xE
     * @param yE
     * @return
     */
    public int getDirection(int x0,int y0,int xE,int yE){
        int dX=xE-x0;
        int dY=yE-y0;
        int direction=20;
        switch (dX){
            case 1: {
                switch (dY){
                    case 1: direction=SE;break;
                    case 0: direction=E; break;
                    case -1: direction=NE;break;
                }
            }
            case 0:{
                switch (dY){
                    case 1: direction= S;break;
                    case -1: direction=N;break;
                }

            }
            case -1:{
                switch (dY){
                    case 1: direction= SW;break;
                    case 0: direction= W; break;
                    case -1: direction=NW;break;
                }
            }
        }
        return direction;
    }

    /**
     * this method reverses a Stack.
     * @param s1
     * @return
     */
   public Stack<Spot> reverseS(Stack<Spot> s1){
        Stack<Spot> s2=new Stack<Spot>();
        while(!s1.isEmpty()){
            s2.push(s1.pop());
        }
        return s2;
   }

   public Stack<Vector> listToStack(ArrayList<Vector> arrayList){
       Stack<Vector> S=new Stack<Vector>();
       while (!arrayList.isEmpty()){
           S.push(arrayList.remove(0));
       }
       return S;
   }


    public void sssss(View view) {
       ArrayList<Spot> S=pathFinding(start,end,grid1);
        imageView2.setImageBitmap(b);
    }
}
