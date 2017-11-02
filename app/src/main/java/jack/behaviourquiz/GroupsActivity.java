package jack.behaviourquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import static jack.behaviourquiz.MainActivity.TAG;

public class GroupsActivity extends BaseActivity {

    private ListView ItemListView;
    private TextView TitleView;

    private String GroupName;
    private int GroupNumber;
    private Section section;
    private boolean[] itemsFinished;
    private PhaseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        Intent groupInfo = getIntent();
        GroupNumber = groupInfo.getIntExtra(MainActivity.EXTRA_QUIZ_GROUP_NUMBER, 0);
        section = MainActivity.mQuizData.quiz.sections.get(GroupNumber);
        GroupName = section.name;

        itemsFinished = new boolean[section.phases.size()];

        TitleView = (TextView) findViewById(R.id.group_title);
        TitleView.setText(GroupName);

        ItemListView = (ListView) findViewById(R.id.group_list);
        mAdapter = new PhaseAdapter(getApplicationContext(), R.layout.list_items, section.phases, GroupNumber);
        ItemListView.setAdapter(mAdapter);
        ItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent startQuiz = new Intent(GroupsActivity.this, QuizActivity.class);
                startQuiz.putExtra(MainActivity.EXTRA_QUIZ_GROUP_NUMBER, GroupNumber);
                startQuiz.putExtra(MainActivity.EXTRA_QUIZ_ITEM_NUMBER, i);
                startActivity(startQuiz);
            }
        });

        updateFinishedItems();
    }

    @Override
    protected void onResume() {
        updateFinishedItems();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "GroupsActivity Pause");
        super.onPause();
    }

    private void updateFinishedItems() {
        Log.d(TAG, "Update Items");
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        for(int i = 0; i < itemsFinished.length; i++) {
            itemsFinished[i] = sharedPref.getBoolean(QuizResultActivity.getQuestionKey(GroupNumber, i), false);
        }
        mAdapter.notifyDataSetChanged();
    }
}
