package web.mvc.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 환경설정을 돕는 클래스, 서버가 스타트될 때 @Configuration 안에 있는 설정이 세팅된다
@Slf4j
public class QueryDSLConfig {

    @PersistenceContext // EntityManagerFactory 로 부터 EntityManager 를 주입 받는다
    private EntityManager entityManager;

    @Bean // 현재 메소드가 리턴하는 객체를 springContainer 영역에 bean 으로 등록한다
    public JPAQueryFactory getQueryFactory() {
        log.info("getQueryFactory call..");
        log.info("enetityManager = {}",entityManager);



        return new JPAQueryFactory(entityManager);
    }

}
