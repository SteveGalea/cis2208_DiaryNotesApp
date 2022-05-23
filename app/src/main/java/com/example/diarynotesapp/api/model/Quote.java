package com.example.diarynotesapp.api.model;

import com.google.gson.annotations.SerializedName;

public class Quote {

    //annotated properties. Necessary for serializing and deserializing
    @SerializedName("text")
    private String text;

    @SerializedName("author")
    private String author;

    // constructor of model
    public Quote(String text, String author) {
        this.text = text;
        this.author = author;
    }

    //getters and setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
