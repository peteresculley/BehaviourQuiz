package jack.behaviourquiz;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import static jack.behaviourquiz.MainActivity.TAG;

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_settings:
                SettingsFragment settingsFragment = new SettingsFragment();
                settingsFragment.show(getFragmentManager(), "settingsFragment");
                return true;

            case R.id.menu_home:
                if(getClass() != MainActivity.class) {
                    Intent goHomeIntent = new Intent(getApplicationContext(), MainActivity.class);
                    goHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    goHomeIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(goHomeIntent);
                }

                return true;

            case R.id.menu_about:
                new AboutFragment().show(getFragmentManager(), "aboutFragment");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class AboutFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle("About")
                    .setMessage("Behavior Analysis teaching tool")
                    .create();
        }
    }
}
