import helpers.DataHelper;
import helpers.RequestHelper;
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

import static helpers.DataHelper.getTestUser;
import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsEqual.equalTo;

public class CommentsTest extends BaseTest {

    private static String resourcePath="/v1/comment";
    private static String PostPath="/v1/post";
    private static Integer createdComment = 0;
    private static Integer IntStatusCode = 0;
    private static Integer createdPost = 0;
    private static String GeneratedName = "";
    private static String GeneratedComment = "";

    @BeforeGroups (groups="1SrcAndCreate_Post")
    public void SearchOrCreatePost() {
        System.out.println("SearchOrCreatePost");
        createdPost = 840;
        RequestHelper.generateBasicToken();
        Response SearchResponse = given()
                .spec(RequestSpecs.generateToken())
                .get(PostPath+ "/"+ createdPost.toString());
        IntStatusCode = SearchResponse.getStatusCode();
        if (IntStatusCode== 200) {
            System.out.println("PostID# " + createdPost.toString()+ " Was Found");

        }
        else {
            System.out.println("PostID# " + createdPost.toString() + " Was NOT Found");
            System.out.println("Generating new Post for Test");
            Post testPost = new Post(DataHelper.generateRandomTitle(),DataHelper.generateRandomContent());
            Response response = given()
                    .spec(RequestSpecs.generateToken())
                    .body(testPost)
                    .post(PostPath);
            JsonPath jsonPathEvaluator = response.jsonPath();
            createdPost = jsonPathEvaluator.get("id");
            System.out.println("Generated Post ID# "+ createdPost.toString());
        }

    }

    @BeforeGroups (groups="2SrchAndCreate_Comment")
    public void SearchOrCreateComment() {
        System.out.println("SearchOrCreateComment");
        createdComment = 755;
        RequestHelper.generateBasicToken();
        Response SearchResponse = given()
                //.spec(RequestSpecs.generateToken())
                .get(resourcePath+ "/"+ createdPost+"/"+createdComment.toString());
        IntStatusCode = SearchResponse.getStatusCode();
        if (IntStatusCode== 200) {
            System.out.println("Comment # " + createdComment + " at post# " + createdPost + " Was Found");

        }
        else {
            System.out.println("Comment was NOT Found");
            System.out.println("Generating new comment for Test");
            Comment testComment = new Comment(DataHelper.generateRandomName(),DataHelper.generateRandomComment());
            Response response = given()
                    .body(testComment)
                    .post(resourcePath+ "/"+ createdPost.toString());
            JsonPath jsonPathEvaluator = response.jsonPath();
            createdComment = jsonPathEvaluator.get("id");
            System.out.println("Generated: PostID "+ createdPost +  " Comment ID " + createdComment);
        }

    }


    @Test (groups="1SrcAndCreate_Post")
    public void ATest_Create_Comment_success(){

        Comment testComment = new Comment(DataHelper.generateRandomName(),DataHelper.generateRandomComment());

        System.out.println("Generated Name: "+ testComment.getName());
        System.out.println("Generated Comment: "+ testComment.getComment());
        System.out.println("Request to: " + resourcePath);
        System.out.println("Adding Comment to Post #: " + createdPost.toString());
        System.out.println("Generate Valid Token");
        RequestHelper.generateBasicToken();

        given()
                .body(testComment)
                .when()
                .post(resourcePath+ "/"+ createdPost.toString())
                .then()
                .body("message", equalTo("Comment created"))
                .and()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }
    @Test (groups = "1SrcAndCreate_Post")
    public void BTest_Create_Comment_Fails(){

        InvalidComment testComment = new InvalidComment(DataHelper.generateRandomName(),DataHelper.generateRandomComment());

        System.out.println("Generated Name: "+ testComment.getNames());
        System.out.println("Generated Comment: "+ testComment.getComments());
        System.out.println("Request to: " + resourcePath);
        System.out.println("Adding Comment to Post #: " + createdPost.toString());
        System.out.println("Generate Valid Token");
        RequestHelper.generateBasicToken();

        given()
                .body(testComment)
                .when()
                .post(resourcePath+ "/"+ createdPost.toString())
                .then()
                .body("message", equalTo("Invalid form"))
                .and()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }
    @Test (groups = "1SrcAndCreate_Post")
    public void CTest_Create_Comment_FailbySecurity(){

        Comment testComment = new Comment(DataHelper.generateRandomName(),DataHelper.generateRandomComment());

        System.out.println("Generated Name: "+ testComment.getName());
        System.out.println("Generated Comment: "+ testComment.getComment());
        System.out.println("Request to: " + resourcePath);
        System.out.println("Basic Authentication with wrong credentials");
        RequestHelper.generateInvalidBasicToken();
        System.out.println("Comment could not be added to Post #: " + createdPost.toString());

        given()
                .body(testComment)
                .when()
                .post(resourcePath+ "/"+ createdPost.toString())
                .then()
                .body("message", equalTo("Please login first"))
                .and()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test (groups = {"1SrcAndCreate_Post","2SrchAndCreate_Comment"})
    public void DTest_ShowComment_success(){
        System.out.println("Request to: " + resourcePath +"/" + createdPost.toString() + "/" + createdComment.toString());
        System.out.println("Generate Valid Token");
        RequestHelper.generateBasicToken();

        given()
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

    @Test (groups = {"1SrcAndCreate_Post","2SrchAndCreate_Comment"})
    public void ETest_ShowComment_Fail(){
        System.out.println("Request to Invalid comment at: " + resourcePath +"/" + createdPost.toString() + "/" + createdComment.toString()+"105");
        System.out.println("Generate Valid Token");
        RequestHelper.generateBasicToken();
        given()
                .when()
                .get(resourcePath +"/" + createdPost.toString() + "/" + createdComment.toString()+"105")
                .then()
                .body("Message", equalTo("Comment not found"))
                .and()
                .body("error", equalTo("sql: no rows in result set"))
                .and()
                .statusCode(404)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test (groups = {"1SrcAndCreate_Post","2SrchAndCreate_Comment"})
    public void FTest_ShowComment_FailbySecurity(){
        System.out.println("Basic Authentication with wrong credentials");
        RequestHelper.generateInvalidBasicToken();
        System.out.println("Request to comment at: " + resourcePath +"/" + createdPost.toString() + "/" + createdComment.toString());
              given()
                .when()
                .get(resourcePath +"/" + createdPost.toString() + "/" + createdComment.toString())
              .then()
                .body("message", equalTo("Please login first"))
              .and()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test (groups = "1SrcAndCreate_Post")
    public void GTest_ShowAllComments_success(){
        System.out.println("Request to: " + resourcePath+"s" +"/" + createdPost.toString());
        System.out.println("Generate Valid Token");
        RequestHelper.generateBasicToken();
        given()
                .when()
                .get(resourcePath +"s"+"/" + createdPost.toString())
                .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test (groups = "1SrcAndCreate_Post")
    public void HTest_ShowAllComments_SchemaValidation(){
        System.out.println("Validating Comments on Post#: "+ createdPost.toString());
        /* schema validation */
        System.out.println("Validating Comments Schema at :" + resourcePath+"s" +"/" + createdPost.toString());
        System.out.println("Generate Valid Token");
        RequestHelper.generateBasicToken();

        Response response = given()
                .get(resourcePath+"s" +"/" + createdPost.toString());
        assertThat(response.asString(),matchesJsonSchemaInClasspath("comments.schema.json"));
        assertThat(response.path("results.data.id"), not(emptyArray()));
    }


}
