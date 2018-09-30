package com.bignerdranch.android.geoquiz;

//tip: alt-enter when hovering an error might add the import statement. like here, you did several.
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//this line was imported when you created the instanceState override method:
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import static com.bignerdranch.android.geoquiz.R.id.show_answer_button;

public class CheatActivity extends AppCompatActivity {

    //my code for lesson 5:
    //the "extra" is data that gets passed when an intent is called.
    //not quite like passing to a constructor, but similar in effect.
    //extras go in key-value pairs
    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.bignerdranch.android.geoquiz.answer_is_true";
    //next line is the extra's key:
    private static final String EXTRA_ANSWER_SHOWN =
            "com.bignerdranch.android.geoquiz.answer_shown";

    //this will the variable to store the value of the extra:
    private boolean mAnswerIsTrue;

    //these will enable the buttons for cheating:
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    //constant as to whether user checked the answer: (for screen rotation bug override)
    //it's modelled after the key/value pair in QuizActivity that tracks the index
    private static final String KEY_CHEAT = "USER_CHEATED";
    //variable as to whether it was actually checked:
    private boolean mUserSawAnswer = false;

    //this constant is added to print results to logcat (see log code below)
    private static final String TAG = "CheatActivity";

    //putting the intent and extras method here means other parts of the program
    // don't need to know what it does. it's tidy.
    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    //this method affects OnActivityResult in QuizActivity
    //when someone uses cheat to see the answer
    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        //this will check if user already cheated, and save that info:

        if (savedInstanceState != null) {
            mUserSawAnswer = savedInstanceState.getBoolean(KEY_CHEAT, false);
        }

    //try this instead:
        /*
        if (savedInstanceState != null) {
            if (mUserSawAnswer = true) {
                mUserSawAnswer = savedInstanceState.getBoolean(KEY_CHEAT, false);
            }
        }
        */

        //added for lesson 5 / cheating extra:
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView)findViewById(R.id.answer_text_view);
        mShowAnswerButton = (Button)findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                }
                else {
                    mAnswerTextView.setText(R.string.false_button);
                }

                //sets variable as to whether user saw the answer:
                mUserSawAnswer = true;

                //next line is part of the result sent back to parent:
                //it calls the method below
                setAnswerShownResult(true);
            }
        });
    }

    //this method will preserve state during a screen rotation
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        //fyi the next line is modelled after QuizActivity, but you're getting an error
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_cheat);
        //next part is also modelled after QuizActivity
        //but instead of assigning an index value to the list of questions
        //it says whether the bool is true or false, and saves that info
        if (savedInstanceState != null) {
            savedInstanceState.putBoolean(KEY_CHEAT, mUserSawAnswer);
        };
    }

    //this method lets parent know if child/cheat showed the answer or not
    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}
