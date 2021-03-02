import helpers.DataHelper;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.*;
import pojo.Article;
import pojo.Comment;
import pojo.InvalidComment;
import pojo.Post;
import specifications.RequestSpecs;
import specifications.ResponseSpecs;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class CommentsTest extends BaseTest {

    private static String resourcePath="/v1/comment";
    private static String PostPath="/v1/post";
    private static Integer createdComment = 0;
    private static Integer IntStatusCode = 0;
    private static Integer createdPost = 0;
    private static String GeneratedName = "";
    private static String GeneratedComment = "";

    @BeforeGroups (groups="SrcCreatComment")
    public void SearchOrCreateComment() {
        System.out.println("SearchOrCreateComment");
        RestAssured.authentication= basic ("testuser","testpass");
        Response SearchResponse = given()
                //.spec(RequestSpecs.generateToken())
                .get(resourcePath+ "/"+ createdPost+"421");
        IntStatusCode = SearchResponse.getStatusCode();
        if (IntStatusCode== 200) {
            System.out.println("Comment In PostID# " + createdPost + " 421 Was Found");
            }
        else {
            System.out.println("Comment was NOT Found");
            System.out.println("Generating new comment for Test");
            Comment testComment = new Comment(DataHelper.generateRandomName(),DataHelper.generateRandomComment());
            Response response = given()
                    //.spec(RequestSpecs.generateToken())
                    .body(testComment)
                    .post(resourcePath+ "/"+ createdPost.toString());
            JsonPath jsonPathEvaluator = response.jsonPath();
            createdComment = jsonPathEvaluator.get("id");
            System.out.println("Generated: PostID "+ createdPost +  " Comment ID " + createdComment);
        }

    }

    @BeforeClass
    public void SearchOrCreatePost() {
        System.out.println("SearchOrCreatePost");
        RestAssured.authentication= basic ("testuser","testpass");
        Response SearchResponse = given()
                .spec(RequestSpecs.generateToken())
                .get(PostPath+ "/"+"840");
        IntStatusCode = SearchResponse.getStatusCode();
        if (IntStatusCode== 200) {
            System.out.println("PostID#840 Was Found");
            createdPost = 840;
        }
        else {
            System.out.println("PostID#840 Was NOT Found");
            System.out.println("Generating new Post for Test");
            Post testPost = new Post(DataHelper.generateRandomTitle(),DataHelper.generateRandomContent());
            Response response = given()
                    .spec(RequestSpecs.generateToken())
                    .body(testPost)
                    .post(PostPath);
            JsonPath jsonPathEvaluator = response.jsonPath();
            createdPost = jsonPathEvaluator.get("id");
            System.out.println("Generated Post ID# "+ createdPost);
        }

    }

    @Test
    public void ATest_Create_Comment_success(){

        Comment testComment = new Comment(DataHelper.generateRandomName(),DataHelper.generateRandomComment());

        System.out.println("Generated Name: "+ testComment.getName());
        System.out.println("Generated Comment: "+ testComment.getComment());
        System.out.println("Request to: " + resourcePath);
        System.out.println("Adding Comment to Post #: " + createdPost.toString());

        given()
               // .auth().basic("testuser","testpass")
                .body(testComment)
                .when()
                .post(resourcePath+ "/"+ createdPost.toString())
                .then()
                .body("message", equalTo("Comment created"))
                .and()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }
    @Test (groups = "create_post")
    public void BTest_Create_Comment_Fails(){

        InvalidComment testComment = new InvalidComment(DataHelper.generateRandomName(),DataHelper.generateRandomComment());

        System.out.println("Generated Name: "+ testComment.getNames());
        System.out.println("Generated Comment: "+ testComment.getComments());
        System.out.println("Request to: " + resourcePath);
        System.out.println("Adding Comment to Post #: " + createdPost.toString());

        given()
               // .auth().basic("testuser","testpass")
                .body(testComment)
                .when()
                .post(resourcePath+ "/"+ createdPost.toString())
                .then()
                .body("message", equalTo("Invalid form"))
                .and()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }
    @Test (groups = "create_post")
    public void CTest_Create_Comment_FailbySecurity(){

        Comment testComment = new Comment(DataHelper.generateRandomName(),DataHelper.generateRandomComment());

        System.out.println("Generated Name: "+ testComment.getName());
        System.out.println("Generated Comment: "+ testComment.getComment());
        System.out.println("Request to: " + resourcePath);
        System.out.println("Basic Authentication with wrong credentials");
        System.out.println("Comment could not be added to Post #: " + createdPost.toString());

        given()
                .auth().basic("testuser1","testpass1")
                .body(testComment)
                .when()
                .post(resourcePath+ "/"+ createdPost.toString())
                .then()
                .body("message", equalTo("Please login first"))
                .and()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test (groups = {"SrcCreatComment"})
    public void DTest_ShowPost_success(){
        System.out.println("Request to: " + resourcePath +"/" + createdPost.toString() + "/" + createdComment.toString());
        given()
                // .auth().basic("testuser","testpass")
                //.body(testComment)
                .when()
                .get(resourcePath +"/" + createdPost.toString() + "/" + createdComment.toString())
                .then()
                .body("data.id", equalTo(createdComment))
                .and()
                .body("data.post_id", equalTo(createdPost.toString()))
                .and()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

}
