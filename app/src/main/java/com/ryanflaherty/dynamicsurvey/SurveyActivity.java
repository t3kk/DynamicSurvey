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

import org.json.JSONArray;
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
        String jsonString = "{\"1\":{\"text\":\"First Name\"},\"2\":{\"number\":\"Zip Code\"},\"3\":{\"picker\":{\"label\":\"LIST!\",\"list\":[\"blah\",\"meh\"]}}}";


        //Grab the main view for this layout
        TableLayout activitySurveyTableLayout = (TableLayout)findViewById(R.id.activitySurveyTableLayout);
        //Keeping the create clean, moved this testing crap to a private method
        String[][] inputs = parseFromJSON(jsonString);
        createInputViews(inputs, activitySurveyTableLayout);
        //Add input for first name and last name
//        activitySurveyTableLayout.addView(buildTextInputLabelPair("First Name", activitySurveyTableLayout.getContext()));
//        activitySurveyTableLayout.addView(buildTextInputLabelPair("Last Name", activitySurveyTableLayout.getContext()));
//
//        ArrayList<String> types = new ArrayList<String>();
//        types.add("red");
//        types.add("blue");
//        types.add("green");
//        activitySurveyTableLayout.addView(buildPickerLabelPair("types", types, activitySurveyTableLayout.getContext()));
//        activitySurveyTableLayout.addView(buildNumberInputLabelPair("Numbers!", activitySurveyTableLayout.getContext()));
    }

    private void createInputViews(String[][] inputs, TableLayout tableLayout){
        for (int i = 0; i < inputs.length; i++){
            //This will be a series of ugly if, else statements until i think of a pretty way, too bad android isn't java 1.7...
            String type = inputs[i][0];
            String label = inputs[i][1];
            //TODO: Add constants for input types
            if ("text".equalsIgnoreCase(type)){
                tableLayout.addView(buildTextInputLabelPair(label, tableLayout.getContext()));
            }else if ("number".equalsIgnoreCase(type)) {
                tableLayout.addView(buildNumberInputLabelPair(label, tableLayout.getContext()));
            }else if ("picker".equalsIgnoreCase(type)){
                //In this case, label is actually JSON...
                try {
                    JSONObject json = new JSONObject(label);
                    label = json.getString("label");
                    JSONArray jsonArray = json.getJSONArray("list");
                    //Convert it to an ArrayList
                    ArrayList<String> arrayList = new ArrayList<String>();
                    for (int c = 0; c<jsonArray.length(); c++){
                        arrayList.add(jsonArray.get(c).toString());
                    }
                    tableLayout.addView(buildPickerLabelPair(label, arrayList, tableLayout.getContext()));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(this.getClass().getSimpleName(), "Problem parsing JSON for list picker");
                }
            }else{

                //This is probably going to be unreachable, since I hope to validate in the json part.
                Log.e(this.getClass().getSimpleName(), "ERROR, input type not defined: ["+inputs[i][0]+"]");
            }
        }
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

    //TODO: I can't use string[][] because the list picker won't fit into that.
    private String[][] parseFromJSON(String jsonString){
        //A hashmap I plan to use later...
        HashMap<String, JSONObject> hashMap = new HashMap<String, JSONObject>();
        //Set to empty array so I don't have to null check it.  Let me know if this is bad...
        String[][] inputs = new String[0][0];
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
            //Now we know how big the array must be
            inputs = new String[hashMap.size()][2];
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
                    //Store into the array and switch to use 0 for 1;
                    inputs[i-1][0] = inputType;
                    inputs[i-1][1] = innerJSON.getString(inputType);
                }else{
                    Log.e(this.getClass().getSimpleName(), "Improperly formatted JSON");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return inputs;
    }
}
