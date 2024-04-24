package com.englivia.quiz.model;

import java.util.ArrayList;
import java.util.Comparator;

public class SubCategory {
    private String id, name, image, categoryId,status,maxLevel,No_ofque;

   private ArrayList<Question> questionList;

    public static class Sortbyroll implements Comparator<SubCategory>
    {
        // Used for sorting in ascending order of
        // roll number
        @Override
        public int compare(SubCategory o1, SubCategory o2) {
            return Integer.parseInt(o1.name.split(" ")[1])-Integer.parseInt(o2.name.split(" ")[1]);
        }


    }

    public static class SortbyrollError implements Comparator<SubCategory>
    {
        // Used for sorting in ascending order of
        // roll number
        @Override
        public int compare(SubCategory o1, SubCategory o2) {
            //return Integer.parseInt(o1.name.split(" ")[1])-Integer.parseInt(o2.name.split(" ")[1]);

            int num1 = Integer.parseInt(o1.getName().substring(4).trim());
            int num2 = Integer.parseInt(o2.getName().substring(4).trim());
            return Integer.compare(num1, num2);
        }


    }

    public SubCategory() {
    }

    public String getNo_ofque() {
        return No_ofque;
    }

    public void setNo_ofque(String no_ofque) {
        No_ofque = no_ofque;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getMaxLevel() {
        return maxLevel;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setMaxLevel(String maxLevel) {
        this.maxLevel = maxLevel;
    }

    public ArrayList<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(ArrayList<Question> questionList) {
        this.questionList = questionList;
    }
}
