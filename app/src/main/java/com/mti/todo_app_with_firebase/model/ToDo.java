/*
 * Created by Tareq Islam on 6/22/18 10:57 PM
 *
 *  Last modified 6/22/18 10:57 PM
 */

package com.mti.todo_app_with_firebase.model;

/***
 * Created by Tareq on 22,June,2018.
 */
public class ToDo {

    private String id,title,description;



    public ToDo(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
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
}
