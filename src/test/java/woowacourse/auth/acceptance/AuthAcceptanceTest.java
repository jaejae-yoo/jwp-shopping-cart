package woowacourse.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import woowacourse.auth.dto.CustomerResponse;
import woowacourse.auth.dto.SignInResponse;
import woowacourse.auth.dto.SignUpRequest;
import woowacourse.auth.dto.TokenRequest;
import woowacourse.auth.dto.TokenResponse;
import woowacourse.auth.utils.EmailUtil;
import woowacourse.shoppingcart.acceptance.AcceptanceTest;

@DisplayName("인증 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Bearer 토큰이 생성된다")
    @Test
    void createToken() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest("rennon", "rennon@woowa.com", "1234");
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(signUpRequest)
                .when().post("/users")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
        TokenRequest tokenRequest = new TokenRequest("rennon@woowa.com", "1234");

        // when
        String token = RestAssured
                .given().log().all()
                .body(tokenRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/auth/login")
                .then().log().all().extract().as(SignInResponse.class).getToken();

        // then
        assertThat(token).isNotBlank();
    }

//    @Disabled
    @DisplayName("Bearer Auth 로그인 성공")
    @Test
    void myInfoWithBearerAuth() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest("rennon", "rennon@woowa.com", "1234");
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(signUpRequest)
                .when().post("/users")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        TokenRequest tokenRequest = new TokenRequest("rennon@woowa.com", "1234");
        String token = RestAssured
                .given().log().all()
                .body(tokenRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/auth/login")
                .then().log().all().extract().as(SignInResponse.class).getToken();

        // when
        // 발급 받은 토큰을 사용하여 내 정보 조회를 요청하면
        String headOfEmail = EmailUtil.getIdentifier(signUpRequest.getEmail());
        CustomerResponse response = RestAssured
                .given().log().all()
                .header("Authorization","Bearer " + token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/users/" + headOfEmail)
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract().as(CustomerResponse.class);
        // then
        // 내 정보가 조회된다

        assertThat(response.getEmail()).isEqualTo("rennon@woowa.com");
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        // 회원이 등록되어 있고
        SignUpRequest signUpRequest = new SignUpRequest("rennon", "rennon@woowa.com", "1234");
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(signUpRequest)
                .when().post("/users")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        // when
        // 잘못된 id, password를 사용해 토큰을 요청하면
        // then
        // 토큰 발급 요청이 거부된다
        TokenRequest tokenRequest = new TokenRequest("rennon@woowa.com", "1235");
        RestAssured
                .given().log().all()
                .body(tokenRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/auth/login")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest("rennon", "rennon@woowa.com", "1234");
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(signUpRequest)
                .when().post("/users")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        // when
        // 유효하지 않은 토큰을 사용하여 내 정보 조회를 요청하면
        // then
        // 내 정보 조회 요청이 거부된다
        String headOfEmail = EmailUtil.getIdentifier(signUpRequest.getEmail());
        String token = "dummy";
        RestAssured
                .given().log().all()
                .header("Authorization","Bearer " + token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/users/" + headOfEmail)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
