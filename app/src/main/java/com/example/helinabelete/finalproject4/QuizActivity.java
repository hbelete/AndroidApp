package com.example.helinabelete.finalproject4;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.helinabelete.finalproject4.PreferenceChangeListener;
import com.example.helinabelete.finalproject4.QuizViewModel;

import java.util.Set;
public class QuizActivity extends AppCompatActivity{
    public static final String CHOICES = "pref_numberOfChoices";
    public static final String REGIONS = "pref_regionsToInclude";
    private boolean deviceIsPhone = true;
    private boolean preferencesChanged = true;
    private QuizFragment quizFragment;
    private QuizViewModel quizViewModel;
    private OnSharedPreferenceChangeListener preferencesChangeListener;

    private void setSharedPreferences() {
        // set default values in the app's SharedPreferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // Register a listener for shared preferences changes
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(preferencesChangeListener);
    }

    private void screenSetUp() {
        if (getScreenSize() == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                getScreenSize() == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            deviceIsPhone = false;
        }
        if (deviceIsPhone) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.quizViewModel = ViewModelProviders.of(this).get(QuizViewModel.class);
        this.preferencesChangeListener = new PreferenceChangeListener(this);
        setContentView(R.layout.activity_quiz);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setSharedPreferences();
        this.screenSetUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (preferencesChanged) {
            this.quizFragment = (QuizFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.quizFragment);
            this.quizViewModel.setGuessRows(PreferenceManager.getDefaultSharedPreferences(this)
                    .getString(CHOICES, null));
            this.quizViewModel.setRegionsSet(PreferenceManager.getDefaultSharedPreferences(this)
                    .getStringSet(REGIONS, null));

            this.quizFragment.resetQuiz();

            preferencesChanged = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent preferencesIntent = new Intent(this, SettingsActivity.class);
                startActivity(preferencesIntent);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public int getScreenSize() {
        return getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
    }

    public QuizFragment getQuizFragment() {
        return this.quizFragment;
    }

    public QuizViewModel getQuizViewModel() {
        return quizViewModel;
    }

    public static String getCHOICES() {
        return CHOICES;
    }

    public static String getREGIONS() {
        return REGIONS;
    }

    public void setPreferencesChanged(boolean preferencesChanged) {
        this.preferencesChanged = preferencesChanged;
    }


}
