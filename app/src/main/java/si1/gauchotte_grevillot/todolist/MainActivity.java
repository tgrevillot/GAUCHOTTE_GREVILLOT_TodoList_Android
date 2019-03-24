package si1.gauchotte_grevillot.todolist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TodoDbHelper tdh;
    private ArrayList<TodoItem> items;
    private RecyclerView recycler;
    private LinearLayoutManager manager;
    private RecyclerAdapter adapter;

    //Utile pour les notifications
    private static String CHANNEL_ID = "channel1";
    private static int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tdh = new TodoDbHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Pour enlever les animations : https://openclassrooms.com/forum/sujet/android-supprimer-l-effet-entre-les-transitions-de-vue-32754
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddActivity.class);
                startActivityForResult(intent, 0);
                finish();
            }
        });

        Log.i("INIT", "Fin initialisation composantes");

        // On récupère les items
        items = TodoDbHelper.getItems(getBaseContext());
        Log.i("INIT", "Fin initialisation items");

        // On initialise le RecyclerView
        recycler = findViewById(R.id.recycler);
        manager = new LinearLayoutManager(this);
        recycler.setLayoutManager(manager);

        adapter = new RecyclerAdapter(items, this);
        recycler.setAdapter(adapter);

        setRecyclerViewItemTouchListener();

        createNotificationChannel();
        Log.i("INIT", "Fin initialisation recycler");

        launchAlarmNotification();
    }

    public void viewAll() {
        Switch sw = this.findViewById(R.id.switch1);
        ImageView imV = this.findViewById(R.id.imageView);
        @SuppressLint("CutPasteId") TextView tv = this.findViewById(R.id.imageView);
    }

    private void createNotificationChannel() {
        // Créer le NotificationChannel, seulement pour API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "AlarmService channel name";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription("AlarmService channel description");
            // Enregister le canal sur le système : attention de ne plus rien modifier après
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }
    }
    public void showNotification(View view, String message) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("TodoList")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManagerCompat.notify(NOTIFICATION_ID, notifBuilder.build());
        NOTIFICATION_ID++;
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.action_settings:
                Intent dbmanager = new Intent(this, AndroidDatabaseManager.class);
                startActivity(dbmanager);
                break;
            case R.id.action_clearDB:
                this.tdh.clearDatabase();
                this.items.clear();
                this.adapter.notifyDataSetChanged();
                break;
            case R.id.action_showNotif:
                showNotification(recycler, "Test de notification");
        }

        return super.onOptionsItemSelected(item);
    }

    public void onStop() {
        //launchAlarmNotification();
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
        launchAlarmNotification();
    }

    private void launchAlarmNotification() {
        startService(new Intent(this, NotificationService.class));
    }

    private void setRecyclerViewItemTouchListener() {
        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
                // Non géré dans cet exemple (ce sont les drags) -> on retourne false
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                TodoItem item = items.get(position);

                switch(swipeDir) {
                    case ItemTouchHelper.RIGHT:
                        item.setDone(true);
                        TodoDbHelper.updateDoneItem(recycler.getContext(), ((RecyclerAdapter.TodoHolder) viewHolder).getLabel(), true);
                    break;
                    case ItemTouchHelper.LEFT:
                        item.setDone(false);
                        TodoDbHelper.updateDoneItem(recycler.getContext(), ((RecyclerAdapter.TodoHolder) viewHolder).getLabel(), false);
                        break;
                }
                recycler.getAdapter().notifyItemChanged(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recycler);
    }
}
