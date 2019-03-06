package si1.Gauchotte_Grevillot;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.td2.R;

import java.util.List;

public class TodoList extends RecyclerView.Adapter<TodoList.TodoListHolder> {

    private List<TodoItem> items;
    private final LayoutInflater inflater;

    public TodoList(Context context){

        inflater = LayoutInflater.from(context);

        /*items.add(new TodoItem(1, Tags.Important, "Réviser ses cours", false));
        items.add(new TodoItem(2, Tags.Normal, "Acheter du pain", false));
        items.add(new TodoItem(3, Tags.Normal, "Marcher 30 mn par jour", false));
        items.add(new TodoItem(Tags.Faible, "Manger 5 fruits et légumes"));
        items.add(new TodoItem(Tags.Normal, "Prendre des nouvelles des parents"));
        items.add(new TodoItem(Tags.Faible, "Préparer la prochaine soirée poker"));
        items.add(new TodoItem(Tags.Normal, "Révoir les premières saisons de Game of thrones"));
        items.add(new TodoItem(Tags.Faible, "Finir la vaisselle"));
        items.add(new TodoItem(Tags.Important, "Acheter un nouveau disque dur"));
        items.add(new TodoItem(Tags.Important, "Revoir les raccourcis clavier"));
        items.add(new TodoItem(Tags.Normal, "Tester une nouvelle techno"));
        items.add(new TodoItem(Tags.Faible, "Tester l'application en cours"));*/

        //this.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TodoListHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.row, parent, false);
        return new TodoListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoList.TodoListHolder holder, int i) {
        TodoItem item = items.get(i);
        int c = Color.BLACK;

        if(item.getTag() == Tags.Faible)
            c = Color.GREEN;
        else if(item.getTag() == Tags.Normal)
            c = Color.YELLOW;
        else if(item.getTag() == Tags.Important)
            c = Color.RED;

        holder.tagImageView.setBackgroundColor(c);
        holder.labelTextView.setText(item.getLabel());
        holder.tacheSwitch.setChecked(item.getTache());
    }

    public void setTodoList(List<TodoItem> todoItems){
        items = todoItems;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        else return 0;
    }


    public class TodoListHolder extends RecyclerView.ViewHolder {
        ImageView tagImageView;
        TextView labelTextView;
        Switch tacheSwitch;
        Context mContext;

        public TodoListHolder(View itemView) {
            super(itemView);
            tagImageView = (ImageView) itemView.findViewById(R.id.tag);
            labelTextView = (TextView) itemView.findViewById(R.id.label);
            tacheSwitch = (Switch) itemView.findViewById(R.id.tache);
        }
    }
}