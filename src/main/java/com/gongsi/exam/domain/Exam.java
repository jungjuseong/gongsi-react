package com.gongsi.exam.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gongsi.exam.domain.enumeration.ExamType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Exam.
 */
@Entity
@Table(name = "exam")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Exam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_type")
    private ExamType examType;

    @NotNull
    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "exam")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "explains", "exam" }, allowSetters = true)
    private Set<Quiz> quizzes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "exams" }, allowSetters = true)
    private Agency implementingAgency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "exams" }, allowSetters = true)
    private License license;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Exam id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Exam title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ExamType getExamType() {
        return this.examType;
    }

    public Exam examType(ExamType examType) {
        this.setExamType(examType);
        return this;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    public LocalDate getEffectiveDate() {
        return this.effectiveDate;
    }

    public Exam effectiveDate(LocalDate effectiveDate) {
        this.setEffectiveDate(effectiveDate);
        return this;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Set<Quiz> getQuizzes() {
        return this.quizzes;
    }

    public void setQuizzes(Set<Quiz> quizzes) {
        if (this.quizzes != null) {
            this.quizzes.forEach(i -> i.setExam(null));
        }
        if (quizzes != null) {
            quizzes.forEach(i -> i.setExam(this));
        }
        this.quizzes = quizzes;
    }

    public Exam quizzes(Set<Quiz> quizzes) {
        this.setQuizzes(quizzes);
        return this;
    }

    public Exam addQuiz(Quiz quiz) {
        this.quizzes.add(quiz);
        quiz.setExam(this);
        return this;
    }

    public Exam removeQuiz(Quiz quiz) {
        this.quizzes.remove(quiz);
        quiz.setExam(null);
        return this;
    }

    public Agency getImplementingAgency() {
        return this.implementingAgency;
    }

    public void setImplementingAgency(Agency agency) {
        this.implementingAgency = agency;
    }

    public Exam implementingAgency(Agency agency) {
        this.setImplementingAgency(agency);
        return this;
    }

    public License getLicense() {
        return this.license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public Exam license(License license) {
        this.setLicense(license);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exam)) {
            return false;
        }
        return id != null && id.equals(((Exam) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Exam{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", examType='" + getExamType() + "'" +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            "}";
    }
}
