package jack.behaviourquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Random;

import static jack.behaviourquiz.MainActivity.TAG;

public class QuizActivity extends BaseActivity implements View.OnClickListener, DialogInterface.OnDismissListener {

    public static final String EXPLANATION_TEXT = "explanationText";

    private int GroupNumber, ItemNumber;
    private int QuestionNumber, CorrectAnswerNumber;
    private int QuestionsCorrect, QuestionsWrong;
    private boolean allowClicks;
    private boolean firstAttempt;
    private Random rand;

    private TextView TitleView, ProgressView, QuestionView;
    private ImageView QuestionImageView;
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
        firstAttempt = true;

        Intent quizInfo = getIntent();
        GroupNumber = quizInfo.getIntExtra(MainActivity.EXTRA_QUIZ_GROUP_NUMBER, 0);
        ItemNumber = quizInfo.getIntExtra(MainActivity.EXTRA_QUIZ_ITEM_NUMBER, 0);
        myQuiz = MainActivity.mQuizData.quiz.sections.get(GroupNumber).phases.get(ItemNumber);

        TitleView = (TextView) findViewById(R.id.quizitem_title);
        ProgressView = (TextView) findViewById(R.id.quizitem_progress);
        QuestionView = (TextView) findViewById(R.id.quizitem_question);
        QuestionImageView = (ImageView) findViewById(R.id.quizitem_questionimage);
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
        Collections.shuffle(myQuiz.quizquestions);
        setQuizQuestion();
    }

    private void setQuizQuestion() {
        QuizQuestion question = myQuiz.quizquestions.get(QuestionNumber);
        Collections.shuffle(question.wrongAnswer);
        QuestionView.setText(question.question);
        Resources res = getResources();
        String imageFilename = ((char) ('a' + GroupNumber)) + "_" + TwoCharString(ItemNumber + 1) + "_" + TwoCharString(question.order + 1);
        int imageFileID = res.getIdentifier(imageFilename, "drawable", this.getPackageName());
        Log.i(TAG, "Question Image: filename: " + imageFilename + " , ID: " + imageFileID);
        ViewGroup.LayoutParams layoutParams = QuestionImageView.getLayoutParams();
        if(imageFileID != 0) {
            layoutParams.height = 400;
            QuestionImageView.setImageResource(imageFileID);
        } else {
            layoutParams.height = 0;
        }
        QuestionImageView.setLayoutParams(layoutParams);
        CorrectAnswerNumber = rand.nextInt(Math.min(4, question.wrongAnswer.size() + 1));
        int wrongIndex = 0;
        for(int i = 0; i < 4; i++) {
            if(i == CorrectAnswerNumber)
                AnswerView[i].setText((i+1) + ". " + question.correctAnswer);
            else if(wrongIndex < question.wrongAnswer.size()) {
                AnswerView[i].setText((i+1) + ". " + question.wrongAnswer.get(wrongIndex));
                wrongIndex++;
            }
            else
                AnswerView[i].setText("");
        }

        int noColor = Color.argb(0,0,0,0);
        AnswerView[0].setBackgroundColor(noColor);
        AnswerView[1].setBackgroundColor(noColor);
        AnswerView[2].setBackgroundColor(noColor);
        AnswerView[3].setBackgroundColor(noColor);

        allowClicks = true;
    }

    private void endQuiz() {
        Intent resultIntent = new Intent(getApplicationContext(), QuizResultActivity.class);
        resultIntent.putExtra(MainActivity.EXTRA_QUIZ_GROUP_NUMBER, GroupNumber);
        resultIntent.putExtra(MainActivity.EXTRA_QUIZ_ITEM_NUMBER, ItemNumber);
        resultIntent.putExtra(MainActivity.EXTRA_QUIZ_CORRECT, QuestionsCorrect);
        resultIntent.putExtra(MainActivity.EXTRA_QUIZ_WRONG, QuestionsWrong);
        startActivity(resultIntent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if(!allowClicks) {
            return;
        }

        int selectedAnswer = 0;
        boolean answeredCorrect = false;
        if(view.getId() == R.id.quizitem_answer1) selectedAnswer = 0;
        if(view.getId() == R.id.quizitem_answer2) selectedAnswer = 1;
        if(view.getId() == R.id.quizitem_answer3) selectedAnswer = 2;
        if(view.getId() == R.id.quizitem_answer4) selectedAnswer = 3;
        if(selectedAnswer > myQuiz.quizquestions.get(QuestionNumber).wrongAnswer.size())
            return; // ignore click

        allowClicks = false;
        view.setBackgroundColor(Color.parseColor("#66111111"));

        if(selectedAnswer == CorrectAnswerNumber) {
            if(firstAttempt) {
                answeredCorrect = true;
                QuestionsCorrect++;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveToNextQuestion();
                }
            }, 200);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.MyApp_Dialog));
            builder.setTitle(R.string.quiz_explanation_title);
            builder.setMessage(myQuiz.quizquestions.get(QuestionNumber).explanation);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    final Activity activity = QuizActivity.this;
                    if(activity instanceof DialogInterface.OnDismissListener) {
                        ((DialogInterface.OnDismissListener)activity).onDismiss(dialog);
                    }
                }
            });
            AlertDialog dialog = builder.show();

            int titleDividerID = getResources().getIdentifier("titleDivider", "id", "android");
            View titleDivider = dialog.findViewById(titleDividerID);
            titleDivider.setBackgroundColor(getResources().getColor(R.color.colorAlertDivider));
            if(firstAttempt) {
                QuestionsWrong++;
            }
        }
        firstAttempt = false;
    }

    protected void moveToNextQuestion() {
        QuestionNumber++;
        if(QuestionNumber >= myQuiz.quizquestions.size())
            endQuiz();
        else {
            setQuizQuestion();
            firstAttempt = true;
            ProgressView.setText(QuestionNumber + " / " + myQuiz.quizquestions.size());
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        //moveToNextQuestion();
        allowClicks = true;
    }

    private String TwoCharString(int i) {
        if(i < 0)
            return "-0";
        if(i < 10)
            return "0" + i;
        if(i < 100)
            return "" + i;
        return "" + (i % 100);
    }
}
