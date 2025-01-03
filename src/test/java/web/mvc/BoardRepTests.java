package web.mvc;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import web.mvc.domain.Board;
import web.mvc.repository.BoardRepository;

import java.util.List;
import java.util.Optional;

//@SpringBootTest // 통합 테스트, 커밋이 기본, 롤백 x, 빈이 자동 주입됨->private final + @AllArgsConstructor 가 다 먹힘

@DataJpaTest // 영속성 관련된 테스트,롤백이 기본,-> jpa 관련 컴포턴느 및 리포지터리만 주입 받는다
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 자동으로 잡힌 데이터베이스인 h2를 쓰지 않겠다
@Rollback(value = false)
@Slf4j
//@AllArgsConstructor
public class BoardRepTests {

    //spring data jpa 가 구현체를 생성해서 주입
    @Autowired
    private BoardRepository boardRep;// jpa 관련 컴포넌트이기 때문에 주입이 가능하다

    /*
    @SpringBootTest 가 아니라 @DataJpaTest 로 하면
    private final + @AllArgsConstructor 는 스프링에서 직접 주입해야 하므로 에러가 생김
     */

    //@Autowired
    //private BoardService boardService;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;



    @Test
    public void init() {
        log.info("boardRep={}", boardRep);
        //log.info("boardService={}",boardService);
    }

    /*
    board 등록하기
     */
    @Test
    @DisplayName("board 등록")
    @Disabled
    public void insert() {
        //boardRep.save(Board.builder().title("제목1").writer("작성자1").content("내용1").build());

//        for(int i=2;i<200;i++)
//        {
//            boardRep.save(Board.builder().title("제목"+i).writer("작성자"+i).content("내용"+i).build());
//        }

        // 이미 존재하는 pk를 설정하면 등록이 아니고 update 가 된다(save 메서드는 insert or update)
        // 원래 persist 는 insert 만
//        boardRep.save(Board.builder()
//                .bno(5L) // 원래 bno 는 자동으로 만들어 준다
//                .title("수정")
//                .writer("수정작성자")
//                .content("수정내용")
//                .build());

        // save() 메소드의 리턴값 사용하기
        Board board = boardRep.save(Board.builder()
                .title("test01")
                .writer("작성자02")
                .content("내용00")
                .build());

        log.info("board={}", board);

        /*
        검증(마지막 번호의 결과가 100인지 검증)
         */
        Assertions.assertEquals(205, board.getBno());

        log.info("--- end ---");

    }// transaction 관리 - 메소드 빠져나가면 commit

    /*
    전체검색
     */
    @Test
    @DisplayName("전체검색")
    public void select() {
        List<Board> list = boardRep.findAll();
        list.forEach(board -> System.out.println(board));

//        list.forEach(new Consumer<Board>() {
//            @Override
//            public void accept(Board board) {
//                System.out.println(board);
//            }
//        }); 이게 위에 람다식을 풀어 쓴 코드
    }

    /*
    pk 에 해당하는 검색
     */
    @Test
    @DisplayName("pk 대상검색")
    public void selectByBno() {
        Optional<Board> optionalBoard = boardRep.findById(300L);
        Board board = optionalBoard.orElse(Board.builder().title("예외!").build());

        log.info("board={}", board); // 디비에 없을 경우 객체를 빌드해서 보내줌
    }


    /*
    수정
     */
    @Test
    @DisplayName("수정하기")
    public void update() {
        Board board = boardRep.findById(100L).orElse(null);
        if (board != null) {
            board.setContent("배고파요");
            board.setWriter("희정");
        }

    }

    /*
    삭제
     */
    @Test
    @DisplayName("삭제하기")
    public void delete() {
        boardRep.deleteById(100L);
    }

    //// JPQL 문법
    /*
    글번호가 100보다 큰 레코드 삭제
     */
    @Test
    @DisplayName("조건 삭제하기")
    public void deleteByBno() {
        boardRep.deleteByBno(100L);
    }

    /*
    글번호 or 제목 에 해당하는 레코드 검색
     */
    @Test
    @DisplayName("조건검색")
    public void selectByBnoTitle() {
        List<Board> list = boardRep.selectByBnoTitle(7L, "제목11");
        list.forEach(board -> System.out.println(board));
    }

    /*
    글번호, 제목, 작성자에 해당하는 레코드 검색
     */
    @Test
    @DisplayName("여러 조건 검색")
    public void multiWhere() {
        List<Board> list = boardRep.multiWhere(Board.builder().bno(50L).writer("작성자11").title("제목20").build());
        list.forEach(board -> System.out.println(board));
    }

///////////////// Query Method 적용 ////////////////


    /*
    전달된 글 번호보다 작고 전달된 작성자와 동일한 레코드 검색
     */
    @Test
    @DisplayName("쿼레 메소드 test")
    public void queryMethod() {
        List<Board> list = boardRep.findByBnoLessThanAndWriter(50L, "작성자11");
        list.forEach(board -> System.out.println(board));
    }

///////////////// 페이징 처리 적용 ////////////////

    @Test
    @DisplayName("페이징처리")
    public void paging(){
        Pageable pageable= PageRequest.of(10,10, Sort.Direction.DESC,"bno");
        // 내부적으로는 1이 아니라 0부터 시작.여기서 0이 첫 번째 페에지.하나의 페이지에 5개의 게시물, hibernate 에서 count를 만든다

        Page<Board> page = boardRep.findAll(pageable);
        //findAll 중에 pageable 를 인수로 받는거 선택하기
        System.out.println("----------------------");
        System.out.println("page.getNumber() = " + page.getNumber());
        System.out.println("page.getSize() = " + page.getSize());
        System.out.println("page.getTotalPages() = " + page.getTotalPages());
        System.out.println("page = " + page.previousPageable());
        System.out.println("page = " + page.nextPageable());

        System.out.println("page = " + page.isFirst());
        System.out.println("page.isLast() = " + page.isLast());
        System.out.println("page.hasNext() = " + page.hasNext());
        System.out.println("page.hasPrevious() = " + page.hasPrevious());// pageNumber 가 0이면 첫 번째
        System.out.println("**********************************************");

        List<Board>list=page.getContent();
        list.forEach(board -> System.out.println(board));


        System.out.println("-----------------------");
    }

    /*
    페이징처리
    한 페이지당 몇 개씩 출력
    현재 페이지 번호
    정렬방식
     */
}




















