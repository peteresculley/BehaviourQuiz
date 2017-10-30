package jack.behaviourquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends BaseActivity {

    public static final String EXTRA_QUIZ_GROUP_NAME = "QuizGroupName",
                               EXTRA_QUIZ_GROUP_NUMBER = "QuizGroupNumber",
                               EXTRA_QUIZ_ITEMS = "QuizItemList",
                               EXTRA_QUIZ_ITEM_NUMBER = "QuizItemNumber",
                               EXTRA_QUIZ_CORRECT = "QuizResultCorrect",
                               EXTRA_QUIZ_WRONG = "QuizResultWrong";

    public static final String TAG = "tagme";
    private ListView GroupListView;

    protected static QuizData mQuizData;

    private ArrayList<String> groups;
    private HashMap<String, ArrayList<String>> items;
    private QuizAdapter mAdapter;
    private boolean[] itemsFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareData(R.raw.quizdescription);

        Log.d(TAG, "Started Successfully");

        mAdapter = new QuizAdapter(getApplicationContext(), R.layout.list_groups, groups);

        GroupListView = (ListView) findViewById(R.id.main_list);
        GroupListView.setAdapter(mAdapter);
        GroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent startGroups = new Intent(MainActivity.this, GroupsActivity.class);
                startGroups.putExtra(EXTRA_QUIZ_GROUP_NAME, groups.get(i));
                startGroups.putExtra(EXTRA_QUIZ_GROUP_NUMBER, i);
                startGroups.putStringArrayListExtra(EXTRA_QUIZ_ITEMS, items.get(groups.get(i)));
                startActivity(startGroups);
            }
        });
    }

    @Override
    protected void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

    private void prepareData(int jsonResourceID) {
        groups = new ArrayList<String>();
        items = new HashMap<String, ArrayList<String>>();

        mQuizData = new Gson().fromJson(new InputStreamReader(getResources().openRawResource(jsonResourceID)), QuizData.class);
        for(Section sec : mQuizData.quiz.sections) {
            groups.add(sec.name);
            ArrayList<String> ite = new ArrayList<String>();
            for(Phase pha : sec.phases)
                ite.add(pha.name);
            items.put(sec.name, ite);
        }

        itemsFinished = new boolean[groups.size()];
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        for(int i = 0; i < itemsFinished.length; i++) {
            boolean isComplete = true;
            for(int j = 0; j < items.get(groups.get(i)).size(); j++)
                isComplete &= sharedPref.getBoolean(QuizResultActivity.getQuestionKey(i, j), false);
            itemsFinished[i] = isComplete;
        }
    }
}
