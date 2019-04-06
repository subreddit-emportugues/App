package org.emportugues.aplicativo.data.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    public int id;
    public String name;
    public String links;
    public String description;
    public boolean nsfw;
    public String age;
    public int members;
    public String icon;

}
