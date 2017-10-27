package jack.behaviourquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static jack.behaviourquiz.MainActivity.TAG;

public class QuizActivity extends Activity implements View.OnClickListener {

    private int GroupNumber, ItemNumber;
    private int QuestionNumber, CorrectAnswerNumber;
    private int QuestionsCorrect, QuestionsWrong;
    private Random rand;

    private TextView TitleView, ProgressView, QuestionView;
    private TextView[] AnswerView = new TextView[4];

    private Phase myQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        rand = new Random();

        QuestionNumber = 0;
        QuestionsCorrect = 0;
        QuestionsWrong = 0;

        Intent quizInfo = getIntent();
        GroupNumber = quizInfo.getIntExtra(MainActivity.EXTRA_QUIZ_GROUP_NUMBER, 0);
        ItemNumber = quizInfo.getIntExtra(MainActivity.EXTRA_QUIZ_ITEM_NUMBER, 0);
        myQuiz = MainActivity.mQuizData.quiz.sections.get(GroupNumber).phases.get(ItemNumber);

        TitleView = (TextView) findViewById(R.id.quizitem_title);
        ProgressView = (TextView) findViewById(R.id.quizitem_progress);
        QuestionView = (TextView) findViewById(R.id.quizitem_question);
        AnswerView[0] = (TextView) findViewById(R.id.quizitem_answer1);
        AnswerView[1] = (TextView) findViewById(R.id.quizitem_answer2);
        AnswerView[2] = (TextView) findViewById(R.id.quizitem_answer3);
        AnswerView[3] = (TextView) findViewById(R.id.quizitem_answer4);

        for(int i = 0; i < 4; i++) {
            AnswerView[i].setOnClickListener(this);
        }

        initQuizInformation();
    }

    private void initQuizInformation() {
        if(myQuiz.quizquestions.size() <= 0)
            return;
        TitleView.setText(myQuiz.name);
        ProgressView.setText("0 / " + myQuiz.quizquestions.size());
        setQuizQuestion();
    }

    private void setQuizQuestion() {
        QuizQuestion question = myQuiz.quizquestions.get(QuestionNumber);
        QuestionView.setText(question.question);
        CorrectAnswerNumber = rand.nextInt(Math.min(4, question.wrongAnswer.size() + 1));
        int wrongIndex = 0;
        for(int i = 0; i < 4; i++) {
            if(i == CorrectAnswerNumber)
                AnswerView[i].setText(question.correctAnswer);
            else if(wrongIndex < question.wrongAnswer.size()) {
                AnswerView[i].setText(question.wrongAnswer.get(wrongIndex));
                wrongIndex++;
            }
            else
                AnswerView[i].setText("");
        }
    }

    private void endQuiz() {
        Toast.makeText(getApplicationContext(), "Correct: " + QuestionsCorrect + ", Wrong: " + QuestionsWrong, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onClick(View view) {
        int selectedAnswer = 0;
        boolean answeredCorrect = false;
        if(view.getId() == R.id.quizitem_answer1) selectedAnswer = 0;
        if(view.getId() == R.id.quizitem_answer2) selectedAnswer = 1;
        if(view.getId() == R.id.quizitem_answer3) selectedAnswer = 2;
        if(view.getId() == R.id.quizitem_answer4) selectedAnswer = 3;
        if(selectedAnswer > myQuiz.quizquestions.get(QuestionNumber).wrongAnswer.size())
            return; // ignore click
        if(selectedAnswer == CorrectAnswerNumber) {
            answeredCorrect = true;
            QuestionsCorrect++;
        }
        else
            QuestionsWrong++;

        QuestionNumber++;
        if(QuestionNumber >= myQuiz.quizquestions.size())
            endQuiz();
        else {
            setQuizQuestion();
            ProgressView.setText(QuestionNumber + " / " + myQuiz.quizquestions.size());
        }
    }
}
