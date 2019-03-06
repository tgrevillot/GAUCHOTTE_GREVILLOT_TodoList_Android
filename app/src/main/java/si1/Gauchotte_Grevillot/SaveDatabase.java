package si1.Gauchotte_Grevillot;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;


@Database(entities = {TodoItem.class}, version = 1, exportSchema = false)
public abstract class SaveDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile SaveDatabase INSTANCE;

    // --- DAO ---
    public abstract DaoItem itemDao();

    // --- INSTANCE ---
    public static SaveDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SaveDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SaveDatabase.class, "Database.db")
                            .addCallback(databaseCallBack)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // ---

    private static RoomDatabase.Callback databaseCallBack =
        new RoomDatabase.Callback() {

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
                new PopulateDbAsync(INSTANCE).execute();
            }

        };




    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final DaoItem dao;

        PopulateDbAsync(SaveDatabase db) {
            dao = db.itemDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            //dao.deleteAll();
            TodoItem item1 = new TodoItem(1, Tags.Important, "Réviser ses cours", false);
            dao.insertItem(item1);
            TodoItem item2 = new TodoItem(2, Tags.Normal, "Acheter du pain", false);
            dao.insertItem(item2);
            return null;
        }
    }

}
