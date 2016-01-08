package com.praveer_rai.connect3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int activePlayer = 1; // yellow, 2-> red

    int[] gameState = new int[10]; // for readability 10 instead of 9

    int[][] winningPositions = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {1, 4, 7}, {2, 5, 7}, {3, 6, 9}, {1, 5, 9}, {3, 5, 7},};

    TextView winMsg = null;
    LinearLayout playAgain = null;
    GridLayout gridLayout = null;

    boolean isGameActive = true;

    public void dropIn(View view) {

        ImageView currentDisk = (ImageView) view;

        int currentBoxInt = Integer.parseInt(currentDisk.getTag().toString());

        if (isGameActive && gameState[currentBoxInt] == 0) {

            gameState[currentBoxInt] = activePlayer;

            //currentDisk.setTranslationY(-1000f);

            if (activePlayer == 1) {
                currentDisk.setImageResource(R.drawable.yellow);
                activePlayer = 2;
            } else {
                currentDisk.setImageResource(R.drawable.red);
                activePlayer = 1;
            }

            AnimationSet animSet = new AnimationSet(true);


            Animation translate = new TranslateAnimation(0, 0, 1000, 0);
            translate.setDuration(900);
            Animation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
            rotate.setDuration(900);

            animSet.addAnimation(translate);
            animSet.addAnimation(rotate);

            Animation.AnimationListener al = new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    checkIfWinOrDraw();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            };

            animSet.setAnimationListener(al);
            currentDisk.startAnimation(animSet);

            //currentDisk.animate().translationYBy(1000f).rotation(720f).setDuration(600);
        }

    }

    public void checkIfWinOrDraw() {
        for (int[] wp : winningPositions) {
            // check if all positions are played
            if (gameState[wp[0]] != 0 && // no need to check all 3, it would be false anyway in next condition
                    // if same player state at all of these winning positions
                    gameState[wp[0]] == gameState[wp[1]] &&
                    gameState[wp[1]] == gameState[wp[2]]) {

                // win condition

                int winner = gameState[wp[0]];
                String winPlayer = "Yellow";
                if (winner == 2) {
                    winPlayer = "Red";
                }

                winMsg.setText(winPlayer + " Wins!");
                playAgain.setVisibility(View.VISIBLE);
                isGameActive = false;

            } else {
                // check draw condition
                boolean gameOver = true;
                for (int i = 1; i < gameState.length; i++) {
                    if (gameState[i] == 0) {
                        // still moves left
                        gameOver = false;
                        break;
                    }
                }

                if (gameOver) {
                    winMsg.setText("It is a draw!");
                    playAgain.setVisibility(View.VISIBLE);
                    isGameActive = false;
                }
            }
        }
    }

    public void playAgain(View view) {
        playAgain.setVisibility(View.INVISIBLE);
        gameState = new int[10]; // for readability 10 instead of 9

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView img = (ImageView) gridLayout.getChildAt(i);
            img.setImageResource(0); // for no image
        }
        isGameActive = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        winMsg = (TextView) findViewById(R.id.winnerMessage);
        playAgain = (LinearLayout) findViewById(R.id.playAgainLayout);
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
