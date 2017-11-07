package jack.behaviourquiz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import java.util.Map;

public class SettingsFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.MyApp_Dialog));
        builder.setMessage("Reset Progress?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        Map<String, ?> allEntries = sharedPref.getAll();
                        SharedPreferences.Editor editor = sharedPref.edit();
                        for(Map.Entry<String, ?> entry : allEntries.entrySet())
                            if(QuizResultActivity.isQuestionKey(entry.getKey()))
                                editor.putBoolean(entry.getKey(), false);
                        editor.commit();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ;
                    }
                });
        return builder.create();
    }
}
