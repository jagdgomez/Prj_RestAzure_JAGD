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
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class ArticleTests extends BaseTest {
   /* removed to use parameter from BaseTest
   private static String baseUrl="https://api-coffee-testing.herokuapp.com";*/

    private static String resourcePath="/v1/article";
    private static Integer createdArticle = 0;
    /*path de aqui es: https://api-coffee-testing.herokuapp.com/v1/user/register"*/

    @BeforeGroups ("create_article")
    public void createArticle() {
        Article testarticle = new Article(DataHelper.generateRandomTitle(),DataHelper.generateRandomContent());
        Response response = given()
                .spec(RequestSpecs.generateToken())
                .body(testarticle)
                .post(resourcePath);
        JsonPath jsonPathEvaluator = response.jsonPath();
        createdArticle = jsonPathEvaluator.get("id");
        System.out.println("Generated Article # "+ createdArticle);
     }


    @Test
    public void Test_Create_Article_success(){

        Article testarticle = new Article(DataHelper.generateRandomTitle(),DataHelper.generateRandomContent());

        System.out.println("Generated Title: "+ testarticle.getTitle());
        System.out.println("Generated Content: "+ testarticle.getContent());
        System.out.println("Request to: " + resourcePath);

                given()
               //rem para usar el requesttoken del base test
                //.headers("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NfdXVpZCI6ImI0MTNkNzk2LTBlM2YtNGNmYi05MWI5LTM4MjBkYzMzOGRhMiIsImF1dGhvcml6ZWQiOnRydWUsImV4cCI6MTYxNDA1MzQ5MSwidXNlcl9pZCI6Mzg2fQ.Qmy94_xR8tE0yiGsbvOoIW-gubMwL20xuWlI9_QacQY")
                .spec(RequestSpecs.generateToken())
                .body(testarticle)
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

        Article testarticle = new Article(DataHelper.generateRandomTitle(),DataHelper.generateRandomContent());

        System.out.println("Generated Title: "+ testarticle.getTitle());
        System.out.println("Generated Content: "+ testarticle.getContent());
        System.out.println("Request to: " + resourcePath + " -> with invalid token");

                given()
                //rem para usar el requesttoken del base test
                //.headers("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NfdXVpZCI6ImI0MTNkNzk2LTBlM2YtNGNmYi05MWI5LTM4MjBkYzMzOGRhMiIsImF1dGhvcml6ZWQiOnRydWUsImV4cCI6MTYxNDA1MzQ5MSwidXNlcl9pZCI6Mzg2fQ.Qmy94_xR8tE0yiGsbvOoIW-gubMwL20xuWlI9_QacQY")
                .spec(RequestSpecs.generateFakeToken())
                .body(testarticle)
                .when()
                .post(resourcePath)
                .then()
                .body("message", equalTo("Please login first"))
                .and()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test (groups = "create_article")
    public void Test_Delete_Article_success(){
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

}

