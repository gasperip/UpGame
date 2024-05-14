package com.example.upgame;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.content.res.ResourcesCompat;
import java.util.ArrayList;
import java.util.Random;


public class GameView extends View{

    Bitmap background, ground, sat;
    Rect rectBackground, rectGround;
    Context context;
    Handler handler;
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    float TEXT_SIZE = 120;
    int points = 0;
    int life = 3;
    static int dWidth, dHeight;
    Random random;
    float satX, satY;
    float oldX;
    float oldAstX;
    ArrayList<Asteroid> asteroids;
    ArrayList<Explosion> explosions;

    public GameView(Context context) {
        super(context);
        this.context = context;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.back);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        sat = BitmapFactory.decodeResource(getResources(), R.drawable.satelite);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rectBackground = new Rect(0,0,dWidth,dHeight);
        rectGround = new Rect(0,dHeight -ground.getHeight(),dWidth, dHeight);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();

            }
        };
        textPaint.setColor(Color.rgb(255,165,0));
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.audiowide));
        healthPaint.setColor(Color.GREEN);
        random = new Random();
        satX = dWidth / 2 - sat.getWidth() / 2;
        satY = dHeight - ground.getHeight() - sat.getHeight();
        asteroids = new ArrayList<>();
        explosions = new ArrayList<>();
        for (int i=0; i<3; i++) {
            Asteroid spike = new Asteroid(context);
            asteroids.add(spike);
        }
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rectBackground, null );
        canvas.drawBitmap(ground, null, rectGround, null );
        canvas.drawBitmap(sat, satX, satY, null);
        for (int i = 0; i< asteroids.size(); i++){
            canvas.drawBitmap(asteroids.get(i).getAst(asteroids.get(i).spikeFrame), asteroids.get(i).astX, asteroids.get(i).astY, null );
            asteroids.get(i).spikeFrame++;
            if(asteroids.get(i).spikeFrame > 2){
                asteroids.get(i).spikeFrame = 0;
            }
            asteroids.get(i).astY += asteroids.get(i).astVelocity;
            if(asteroids.get(i).astY + asteroids.get(i).getAstHeight() >= dHeight - ground.getHeight()){
                points += 10;
                Explosion explosion = new Explosion(context);
                explosion.explosionX = asteroids.get(i).astX;
                explosion.explosionY = asteroids.get(i).astY;
                explosions.add(explosion);
                asteroids.get(i).resetPosition();
                if(points%100==0) {
                    asteroids.get(0).astVelocity += 35;
                    asteroids.get(1).astVelocity += 35;
                    asteroids.get(2).astVelocity += 35;
                }
            }

        }
        for (int i = 0; i < asteroids.size(); i++){
            if (asteroids.get(i).astX + asteroids.get(i).getAstWidth() >= satX
                    && asteroids.get(i).astX <= satX + sat.getWidth()
                    && asteroids.get(i).astY + asteroids.get(i).getAstWidth() >= satY
                    && asteroids.get(i).astY + asteroids.get(i).getAstWidth() <= satY + sat.getHeight()){
                life--;
                asteroids.get(i).resetPosition();
                if (life == 0){
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("points", points);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        }

        for (int i=0; i<explosions.size(); i++) {
            canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionFrame), explosions.get(i).explosionX,
                    explosions.get(i).explosionY, null);
            explosions.get(i).explosionFrame++;
            if (explosions.get(i).explosionFrame > 3){
                explosions.remove(i);
            }
        }
        if (life == 2){
            healthPaint.setColor(Color.YELLOW);
        }else if (life == 1){
            healthPaint.setColor(Color.RED);

        }
        canvas.drawRect(dWidth-200, 30, dWidth-200+60*life, 80, healthPaint);
        canvas.drawText("" + points, 20, TEXT_SIZE, textPaint);
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        if (touchY >= satY){
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN){
                oldX = event.getX();
                oldAstX = satX;
            }
            if (action == MotionEvent.ACTION_MOVE){
                float shift = oldX - touchX;
                float newSatX = oldAstX - shift;
                if (newSatX <= 0)
                    satX = 0;
                else if (newSatX >= dWidth - sat.getWidth())
                    satX = dWidth - sat.getWidth();
                else
                    satX = newSatX;
            }
        }
        return true;
    }
}
