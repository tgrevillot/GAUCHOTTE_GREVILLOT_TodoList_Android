package si1.gauchotte_grevillot.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by phil on 11/02/17.
 */

public class TodoDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "todo.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TodoContract.TodoEntry.TABLE_NAME + " (" +
                    TodoContract.TodoEntry._ID + " INTEGER PRIMARY KEY," +
                    TodoContract.TodoEntry.COLUMN_NAME_LABEL + " TEXT," +
                    TodoContract.TodoEntry.COLUMN_NAME_TAG + " TEXT,"  +
                    TodoContract.TodoEntry.COLUMN_NAME_DONE +  " INTEGER," +
                    TodoContract.TodoEntry.COLUMN_NAME_DATE + " DATE)";

    public TodoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Rien pour le moment
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TodoContract.TodoEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public static void removeItem(Context context, long id) {
        TodoDbHelper tdh = new TodoDbHelper(context);
        SQLiteDatabase sql = tdh.getReadableDatabase();
        sql.execSQL("DELETE FROM items WHERE " + TodoContract.TodoEntry._ID + " = '" + id + "'");
        tdh.close();
    }

    static ArrayList<TodoItem> getItem(Context context, long id){
        TodoDbHelper tdh = new TodoDbHelper(context);
        SQLiteDatabase sql = tdh.getReadableDatabase();

        // Création de la projection souhaitée
        String[] projection = {
                TodoContract.TodoEntry._ID,
                TodoContract.TodoEntry.COLUMN_NAME_LABEL,
                TodoContract.TodoEntry.COLUMN_NAME_TAG,
                TodoContract.TodoEntry.COLUMN_NAME_DONE,
                TodoContract.TodoEntry.COLUMN_NAME_DATE
        };

        String condition = TodoContract.TodoEntry._ID + " = '" + id + "'";

        Cursor cursor = sql.query(
                TodoContract.TodoEntry.TABLE_NAME,
                projection,
                condition,
                null,
                null,
                null,
                null
        );//"SELECT label FROM items WHERE label like '" + label + "'");

        ArrayList<TodoItem> items = new ArrayList<TodoItem>();

        while (cursor.moveToNext()) {
            String l = cursor.getString(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_NAME_LABEL));
            TodoItem.Tags tag = TodoItem.getTagFor(cursor.getString(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_NAME_TAG)));
            boolean done = (cursor.getInt(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_NAME_DONE)) == 1);

            Date dateTime = null;
            try{
               SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
               dateTime = format.parse(cursor.getString(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_NAME_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            TodoItem item = new TodoItem(id, l, tag, done, dateTime);
            items.add(item);
        }

        // Ménage
        tdh.close();

        // Retourne le résultat
        return items;
    }

    /*static boolean isIntTable(Context context, String label){
        ArrayList<TodoItem> items = getItem(context, label);

        return (items.size() != 0);
    }*/

    static ArrayList<TodoItem> getItems(Context context) {
        TodoDbHelper dbHelper = new TodoDbHelper(context);

        // Récupération de la base
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Création de la projection souhaitée
        String[] projection = {
                TodoContract.TodoEntry._ID,
                TodoContract.TodoEntry.COLUMN_NAME_LABEL,
                TodoContract.TodoEntry.COLUMN_NAME_TAG,
                TodoContract.TodoEntry.COLUMN_NAME_DONE,
                TodoContract.TodoEntry.COLUMN_NAME_DATE
        };

        // Requête
        Cursor cursor = db.query(
                TodoContract.TodoEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        // Exploitation des résultats
        ArrayList<TodoItem> items = new ArrayList<TodoItem>();

        while (cursor.moveToNext()) {
            long numId = Long.parseLong(cursor.getString(cursor.getColumnIndex(TodoContract.TodoEntry._ID)));
            String label = cursor.getString(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_NAME_LABEL));
            TodoItem.Tags tag = TodoItem.getTagFor(cursor.getString(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_NAME_TAG)));
            boolean done = (cursor.getInt(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_NAME_DONE)) == 1);

            Date dateTime = null;
            try{
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
                dateTime = format.parse(cursor.getString(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_NAME_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            TodoItem item = new TodoItem(numId, label, tag, done, dateTime);
            items.add(item);
        }

         // Ménage
        dbHelper.close();

        // Retourne le résultat
        return items;
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM items");
    }

    public boolean insertData(long id, String label, String tag, String done, Date date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentVal = new ContentValues();
        contentVal.put(TodoContract.TodoEntry._ID, id);
        contentVal.put(TodoContract.TodoEntry.COLUMN_NAME_LABEL, label);
        contentVal.put(TodoContract.TodoEntry.COLUMN_NAME_TAG, tag);
        contentVal.put(TodoContract.TodoEntry.COLUMN_NAME_DONE, done);
        contentVal.put(TodoContract.TodoEntry.COLUMN_NAME_DATE, date.toString());

        long result = db.insert(TodoContract.TodoEntry.TABLE_NAME, null, contentVal);
        return result == -1;
    }

    public Cursor getData() {
        SQLiteDatabase sql = this.getWritableDatabase();
        Cursor result =  sql.rawQuery("SELECT * FROM " + TodoContract.TodoEntry.TABLE_NAME, null);
        return result;
    }

    static void updateDoneItem(Context context, String label, boolean state) {
        TodoDbHelper tdh = new TodoDbHelper(context);

        //Récupération de la base en mode écriture
        SQLiteDatabase sql = tdh.getWritableDatabase();
        int etat = 0;
        if(state)
            etat = 1;

       //Update de la ligne correspondante
        sql.execSQL("UPDATE items SET done = " + etat + " WHERE label = '" + label + "'");
        sql.close();
    }

    static long addItem(TodoItem item, Context context) {
        TodoDbHelper dbHelper = new TodoDbHelper(context);

        // Récupération de la base
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Création de l'enregistrement
        ContentValues values = new ContentValues();
        values.put(TodoContract.TodoEntry.COLUMN_NAME_LABEL, item.getLabel());
        values.put(TodoContract.TodoEntry.COLUMN_NAME_TAG, item.getTag().getDesc());
        values.put(TodoContract.TodoEntry.COLUMN_NAME_DONE, item.isDone());

        String dateTime = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
        dateFormat.setLenient(false);
        try {
            dateTime = dateFormat.format(item.getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        values.put(TodoContract.TodoEntry.COLUMN_NAME_DATE, dateTime);

        // Enregistrement
        long newRowId = db.insert(TodoContract.TodoEntry.TABLE_NAME, null, values);

        // Ménage
        dbHelper.close();

        return newRowId;
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
}
