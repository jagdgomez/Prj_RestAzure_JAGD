import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import static io.restassured.RestAssured.given;

public class BaseTest {

    protected RequestSpecification baseRequest;
    protected static String baseUrl;

    @Parameters("baseUrl")
    @BeforeMethod
    public void setup(@Optional("https://api-coffee-testing.herokuapp.com") String baseUrl ) {

     //   this.baseUrl = baseUrl;  * RestAssured tiene una forma para mandar un baseurl, hay que eliminarlo
     //   de los otros urls */
        RestAssured.baseURI = baseUrl;
        baseRequest = given().headers("User-Agent","Johnny's user Agent");

    }
}
