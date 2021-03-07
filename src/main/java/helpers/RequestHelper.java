package helpers;

import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;

public class RequestHelper {
    private static AuthenticationScheme BasicToken;

    public static String GetUserToken(){
        /* usamos Response para manipular la respuesta sin tener que validarlo */

        Response response = given().body(DataHelper.getTestUser()).post("/v1/user/login");
        JsonPath jsonPathEvaluator = response.jsonPath();
        String token = jsonPathEvaluator.get("token.access_token");
        return token;
    }

    public static void generateBasicToken() {
        BasicToken  = RestAssured.authentication = basic ("testuser","testpass");
    };


    public static void generateInvalidBasicToken() {
        BasicToken  = RestAssured.authentication = basic ("testuser1","testpass1");
    };


}
