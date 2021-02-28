import helpers.DataHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;
import pojo.Article;
import pojo.User;
import specifications.RequestSpecs;
import specifications.ResponseSpecs;

import static helpers.DataHelper.generateRandomEmail;
import static helpers.DataHelper.getTestUser;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ArticleTests extends BaseTest {
   /* removed to use parameter from BaseTest
   private static String baseUrl="https://api-coffee-testing.herokuapp.com";*/

    private static String resourcePath="/v1/article";
    private static Integer createdArticle = 0;
    private static String createdTitle = "";
    private static String createdContent = "";
    /*path de aqui es: https://api-coffee-testing.herokuapp.com/v1/user/register"*/

    @BeforeGroups ("create_article")
    public void createArticle() {
        Article testArticle = new Article(DataHelper.generateRandomTitle(),DataHelper.generateRandomContent());
        Response response = given()
                .spec(RequestSpecs.generateToken())
                .body(testArticle)
                .post(resourcePath);
        JsonPath jsonPathEvaluator = response.jsonPath();
        createdArticle = jsonPathEvaluator.get("id");
        createdTitle = testArticle.getTitle();
        createdContent = testArticle.getContent();
        System.out.println("Generated Article # "+ createdArticle);
        System.out.println("Generated Title: "+ createdTitle);
        System.out.println("Generated Content: "+ createdContent);
     }


    @Test
    public void Test_Create_Article_success(){

        Article testArticle = new Article(DataHelper.generateRandomTitle(),DataHelper.generateRandomContent());

        System.out.println("Generated Title: "+ testArticle.getTitle());
        System.out.println("Generated Content: "+ testArticle.getContent());
        System.out.println("Request to: " + resourcePath);

                given()
               //rem para usar el requesttoken del base test
                //.headers("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NfdXVpZCI6ImI0MTNkNzk2LTBlM2YtNGNmYi05MWI5LTM4MjBkYzMzOGRhMiIsImF1dGhvcml6ZWQiOnRydWUsImV4cCI6MTYxNDA1MzQ5MSwidXNlcl9pZCI6Mzg2fQ.Qmy94_xR8tE0yiGsbvOoIW-gubMwL20xuWlI9_QacQY")
                .spec(RequestSpecs.generateToken())
                .body(testArticle)
                .when()
                    .post(resourcePath)
                .then()
                    .body("message", equalTo("Article created"))
                .and()
                    .statusCode(200)
                    .spec(ResponseSpecs.defaultSpec());
    }
    @Test
    public void Test_InvalidToken_Cant_Create_New_Article(){

        Article testArticle = new Article(DataHelper.generateRandomTitle(),DataHelper.generateRandomContent());

        System.out.println("Generated Title: "+ testArticle.getTitle());
        System.out.println("Generated Content: "+ testArticle.getContent());
        System.out.println("Request to: " + resourcePath + " -> with invalid token");

                given()
                //rem para usar el requesttoken del base test
                //.headers("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NfdXVpZCI6ImI0MTNkNzk2LTBlM2YtNGNmYi05MWI5LTM4MjBkYzMzOGRhMiIsImF1dGhvcml6ZWQiOnRydWUsImV4cCI6MTYxNDA1MzQ5MSwidXNlcl9pZCI6Mzg2fQ.Qmy94_xR8tE0yiGsbvOoIW-gubMwL20xuWlI9_QacQY")
                .spec(RequestSpecs.generateFakeToken())
                .body(testArticle)
                .when()
                .post(resourcePath)
                .then()
                .body("message", equalTo("Please login first"))
                .and()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test (groups = "create_article")
    public void Z_Test_Delete_Article_success(){
        System.out.println("Article to be deleted: "+ createdArticle);
                given()
                    .spec(RequestSpecs.generateToken())
                    .delete(String.format("%s/%s",resourcePath,createdArticle.toString()))
                .then()
                .body("message", equalTo("Article deleted"))
                .and()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }
    @Test (groups = "create_article")
    public void Test_Show_Article_success(){
        System.out.println("Article to be retrieved #: "+ createdArticle);
        System.out.println("Article Retrieved Title: "+ createdTitle);
        System.out.println("Article Retrieved Content : "+ createdContent);
        given()
                .spec(RequestSpecs.generateToken())
                .get(String.format("%s/%s",resourcePath,createdArticle.toString()))
                .then()
                .body("data.id", equalTo(createdArticle))
                .and()
                .body("data.title", equalTo(createdTitle))
                .and()
                .body("data.content", equalTo(createdContent))
                .and()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test (groups = "create_article")
    public void Test_Show_All_Articles_success(){
        System.out.println("Show All Articles User: "+ getTestUser().getName() + " and email: " + getTestUser().getEmail());
        given()
                .spec(RequestSpecs.generateToken())
                .get(resourcePath+"s")
                .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }
    @Test (groups = "create_article")
    public void Test_Articles_Schema(){
        System.out.println("Article recently created #: "+ createdArticle);
        /* schema validation example */
        System.out.println("Validating Articles Schema -> User: "+ getTestUser().getName() + " and email: " + getTestUser().getEmail());
          Response response = given()
                    .spec(RequestSpecs.generateToken())
                .get("/v1/articles");
                assertThat(response.asString(),matchesJsonSchemaInClasspath("articles.schema.json"));
                assertThat(response.path("results[0].data[0].id"),equalTo(createdArticle) );




    }


}

