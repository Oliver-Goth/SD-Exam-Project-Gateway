package com.mtogo.customer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import com.mtogo.customer.infrastructure.adapter.out.persistence.CustomerJpaRepository;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase
@SuppressWarnings("removal")
class CustomerServiceApplicationTests {

	@MockBean
	private CustomerJpaRepository customerJpaRepository;

	@Test
	void contextLoads() {
	}

}
