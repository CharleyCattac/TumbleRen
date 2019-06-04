package com.spo.tumbleren.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.spo.tumbleren.R;
import com.spo.tumbleren.utils.shared.Preferences;
import static com.spo.tumbleren.utils.shared.Preferences.OnPrefChanged;
import static com.spo.tumbleren.utils.shared.Preferences.getDefaults;

import com.spo.tumbleren.utils.validator.TextValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    boolean nonEmptyBlogFlag = true;
    boolean validTagFlag = true;
    boolean validLimitFlag = true;

    ArrayList<String> types = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SettingsActivity.this.setTitle("Settings");

        fillTypesList();

        initToolbar();
        initActivityWidgets();

        OnPrefChanged = false;
    }

    private void fillTypesList(){
        types.add("");
        types.add("text");
        types.add("photo");
        types.add("link");
        types.add("quote");
        types.add("chat");
        types.add("audio");
        types.add("video");
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initActivityWidgets() {

        final TextView blogDefaultView = findViewById(R.id.blog_default_view);
        blogDefaultView.setVisibility(View.GONE);
        final TextView tagInvalidView = findViewById(R.id.tag_invalid_view);
        tagInvalidView.setVisibility(View.GONE);
        final TextView limitInvalidView = findViewById(R.id.limit_invalid_view);
        limitInvalidView.setVisibility(View.GONE);

        final EditText blogEdit = findViewById(R.id.blog_value);
        blogEdit.addTextChangedListener(new TextValidator(blogEdit) {
            @Override public void validate(TextView textView, String text) {
                if(text.isEmpty()){
                    nonEmptyBlogFlag = false;
                    blogDefaultView.setVisibility(View.VISIBLE);
                    return;
                }
                nonEmptyBlogFlag = true;
                blogDefaultView.setVisibility(View.GONE);
            }
        });
        final Spinner typeSpin = findViewById(R.id.type_value);
        // Connect to spinner for "numberOfNewsDisplay".
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(SettingsActivity.this,
                android.R.layout.simple_list_item_1, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpin.setAdapter(typeAdapter);
        final EditText tagEdit = findViewById(R.id.tag_value);
        tagEdit.addTextChangedListener(new TextValidator(tagEdit) {
            @Override public void validate(TextView textView, String text) {
                if(text.isEmpty()){
                    validTagFlag = true;
                    tagInvalidView.setVisibility(View.GONE);
                    return;
                }
                if(!text.matches((".*[a-zA-Zа-яА-Я]+.*"))){
                    validTagFlag = false;
                    tagInvalidView.setVisibility(View.VISIBLE);
                    return;
                }
                validTagFlag = true;
                tagInvalidView.setVisibility(View.GONE);
            }
        });

        final EditText limitEdit = findViewById(R.id.limit_value);
        limitEdit.addTextChangedListener(new TextValidator(limitEdit) {
            @Override public void validate(TextView textView, String text) {
                if(text.isEmpty()){
                    validLimitFlag = true;
                    limitInvalidView.setVisibility(View.GONE);
                    return;
                }
                try{
                    if (Integer.parseInt(text) > 0 && Integer.parseInt(text) <= 50) {
                        validLimitFlag = true;
                        limitInvalidView.setVisibility(View.GONE);
                        return;
                    }
                    throw new IllegalArgumentException();
                }
                catch (Exception ex) {
                    validLimitFlag = false;
                    limitInvalidView.setVisibility(View.VISIBLE);
                }
            }
        });

        final Button applyButton = findViewById(R.id.settings_button);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validTagFlag && validLimitFlag){
                    int changeCounter = 0;
                    if(!blogEdit.getText().toString().equals(getDefaults("name", SettingsActivity.this))) {
                        Preferences.setDefaults("name", blogEdit.getText().toString(), SettingsActivity.this);
                        changeCounter++;
                    }
                    if(!typeSpin.getSelectedItem().toString().equals(getDefaults("type", SettingsActivity.this))) {
                        Preferences.setDefaults("type", typeSpin.getSelectedItem().toString(), SettingsActivity.this);
                        changeCounter++;
                    }
                    if(!tagEdit.getText().toString().equals(getDefaults("tag", SettingsActivity.this))) {
                        Preferences.setDefaults("tag", tagEdit.getText().toString(), SettingsActivity.this);
                        changeCounter++;
                    }
                    if(!limitEdit.getText().toString().equals(getDefaults("limit", SettingsActivity.this))) {
                        Preferences.setDefaults("limit", limitEdit.getText().toString(), SettingsActivity.this);
                        changeCounter++;
                    }
                    if (changeCounter == 0) {
                        Toast.makeText(SettingsActivity.this, "Nothing changed!", Toast.LENGTH_SHORT).show();
                        OnPrefChanged = false;
                    }
                    else {
                        Toast.makeText(SettingsActivity.this, "Applied!", Toast.LENGTH_SHORT).show();
                    }
                    SettingsActivity.this.onBackPressed();
                }
                else
                    Toast.makeText(SettingsActivity.this, "Invalid arguments!", Toast.LENGTH_SHORT).show();
            }
        });

        Map<String, String> params = loadSharedPreferences();
        String blogValue = params.get("name");
        String typeValue = params.get("type");
        String tagValue = params.get("tag");
        String limitValue = params.get("limit");

        if(blogValue != null)
            blogEdit.setText(blogValue);
        typeSpin.setSelection(types.indexOf(typeValue));
        if(tagValue != null)
            tagEdit.setText(tagValue);
        if(limitValue != null)
            limitEdit.setText(limitValue);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Map<String, String> loadSharedPreferences() {
        Map<String, String> params = new HashMap<>();
        params.put("name", Preferences.getDefaults("name", SettingsActivity.this));
        params.put("type", Preferences.getDefaults("type", SettingsActivity.this));
        params.put("tag", Preferences.getDefaults("tag", SettingsActivity.this));
        params.put("limit", Preferences.getDefaults("limit", SettingsActivity.this));
        return params;
    }

}
