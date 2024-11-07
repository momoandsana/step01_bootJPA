package web.mvc;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import web.mvc.domain.Board;
import web.mvc.domain.BoardDTO;
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
    /*
    QueryDSLConfig 에서 getQueryFactory() 의 빈을 주입받음
     */
    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void init() {
        log.info("jpaQueryFactory: {}", jpaQueryFactory);
        log.info("boardRepository: {}", boardRepository);
    }

    /*
    querydsl 사용하기
     */
    @Test
    @DisplayName("QueryDSL테스트")
    public void queryDSL() {
        QBoard board = QBoard.board;
        /*
        QBoard 클래스: Querydsl을 사용하면 각 엔티티 클래스(Board)에 대응하는 Q 접두사를 가진 클래스를 자동으로 생성합니다.
        예를 들어, Board 엔티티가 있다면 Querydsl이 빌드 과정에서 QBoard라는 클래스를 만들어줍니다.
        이 클래스는 Board 엔티티의 필드들을 속성으로 가지며, 동적 쿼리를 작성할 때 사용됩니다.

        QBoard.board: QBoard 클래스에는 엔티티를 나타내는 board라는 정적 필드가 있습니다.
        이 필드를 사용하면 Board 엔티티의 필드에 쉽게 접근하여 조건을 지정할 수 있습니다.

        board 는 QBoard 의 정적 필드
         */

        //selectAll 과 같은 효과
        List<Board> list = jpaQueryFactory.selectFrom(board).fetch();
        System.out.println("---------------------------------------");
        list.forEach(board1 -> System.out.println(board1));

    }

    @Test
    @DisplayName("QueryDSL 조건 테스트")
    public void queryDSL2() {
        QBoard board = QBoard.board; /*
        Board 클래스이기 때문에 QBoard.board -> User 클래스였다면 QUser.user
        build.gradle 이나 pom.xml 에 queryDSL 을 추가했기 때문에 자동으로 생긴다.
        board 클래스에는 @Entity 가 있고
        */
        List<Board> list = jpaQueryFactory
                .selectFrom(board)
                .where(board.bno.lt(50L).or(board.title.eq("제목80"))).fetch();
        list.forEach(board1 -> System.out.println(board1));
    }

    /**
     * QueryDSL 사용하기
     * interface 에 QueryPredicateExecutor<> 상속받는다
     * QueryPredicateExecutor 안에서 제공하는 메소드를 사용해서 자바중심으로 조건을 만들 수 있다
     * (자동으로 QEntity 이름.java 가 생성된다)
     * ex) ~.findAll(Predicate p)
     * <p>
     * 재사용성이나 복잡한 조건을 사용할 때 predicate 필요
     */
    @Test
    public void querydslPredicate() {

        BooleanBuilder builder = new BooleanBuilder();// 조건을 만들 때 사용, where 절이 들어감, predicate 전용
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
        list.forEach(board1 -> System.out.println(board1));
    }

    @Test
    public void dml() {
        QBoard board = QBoard.board;

        // 삭제
        jpaQueryFactory
                .delete(board)
                .where(board.bno.gt(100L))
                .execute();

//        이것도 가능

//        BooleanBuilder deleteCondition = new BooleanBuilder();
//        deleteCondition.and(board.bno.gt(100L)); // bno가 100보다 큰 조건
//
//        jpaQueryFactory
//                .delete(board)
//                .where(deleteCondition) // 조건 적용
//                .execute();



        //수정
        jpaQueryFactory
                .update(board)
                .set(board.writer, "얼짱")
                .set(board.title, "QueryFactory 연습")
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

    @Test
    public void translateDTO(){// 일반적으로 이런 변환 함수는 서비스 계층에 위치, 리포지터리에서 엔티티로 받아서 서비스 게층에서 변환
        QBoard board = QBoard.board;

        /*
        queryDSL 을 사용하지 않으면 dto 코드에서 from 함수를 통해 엔티티를 dto 로 변환
        from 함수는 간단한 겨웅에 dto 코드에 들어간다
        Projections 코드는 서비스에 들어간다
         */
        List<BoardDTO> list = jpaQueryFactory
                .select(Projections.fields(BoardDTO.class,
                        board.bno,
                        board.title,
                        board.writer,
                        board.content,
                        board.insertDate,
                        board.updateDate)) // Board 에는 있고 BoardDTO 에는 없는 컬럼이 들어가면 그냥 무시하고 해당 컬럼 안 만들어
                .from(board)
                .fetch();

        list.forEach(board1 -> System.out.println(board1));
    }
    /*
    Projections.fields 를 통해 원하는 컬럼들만 지정해서 가지고 온다
    동시에 Board 엔티티가 아니라 BoardDTO 로 바꿔서 가지고 온다

    엔티티는 디비랑 주고 받는 경우에만 사용하고
    비즈니스로직에서 데이터가 이동할 때는 dto 로 변환해서 이동한다
     */
}
