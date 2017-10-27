package jack.behaviourquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class GroupsActivity extends Activity {

    private ListView ItemListView;
    private TextView TitleView;

    private List<String> items;
    private String GroupName;
    private int GroupNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        Intent groupInfo = getIntent();
        items = groupInfo.getStringArrayListExtra(MainActivity.EXTRA_QUIZ_ITEMS);
        GroupName = groupInfo.getStringExtra(MainActivity.EXTRA_QUIZ_GROUP_NAME);
        GroupNumber = groupInfo.getIntExtra(MainActivity.EXTRA_QUIZ_GROUP_NUMBER, 0);

        TitleView = (TextView) findViewById(R.id.group_title);
        TitleView.setText(GroupName);

        ItemListView = (ListView) findViewById(R.id.group_list);
        ItemListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_items, items));
        ItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent startQuiz = new Intent(GroupsActivity.this, QuizActivity.class);
                startQuiz.putExtra(MainActivity.EXTRA_QUIZ_GROUP_NUMBER, GroupNumber);
                startQuiz.putExtra(MainActivity.EXTRA_QUIZ_ITEM_NUMBER, i);
                startActivity(startQuiz);
            }
        });
    }
}
