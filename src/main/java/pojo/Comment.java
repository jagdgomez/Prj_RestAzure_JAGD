package pojo;

public class Comment {
    public String name;
    public String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }



    public Comment(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }


}
