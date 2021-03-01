import helpers.DataHelper;
import org.testng.annotations.Test;
import pojo.User;
import specifications.RequestSpecs;
import specifications.ResponseSpecs;

import static helpers.DataHelper.generateRandomEmail;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class UserTests extends BaseTest {
   /* removed to use parameter from BaseTest
   private static String baseUrl="https://api-coffee-testing.herokuapp.com";*/

    private static String resourcePath="/v1/user";

    /*path de aqui es: https://api-coffee-testing.herokuapp.com/v1/user/register"*/

    @Test
    public void BNegativeTest_Creating_User_Already_Exist(){

        User user = new User("Mauricio","pablo@test.com","castro");
        System.out.println("Already Created User: "+ user.getEmail());
        System.out.println("Request to: " + String.format("%s/register",resourcePath));

                  given()
                    .body(user)
                .when()
                    .post(String.format("%s/register",resourcePath))
                .then()
                    .body("message", equalTo("User already exists"))
                .and()
                    .statusCode(406);
    }
    @Test
    public void ATest_Create_User_Successful(){

        User user = new User("Johnny",generateRandomEmail(),"gomez");
        System.out.println("Email Generated: "+ user.getEmail());
        System.out.println("Request to: " + String.format("%s/register",resourcePath));

                given()
                    .body(user)
                .when()
                    .post(String.format("%s/register",resourcePath))
                .then()
                    .body("message", equalTo("Successfully registered"))
                .and()
                    .statusCode(200)
                //usando el spect que se creo en el base test
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void CTest_login_User_Successful(){

        System.out.println ("Login User with: "+ " eMail: " + DataHelper.getTestUser().getEmail() +  " Name: " + DataHelper.getTestUser().getName());
        System.out.println("Request to: " + String.format("%s/login",resourcePath));

                given()
                    .body(DataHelper.getTestUser())
                .when()
                    .post(String.format("%s/login",resourcePath))
                .then()
                    .body("message", equalTo("User signed in"))
                .and()
        //        .header("Content-Type",equalTo("application/json; charset=utf-8"))
        //        .header("Access-Control-Allow-Origin",equalTo("http://localhost"))
                    .statusCode(200)
        /* en lugar de validar el header,, ahora usamos el spec aqui */
                    .spec(ResponseSpecs.defaultSpec());

    }

    @Test
    public void DTest_logOut_User_Successful(){

        System.out.println ("Logout User with: "+ " eMail: " + DataHelper.getTestUser().getEmail() +  " Name: " + DataHelper.getTestUser().getName());
        System.out.println("Request to: " + String.format("%s/logout",resourcePath));

        given()
                .spec(RequestSpecs.generateToken())
                .get(String.format("%s/logout",resourcePath))
                .then()
                .body("message", equalTo("Successfully logged out"))
                .and()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());

    }
}

