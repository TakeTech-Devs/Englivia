package com.englivia.quiz.model;

import java.util.Comparator;

public class Sets {
    private String id, language_id, title;// maxLevel, noOfCate, message, date, ttlQues;

    public Sets() {
    }
    public static class Sortbyroll implements Comparator<Sets>
    {
        // Used for sorting in ascending order of
        // roll number
        @Override
        public int compare(Sets o1, Sets o2) {
            return Integer.parseInt(o1.title.split(" ")[1])-Integer.parseInt(o2.title.split(" ")[1]);
        }


    }
    public Sets(String id, String language_id, String title) {
        this.id = id;
        this.language_id = language_id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
