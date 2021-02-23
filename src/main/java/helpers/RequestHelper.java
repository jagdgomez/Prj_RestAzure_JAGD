package helpers;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import specifications.ResponseSpecs;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class RequestHelper {
    public static String GetUserToken(){
        /* usamos Response para manipular la respuesta sin tener que validarlo */

        Response response = given().body(DataHelper.getTestUser()).post("/v1/user/login");
        JsonPath jsonPathEvaluator = response.jsonPath();
        String token = jsonPathEvaluator.get("token.access_token");
        return token;
    }
}
