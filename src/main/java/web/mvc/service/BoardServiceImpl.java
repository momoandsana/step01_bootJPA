package web.mvc.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import web.mvc.domain.Board;
import web.mvc.repository.BoardRepository;

@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public void insert(Board board) {
        // boardRepository.save();
    }
}
