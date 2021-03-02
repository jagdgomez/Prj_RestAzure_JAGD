package pojo;

public class InvalidComment {
    public String names;
    public String comments;

    public String getNames() {
        return names;
    }

    public void setName(String names) {
        this.names = names;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }



    public InvalidComment(String names, String comments) {
        this.names = names;
        this.comments = comments;
    }


}
