package specifications;

import helpers.RequestHelper;
import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.basic;

public class RequestSpecs {

      public static RequestSpecification generateToken() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        String token = RequestHelper.GetUserToken();
        requestSpecBuilder.addHeader("Authorization","Bearer " + token);
        return requestSpecBuilder.build();
    };
/* se pone static para no tener que declarar una instancia */
    public static RequestSpecification generateFakeToken() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.addHeader("Authorization","Bearer eyJhbGciOijIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NfdXVpZCI6IjdhMjVjYTBmLThjOTEtNDExZC05NmRkLWJkOTM2Mjg2YWM0ZCIsImF1dGhvcml6ZWQiOnRydWUsImV4cCI6MTYxNDA1NDUzNCwidXNlcl9pZCI6Mzg2fQ.7wgHpHEErgnDZmZEndSNxpPa-3x60OBtri3X51V2rIM");
        return requestSpecBuilder.build();
    };

}
