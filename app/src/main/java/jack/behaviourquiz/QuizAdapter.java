package jack.behaviourquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QuizAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private LayoutInflater inflater;
    private int resource;

    public QuizAdapter(Context context, int resource, List<String> list) {
        this.context = context;
        this.list = list;
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View outView = view;
        if(view == null) {
            outView = inflater.inflate(resource, null);
        }

        Section sec = MainActivity.mQuizData.quiz.sections.get(i);
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int numComplete = 0;
        for(int j = 0; j < sec.phases.size(); j++) {
            if(sharedPref.getBoolean(QuizResultActivity.getQuestionKey(i, j), false)) {
                numComplete++;
            }
        }

        TextView QuizText = (TextView) outView.findViewById(R.id.list_group_text);

        QuizText.setText(list.get(i));
        ((TextView)outView.findViewById(R.id.list_group_rating_text)).setText(numComplete + "/" + sec.phases.size());

        if(numComplete >= sec.phases.size()) {
            QuizText.setTextColor(Color.argb(255, 54, 140, 93));
        }
        else {
            QuizText.setTextColor(Color.argb(255, 0, 0, 0));
        }

        return outView;
    }
}
