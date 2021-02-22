import org.testng.annotations.Test;
import pojo.User;

import static helpers.DataHelper.generateRandomEmail;
import static org.hamcrest.core.IsEqual.equalTo;

public class UserTests extends BaseTest {
   /* removed to use parameter from BaseTest
   private static String baseUrl="https://api-coffee-testing.herokuapp.com";*/

    private static String resourcePath="/v1/user";

    /*path de aqui es: https://api-coffee-testing.herokuapp.com/v1/user/register"*/

    @Test
    public void NegativeTest_Creating_User_Already_Exist(){

        User user = new User("Mauricio","pablo@test.com","castro");
        System.out.println("Already Created User: "+ user.getEmail());
        System.out.println("Request to: " + String.format("%s/register",resourcePath));
        baseRequest
                .body(user)
                .when()
                    .post(String.format("%s/register",resourcePath))
                .then()
                    .body("message", equalTo("User already exists"))
                .and()
                    .statusCode(406);
    }
    @Test
    public void Test_Create_User_Successful(){

        User user = new User("Johnny",generateRandomEmail(),"gomez");
        System.out.println("Email Generated: "+ user.getEmail());
        System.out.println("Request to: " + String.format("%s/register",resourcePath));

        baseRequest
                .body(user)
                .when()
                    .post(String.format("%s/register",resourcePath))
                .then()
                    .body("message", equalTo("Successfully registered"))
                .and()
                    .statusCode(200);
    }

    @Test
    public void Test_login_User_Successful(){

        User user = new User("Johnny","jagdtest@test.com","gomez");
        System.out.println ("Login User: "+ user.getEmail());
        System.out.println("Request to: " + String.format("%s/login",resourcePath));
        baseRequest
                .body(user)
                .when()
                    .post(String.format("%s/login",resourcePath))
                .then()
                    .body("message", equalTo("User signed in"))
                .and()
                    .statusCode(200);
    }
}

