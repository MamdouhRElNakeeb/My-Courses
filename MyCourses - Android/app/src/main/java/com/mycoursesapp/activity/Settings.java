package com.mycoursesapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.mycoursesapp.BuildConfig;
import com.mycoursesapp.R;
import com.mycoursesapp.helper.LocaleManager;

/**
 * Created by mamdouhelnakeeb on 2/20/18.
 */

public class Settings extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.settings_activity;
    }

    Spinner languagesS;

    TextView versionNoTV;
    TextView aboutTV, privacyTV;

    int lang = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        languagesS = findViewById(R.id.languagesS);

        initLanguages();

        setupToolbar();

        versionNoTV = findViewById(R.id.versionNoTV);

        versionNoTV.setText(BuildConfig.VERSION_NAME);


        findViewById(R.id.aboutTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://mycoursesapp.com/about";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

        findViewById(R.id.termsTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://mycoursesapp.com/terms";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

    }

    private void setNewLocale(String language) {
        LocaleManager.setNewLocale(this, language);

        Intent i = new Intent(this, Home.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    private void initLanguages(){

        ArrayAdapter<CharSequence> rstArrAdapter = ArrayAdapter.createFromResource(this, R.array.languages, R.layout.spinner_item);

        languagesS.setAdapter(rstArrAdapter);

        languagesS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (lang != i){

                    lang = i;
                    AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                    builder.setMessage(getString(R.string.change_lang))
                            .setTitle(getResources().getString(R.string.app_name))
                            .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    String langCode = "en";
                                    switch (lang){
                                        case 1:
                                            langCode = "en";
                                            break;
                                        case 2:
                                            langCode = "ar";
                                    }

                                    setNewLocale(langCode);

                                }
                            })
                            .setNegativeButton(getString(R.string.no), null);

                    final AlertDialog dialog = builder.create();

                    dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.grey_dark));
                        }
                    });
                    dialog.show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setupToolbar(){

        TextView toolbarTV = findViewById(R.id.toolbarTV);

        toolbarTV.setText(R.string.settings);

    }

}
