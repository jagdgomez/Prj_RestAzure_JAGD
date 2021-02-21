import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;


public class MiscTests {
    private static String baseUrl="http://localhost:9000";
    private static String resourcePath="";


    @Test
    public void ATest_PING_ENDPOINT(){

        given()
                .headers("User-Agent","Pablo Agent")
                .when()
                .get(String.format("%s%s/ping",baseUrl,resourcePath))
                .then()
                .header("Content-Length",equalTo("50"))
                .header("Access-Control-Allow-Origin",equalTo("http://localhost"))
                .and()
                .body("response", equalTo("pong"))
                .and()
                .statusCode(200);
    }

    @Test
    public void BTest_HomePage_Response(){
        given()
                .get(String.format("%s/",baseUrl))
                .then()
                .body(containsString("Gin Boilerplate"))
                .statusCode(200);
    }
}
