package si1.Gauchotte_Grevillot;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DaoItem {

    @Query("SELECT * FROM TodoItem WHERE id = :id")
    LiveData<List<TodoItem>> getItem(long id);

    @Query("SELECT * FROM TodoItem")
    LiveData<List<TodoItem>> getItems();

    @Insert
    long insertItem(TodoItem item);

    @Update
    int updateItem(TodoItem item);

    @Query("DELETE FROM TodoItem WHERE id = :id")
    int deleteItem(long id);

}
