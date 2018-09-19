package mokkozi.com.mokkozi;

public class Information {
    private String id;
    private String pw;
    private String email;
    private String gender;

    public Information(String id, String pw, String email, String gender) {
        this.id = id;
        this.pw = pw;
        this.email = email;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public String getPw() {
        return pw;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

}
