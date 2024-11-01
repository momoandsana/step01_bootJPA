package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.Board;

// 스프링 jpa -> 이렇게 만들면 기본 쿼리는 모두 만들어줌, 커스텀 쿼리는 따로 만들어야함
public interface BoardRepository extends JpaRepository<Board, Long> {
/*
JpaRepository 는 jpa 는 이 인터페이스를 구현하는 기본적인 CRUD 메소드를 자동으로 제공함
사용자가 바로

    @Autowired// spring data jpa 가 구현체를 생성해서 주입
    private BoardRepository boardRep;
    이렇게 바로 적용 가능
 */


    /*
    글번호를 인수로 받아서 보다 큰 레코드 삭제->기본으로 없는 기능이므로 여기다가 만들어야 한다
    jpql 문법을 사용할 때, dml(insert,update,delete)을 할 때는
    반드시 @Modifying 어노테이션과 같이 사용
    select 는 필요x
     */
    @Query(value="delete from Board b where b.bno>?1")// jpql 이기 때문에 대소문자 가림
    @Modifying
    void deleteByBno(Long bno);
}

/*
<가능한 모든 함수>
// 게시글 저장 또는 업데이트
public Board save(Board board) {
    return boardRepository.save(board); // 주어진 게시글을 저장하고, 저장된 게시글을 반환
}

// 특정 ID로 게시글 조회
public Optional<Board> findById(Long id) {
    return boardRepository.findById(id); // 주어진 ID에 해당하는 게시글을 Optional로 반환
}

// 모든 게시글 조회
public List<Board> findAll() {
    return boardRepository.findAll(); // 모든 게시글을 리스트로 반환
}

// 특정 ID 목록에 해당하는 게시글 조회
public List<Board> findAllById(Iterable<Long> ids) {
    return boardRepository.findAllById(ids); // 주어진 ID 목록에 해당하는 모든 게시글을 리스트로 반환
}

// 게시글 개수 세기
public long count() {
    return boardRepository.count(); // 총 게시글 수를 반환
}

// 특정 ID로 게시글 삭제
public void deleteById(Long id) {
    boardRepository.deleteById(id); // 주어진 ID에 해당하는 게시글을 삭제
}

// 주어진 게시글 삭제
public void delete(Board board) {
    boardRepository.delete(board); // 주어진 게시글을 삭제
}

// 모든 게시글 삭제
public void deleteAll() {
    boardRepository.deleteAll(); // 모든 게시글을 삭제
}

// 주어진 ID 목록에 해당하는 게시글 삭제
public void deleteAllById(Iterable<Long> ids) {
    boardRepository.deleteAllById(ids); // 주어진 ID 목록에 해당하는 모든 게시글을 삭제
}

// 여러 게시글 저장
public List<Board> saveAll(Iterable<Board> boards) {
    return boardRepository.saveAll(boards); // 주어진 게시글 목록을 저장하고, 저장된 게시글 목록을 반환
}

// 모든 게시글 정렬 조회
public List<Board> findAll(Sort sort) {
    return boardRepository.findAll(sort); // 주어진 정렬 기준에 따라 모든 게시글을 리스트로 반환
}

// 페이징된 게시글 조회
public Page<Board> findAll(Pageable pageable) {
    return boardRepository.findAll(pageable); // 주어진 Pageable 정보를 바탕으로 게시글을 페이징하여 조회
}

업데이트는 따로 없고 save 하면 insert or update 된다
 */