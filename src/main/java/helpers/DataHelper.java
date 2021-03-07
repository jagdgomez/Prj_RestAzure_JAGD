package helpers;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import pojo.Comment;
import pojo.User;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class DataHelper {
    public static String generateRandomEmail(){
        return String.format("%s@testemail.com" , generateRandomString(7));
    }

    public static String generateRandomTitle(){
        return String.format("%s" , generateRandomString(10));
    }

    public static String generateRandomContent(){
        return String.format("%s" , generateRandomString(100));
    }

    public static String generateRandomName(){
        return String.format("%s" , generateRandomString(10));
    }

    public static String generateRandomComment(){
        return String.format("%s" , generateRandomString(100));
    }

    private static String generateRandomString(int targetStringLength){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
    }

    public static User  getTestUser(){
        return new User("Johnny","jagdtest@test.com","gomez");

    }

    public static Integer CreatedNewCommentId(String resourcePath, Integer createdPost) {
        System.out.println("Creating new comment for Test");
        Integer createdCommentId;
        Comment testComment = new Comment(DataHelper.generateRandomName(),DataHelper.generateRandomComment());
        RequestHelper.generateBasicToken();
        Response response = given()
                .body(testComment)
                .post(resourcePath+ "/"+ createdPost.toString());
        if (response.getStatusCode()== 200) {
        JsonPath jsonPathEvaluator = response.jsonPath();
        createdCommentId = jsonPathEvaluator.get("id");
        System.out.println("Status 200 - Generated: PostID "+ createdPost +  " Comment ID " + createdCommentId);
        System.out.println ("New Comment Name: " + DataHelper.generateRandomName());
        System.out.println ("New Comment: " + DataHelper.generateRandomComment());
        }
        else {
        System.out.println("Error creating comment, status code = "+  response.getStatusCode());
        createdCommentId=0;
        }
        return createdCommentId;
    }
}
