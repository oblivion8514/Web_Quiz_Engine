package engine.model;

public class QuizResult {
    public static final QuizResult SUCCESS=new QuizResult(true);
    public static final QuizResult FAIL=new QuizResult(false);
    private boolean success;
    private String feedback;

    public QuizResult(boolean _success){
        this.success=_success;
        this.feedback=_success ? "Congratulations, you are right!" : "Wrong answer! Please, try again.";
    }
    public boolean isSuccess(){
        return success;
    }

    public String getFeedback(){
        return  feedback;
    }
}
