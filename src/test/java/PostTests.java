import helpers.DataHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;
import pojo.Article;
import pojo.InvalidPost;
import pojo.Post;
import specifications.RequestSpecs;
import specifications.ResponseSpecs;

import java.security.acl.Group;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;

public class PostTests extends BaseTest {
    private static String resourcePath="/v1/post";
    private static Integer createdPost = 0;
    private static String createdTitle = "";
    private static String createdContent = "";

    @BeforeGroups ("create_post")
    public void createPost() {
        Post testPost = new Post(DataHelper.generateRandomTitle(),DataHelper.generateRandomContent());
        Response response = given()
                .spec(RequestSpecs.generateToken())
                .body(testPost)
                .post(resourcePath);
        JsonPath jsonPathEvaluator = response.jsonPath();
        createdPost = jsonPathEvaluator.get("id");
        createdTitle = testPost.getTitle();
        createdContent = testPost.getContent();
        System.out.println("Generated Post ID# "+ createdPost);
        System.out.println("Generated Title: "+ createdTitle);
        System.out.println("Generated Content: "+ createdContent);
    }


    @Test
    public void ATest_Create_Post_success(){

        Post testPost = new Post(DataHelper.generateRandomTitle(),DataHelper.generateRandomContent());

        System.out.println("Generated Post Title: "+ testPost.getTitle());
        System.out.println("Generated Post Content: "+ testPost.getContent());
        System.out.println("Request to: " + resourcePath);

        given()
                .spec(RequestSpecs.generateToken())
                .body(testPost)
                .when()
                .post(resourcePath)
                .then()
                .body("message", equalTo("Post created"))
                .and()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }
    @Test
    public void BTest_Create_Post_Fails(){

        InvalidPost  NotValidPost = new InvalidPost(DataHelper.generateRandomTitle(),DataHelper.generateRandomContent());

        System.out.println("Generated Post with Invalid Body values");
        System.out.println("Request to: " + resourcePath);

        given()
                .spec(RequestSpecs.generateToken())
                .body(NotValidPost)
                .when()
                .post(resourcePath)
                .then()
                .body("message", equalTo("Invalid form"))
                .and()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }
    @Test
    public void CTest_Create_Post_FailBySecurity(){

        Post testPost = new Post(DataHelper.generateRandomTitle(),DataHelper.generateRandomContent());

        System.out.println("Generated Post Title: "+ testPost.getTitle());
        System.out.println("Generated Post Content: "+ testPost.getContent());
        System.out.println("Fake token Generated");
        System.out.println("Request to: " + resourcePath);

        given()
                .spec(RequestSpecs.generateFakeToken())
                .body(testPost)
                .when()
                .post(resourcePath)
                .then()
                .body("message", equalTo("Please login first"))
                .and()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }



