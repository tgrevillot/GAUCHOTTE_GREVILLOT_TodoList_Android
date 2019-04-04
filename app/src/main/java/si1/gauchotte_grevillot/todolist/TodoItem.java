package si1.gauchotte_grevillot.todolist;

import java.util.Date;

/**
 * Created by phil on 06/02/17.
 */

public class TodoItem {

    public enum Tags {
        Faible("Faible"), Normal("Normal"), Important("Important");

        private String desc;
        Tags(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    private String label;
    private Tags tag;
    private boolean done;
    private Date date;
    private long id;
    private int position;

    public TodoItem(String label, Tags tag, Date date, int position) {
        this.id = 0;
        this.label = label;
        this.tag = tag;
        this.date = date;
        this.done = false;
        this.position = position;
    }

    /*public TodoItem(Tags tag, String label) {
        this.tag = tag;
        this.label = label;
        this.done = false;
    }*/

    public TodoItem(long id, String label, Tags tag, boolean done, Date date, int position) {
        this.id = id;
        this.label = label;
        this.tag = tag;
        this.done = done;
        this.date = date;
        this.position = position;
    }

    public static Tags getTagFor(String desc) {
        for (Tags tag : Tags.values()) {
            if (desc.compareTo(tag.getDesc()) == 0)
                return tag;
        }

        return Tags.Faible;
    }

    public String getLabel() {
        return label;
    }

    public Tags getTag() {
        return tag;
    }

    public boolean isDone() {
        return done;
    }

    public Date getDate() {
        return date;
    }

    public long getId(){
        return id;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setTag(Tags tag) {
        this.tag = tag;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setId(long id){
        this.id = id;
    }
}
