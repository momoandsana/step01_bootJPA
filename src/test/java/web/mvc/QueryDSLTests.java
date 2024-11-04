package web.mvc;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import web.mvc.domain.Board;
import web.mvc.domain.QBoard;
import web.mvc.repository.BoardRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
//@RequiredArgsConstructor
@Slf4j
public class QueryDSLTests {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void init(){
        log.info("jpaQueryFactory: {}", jpaQueryFactory);
        log.info("boardRepository: {}", boardRepository);
    }

    /*
    querydsl 사용하기
     */
    @Test
    @DisplayName("QueryDSL테스트")
    public void queryDSL(){
        QBoard board = QBoard.board;

        //selectAll 과 같은 효과
        List<Board> list = jpaQueryFactory.selectFrom(board).fetch();
        System.out.println("---------------------------------------");
        list.forEach(board1-> System.out.println(board1));

    }

    @Test
    @DisplayName("QueryDSL 조건 테스트")
    public void queryDSL2(){
        QBoard board = QBoard.board;
        List<Board> list = jpaQueryFactory
                .selectFrom(board)
                .where(board.bno.lt(50L).or(board.title.eq("제목80"))).fetch();
        list.forEach(board1-> System.out.println(board1));
    }

    /**
     * QueryDSL 사용하기
     * interface 에 QueryPredicateExecutor<> 상속받는다
     * QueryPredicateExecutor 안에서 제공하는 메소드를 사용해서 자바중심으로 조건을 만들 수 있다
     * (자동으로 QEntity 이름.java 가 생성된다)
     * ex) ~.findAll(Predicate p)
     */
    @Test
    public void querydslPredicate(){

        BooleanBuilder builder = new BooleanBuilder();// 조건을 만들 때 사용, where 절이 들어감
        QBoard board = QBoard.board;
        //조건
//        builder.and(board.bno.eq(2L));
//        builder.or(board.writer.like("%작성자1%"));// where 절의 writer like '%작성자1%'

        LocalDateTime from = LocalDateTime.of(2024, 11, 1, 0, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 11, 2, 12, 0, 0);
//
//        builder.and(board.writer.eq("userq")); // 대소문자 구분한다
//
//        builder.and(board.writer.equalsIgnoreCase("user1"));
//
//       builder.and(board.writer.toUpperCase().eq("user1".toUpperCase()));// 대소문자 구분x
//
        //builder.and(board.writer.toUpperCase().eq("user1".toUpperCase())).or(board.bno.gt(140L));


        Iterable<Board> iterable = boardRepository.findAll(builder);

        ArrayList<Board> list = Lists.newArrayList(iterable);// Iterable 을 ArrayList 로 변환
        list.forEach(board1-> System.out.println(board1));
    }

    @Test
    public void dml(){
        QBoard board = QBoard.board;

        // 삭제
        jpaQueryFactory
                .delete(board)
                        .where(board.bno.gt(100L))
                        .execute();

        //수정
        jpaQueryFactory
                .update(board)
                .set(board.writer,"얼짱")
                .set(board.title,"QueryFactory연습")
                .where(board.bno.eq(2L))
                .execute();
    }
    /*
    queryDSL 은 쿼리를 만들어주기만 한다
    ->JPAQueryFactory 를 통해 진짜 쿼리로 변환해준다

    predicate -> 조건을 다양하게 하고 싶다면
    BooleanBuilder
    QueryDSLPredicateExecutor 가 필요
     */
}
