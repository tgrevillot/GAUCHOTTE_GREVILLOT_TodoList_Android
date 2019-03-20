package si1.gauchotte_grevillot.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class AddActivity extends AppCompatActivity {
        private DatePicker date;
        private Button bValide, bRetour;
        private int anneeC, moisC, jourC, age = 0, annee = 0, jour = 0, mois = 0;
        private String nom;
        private boolean sexe;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //setContentView(R.layout.calendrier);

        bValide = (Button) findViewById(R.id.valide);
        bRetour = (Button) findViewById(R.id.retour);
        //date = (DatePicker) findViewById(R.id.calendrierTache);

        //anneeC = date.getYear();
        //moisC = date.getMonth();
        //jourC = date.getDayOfMonth();

        /*if(annee != 0){
            date.updateDate(annee, mois, jour);
        }*/

        bRetour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivityForResult(intent, 0);
                finish();
            }
        });


        bValide.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String nomTache = ((TextView) findViewById(R.id.nomTache)).getText().toString();

                if(nomTache.isEmpty()){
                    Snackbar.make(v, "Vous devez entrer un nom de tâche !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else{
                    int item = ((RadioGroup) findViewById(R.id.importanceGroup)).getCheckedRadioButtonId();
                    TodoItem.Tags tag = TodoItem.Tags.Normal;

                    if(item == R.id.radioImp1)
                        tag = TodoItem.Tags.Faible;
                    else if(item == R.id.radioImp2)
                        tag = TodoItem.Tags.Normal;
                    else
                        tag = TodoItem.Tags.Important;

                    TodoItem todoItem = new TodoItem(tag, nomTache);
                    TodoDbHelper.addItem(todoItem, getBaseContext());

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivityForResult(intent, 0);
                    finish();
                }
            }
        });

        /*date.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setContentView(R.layout.calendrier);
            }
        });*/
    }
}
