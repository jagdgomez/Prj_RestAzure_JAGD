import org.testng.annotations.Test;
import pojo.User;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class UserTests {
    private static String baseUrl="http://localhost:9000";
    private static String resourcePath="v1/user";

    @Test
    public void Test_Creat_User_Already_Exist(){

        User user = new User("Mauricio","pablo@test.com","castro");

        given().body(user)
                .when()
                .post(String.format("%s%s/register",baseUrl,resourcePath))
                .then()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(406);
    }
}

