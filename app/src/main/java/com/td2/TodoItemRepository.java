package com.td2;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class TodoItemRepository {
    private DaoItem itemDao;
    private LiveData<List<TodoItem>> items;

    TodoItemRepository(Application app){
        SaveDatabase_Impl db = (SaveDatabase_Impl) SaveDatabase_Impl.getInstance(app);
        itemDao = db.itemDao();
        items = itemDao.getItems();
    }

    LiveData<List<TodoItem>> getItems(){
        return items;
    }

    public void insert(TodoItem item){
        new insertAsyncTask(itemDao).execute(item);
    }

    private static class insertAsyncTask extends AsyncTask<TodoItem, Void, Void> {
        private DaoItem mAsyncTaskDao;

        insertAsyncTask(DaoItem dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TodoItem... params) {
            mAsyncTaskDao.insertItem(params[0]);
            return null;
        }
    }
}
