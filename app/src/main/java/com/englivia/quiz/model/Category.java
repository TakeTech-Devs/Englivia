package com.englivia.quiz.model;

import java.util.ArrayList;
import java.util.Comparator;

public class Category {
    private String id, name, image, maxLevel, noOfCate, message, date, ttlQues;
    public ArrayList<SubCategory> subCategoryList;
    public boolean adsShow;

    public Category() {
    }

    public Category(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }
    public static class Sortbyroll implements Comparator<Category> {
        @Override
        public int compare(Category o1, Category o2) {
            try {
                // Split the names by space and parse the second part as an integer
                int rollNumber1 = Integer.parseInt(o1.name.split(" ")[1]);
                int rollNumber2 = Integer.parseInt(o2.name.split(" ")[1]);

                // Compare the parsed integers
                return Integer.compare(rollNumber1, rollNumber2);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                // Handle the case where parsing fails or the array index is out of bounds
                // You might want to log the error or handle it in a way that makes sense for your application
                e.printStackTrace();
                return 0; // or any other appropriate value
            }
        }
    }

    public Category(boolean adsShow) {
        this.adsShow = adsShow;
    }

    public boolean isAdsShow() {
        return adsShow;
    }

    public void setAdsShow(boolean adsShow) {
        this.adsShow = adsShow;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNoOfCate() {
        return noOfCate;
    }

    public void setNoOfCate(String noOfCate) {
        this.noOfCate = noOfCate;
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

    public String getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(String maxLevel) {
        this.maxLevel = maxLevel;
    }

    public String getTtlQues() {
        return ttlQues;
    }

    public void setTtlQues(String ttlQues) {
        this.ttlQues = ttlQues;
    }

    public ArrayList<SubCategory> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(ArrayList<SubCategory> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }
}
