package jack.behaviourquiz;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuizData {
    public Quiz quiz;

    public static QuizData fromJson(String s) {
        return new Gson().fromJson(s, QuizData.class);
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
class Quiz {
    @SerializedName("section")
    public List<Section> sections;

    public String name;
}
class Section {
    @SerializedName("phase")
    public List<Phase> phases;

    public String name;
}
class Phase {
    @SerializedName("quizquestion")
    public List<QuizQuestion> quizquestions;

    public String name;
}
class QuizQuestion {
    @SerializedName("wrongAnswer")
    public List<String> wrongAnswer;

    public String correctAnswer;
    public String question;
    public String explanation;
    public String imageFilename;
}
