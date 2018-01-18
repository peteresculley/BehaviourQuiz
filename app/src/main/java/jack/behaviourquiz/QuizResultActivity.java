package jack.behaviourquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static jack.behaviourquiz.Constants.QUIZ_STATUS_ATTEMPTED;
import static jack.behaviourquiz.Constants.QUIZ_STATUS_CORRECT;

public class QuizResultActivity extends BaseActivity {

    private int GroupNumber, ItemNumber;
    private int AnswerCorrect, AnswerWrong, AnswerTotal;
    private Phase myQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent resultInfo = getIntent();
        GroupNumber = resultInfo.getIntExtra(MainActivity.EXTRA_QUIZ_GROUP_NUMBER, 0);
        ItemNumber = resultInfo.getIntExtra(MainActivity.EXTRA_QUIZ_ITEM_NUMBER, 0);
        AnswerCorrect = resultInfo.getIntExtra(MainActivity.EXTRA_QUIZ_CORRECT, 0);
        AnswerWrong = resultInfo.getIntExtra(MainActivity.EXTRA_QUIZ_WRONG, 0);

        if(GroupNumber >= 0 && ItemNumber >= 0) {
            myQuiz = MainActivity.mQuizData.quiz.sections.get(GroupNumber).phases.get(ItemNumber);
        } else {
            myQuiz = MainActivity.RandomQuiz;
        }
        AnswerTotal = myQuiz.quizquestions.size();

        TextView TitleView = (TextView) findViewById(R.id.quizresult_title);
        TextView ResultView = (TextView) findViewById(R.id.quizresult_result);
        Button EndButton = (Button) findViewById(R.id.quizresult_endbutton);

        TitleView.setText(myQuiz.name);
        if(AnswerTotal == AnswerCorrect + AnswerWrong)
            ResultView.setText(AnswerCorrect + " / " + AnswerTotal);
        else if(AnswerTotal > AnswerCorrect + AnswerWrong)
            ResultView.setText(AnswerCorrect + " / " + AnswerTotal + "\nUnanswered: " + (AnswerTotal - AnswerCorrect - AnswerWrong));
        else
            ResultView.setText(AnswerTotal + " / " + AnswerTotal);

        EndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(GroupNumber >= 0 && ItemNumber >= 0) {
            updateQuizRecord();
        }
    }

    private void updateQuizRecord() {
        boolean quizPassed = AnswerCorrect >= AnswerTotal && AnswerWrong == 0;
        if(quizPassed) {
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getQuestionKey(GroupNumber, ItemNumber), QUIZ_STATUS_CORRECT);
            editor.commit();
        } else {
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            if(!sharedPref.getString(getQuestionKey(GroupNumber, ItemNumber), "").equals(QUIZ_STATUS_CORRECT)) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getQuestionKey(GroupNumber, ItemNumber), QUIZ_STATUS_ATTEMPTED);
                editor.commit();
            }
        }
    }

    protected static String getQuestionKey(int groupNum, int itemNum) {
        return "QuizKey " + groupNum + " " + itemNum;
    }

    protected static boolean isQuestionKey(String key) {
        return key.startsWith("QuizKey ");
    }
}
