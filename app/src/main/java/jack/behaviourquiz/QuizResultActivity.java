package jack.behaviourquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

        myQuiz = MainActivity.mQuizData.quiz.sections.get(GroupNumber).phases.get(ItemNumber);
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

        updateQuizRecord();
    }

    private void updateQuizRecord() {
        boolean quizPassed = AnswerCorrect >= AnswerTotal && AnswerWrong == 0;
        if(quizPassed) {
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getQuestionKey(GroupNumber, ItemNumber), true);
            editor.commit();
        }
    }

    protected static String getQuestionKey(int groupNum, int itemNum) {
        return "Quiz " + groupNum + " " + itemNum;
    }

    protected static boolean isQuestionKey(String key) {
        return key.startsWith("Quiz ");
    }
}
