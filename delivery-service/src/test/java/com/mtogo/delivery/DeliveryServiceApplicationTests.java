package com.mtogo.delivery;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Disabled("Disabled to allow isolated unit testing without getting errors about missing configurations")
class DeliveryServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
