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
import java.util.HashMap;
import java.util.Iterator;

public class SurveyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        //Make some JSON
        String jsonString = "{\"1\":{\"name\":\"Ryan\"},\"2\":{\"test\":\"other\"}}";
        //Keeping the create clean, moved this testing crap to a private method
        parseFromJSON(jsonString);

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

    private TableRow buildNumberInputLabelPair(String labelName, Context context)
    {
        EditText textField = new EditText(context);
        textField.setInputType(InputType.TYPE_CLASS_NUMBER);

        return wrapLabelAndInput(labelName, textField, context);
    }

    private TableRow buildTextInputLabelPair(String labelName, Context context){
        //create the field
        EditText textField = new EditText(context);
        textField.setHint(labelName);

        return wrapLabelAndInput(labelName, textField, context);
    }

    private TableRow buildPickerLabelPair(String labelName, ArrayList<String> choices, Context context) {
        //Make the picker
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, choices);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = new Spinner(context);
        spinner.setAdapter(arrayAdapter);

        return wrapLabelAndInput(labelName, spinner, context);
    }

    private TableRow wrapLabelAndInput(String labelName, View inputView, Context context){
        //Build the linear layout
        LinearLayout linearView = new LinearLayout(context);
        linearView.setOrientation(LinearLayout.VERTICAL);
        //Make a label
        TextView label = new TextView(context);
        label.setTextAppearance(context, android.R.style.TextAppearance_Medium);
        label.setText(labelName);
        //Add the label and input to the linear view
        linearView.addView(label);
        linearView.addView(inputView);
        //Wrap it in a row
        TableRow theRow = new TableRow(context);
        theRow.addView(linearView);
        return theRow;
    }

    //TODO: return a list in order from 1 and up, with both the type and label inside.  Maybe String[][] for now if im lazy?
    private void parseFromJSON(String jsonString){
        HashMap<String, JSONObject> hashMap = new HashMap<String, JSONObject>();
        try {
            JSONObject json = new JSONObject(jsonString);
            //Go through each item in the json
            Iterator<?> jsonIterator = json.keys();
            while (jsonIterator.hasNext()){
                String key = jsonIterator.next().toString();
                if (json.get(key) instanceof JSONObject){
                    hashMap.put(key, (JSONObject)json.get(key));
                }
            }
            Log.d(this.getClass().getSimpleName(), "Finished adding to hashmap");
            for (Integer i = 1; i <= hashMap.size(); i++){
                JSONObject innerJSON = hashMap.get(i.toString());
                //verify that there is only 1 key and that is is something we are expecting
                Iterator<?> keyIterator = innerJSON.keys();
                int counter = 0;
                String inputType = null;
                while(keyIterator.hasNext()){
                    inputType = keyIterator.next().toString();
                    //Increment counter
                    counter++;
                }
                if (counter == 1){
                    Log.d(this.getClass().getSimpleName(), "Input type found: "+inputType);
                }else{
                    Log.e(this.getClass().getSimpleName(), "Improperly formatted JSON");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
