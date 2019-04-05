package si1.gauchotte_grevillot.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TodoHolder> {

    private ArrayList<TodoItem> items;
    private Activity activity;

    public RecyclerAdapter(ArrayList<TodoItem> items, Activity act) {
        this.items = items;
        activity = act;
    }

    @Override
    public TodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new TodoHolder(inflatedView, activity);
    }

    @Override
    public void onBindViewHolder(TodoHolder holder, int i) {
        TodoItem item = items.get(i);

        int c = Color.BLACK;

        if(item.getTag().equals(TodoItem.Tags.Faible))
            c = Color.GREEN;
        else if(item.getTag().equals(TodoItem.Tags.Normal))
            c = Color.YELLOW;
        else if(item.getTag().equals(TodoItem.Tags.Important))
            c = Color.RED;

        holder.image.setBackgroundColor(c);
        holder.image.setTag(item.getTag());
        holder.label.setText(item.getLabel());
        holder.id.setText(item.getId() + "");
        holder.sw.setChecked(item.isDone());

        String dateTime = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
        dateFormat.setLenient(false);
        try {
            dateTime = dateFormat.format(item.getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.dateTime.setText(dateTime);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class TodoHolder extends RecyclerView.ViewHolder {
        private Activity act;
        private Resources resources;
        private ImageView image;
        private Switch sw;
        private TextView label, dateTime, id;
        private View view;

        public TodoHolder(View itemView, Activity a) {
            super(itemView);

            view = itemView;

            act = a;
            image = itemView.findViewById(R.id.imageView);
            sw = itemView.findViewById(R.id.switch1);
            label = itemView.findViewById(R.id.textView);
            dateTime = itemView.findViewById(R.id.dateTime);
            id = itemView.findViewById(R.id.id);
            resources = itemView.getResources();

            addListenerLongClickItem();
            addListenerClickSwitch();
            addListenerClickItem();
        }

        public String getLabel() {
            return this.label.getText().toString();
        }

        public void addListenerClickSwitch() {
            this.sw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Switch sw2 = v.findViewById(R.id.switch1);
                    TodoDbHelper.updateDoneItem(act.getBaseContext(), Long.parseLong(id.getText().toString()), sw2.isChecked());

                    //Changement de la couleur du Background lors du clic sur le switch
                    LinearLayout ll = itemView.findViewById(R.id.itemLigne);
                    if(ll == null)
                        Log.d("RecyclerAdapter", "LinearLayout null Error");
                    if(ll != null)
                    if(sw2.isChecked())
                        ll.setBackgroundColor(Color.GRAY);
                    else
                        ll.setBackgroundColor(Color.WHITE);
                }
            });
        }

        public void addListenerLongClickItem() {

        }

        public void addListenerClickItem(){
            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(v.getContext(), v);
                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            switch(item.getItemId()){
                                case(R.id.modif):
                                    Intent intent = new Intent(act.getBaseContext(), ModifActivity.class);
                                    intent.putExtra("date", dateTime.getText().toString().split(" ")[0]);
                                    intent.putExtra("time", dateTime.getText().toString().split(" ")[1]);
                                    intent.putExtra("tache", label.getText().toString());
                                    intent.putExtra("id", id.getText().toString());

                                    String tag = TodoItem.Tags.Normal.getDesc();
                                    if(image.getTag().equals(TodoItem.Tags.Faible))
                                        tag = TodoItem.Tags.Faible.getDesc();
                                    else if(image.getTag().equals(TodoItem.Tags.Important))
                                        tag = TodoItem.Tags.Important.getDesc();

                                    intent.putExtra("importance", tag);
                                    act.startActivityForResult(intent, 0);
                                    break;

                                case(R.id.suppr):
                                    new AlertDialog.Builder(view.getContext())
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setMessage("Êtes-vous sûr de vouloir supprimer cet item ?")
                                            .setTitle("Suppression d'item")
                                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    TodoDbHelper.removeItem(act.getBaseContext(), Long.parseLong(id.getText().toString()));
                                                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                                                    act.startActivityForResult(intent, 0);
                                                    act.finish();
                                                }
                                            })
                                            .setNegativeButton("Non", null)
                                            .show();

                                    break;
                            }

                            return true;
                        }
                    });

                    popup.show();
                }
            });
        }

        public void bindTodo(TodoItem todo) {
            label.setText(todo.getLabel());
            dateTime.setText(todo.getDate().toString());
            sw.setChecked(todo.isDone());
            id.setText(todo.getId() + "");

            switch(todo.getTag()) {
                case Faible:
                    image.setBackgroundColor(resources.getColor(R.color.faible));
                    break;
                case Normal:
                    image.setBackgroundColor(resources.getColor(R.color.normal));
                    break;
                case Important:
                    image.setBackgroundColor(resources.getColor(R.color.important));
                    break;

            }
        }

    }
}
