package clarkson.ee408.tictactoev4;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import java.util.Timer;
import java.util.TimerTask;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TicTacToe tttGame;
    private Button [][] buttons;
    private TextView status;

    private boolean shouldRequestMove;
    private Handler handler;
    private Timer timer;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView(R.layour.activity_main);

        handler = new Handler(Looper.getMainLooper());

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                handler.post(new Runnable()){
                    public void run(){
                        requestMove();
                    }
                });

            }
        }, 0, DELAY);
        tttGame = new TicTacToe(1);
        buildGuiByCode( );
        updateTurnStatus();
        shouldRequestMove = true;
    }

    private void sendMove(Event event){
        SocketClient socketClient = SocketClient.getInstance();
        Request request = new Request(RequestType.SEND_MOVE);
        request.setData(event.getMove);

        socketClient.sendRequest(request);
    }
    public void requestMove(){
        SocketClient socketClient = SocketClient.getInstance();
        Request request = new Request(RequestType.REQUEST_MOVE);
        Response response = socketClient.sendRequest(request);

        if (response != null && response.isValidMove()) {
            update(response.getRow(), response.getCol());
        }else if (!response.isActive()) {
            status.setBackgroundColor( Color.RED );
            status.setText( tttGame.result( ) );
            shouldRequestMove = false;
            tttgame = null;
        }
    }

    public void buildGuiByCode( ) {
        // Get width of the screen
        Point size = new Point( );
        getWindowManager( ).getDefaultDisplay( ).getSize( size );
        int w = size.x / TicTacToe.SIDE;

        // Create the layout manager as a GridLayout
        GridLayout gridLayout = new GridLayout( this );
        gridLayout.setColumnCount( TicTacToe.SIDE );
        gridLayout.setRowCount( TicTacToe.SIDE + 2 );

        // Create the buttons and add them to gridLayout
        buttons = new Button[TicTacToe.SIDE][TicTacToe.SIDE];
        ButtonHandler bh = new ButtonHandler( );

//        GridLayout.LayoutParams bParams = new GridLayout.LayoutParams();
//        bParams.width = w - 10;
//        bParams.height = w -10;
//        bParams.bottomMargin = 15;
//        bParams.rightMargin = 15;

        gridLayout.setUseDefaultMargins(true);

        for( int row = 0; row < TicTacToe.SIDE; row++ ) {
            for( int col = 0; col < TicTacToe.SIDE; col++ ) {
                buttons[row][col] = new Button( this );
                buttons[row][col].setTextSize( ( int ) ( w * .2 ) );
                buttons[row][col].setOnClickListener( bh );
                GridLayout.LayoutParams bParams = new GridLayout.LayoutParams();
//                bParams.width = w - 10;
//                bParams.height = w -40;

                bParams.topMargin = 10;
                bParams.bottomMargin = 10;
                bParams.leftMargin = 10;
                bParams.rightMargin = 10;
                bParams.width=w-20;
                bParams.height=w-20;
                buttons[row][col].setLayoutParams(bParams);
                gridLayout.addView( buttons[row][col]);
//                gridLayout.addView( buttons[row][col], bParams );
            }
        }

        // set up layout parameters of 4th row of gridLayout
        status = new TextView( this );
        GridLayout.Spec rowSpec = GridLayout.spec( TicTacToe.SIDE, 2 );
        GridLayout.Spec columnSpec = GridLayout.spec( 0, TicTacToe.SIDE );
        GridLayout.LayoutParams lpStatus
                = new GridLayout.LayoutParams( rowSpec, columnSpec );
        status.setLayoutParams( lpStatus );

        // set up status' characteristics
        status.setWidth( TicTacToe.SIDE * w );
        status.setHeight( w * 2 );
        status.setGravity( Gravity.CENTER );
        status.setBackgroundColor( Color.BLUE );
        status.setTextSize( ( int )c ( w * .15 ) );
        status.setText( tttGame.result( ) );

        gridLayout.addView( status );

        // Set gridLayout as the View of this Activity
        setContentView( gridLayout );
    }

    private void updateTurnStatus(){
        if (isPlayerTurn) {
            statusTextView.setText("Your Turn");
            enableButtons(true);
        } else{
            statusTextView.setText("Waiting for Opponent");
            enableButtons(false);
        }
    }

    public void update( int row, int col ) {
        int play = tttGame.play( row, col );
        if( play == 1 )
            buttons[row][col].setText( "+" );
        else if( play == 2 )
            buttons[row][col].setText( "-" );
        if( tttGame.isGameOver( ) ) {
            status.setBackgroundColor( Color.ORANGE );
            enableButtons( false );
            status.setText( tttGame.result( ) );
            showNewGameDialog( );	// offer to play again
        }
        isPlayerTurn = !isPlayerTurn;
        updateTurnStatus();
    }

    public void enableButtons( boolean enabled ) {
        for( int row = 0; row < TicTacToe.SIDE; row++ )
            for( int col = 0; col < TicTacToe.SIDE; col++ )
                buttons[row][col].setEnabled( enabled );
    }

    public void resetButtons( ) {
        for( int row = 0; row < TicTacToe.SIDE; row++ )
            for( int col = 0; col < TicTacToe.SIDE; col++ )
                buttons[row][col].setText( "" );
    }

    public void showNewGameDialog( ) {
        AlertDialog.Builder alert = new AlertDialog.Builder( this );
        alert.setTitle( "TicTacToe Game" );
        alert.setMessage( "Do you want to play again?" );
        PlayDialog playAgain = new PlayDialog( );
        alert.setPositiveButton( "YES", playAgain );
        alert.setNegativeButton( "NO", playAgain );
        alert.show( );
    }

    private class ButtonHandler implements View.OnClickListener {
        public void onClick( View v ) {
            Log.d("button clicked", "button clicked");

            for( int row = 0; row < TicTacToe.SIDE; row ++ )
                for( int column = 0; column < TicTacToe.SIDE; column++ )
                    if( v == buttons[row][column] )
                        update( row, column );
        }
    }

    private class PlayDialog implements DialogInterface.OnClickListener {
        public void onClick( DialogInterface dialog, int id ) {
            if( id == -1 ) /* YES button */ {
                tttGame.resetGame( );
                enableButtons( true );
                resetButtons( );
                status.setBackgroundColor( Color.BLUE );
                status.setText( tttGame.result( ) );
                shouldRequestMove = true;
            }
            else if( id == -2 ) // NO button
                MainActivity.this.finish( );
        }
        // Function to send an ABORT_GAME request to the server
        public void abortGame() {
            appExecutors.networkIO().execute(() -> {
                try {
                    // Assuming SocketClient has a method sendRequest for sending requests
                    socketClient.sendRequest("ABORT_GAME");

                    // If successful, show a success message on the UI thread
                    appExecutors.mainThread().execute(() -> showToast("Game aborted successfully"));
                } catch (Exception e) {
                    // If there's an error, show a failure message on the UI thread
                    appExecutors.mainThread().execute(() -> showToast("Failed to abort game: " + e.getMessage()));
                }
            });
        }
        // Function to send a COMPLETE_GAME request to the server
        public void completeGame() {
            appExecutors.networkIO().execute(() -> {
                try {
                    // Assuming SocketClient has a method sendRequest for sending requests
                    socketClient.sendRequest("COMPLETE_GAME");

                    // If successful, show a success message on the UI thread
                    appExecutors.mainThread().execute(() -> showToast("Game completed successfully"));
                } catch (Exception e) {
                    // If there's an error, show a failure message on the UI thread
                    appExecutors.mainThread().execute(() -> showToast("Failed to complete game: " + e.getMessage()));
                }
            });
        }

        // Helper function to show a Toast message
        private void showToast(String message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }

    }





}