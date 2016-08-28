package com.example.splash;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by abc on 2/19/2016.
 */
public class Question implements Parcelable{

    int questionID;
    String questionText;
    HashMap<String,Integer> answerOptions;
    String questionImage;

    public Question() {
        this.answerOptions = new HashMap<>();
        this.questionID = -1;
        this.questionImage = "";
        this.questionText = "";
    }

    public Question(HashMap<String, Integer> answerOptions, int questionID, String questionImage, String questionText) {
        this.answerOptions = answerOptions;
        this.questionID = questionID;
        this.questionImage = questionImage;
        this.questionText = questionText;
    }

    public HashMap<String, Integer> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(HashMap<String, Integer> answerOptions) {
        this.answerOptions = answerOptions;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(String questionImage) {
        this.questionImage = questionImage;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    @Override
    public String toString() {
        return "Question{" +
                "answerOptions=" + answerOptions.toString() +
                "answerOptions=" + answerOptions.toString() +
                ", questionID=" + questionID +
                ", questionText='" + questionText + '\'' +
                ", questionImage='" + questionImage + '\'' +
                '}';
    }

//Parcelable implementation

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };


    protected Question(Parcel in) {
        questionID = in.readInt();
        questionText = in.readString();
        answerOptions = (HashMap) in.readValue(HashMap.class.getClassLoader());
        questionImage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(questionID);
        dest.writeString(questionText);
        dest.writeValue(answerOptions);
        dest.writeString(questionImage);
    }

}
