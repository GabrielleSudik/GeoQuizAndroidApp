//this file is a controller

package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
//the textbook neglected to tell you to import Intent for lesson 5:
//it didn't mention importing several of these statements, but you figured it out.
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class QuizActivity extends AppCompatActivity {

    //the next line is to log changes in the status:
    //this is for lesson 4 and I don't think it's usually used:
    private static final String TAG = "QuizActivity";
    //next line part of fixing the rotation reset bug:
    private static final String KEY_INDEX = "index";
    //next line sends an "id" called a request code to the child activity
    //this request code identifies which child is sending info back to the parent
    //not needed when only one child, but good practice, and needed when more than one
    private static final int REQUEST_CODE_CHEAT = 0;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    //next line (2) is for cheat activity:
    private Button mCheatButton;
    private boolean mIsCheater;
    private TextView mQuestionTextView;

    //this is a way to populate the array of questions
    //not best solution for very long arrays/lists.
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
            //textbook showed a comma after the last line, but I think that's wrong?
            //I tried putting it in, didn't get an error.
    };

    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //next line is for logging status:
        Log.d(TAG, "onCreate(Bundle)called");
        setContentView(R.layout.activity_quiz);
        //next block added to save state when re-created on rotation:
        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        //listeners on true and false buttons:
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        //move to next question button:
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                //next line is about the child cheat activity:
                mIsCheater = false;
                updateQuestion();
            }
        });

        //lesson 5 / cheat code starts here

        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //start CheatActivity
                //Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                //that last line was "moved" to its own method in CheatActivity.java
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                //startActivity(intent);
                //instead of just starting the activity with that last line
                //well start it and send the result code that identifies which child gets the command
                startActivityForResult(intent, REQUEST_CODE_CHEAT);

            }
        });

        updateQuestion();
    }

    //lesson 5 cheating -- method reports cheating report from child

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    //lesson 4 override codes start here
    //it's for logging steps of the activity

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    //when i first wrote this code, it was definitely "public"
    // later, the book quotes this block as being "protected". typo??

    //to save state when re-created due to screen rotation:
    //note the non-override method above is protected, although the text in chapt 3 says public
    //tinker with those if you have problems
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSavedInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    //moves to next question in array:
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    //check answers:
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        //this part will print a shaming statement if someone cheated:
        if(mIsCheater){
            messageResId = R.string.judgment_toast;
        }
        else{
            if (userPressedTrue == answerIsTrue){
                messageResId = R.string.correct_toast;
            }
            else{
                messageResId = R.string.incorrect_toast;
            }
        }

        //the following was here before you also added the shaming statement
        //it's not part of the preceding block. delete later.
        /*
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
        }
        */

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();
    }
}
