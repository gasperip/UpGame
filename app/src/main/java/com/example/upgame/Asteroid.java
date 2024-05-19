package com.example.upgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Asteroid {
    Bitmap ast[] = new Bitmap[3];
    int astFrame = 0;
    int astX, astY, astVelocity;
    Random random;

    public Asteroid(Context context){
        ast[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid);
        ast[1] = BitmapFactory.decodeResource(context.getResources(),R.drawable.asteroid1);
        ast[2] = BitmapFactory.decodeResource(context.getResources(),R.drawable.asteroid2);
        random = new Random();
        resetPosition();
    }

    public Bitmap getAst(int astFrame)
    {
        return ast[astFrame];
    }

    public int getAstWidth()
    {
        return ast[0].getWidth();
    }

    public int getAstHeight()
    {
        return  ast[0].getHeight();
    }

    public void resetPosition(){
        astX = random.nextInt(GameView.dWidth - getAstWidth());
        astY = -200 + random.nextInt(600) * -1;
        astVelocity = 35 + random.nextInt(16);
    }
}
