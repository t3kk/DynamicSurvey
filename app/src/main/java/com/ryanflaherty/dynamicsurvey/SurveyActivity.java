package com.ryanflaherty.dynamicsurvey;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;



public class SurveyActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        //Grab the main view for this layout
        TableLayout activitySurveyTableLayout = (TableLayout)findViewById(R.id.activitySurveyTableLayout);
        //Add input for first name and last name
        activitySurveyTableLayout.addView(buildTextInputLabelPair("First Name", activitySurveyTableLayout.getContext()));
        activitySurveyTableLayout.addView(buildTextInputLabelPair("Last Name", activitySurveyTableLayout.getContext()));
        ArrayList<String> types = new ArrayList<String>();
        types.add("red");
        types.add("blue");
        types.add("green");
        activitySurveyTableLayout.addView(buildPickerLabelPair("types", types, activitySurveyTableLayout.getContext()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.survey, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private TableRow buildTextInputLabelPair(String inputName, Context context){
        //Make a row to hold a linear layout for input
        TableRow theRow = new TableRow(context);
        //Build the linear layout
        LinearLayout linearView = new LinearLayout(context);
        linearView.setOrientation(LinearLayout.VERTICAL);
        //Add the label
        TextView label = new TextView(context);
        label.setTextAppearance(context, android.R.style.TextAppearance_Medium);
        label.setText(inputName);
        linearView.addView(label);
        //Add the field
        EditText textField = new EditText(context);
        textField.setHint(inputName);
        linearView.addView(textField);
        //Add linear layout to the row
        theRow.addView(linearView);

        return theRow;
    }

    private TableRow buildPickerLabelPair(String labelName, ArrayList<String> choices, Context context) {
        TableRow theRow = new TableRow(context);
        //Build the linear layout
        LinearLayout linearView = new LinearLayout(context);
        linearView.setOrientation(LinearLayout.VERTICAL);
        //Make a label
        TextView label = new TextView(context);
        label.setTextAppearance(context, android.R.style.TextAppearance_Medium);
        label.setText(labelName);
        linearView.addView(label);

        //Make the picker
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, choices);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = new Spinner(context);
        spinner.setAdapter(arrayAdapter);
        linearView.addView(spinner);

        theRow.addView(linearView);
        return  theRow;
    }
}
