package com.gongsi.exam.repository;

import com.gongsi.exam.domain.Exam;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Exam entity.
 */
@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    default Optional<Exam> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Exam> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Exam> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select exam from Exam exam left join fetch exam.agency left join fetch exam.license",
        countQuery = "select count(exam) from Exam exam"
    )
    Page<Exam> findAllWithToOneRelationships(Pageable pageable);

    @Query("select exam from Exam exam left join fetch exam.agency left join fetch exam.license")
    List<Exam> findAllWithToOneRelationships();

    @Query("select exam from Exam exam left join fetch exam.agency left join fetch exam.license where exam.id =:id")
    Optional<Exam> findOneWithToOneRelationships(@Param("id") Long id);
}
