package jack.behaviourquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    public static final String EXTRA_QUIZ_GROUP_NUMBER = "QuizGroupNumber",
                               EXTRA_QUIZ_ITEM_NUMBER = "QuizItemNumber",
                               EXTRA_QUIZ_CORRECT = "QuizResultCorrect",
                               EXTRA_QUIZ_WRONG = "QuizResultWrong";

    public static final String TAG = "tagme";
    private GridView GroupGridView;

    protected static QuizData mQuizData;
    protected static Resources resources;

    private SectionAdapter mAdapter;
    private boolean[] itemsFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareData(R.raw.quizdescription);

        Log.d(TAG, "Started Successfully");
        Log.i(TAG, "Number of Sections: " + mQuizData.quiz.sections.size());

        resources = getResources();

        ArrayList<Drawable> icons = new ArrayList<Drawable>();
        icons.add(resources.getDrawable(R.drawable.icon_measurement));
        icons.add(resources.getDrawable(R.drawable.icon_experimental_design));
        icons.add(resources.getDrawable(R.drawable.icon_behavior_change_considerations));
        icons.add(resources.getDrawable(R.drawable.icon_fundamental_elements));
        icons.add(resources.getDrawable(R.drawable.icon_specific_procedures));
        icons.add(resources.getDrawable(R.drawable.icon_behavior_change_systems));
        icons.add(resources.getDrawable(R.drawable.icon_identification_of_the_problem));
        icons.add(resources.getDrawable(R.drawable.icon_measurement2));
        icons.add(resources.getDrawable(R.drawable.icon_assessment));
        icons.add(resources.getDrawable(R.drawable.icon_intervention));
        icons.add(resources.getDrawable(R.drawable.icon_implementation_and_managment));

        mAdapter = new SectionAdapter(getApplicationContext(), R.layout.list_groups, icons, mQuizData.quiz.sections);

        GroupGridView = (GridView) findViewById(R.id.main_list);
        GroupGridView.setAdapter(mAdapter);
        GroupGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent startGroups = new Intent(MainActivity.this, GroupsActivity.class);
                startGroups.putExtra(EXTRA_QUIZ_GROUP_NUMBER, i);
                startActivity(startGroups);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

    private void prepareData(int jsonResourceID) {

        mQuizData = new Gson().fromJson(new InputStreamReader(getResources().openRawResource(jsonResourceID)), QuizData.class);
        for(Section section : mQuizData.quiz.sections) {
            for(Phase phase : section.phases) {
                for(int i = 0; i < phase.quizquestions.size(); i++) {
                    phase.quizquestions.get(i).order = i;
                }
            }
        }

        itemsFinished = new boolean[mQuizData.quiz.sections.size()];
        updateItemFinished();
    }

    private void updateItemFinished() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        for(int i = 0; i < itemsFinished.length; i++) {
            boolean isComplete = true;
            for(int j = 0; j < mQuizData.quiz.sections.get(i).phases.size(); j++)
                isComplete &= sharedPref.getString(QuizResultActivity.getQuestionKey(i, j), "").equals(Constants.QUIZ_STATUS_CORRECT);
            itemsFinished[i] = isComplete;
        }
    }

    public static int getRatingColor() {
        if(resources == null) return 0;
        return resources.getColor(android.R.color.darker_gray);
    }
}
