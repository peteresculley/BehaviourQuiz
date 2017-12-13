package jack.behaviourquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import static jack.behaviourquiz.Constants.QUIZ_STATUS_ATTEMPTED;
import static jack.behaviourquiz.Constants.QUIZ_STATUS_CORRECT;

public class PhaseAdapter extends BaseAdapter {

    private Context context;
    private List<Phase> list;
    private LayoutInflater inflater;
    private int resource, groupNumber;

    public PhaseAdapter(Context context, int resource, List<Phase> list, int groupNumber) {
        this.context = context;
        this.list = list;
        this.resource = resource;
        this.groupNumber = groupNumber;
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

        Phase phase = MainActivity.mQuizData.quiz.sections.get(groupNumber).phases.get(i);
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String quizStatus = sharedPref.getString(QuizResultActivity.getQuestionKey(groupNumber, i), "");

        TextView quizText = (TextView) outView.findViewById(R.id.list_items_text);

        quizText.setText(list.get(i).name);

        if(quizStatus == QUIZ_STATUS_CORRECT) {
            quizText.setTextColor(Color.argb(255, 54, 140, 93));
        }
        else if(quizStatus == QUIZ_STATUS_ATTEMPTED) {
            quizText.setTextColor(Color.argb(255, 255, 128, 0));
        }
        else {
            quizText.setTextColor(Color.argb(255, 0, 0, 0));
        }

        return outView;
    }
}
