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
import com.spo.tumbleren.utils.shared.Shared;
import static com.spo.tumbleren.utils.shared.Shared.OnPrefChanged;
import static com.spo.tumbleren.utils.shared.Shared.getDefaults;

import com.spo.tumbleren.utils.validator.TextValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    boolean nonEmptyBlogFlag = true;
    boolean validTagFlag = true;
    boolean validLimitFlag = true;

    ArrayList<String> types = new ArrayList<>();

    private Toolbar toolbar;
    private EditText blogEdit;
    private Spinner typeSpin;
    private EditText tagEdit;
    private EditText limitEdit;
    private TextView blogDefaultView;
    private TextView tagInvalidView;
    private TextView limitInvalidView;
    private Button applyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SettingsActivity.this.setTitle("Settings");

        fillTypesList();

        initToolbar();
        initActivityWidgets();
        setWidgetHints();

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
        toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initActivityWidgets() {
        blogDefaultView = findViewById(R.id.blog_default_view);
        blogDefaultView.setVisibility(View.GONE);
        tagInvalidView = findViewById(R.id.tag_invalid_view);
        tagInvalidView.setVisibility(View.GONE);
        limitInvalidView = findViewById(R.id.limit_invalid_view);
        limitInvalidView.setVisibility(View.GONE);

        blogEdit = findViewById(R.id.blog_value);
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
        typeSpin = findViewById(R.id.type_value);
        // Connect to spinner for "numberOfNewsDisplay".
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(SettingsActivity.this,
                android.R.layout.simple_list_item_1, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpin.setAdapter(typeAdapter);
        tagEdit = findViewById(R.id.tag_value);
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
        limitEdit = findViewById(R.id.limit_value);
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

        applyButton = findViewById(R.id.settings_button);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validTagFlag && validLimitFlag){
                    int changeCounter = 0;
                    if(!blogEdit.getText().toString().equals(getDefaults("name", SettingsActivity.this)
                            .substring(0, getDefaults("name", SettingsActivity.this).indexOf(".tumblr")))) {
                        Shared.setDefaults("name", blogEdit.getText().toString() + ".tumblr.com", SettingsActivity.this);
                        changeCounter++;
                    }
                    if(!typeSpin.getSelectedItem().toString().equals(getDefaults("type", SettingsActivity.this))) {
                        Shared.setDefaults("type", typeSpin.getSelectedItem().toString(), SettingsActivity.this);
                        changeCounter++;
                    }
                    if(!tagEdit.getText().toString().equals(getDefaults("tag", SettingsActivity.this))) {
                        Shared.setDefaults("tag", tagEdit.getText().toString(), SettingsActivity.this);
                        changeCounter++;
                    }
                    if(!limitEdit.getText().toString().equals(getDefaults("limit", SettingsActivity.this))) {
                        Shared.setDefaults("limit", limitEdit.getText().toString(), SettingsActivity.this);
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
    }


    private void setWidgetHints() {
        Map<String, String> params = loadSharedPreferences();
        String blogValue = params.get("name");
        String typeValue = params.get("type");
        String tagValue = params.get("tag");
        String limitValue = params.get("limit");

         if(blogValue != null)
            blogEdit.setText(blogValue.substring(0, blogValue.indexOf(".tumblr")));

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
        params.put("name", Shared.getDefaults("name", SettingsActivity.this));
        params.put("type", Shared.getDefaults("type", SettingsActivity.this));
        params.put("tag", Shared.getDefaults("tag", SettingsActivity.this));
        params.put("limit", Shared.getDefaults("limit", SettingsActivity.this));
        return params;
    }

}
