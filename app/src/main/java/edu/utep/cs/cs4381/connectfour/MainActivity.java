package edu.utep.cs.cs4381.connectfour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import edu.utep.cs.cs4381.connectfour.model.Board;
import edu.utep.cs.cs4381.connectfour.model.Player;

public class MainActivity extends AppCompatActivity {

    private Board board;
    private GameView gameView;

// Players information
    public Player playerOne = new Player("Red");
    public Player playerTwo = new Player("Yellow");
    public Player currentPlayer = playerOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        board = new Board();

        gameView = new GameView(this);
        gameView.setBoard(board);
        setContentView(gameView);

// Senses tapping and continues until game is over
        gameView.setDiscClickListener(i ->
        {boolean gameOver = board.isWonBy(currentPlayer) || board.isFull();
            if (!gameOver && board.isColumnOpen(i)) {
                board.dropDisc(i, currentPlayer);
                if (board.isWonBy(currentPlayer)) {
                    gameOver = true; // win
                } else if (board.isFull()) {
                    gameOver = true; // draw
                } else {
                    if(currentPlayer == playerOne) {
                        currentPlayer = playerTwo;
                    }
                    else {
                        currentPlayer = playerOne;
                    }
                }
                gameView.invalidate();}
        });
    }
}