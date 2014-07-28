package com.ryanflaherty.dynamicsurvey;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;




public class SurveyActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        String result = "fail";
        try {
            JSONObject json = new JSONObject("{\"name\":\"Ryan\"}");
            result = json.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("SURVEY", "Result [" + result + "]");

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

       activitySurveyTableLayout.addView(buildNumberInputLabelPair("Numbers!", activitySurveyTableLayout.getContext()));

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

    private TableRow buildNumberInputLabelPair(String inputName, Context context)
    {
        LinearLayout linearView = new LinearLayout(context);
        linearView.setOrientation(LinearLayout.VERTICAL);
        TextView label = new TextView(context);
        label.setTextAppearance(context, android.R.style.TextAppearance_Medium);
        label.setText(inputName);
        linearView.addView(label);

        EditText textField = new EditText(context);
        textField.setInputType(InputType.TYPE_CLASS_NUMBER);
        linearView.addView(textField);

        return wrapInRow(linearView, context);
    }

    private TableRow buildTextInputLabelPair(String inputName, Context context){
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

        return wrapInRow(linearView, context);
    }

    private TableRow buildPickerLabelPair(String labelName, ArrayList<String> choices, Context context) {
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

        return wrapInRow(linearView, context);
    }

    private TableRow wrapInRow(View view, Context context){
        TableRow theRow = new TableRow(context);
        theRow.addView(view);
        return theRow;
    }
}
