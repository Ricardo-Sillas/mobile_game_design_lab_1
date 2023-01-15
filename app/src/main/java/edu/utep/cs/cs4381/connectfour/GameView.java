package edu.utep.cs.cs4381.connectfour;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import edu.utep.cs.cs4381.connectfour.model.Board;
import edu.utep.cs.cs4381.connectfour.model.Player;

public class GameView extends View {

    private float width, height; // width and height of this view in pixels
    private Board board;

// Players information
    private Player playerOne = new Player("Red");
    private Player playerTwo = new Player("Yellow");
    private Player currentPlayer = playerOne;
    private int count = 0;

// For custom view
    public GameView(Context context) {
        super(context);
        calculateWidthAndHeight();
    }

// Calculates the width and height of screen
    private void calculateWidthAndHeight() {
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    width = Math.min(getWidth(), getHeight());
                    height = Math.max(getWidth(), getHeight());
                }
            });
        }
    }

// Colors used for the UI
    private Paint border = new Paint();
    private Paint red = new Paint();
    private Paint yellow = new Paint();
    private Paint boardColor = new Paint();
    private Paint text = new Paint();
    private Paint space = new Paint();

// The view for the game
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

// Setting the colors
        border.setColor(Color.BLACK);
        red.setColor(Color.RED);
        yellow.setColor(Color.YELLOW);
        boardColor.setColor(Color.BLUE);
        space.setColor(Color.WHITE);

// Boxes are width/8 by width/8 and disks have radius of width/20.
// Board is width/16 away from left, right and bottom of screen
// Creating the vertical lines
        canvas.drawRect(width/16,height-(13*width)/16,(width*15)/16,height-(width/16), boardColor);
        for(int i = 0; i < 8; i++) {
            canvas.drawLine(width/16+((i*width)/8), height-(13*width)/16, width/16+((i*width)/8), height-(width/16), border);
        }

// Creating horizontal lines and floating disks
        for(int i = 0; i < 7; i++) {
            canvas.drawLine(width/16, height-(13*width)/16+((i*width)/8), (width*15)/16, height-(13*width)/16+((i*width)/8),  border);
            if(currentPlayer.name().equals("Red")) {
                canvas.drawCircle(((i + 1) * width) / 8, height - (14 * width) / 16, width / 20, red);
            }
            else {
                canvas.drawCircle(((i+1)*width)/8, height-(14*width)/16, width/20, yellow);
            }
        }
// For the empty spaces in the board
        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < 6; j++) {
                canvas.drawCircle(((i+1)*width)/8, height-(12*width)/16+((j*width)/8), width/20, space);
            }
        }

// Players avatar and name (Red and Yellow)
        text.setTextSize(100);
        text.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Red", (width/10) + 100, (width/5) + 100, text);
        canvas.drawCircle(width/10 + 100, width/10 + 20, width/10, red);
        canvas.drawText("Yellow", width - (width/10) - 100 , (width/5) + 100, text);
        canvas.drawCircle(width-(width/10) - 100, width/10 + 20, width/10, yellow);

// Updating board with discs being placed
        for(int i = 6; i >= 0; i--) {
            for(int j = 5; j >= 0; j--) {
                if(board.isOccupied(i, j) && board.playerAt(i, j).name().equals("Red")) {
                    canvas.drawCircle(((i+1)*width)/8, height-(12*width)/16+((j*width)/8), width/20, red);
                }
                else if(board.isOccupied(i, j) && board.playerAt(i, j).name().equals("Yellow")) {
                    canvas.drawCircle(((i+1)*width)/8, height-(12*width)/16+((j*width)/8), width/20, yellow);
                }
            }
        }

// Text for when game is over
        if(board.isFull()) {
            canvas.drawText("Draw", width/2, width/5 + 200, text);
        }
//        else if(board.isWonBy(currentPlayer)) {
//            String win = currentPlayer.name() + " wins";
//            canvas.drawText(win, width/2, width/5 + 200, text);
//        }
        else if(!board.isWonBy(currentPlayer)) {
            String win = currentPlayer.name() + "'s turn";
            canvas.drawText(win, width/2, width/5 + 200, text);
        }
    }

// Setting up board
    public void setBoard(Board board) {
        this.board = board;
    }

    public interface DiscClickListener {
        void clicked(int index);
    }

    private DiscClickListener discClickListener;

    public void setDiscClickListener(DiscClickListener listener) {
        discClickListener = listener;
    }

// Senses touch and gets where it was touched on the screen
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                int index = locateDisc(event.getX(), event.getY());
                if (index >= 0) {
                    discClickListener.clicked(index);
                // Way to take turns
                    if(currentPlayer == playerOne) {
                        currentPlayer = playerTwo;
                    }
                    else{
                        currentPlayer = playerOne;
                    }}
        }  
        return true;
    }

// Location of discs
    public int locateDisc(float x, float y) {
        for(int i = 0; i < 7; i++) {
            if(isIn(x, y,((i+1)*width)/8, height-(14*width)/16, width/20)== true) {
                return i;
            }
        }
        return -1;
    }


// Checks if the discs were touched
    private static boolean isIn(float x, float y, float cX, float cY, float radius) {
        float dx = x - cX;
        float dy = y - cY;
        return dx * dx + dy * dy <= radius * radius;
    }
}