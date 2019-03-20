package si1.gauchotte_grevillot.todolist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
        private Button bValide, bRetour, date, time;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        bValide = (Button) findViewById(R.id.valide);
        bRetour = (Button) findViewById(R.id.retour);
        date = (Button) findViewById(R.id.btn_date);
        time = (Button) findViewById(R.id.btn_time);

        date.setOnClickListener(new date_timeListener(this));
        time.setOnClickListener(new date_timeListener(this));

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
    }
}


class date_timeListener implements View.OnClickListener{

    Activity act;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    public date_timeListener(Activity a){
        act = a;
    }

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.btn_date) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            txtDate = act.findViewById(R.id.date);

            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v.getId() == R.id.btn_time) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            txtTime = act.findViewById(R.id.time);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}