import org.testng.annotations.Test;
import pojo.User;
import helpers.DataHelper;

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

        baseRequest
                .body(user)
                .when()
                .post(String.format("%s%s/register",baseUrl,resourcePath))
                .then()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(406);
    }
    @Test
    public void Test_Create_User_Successful(){

        User user = new User("Johnny",generateRandomEmail(),"gomez");
        System.out.println("Email Generated: "+ user.getEmail());

        baseRequest
                .body(user)
                .when()
                .post(String.format("%s%s/register",baseUrl,resourcePath))
                .then()
                .body("message", equalTo("Successfully registered"))
                .and()
                .statusCode(200);
    }
}

