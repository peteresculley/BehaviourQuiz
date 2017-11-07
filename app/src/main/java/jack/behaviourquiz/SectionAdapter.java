package jack.behaviourquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static jack.behaviourquiz.MainActivity.TAG;

public class SectionAdapter extends BaseAdapter {

    private Context context;
    private List<Section> list;
    private LayoutInflater inflater;
    private int resource;
    private ArrayList<Drawable> icons;

    public SectionAdapter(Context context, int resource, ArrayList<Drawable> icons, List<Section> list) {
        this.context = context;
        this.list = list;
        this.resource = resource;
        this.icons = icons;
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
        ImageView iconView = ((ImageView)outView.findViewById(R.id.list_group_image));
        iconView.setImageDrawable(icons.get(i));

        Section sec = list.get(i);
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int numComplete = 0;
        for(int j = 0; j < sec.phases.size(); j++) {
            if(sharedPref.getBoolean(QuizResultActivity.getQuestionKey(i, j), false)) {
                numComplete++;
            }
        }

        TextView RatingText = (TextView) outView.findViewById(R.id.list_group_rating_text);

        RatingText.setText(numComplete + "/" + sec.phases.size());

        if(numComplete >= sec.phases.size()) {
            RatingText.setTextColor(Color.argb(255, 54, 140, 93));
        }
        else {
            RatingText.setTextColor(MainActivity.getRatingColor());
        }

        return outView;
    }
}
