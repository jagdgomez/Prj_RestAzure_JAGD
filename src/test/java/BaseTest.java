import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;


public class BaseTest {

    protected static String baseUrl;

    @Parameters("baseUrl")
    @BeforeClass
    public void setup(@Optional("https://api-coffee-testing.herokuapp.com") String baseUrl ) {

     //   this.baseUrl = baseUrl;  * RestAssured tiene una forma para mandar un baseurl, hay que eliminarlo
     //   de los otros urls */

        RestAssured.baseURI = baseUrl;
    }


}
