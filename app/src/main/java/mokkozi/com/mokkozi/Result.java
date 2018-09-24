package mokkozi.com.mokkozi;

public class Result {

    private String result;
    private String sentence;
    private String message;

    public Result(String result, String sentence, String message) {
        this.result = result;
        this.sentence = sentence;
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public String getSentence() {
        return sentence;
    }

    public String getMessage() {
        return message;
    }
}
