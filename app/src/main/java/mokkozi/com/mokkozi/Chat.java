package mokkozi.com.mokkozi;

public class Chat {
    public String email;
    public String text;
    public String time;

    public Chat(){}

    public Chat(String text){
        this.text = text;
    }

    public String getEmail() {
        return email;
    }

    public String getText() {
        return text;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time){this.time=  time;}
}
