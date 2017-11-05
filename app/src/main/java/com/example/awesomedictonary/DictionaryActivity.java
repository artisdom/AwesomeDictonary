package com.example.awesomedictonary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class DictionaryActivity extends AppCompatActivity {

    // a dictionary of {word -> definition} pairs for lookup
    private Map<String, String> dictionary;
    private List<String> words;

    private void readFileData() {
        Scanner scan = new Scanner(getResources().openRawResource(R.raw.grewords2));
        while (scan.hasNextLine()) {
            //"abate	to lessen to subside"
            String line = scan.nextLine();
            String[] parts = line.split("\t");
            if (parts.length < 2) continue;
            dictionary.put(parts[0], parts[1]);
            words.add(parts[0]);
        }
    }
    // pick the word,
    // pick ~5 random defns for that word (1 is correct)
    // show all of that on screen
    private void chooseWords() {
        Random randy = new Random();
        int randomIndex = randy.nextInt(words.size());
        String theWord = words.get(randomIndex);
        String theDefn = dictionary.get(theWord);

        // pick 4 other (wrong) definitions at random
        List<String> defns = new ArrayList<>(dictionary.values());
        defns.remove(theDefn);
        Collections.shuffle(defns);
        defns = defns.subList(0, 4);
        defns.add(theDefn);
        Collections.shuffle(defns);

        TextView textView = findViewById(R.id.the_word);
        textView.setText(theWord);

        //SimpleList.with(this).setItems(R.id.word_list, defns);
        ListView list = findViewById(R.id.word_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                defns
        );
        list.setAdapter(adapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        dictionary = new HashMap<>();
        words = new ArrayList<>();
        readFileData();

        chooseWords();

        ListView list = findViewById(R.id.word_list);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this, // activity
//                android.R.layout.simple_list_item_1, // layout,
//                (List<String>) dictionary.keySet()
//        );
        //list.setAdapter(adapter);
        //SimpleList.with(this).setItems(list, dictionary.keySet());

        //this is the code that should run when the user taps items in the list
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // look up definition of word and display as a Toast
                //String word = adapterView.getItemAtPosition(i).toString();
                //String defn = dictionary.get(word);
                //toast(defn);

                String defnClicked = adapterView.getItemAtPosition(i).toString();
                TextView textView = findViewById(R.id.the_word);
                String theWord = textView.getText().toString();
                String correctDefn = dictionary.get(theWord);
                if (defnClicked.equals(correctDefn)) {
                    Toast.makeText(getApplicationContext(), "AWESOME!", Toast.LENGTH_SHORT).show();
//                    toast("AWESOME!");
                } else {
                    Toast.makeText(getApplicationContext(), ":-( LOLOLOL duuuh!", Toast.LENGTH_SHORT).show();
                    //toast(":-( LOLOLOL duuuh");
                }
                chooseWords();
            }
        });
    }
}
