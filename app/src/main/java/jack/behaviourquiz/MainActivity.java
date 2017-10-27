package jack.behaviourquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {

    public static final String EXTRA_QUIZ_GROUP_NAME = "QuizGroupName",
                               EXTRA_QUIZ_GROUP_NUMBER = "QuizGroupNumber",
                               EXTRA_QUIZ_ITEMS = "QuizItemList",
                               EXTRA_QUIZ_ITEM_NUMBER = "QuizItemNumber";

    public static final String TAG = "tagme";
    private ListView GroupListView;

    protected static QuizData mQuizData;

    private List<String> groups;
    private HashMap<String, ArrayList<String>> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareData(R.raw.quizdescription);

        Log.d(TAG, "Started Successfully");

        GroupListView = (ListView) findViewById(R.id.main_list);
        GroupListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_groups, groups));
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
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
