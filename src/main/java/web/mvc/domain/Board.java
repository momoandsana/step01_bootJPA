package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity // jpa의 관리대상, @NoArgsConstructor 랑 세트->jpa에서 엔티티 클래스 관리하기 위해 필요함
@Builder
@Getter
@Setter
@NoArgsConstructor // jpa 에서 엔티티 클래스는 기본 생성자가 필요함
@AllArgsConstructor // @Builder 에서 모든 필드를 인자로 받는 생성자가 필요함
@ToString
@DynamicUpdate // 원하는 부분만 수정, 전체 업데이트 x
public class Board {

    @Id // pk
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="board_bno_seq")
    @SequenceGenerator(name="board_bno_seq",sequenceName = "board_bno_seq",allocationSize=1)
    private Long bno;

    @Column(nullable = false) // not null
    private String title;

    @Column(length=20)
    private String writer;

    private String content;

    @CreationTimestamp
    private LocalDateTime insertDate;
    @UpdateTimestamp
    private LocalDateTime updateDate;
}
