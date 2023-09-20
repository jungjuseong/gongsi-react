package com.gongsi.exam.repository;

import com.gongsi.exam.domain.Explain;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Explain entity.
 */
@Repository
public interface ExplainRepository extends JpaRepository<Explain, Long> {
    default Optional<Explain> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Explain> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Explain> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select jhiExplain from Explain jhiExplain left join fetch jhiExplain.quiz",
        countQuery = "select count(jhiExplain) from Explain jhiExplain"
    )
    Page<Explain> findAllWithToOneRelationships(Pageable pageable);

    @Query("select jhiExplain from Explain jhiExplain left join fetch jhiExplain.quiz")
    List<Explain> findAllWithToOneRelationships();

    @Query("select jhiExplain from Explain jhiExplain left join fetch jhiExplain.quiz where jhiExplain.id =:id")
    Optional<Explain> findOneWithToOneRelationships(@Param("id") Long id);
}