    @Test (groups = "create_post")
    public void DTest_Show_Post_success(){

      //Post testPost = new Post(DataHelper.generateRandomTitle(),DataHelper.generateRandomContent());

        System.out.println("Show PostID# "+ createdPost);
        System.out.println("Generated Post Title: "+ createdTitle);
        System.out.println("Generated Post Content: "+ createdContent);
        System.out.println("Request to: " + resourcePath + "/" + createdPost);

        given()
                .spec(RequestSpecs.generateToken())
                .get(String.format("%s/%s",resourcePath,createdPost.toString()))
                .then()
                .body("data.id", equalTo(createdPost))
                .and()
                .body("data.title", equalTo(createdTitle))
                .and()
                .body("data.content", equalTo(createdContent))
                .and()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }
    @Test (groups = "create_post")
    public void ETest_Show_Post_Fails(){

        System.out.println("Request to: " + resourcePath + "/" + (createdPost+1));

        given()
                .spec(RequestSpecs.generateToken())
                .get(resourcePath + "/" + (createdPost+1))
                .then()
                .body("Message", equalTo("Post not found"))
                .and()
                .body("error", equalTo("sql: no rows in result set"))
                .and()
                .statusCode(404)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test (groups = "create_post")
    public void FTest_Show_Post_FailBySecurity(){

        System.out.println("Show PostID# "+ createdPost);
        System.out.println("Generated Post Title: "+ createdTitle);
        System.out.println("Generated Post Content: "+ createdContent);
        System.out.println("Fake Token Generated");
        System.out.println("Request to: " + resourcePath + "/" + createdPost);

        given()
                .spec(RequestSpecs.generateFakeToken())
                .get(String.format("%s/%s",resourcePath,createdPost.toString()))
                .then()
                .body("message", equalTo("Please login first"))
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test (groups = "create_post")
    public void GTest_ShowAll_Posts_Success(){

        System.out.println("Show PostID# "+ createdPost);
        System.out.println("Generated Post Title: "+ createdTitle);
        System.out.println("Generated Post Content: "+ createdContent);
        System.out.println("Request to: " + resourcePath + "s");

        Response response = given()
                .spec(RequestSpecs.generateToken())
                .get(resourcePath + "s");
                 assertThat(response.path("results.data.id"), not(emptyArray()));
    }

    @Test (groups = "create_post")
    public void HTest_ShowAll_Posts_FailBySecurity(){

        System.out.println("Show PostID# "+ createdPost);
        System.out.println("Generated Post Title: "+ createdTitle);
        System.out.println("Generated Post Content: "+ createdContent);
        System.out.println("Fake Token Generated");
        System.out.println("Request to: " + resourcePath + "/" + createdPost);

        given()
                .spec(RequestSpecs.generateFakeToken())
                .get(resourcePath+"s")
                .then()
                .body("message", equalTo("Please login first"))
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test (groups = "create_post")
    public void ITest_Edit_Post_success(){

        Post testPost = new Post(DataHelper.generateRandomTitle(),DataHelper.generateRandomContent());

        System.out.println("Edit PostID# "+ createdPost);
        System.out.println("Generated Post Title: "+ testPost.getTitle());
        System.out.println("Generated Post Content: "+ testPost.getContent());
        System.out.println("Request to: " + resourcePath + "/" + createdPost);

                given()
                .spec(RequestSpecs.generateToken())
                .body(testPost)
                .when()
                .put(resourcePath + "/" + createdPost)
                .then()
                .body("message", equalTo("Post updated"))
                .and()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test (groups = "create_post")
    public void JTest_Edit_Post_Fails(){

        InvalidPost NotValidPost = new InvalidPost(DataHelper.generateRandomTitle(),DataHelper.generateRandomContent());

        System.out.println("Edit PostID# "+ createdPost);
        System.out.println("Generated Post with wrong body = Titles: "+ NotValidPost.getTitles());
        System.out.println("Generated Post with wrong body = Contents: "+ NotValidPost.getContents());
        System.out.println("Request to: " + resourcePath + "/" + createdPost);

        given()
                .spec(RequestSpecs.generateToken())
                .body(NotValidPost)
                .when()
                .put(resourcePath + "/" + createdPost)
                .then()
                .body("message", equalTo("Invalid form"))
                .and()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test (groups = "create_post")
    public void KTest_Edit_Post_FailBySecurity(){

        Post testPost = new Post(DataHelper.generateRandomTitle(),DataHelper.generateRandomContent());

        System.out.println("Edit PostID# "+ createdPost);
        System.out.println("Generated Post Title: "+ testPost.getTitle());
        System.out.println("Generated Post Content: "+ testPost.getContent());
        System.out.println("Fake token generated");
        System.out.println("Request to: " + resourcePath + "/" + createdPost);

        given()
                .spec(RequestSpecs.generateFakeToken())
                .body(testPost)
                .when()
                .put(resourcePath + "/" + createdPost)
                .then()
                .body("message", equalTo("Please login first"))
                .and()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test (groups = "create_post")
    public void LTest_Delete_Post_success(){

     //   Post testPost = new Post(DataHelper.generateRandomTitle(),DataHelper.generateRandomContent());

        System.out.println("Delete PostID# "+ createdPost);
        System.out.println("Request to: " + resourcePath + "/" + createdPost);

        given()
                .spec(RequestSpecs.generateToken())
                //.body(testPost)
                .when()
                .delete(resourcePath + "/" + createdPost)
                .then()
                .body("message", equalTo("Post deleted"))
                .and()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test (groups = "create_post")
    public void MTest_Delete_Post_Fails(){

        System.out.println("Edit PostID# "+ createdPost);
        System.out.println("Request to: " + resourcePath + "/" + createdPost);

        given()
                .spec(RequestSpecs.generateToken())
                .when()
                .delete(resourcePath + "/" + (createdPost+1))
                .then()
                .body("error", equalTo("Post not found"))
                .and()
                .body("message", equalTo("Post could not be deleted"))
                .and()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test (groups = "create_post")
    public void NTest_Delete_Post_FailBySecurity(){

        Post testPost = new Post(DataHelper.generateRandomTitle(),DataHelper.generateRandomContent());

        System.out.println("Delete PostID# "+ createdPost);
        System.out.println("Generated Post Title: "+ testPost.getTitle());
        System.out.println("Generated Post Content: "+ testPost.getContent());
        System.out.println("Fake token generated");
        System.out.println("Request to: " + resourcePath + "/" + createdPost);

        given()
                .spec(RequestSpecs.generateFakeToken())
                .body(testPost)
                .when()
                .put(resourcePath + "/" + createdPost)
                .then()
                .body("message", equalTo("Please login first"))
                .and()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

}
