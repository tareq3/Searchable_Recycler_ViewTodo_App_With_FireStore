/*
 * Created by Tareq Islam on 6/22/18 10:57 PM
 *
 *  Last modified 6/22/18 10:57 PM
 */

package com.mti.todo_app_with_firebase.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/***
 * Created by Tareq on 22,June,2018.
 */

@Entity(tableName = "todo_table")
public class ToDo {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "id")
    public String id;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "description")
    public String description;



    public ToDo(@NonNull String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    private ToDo(Builder builder) {
        setId(builder.id);
        setTitle(builder.title);
        setDescription(builder.description);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public static final class Builder {
        private String id;
        private String title;
        private String description;

        public Builder() {
        }

        public Builder withId(String val) {
            id = val;
            return this;
        }

        public Builder withTitle(String val) {
            title = val;
            return this;
        }

        public Builder withDescription(String val) {
            description = val;
            return this;
        }

        public ToDo build() {
            return new ToDo(this);
        }
    }
}
