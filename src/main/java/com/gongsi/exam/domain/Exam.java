package com.gongsi.exam.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gongsi.exam.domain.enumeration.SubjectType;
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
    @Column(name = "subject")
    private SubjectType subject;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "exam")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "explains", "exam" }, allowSetters = true)
    private Set<Quiz> quizzes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "exams" }, allowSetters = true)
    private Agency agency;

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

    public SubjectType getSubject() {
        return this.subject;
    }

    public Exam subject(SubjectType subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(SubjectType subject) {
        this.subject = subject;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Exam date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public Agency getAgency() {
        return this.agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public Exam agency(Agency agency) {
        this.setAgency(agency);
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
            ", subject='" + getSubject() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
