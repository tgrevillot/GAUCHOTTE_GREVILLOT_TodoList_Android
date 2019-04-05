package si1.gauchotte_grevillot.todolist;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifActivity extends AppCompatActivity {
    private Button bValide, bRetour, date, time;
    private String dateTxt, timeTxt, tacheTxt, impTxt;
    private long idTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        bValide = findViewById(R.id.valide);
        bRetour = findViewById(R.id.retour);
        date = findViewById(R.id.btn_date);
        time = findViewById(R.id.btn_time);
        TextView tache = findViewById(R.id.nomTache);
        TextView dateT = findViewById(R.id.date);
        TextView timeT = findViewById(R.id.time);
        RadioGroup rG = findViewById(R.id.importanceGroup);

        idTxt = Long.parseLong(getIntent().getStringExtra("id"));
        dateTxt = getIntent().getStringExtra("date");
        timeTxt = getIntent().getStringExtra("time");
        tacheTxt = getIntent().getStringExtra("tache");
        impTxt = getIntent().getStringExtra("importance");

        date.setOnClickListener(new date_timeListener(this));
        time.setOnClickListener(new date_timeListener(this));
        tache.setText(tacheTxt);
        dateT.setText(dateTxt);
        timeT.setText(timeTxt);

        if(impTxt.equals(TodoItem.Tags.Faible.getDesc()))
            rG.check(R.id.radioImp1);
        else if(impTxt.equals(TodoItem.Tags.Important.getDesc()))
            rG.check(R.id.radioImp3);
        else
            rG.check(R.id.radioImp2);


        bRetour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivityForResult(intent, 0);
                finish();
            }
        });


        bValide.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(idTxt != 0){
                    String nomTache = ((TextView) findViewById(R.id.nomTache)).getText().toString();
                    String dateT = ((TextView) findViewById(R.id.date)).getText().toString();
                    String timeT = ((TextView) findViewById(R.id.time)).getText().toString();

                    if (nomTache.isEmpty()) {
                        Snackbar.make(v, "Vous devez entrer un nom de tâche !", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else if ((dateT.isEmpty()) || (timeT.isEmpty())) {
                        Snackbar.make(v, "Vous devez entrer une date et une heure pour la tâche !", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {

                        Date dateTime = null;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
                        dateFormat.setLenient(false);
                        try {
                            dateTime = dateFormat.parse(dateT + " " + timeT);
                        } catch (Exception e) {
                            Snackbar.make(v, "Le format de la date ou de l'heure n'est pas correct !", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                        int item = ((RadioGroup) findViewById(R.id.importanceGroup)).getCheckedRadioButtonId();
                        TodoItem.Tags tag;

                        if (item == R.id.radioImp1)
                            tag = TodoItem.Tags.Faible;
                        else if (item == R.id.radioImp3)
                            tag = TodoItem.Tags.Important;
                        else
                            tag = TodoItem.Tags.Normal;

                        TodoItem todoItem = new TodoItem(nomTache, tag, dateTime);
                        TodoDbHelper.updateItem(idTxt, todoItem, getBaseContext());

                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivityForResult(intent, 0);
                        finish();
                    }

                } else{
                    Snackbar.make(v, "La tâche n'a pas éta reconnue !\nVeuillez retourner à la liste des tâches et réessayer.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }
}

