package com.lubenard.dotscreentest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView timerTextView;
    private TextView currPlayerTextView;
    private TextView scoreTextView;

    private Player currentPlayer;

    private Player playerOne;
    private Player playerTwo;

    private boolean isPartyRunning = false;

    private CountDownTimer countDownTimer;

    private Context context;

    private char[] board = {0, 0, 0, 0, 0, 0, 0, 0, 0};

                                        // Horizontal possibilities
    private int[][] allPossibilities = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                                        // Vertical possibilities
                                        {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                                        // both diagonales
                                        {0, 4, 8}, {2, 4, 6}};

    private ArrayList<ImageView> clickedViews = new ArrayList<>();  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timerTextView);
        currPlayerTextView = findViewById(R.id.currPlayerTextView);
        scoreTextView = findViewById(R.id.scoreTextView);

        playerOne = new Player("Tom", R.drawable.circle, 'a');
        playerTwo = new Player("Boby", R.drawable.cross, 'b');

        context = this;

        changeCurrPlayer(playerOne);

        showScore();

        launchTimer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void changeCurrPlayer(Player newCurrPlayer) {
        currentPlayer = newCurrPlayer;
        currPlayerTextView.setText("Current Player: " + currentPlayer.getPlayerName());
    }

    private void launchTimer() {
        countDownTimer = new CountDownTimer(180000, 1000) {

            public void onTick(long millisRemaining) {
                if (!isPartyRunning)
                    isPartyRunning = true;
                long secondRemaining = (millisRemaining / 1000) % 60;
                long minutesRemaining = (millisRemaining / (1000 * 60)) % 60;
                timerTextView.setText("Timer: " + minutesRemaining + ":" + secondRemaining);
            }

            public void onFinish() {
                timerTextView.setText("Party is over !");

                isPartyRunning = false;

                String winner;
                if (playerOne.getScore() > playerTwo.getScore())
                    winner = playerOne.getPlayerName();
                else if (playerTwo.getScore() > playerOne.getScore())
                    winner = playerTwo.getPlayerName();
                else
                    winner = "Its' a draw!";

                new AlertDialog.Builder(context).setTitle("And the winner is: ")
                        .setMessage("And the final winner is: " + winner + "! Congratulations !")
                        .setPositiveButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_alert).show();
            }
        };
    }

    private void resetBoard() {
        // reset Ui board
        for (int i = 0; i < clickedViews.size(); i++) {
            clickedViews.get(i).setImageResource(0);
        }
        // reset board
        for (int j = 0; j < board.length; j++) {
            board[j] = 0;
        }
    }

    private void showScore() {
        scoreTextView.setText("Score: " + playerOne.getPlayerName() + ": " + playerOne.getScore() +
                              " " + playerTwo.getPlayerName() + ": " + playerTwo.getScore());
    }

    private boolean checkWin() {
        for (int i = 0; i < allPossibilities.length; i++) {
            if (board[allPossibilities[i][0]] == currentPlayer.getUniqueId() &&
            (board[allPossibilities[i][1]] == currentPlayer.getUniqueId()) &&
            (board[allPossibilities[i][2]] == currentPlayer.getUniqueId())) {
                currentPlayer.increaseScore();
                showScore();
                Toast.makeText(this, currentPlayer.getPlayerName() + " won a point!", Toast.LENGTH_SHORT).show();
                // Clean board
                resetBoard();
                // Player one is set to it's place !
                changeCurrPlayer(playerOne);
                return true;
            }
        }
        return false;
    }

    public void pieceHandler(View v) {

        // Getting tag from XML
        String tagFromXml = (String)v.getTag();
        int tagFromXmlInt = Integer.parseInt(tagFromXml);

        if (!isPartyRunning)
            Toast.makeText(this, "The party has not started! Click on the play Button", Toast.LENGTH_SHORT).show();
        else if (board[tagFromXmlInt] == 0) {
            board[tagFromXmlInt] = currentPlayer.getUniqueId();
            ((ImageView) v).setImageResource(currentPlayer.getImageId());
            clickedViews.add((ImageView)v);

            // Detection algorythm
            if (checkWin() == false) {
                // Change player
                changeCurrPlayer((currentPlayer == playerOne) ? playerTwo : playerOne);
            }
        } else
            Toast.makeText(this, "This piece is already taken !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.playButton:
                countDownTimer.start();
            default:
                return false;
        }
    }
}