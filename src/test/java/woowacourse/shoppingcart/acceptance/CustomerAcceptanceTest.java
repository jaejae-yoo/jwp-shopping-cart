package woowacourse.shoppingcart.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import woowacourse.acceptance.RestAssuredFixture;
import woowacourse.acceptance.AcceptanceTest;
import woowacourse.auth.dto.LogInRequest;
import woowacourse.shoppingcart.dto.DeleteCustomerRequest;
import woowacourse.shoppingcart.dto.SignUpRequest;
import woowacourse.shoppingcart.dto.UpdatePasswordRequest;

import static org.hamcrest.core.Is.is;

@DisplayName("회원 관련 기능")
public class CustomerAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("회원가입을 할 수 있다.")
    void addCustomer() {
        SignUpRequest signUpRequest = new SignUpRequest("alien", "alien@woowa.com", "1234");

        RestAssuredFixture.post(signUpRequest, "users", HttpStatus.CREATED.value())
                .body("username", is("alien"))
                .body("email", is("alien@woowa.com"));
    }

    @Test
    @DisplayName("회원가입을 할 수 없다 - 중복된 이름 입력")
    void addCustomerDuplicateUsernameException() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("alien", "alien@woowa.com", "1234");

        RestAssuredFixture.post(signUpRequest, "users", HttpStatus.CREATED.value())
                .body("username", is("alien"))
                .body("email", is("alien@woowa.com"));

        //when & then
        RestAssuredFixture.post(signUpRequest, "users", HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("회원가입을 할 수 없다 - 중복된 이메일 입력")
    void addCustomerDuplicateEmailException() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("alien", "alien@woowa.com", "1234");

        RestAssuredFixture.post(signUpRequest, "users", HttpStatus.CREATED.value())
                .body("username", is("alien"))
                .body("email", is("alien@woowa.com"));

        //when & then
        SignUpRequest signUpRequest2 = new SignUpRequest("rennon", "alien@woowa.com", "1234");
        RestAssuredFixture.post(signUpRequest2, "users", HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("로그인을 할 수 있다.")
    void signInCustomer() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("rennon", "rennon@woowa.com", "1234");
        RestAssuredFixture.post(signUpRequest, "users", HttpStatus.CREATED.value());

        LogInRequest logInRequest = new LogInRequest("rennon@woowa.com", "1234");
        String token = RestAssuredFixture.getSignInResponse(logInRequest, "/login").getToken();

        //when & then
        RestAssuredFixture.get(token, "/users/me", HttpStatus.OK.value())
                .body("username", is("rennon"))
                .body("email", is("rennon@woowa.com"));
    }

    @Test
    @DisplayName("로그인을 할 수 없다. - 잘못된 password")
    void signInEmailException() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("rennon", "rennon@woowa.com", "1234");
        RestAssuredFixture.post(signUpRequest, "users", HttpStatus.CREATED.value());

        //when & then
        LogInRequest logInRequest = new LogInRequest("rennon@woowa.com", "1235");
        RestAssuredFixture.post(logInRequest, "/login", HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("로그인을 할 수 없다. - 잘못된 email")
    void signInUsernameException() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("rennon", "rennon@woowa.com", "1234");
        RestAssuredFixture.post(signUpRequest, "users", HttpStatus.CREATED.value());

        //when & then
        LogInRequest logInRequest = new LogInRequest("rennon1@woowa.com", "1234");
        RestAssuredFixture.post(logInRequest, "/login", HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("내 정보 조회")
    @Test
    void getMe() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("rennon", "rennon@woowa.com", "1234");
        RestAssuredFixture.post(signUpRequest, "users", HttpStatus.CREATED.value());

        LogInRequest logInRequest = new LogInRequest("rennon@woowa.com", "1234");
        String token = RestAssuredFixture.getSignInResponse(logInRequest, "/login").getToken();

        //when & then
        RestAssuredFixture.get(token, "/users/me", HttpStatus.OK.value())
                .body("username", is("rennon"))
                .body("email", is("rennon@woowa.com"));
    }

    @DisplayName("토큰이 없으면 내 정보를 조회할 수 없다.")
    @Test
    void getMeException() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("rennon", "rennon@woowa.com", "1234");
        RestAssuredFixture.post(signUpRequest, "users", HttpStatus.CREATED.value());

        //when & then
        RestAssuredFixture.get("dummy", "/users/me", HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("내 정보 수정")
    @Test
    void updateMe() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("rennon", "rennon@woowa.com", "1234");
        RestAssuredFixture.post(signUpRequest, "users", HttpStatus.CREATED.value());

        LogInRequest logInRequest = new LogInRequest("rennon@woowa.com", "1234");
        String token = RestAssuredFixture.getSignInResponse(logInRequest, "/login").getToken();

        //when & then
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest("1234", "5678");
        RestAssuredFixture.patch(updatePasswordRequest, token, "/users/me", HttpStatus.OK.value());
    }

    @DisplayName("토큰이 없으면 내 정보를 수정할 수 없다")
    @Test
    void updateMeThrowException() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("rennon", "rennon@woowa.com", "1234");
        RestAssuredFixture.post(signUpRequest, "users", HttpStatus.CREATED.value());

        //when & then
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest("1234", "5678");
        RestAssuredFixture.patch(updatePasswordRequest, "", "/users/me", HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("회원 탈퇴")
    @Test
    void deleteMe() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("rennon", "rennon@woowa.com", "1234");
        RestAssuredFixture.post(signUpRequest, "users", HttpStatus.CREATED.value());

        LogInRequest logInRequest = new LogInRequest("rennon@woowa.com", "1234");
        String token = RestAssuredFixture.getSignInResponse(logInRequest, "/login").getToken();

        //when & then
        DeleteCustomerRequest deleteCustomerRequest = new DeleteCustomerRequest("1234");
        RestAssuredFixture.delete(deleteCustomerRequest, token, "/users/me", HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("회원 탈퇴 - 비밀 번호가 틀린 경우 예외")
    @Test
    void deleteInValidPasswordException() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("rennon", "rennon@woowa.com", "1234");
        RestAssuredFixture.post(signUpRequest, "users", HttpStatus.CREATED.value());

        LogInRequest logInRequest = new LogInRequest("rennon@woowa.com", "1234");
        String token = RestAssuredFixture.getSignInResponse(logInRequest, "/login").getToken();

        //when & then
        DeleteCustomerRequest deleteCustomerRequest = new DeleteCustomerRequest("1235");
        RestAssuredFixture.delete(deleteCustomerRequest, token, "/users/me", HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("토큰이 없으면 탈퇴할 수 없다")
    @Test
    void deleteMeThrowException() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("rennon", "rennon@woowa.com", "1234");
        RestAssuredFixture.post(signUpRequest, "users", HttpStatus.CREATED.value());

        //when & then
        DeleteCustomerRequest deleteCustomerRequest = new DeleteCustomerRequest("1234");
        RestAssuredFixture.delete(deleteCustomerRequest, "", "/users/me", HttpStatus.UNAUTHORIZED.value());
    }
}
