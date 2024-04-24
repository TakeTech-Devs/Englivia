package com.englivia.quiz.model;

public class Dict {
    private String no, id, dictionary_id, word,meaning;// maxLevel, noOfCate, message, date, ttlQues;

    public Dict() {
    }

    public Dict(String no, String id, String dictionary_id, String word, String meaning) {
        this.no = no;
        this.id = id;
        this.dictionary_id = dictionary_id;
        this.word = word;
        this.meaning = meaning;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Dict(String id, String dictionary_id, String word, String meaning) {
        this.id = id;
        this.dictionary_id = dictionary_id;
        this.word = word;
        this.meaning = meaning;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDictionary_id() {
        return dictionary_id;
    }

    public void setDictionary_id(String dictionary_id) {
        this.dictionary_id = dictionary_id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
