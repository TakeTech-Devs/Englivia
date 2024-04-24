package com.englivia.quiz.model;

public class Learn {
    String id,learning_id,detail,image,heading,heading_meaning;

    public Learn(String id, String learning_id, String detail, String image, String heading, String heading_meaning) {
        this.id = id;
        this.learning_id = learning_id;
        this.detail = detail;
        this.image = image;
        this.heading = heading;
        this.heading_meaning = heading_meaning;
    }

    public String getHeading_meaning() {
        return heading_meaning;
    }

    public void setHeading_meaning(String heading_meaning) {
        this.heading_meaning = heading_meaning;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLearning_id() {
        return learning_id;
    }

    public void setLearning_id(String learning_id) {
        this.learning_id = learning_id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }
}
