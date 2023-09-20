package com.gongsi.exam.repository;

import com.gongsi.exam.domain.Quiz;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Quiz entity.
 */
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    default Optional<Quiz> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Quiz> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Quiz> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select quiz from Quiz quiz left join fetch quiz.exam", countQuery = "select count(quiz) from Quiz quiz")
    Page<Quiz> findAllWithToOneRelationships(Pageable pageable);

    @Query("select quiz from Quiz quiz left join fetch quiz.exam")
    List<Quiz> findAllWithToOneRelationships();

    @Query("select quiz from Quiz quiz left join fetch quiz.exam where quiz.id =:id")
    Optional<Quiz> findOneWithToOneRelationships(@Param("id") Long id);
}
