import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;


public class MiscTests extends BaseTest {
  /* removed to use the parameter from Base Test
    private static String baseUrl="https://api-coffee-testing.herokuapp.com"; */

    private static String resourcePath="";


    @Test
    public void ATest_PING_ENDPOINT(){

        //given()
        /*se cambia este given de arriba para poder usar lo que se genere en el base test,
         pero hay que extender la clase base test para usarlo igual que abajo */

        /*como parte de los headers van los authorizations*/

          given()
                .headers("User-Agent","Johnny Agent")
         .when()
                .get(resourcePath + "/ping")
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
                .get("/")
        .then()
                .body(containsString("Gin Boilerplate"))
                .statusCode(200);
    }
}
