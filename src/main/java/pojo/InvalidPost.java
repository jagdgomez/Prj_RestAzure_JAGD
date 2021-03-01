package pojo;

public class InvalidPost {

    public InvalidPost(String titles, String contents) {

        //expected values in body= title and content, invalid values = titles and contents

        this.titles = titles;
        this.contents = contents;
    }


    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String content) {
        this.contents = content;
    }

    public String titles;
    public String contents;


}


