package si1.Gauchotte_Grevillot;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class TodoItemViewModel extends AndroidViewModel {

    private TodoItemRepository repository;
    private LiveData<List<TodoItem>> items;

    public TodoItemViewModel(@NonNull Application application) {
        super(application);
        repository = new TodoItemRepository(application);
        items = repository.getItems();
    }

    public LiveData<List<TodoItem>> getItems(){
        return items;
    }

    public void insert(TodoItem item){
        repository.insert(item);
    }
}
