package web.mvc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class Step01BootJpaApplicationTests {

	@Test
	@DisplayName("처음 test") // 테스트 이름
	void contextLoads() {
		log.info("단위테스트 테스트");
	}

	@BeforeEach // 이거는 테스트 아님
	@DisplayName("사전처리")
	public void beforeEach() {
		log.info("beforeEach 테스트");
	}

	@AfterEach // 이거는 테스트 아님
	@DisplayName("사후처리")
	public void afterEach() {
		log.info("afterEach 테스트");
	}

}
