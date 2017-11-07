package jack.behaviourquiz;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import static jack.behaviourquiz.MainActivity.TAG;

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
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

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.MyApp_Dialog));
                builder.setTitle(R.string.about_dialog_title);
                builder.setMessage(R.string.about_dialog_message);
                AlertDialog dialog = builder.show();

                int titleDividerID = getResources().getIdentifier("titleDivider", "id", "android");
                View titleDivider = dialog.findViewById(titleDividerID);
                titleDivider.setBackgroundColor(getResources().getColor(R.color.colorAlertDivider));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
