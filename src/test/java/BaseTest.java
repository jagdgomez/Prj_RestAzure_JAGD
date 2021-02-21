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
    public void setup(@Optional("http://localhost:9000") String baseUrl ) {

        this.baseUrl = baseUrl;
        baseRequest = given().headers("User-Agent","Mi user Agent");

    }
}
