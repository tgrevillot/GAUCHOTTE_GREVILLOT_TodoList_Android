package si1.gauchotte_grevillot.todolist;

import android.provider.BaseColumns;

/**
 * Created by phil on 11/02/17.
 */

public final class TodoContract {
    public static class TodoEntry implements BaseColumns {
        public static final String TABLE_NAME = "items";
        public static final String COLUMN_NAME_LABEL = "label";
        public static final String COLUMN_NAME_TAG = "tag";
        public static final String COLUMN_NAME_DONE = "done";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_POSITION = "position";
    }
}
