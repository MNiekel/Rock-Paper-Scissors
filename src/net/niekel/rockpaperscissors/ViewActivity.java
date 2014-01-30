package net.niekel.rockpaperscissors;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.graphics.Point;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;

import java.util.Random;
import java.util.HashMap;

public class ViewActivity extends Activity
{
    static final String STATE_SCORE_PLAYER = "scorePlayer";
    static final String STATE_SCORE_ANDROID = "scoreAndroid";
    static final String STATE_RESULT = "resultText";
    static final String STATE_PLAYER = "playerID";
    static final String STATE_ANDROID = "androidID";
    static final String STATE_COLOR = "resultColor";

    private TextView msg;
    private HashMap<Point, String> messages = new HashMap<Point, String>();
    private TextView resultText;
    private TextView scoreText;
    private ImageView playerChoice;
    private ImageView androidChoice;
    private int playerID;
    private int androidID;
    private int scorePlayer;
    private int scoreAndroid;
    private int resultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.images);

        initViews(savedInstanceState);
        setScoreView();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        scorePlayer = savedInstanceState.getInt(STATE_SCORE_PLAYER);
        scoreAndroid = savedInstanceState.getInt(STATE_SCORE_ANDROID);
        resultText.setText(savedInstanceState.getString(STATE_RESULT));
        playerID = savedInstanceState.getInt(STATE_PLAYER);
        androidID = savedInstanceState.getInt(STATE_ANDROID);
        resultColor = savedInstanceState.getInt(STATE_COLOR);

        playerChoice.setImageResource(playerID);
        androidChoice.setImageResource(androidID);
        ((LinearLayout) findViewById(R.id.resultlayout)).setBackgroundColor(resultColor);
        setScoreView();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putInt(STATE_SCORE_PLAYER, scorePlayer);
        savedInstanceState.putInt(STATE_SCORE_ANDROID, scoreAndroid);
        savedInstanceState.putString(STATE_RESULT,
                                    (String) resultText.getText());
        savedInstanceState.putInt(STATE_PLAYER, playerID);
        savedInstanceState.putInt(STATE_ANDROID, androidID);
        savedInstanceState.putInt(STATE_COLOR, resultColor);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.reset:
                resetScore();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initViews(Bundle saved)
    {
        resultText = (TextView) findViewById(R.id.result);
        scoreText = (TextView) findViewById(R.id.score);
        msg = (TextView) findViewById(R.id.msg);
        playerChoice = (ImageView) findViewById(R.id.playerchoice);
        androidChoice = (ImageView) findViewById(R.id.androidchoice);

        createMessageMap();
    }

    private void updateMsg(String tMsg)
    {
        msg.setText(tMsg);
    }

    public void debug(String s)
    {
        Toast.makeText(this, "Debug: " + s, Toast.LENGTH_SHORT).show();
    }

    public void handleTouch(float x, float y, int touch)
    {
        updateMsg("Touched@ " + x + " " + y);
        if (touch >= 0) { doAction(touch); }
    }

    private void resetScore()
    {
        scorePlayer = 0;
        scoreAndroid = 0;
        setScoreView();
    }

    private void setScoreView()
    {
        scoreText.setText(scorePlayer + " - " + scoreAndroid);
    }

    private void doAction(int player)
    {
        int android = new Random().nextInt(5);
        int res = evaluateChoices(player, android);

        playerID = getResources().getIdentifier(getName(player)+"_t",
                                            "drawable", getPackageName());
        androidID = getResources().getIdentifier(getName(android)+"_t",
                                            "drawable", getPackageName());
        playerChoice.setImageResource(playerID);
        androidChoice.setImageResource(androidID);
        setResultLayout(res);

        resultText.setText(getMessage(player, android, res));
        scoreText.setText(scorePlayer + " - " + scoreAndroid);
    }

    private void setResultLayout(int res)
    {
        LinearLayout l = (LinearLayout) findViewById(R.id.resultlayout);
        if (res == 0) { resultColor = Color.YELLOW; }
        if (res == 1) { resultColor = Color.GREEN; }
        if (res == 2) { resultColor = Color.RED; }
        l.setBackgroundColor(resultColor);
    }

    private String getMessage(int player, int android, int res)
    {
        String s = "";
        if (res == 0) { s = "Draw!"; }
        if (res == 1)
        {
            s = messages.get(new Point(player, android));
            s = s + " => You win!";
            scorePlayer += 1;
        }
        if (res == 2)
        {
            s = messages.get(new Point(android, player));
            s = s + " => Android wins!";
            scoreAndroid += 1;
        }
        return s;
    }

    private void createMessageMap()
    {
        messages.put(new Point(0, 3), getString(R.string.rocklizard));
        messages.put(new Point(0, 4), getString(R.string.rockscissors));
        messages.put(new Point(1, 0), getString(R.string.spockrock));
        messages.put(new Point(1, 4), getString(R.string.spockscissors));
        messages.put(new Point(2, 0), getString(R.string.paperrock));
        messages.put(new Point(2, 1), getString(R.string.paperspock));
        messages.put(new Point(3, 1), getString(R.string.lizardspock));
        messages.put(new Point(3, 2), getString(R.string.lizardpaper));
        messages.put(new Point(4, 2), getString(R.string.scissorspaper));
        messages.put(new Point(4, 3), getString(R.string.scissorslizard));
    }

    private int evaluateChoices(int player, int android)
    {
        return (((5 + player - android) % 5) + 1) / 2;
    }

    private String getName(int i)
    {
        switch (i)
        {
            case 0: return "rock";
            case 1: return "spock";
            case 2: return "paper";
            case 3: return "lizard";
            case 4: return "scissors";
        }
        return "";
    }
}
