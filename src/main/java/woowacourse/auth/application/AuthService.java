package woowacourse.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.domain.Customer;
import woowacourse.auth.dto.LogInRequest;
import woowacourse.auth.dto.LogInResponse;
import woowacourse.auth.support.JwtTokenProvider;
import woowacourse.shoppingcart.dao.CustomerDao;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomerDao customerDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, CustomerDao customerDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customerDao = customerDao;
    }

    @Transactional
    public LogInResponse signIn(LogInRequest logInRequest) {
        Customer customer = customerDao.findByEmail(logInRequest.getEmail());
        customer.isValidPassword(logInRequest.getPassword());

        return new LogInResponse(
                customer.getUsername(),
                customer.getEmail(),
                jwtTokenProvider.createToken(customer.getUsername())
        );
    }
}
