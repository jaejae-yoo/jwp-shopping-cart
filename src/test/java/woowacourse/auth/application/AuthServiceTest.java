package woowacourse.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.auth.dto.SignInRequest;
import woowacourse.auth.dto.SignInResponse;
import woowacourse.shoppingcart.application.CustomerService;
import woowacourse.shoppingcart.dto.SignUpRequest;
import woowacourse.shoppingcart.exception.InvalidCustomerException;

@SpringBootTest
@Sql(scripts = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private CustomerService customerService;

    @Test
    @DisplayName("로그인에 성공한다.")
    void signIn() {
        customerService.addCustomer(new SignUpRequest("레넌", "rennon@woowa.com", "1234"));
        SignInResponse signInResponse = authService.signIn(new SignInRequest("rennon@woowa.com", "1234"));

        assertThat(signInResponse.getUsername()).isEqualTo("레넌");
        assertThat(signInResponse.getToken()).isNotNull();
    }

    @Test
    @DisplayName("로그인에 실패한다.")
    void signInFail() {
        assertThatThrownBy(() -> authService.signIn(new SignInRequest("rennon@woowa.com", "1234")))
                .isInstanceOf(InvalidCustomerException.class)
                .hasMessageContaining("로그인 실패");
    }
}
