package com.gongsi.exam.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Quiz.
 */
@Entity
@Table(name = "quiz")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Quiz implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Size(max = 1024)
    @Column(name = "question", length = 1024, nullable = false)
    private String question;

    @Lob
    @Column(name = "example")
    private String example;

    @Size(max = 1024)
    @Column(name = "selections", length = 1024)
    private String selections;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "quiz" }, allowSetters = true)
    private Set<Explain> explains = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "quizzes", "agency", "license" }, allowSetters = true)
    private Exam exam;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Quiz id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Quiz code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQuestion() {
        return this.question;
    }

    public Quiz question(String question) {
        this.setQuestion(question);
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getExample() {
        return this.example;
    }

    public Quiz example(String example) {
        this.setExample(example);
        return this;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getSelections() {
        return this.selections;
    }

    public Quiz selections(String selections) {
        this.setSelections(selections);
        return this;
    }

    public void setSelections(String selections) {
        this.selections = selections;
    }

    public Set<Explain> getExplains() {
        return this.explains;
    }

    public void setExplains(Set<Explain> explains) {
        if (this.explains != null) {
            this.explains.forEach(i -> i.setQuiz(null));
        }
        if (explains != null) {
            explains.forEach(i -> i.setQuiz(this));
        }
        this.explains = explains;
    }

    public Quiz explains(Set<Explain> explains) {
        this.setExplains(explains);
        return this;
    }

    public Quiz addExplain(Explain explain) {
        this.explains.add(explain);
        explain.setQuiz(this);
        return this;
    }

    public Quiz removeExplain(Explain explain) {
        this.explains.remove(explain);
        explain.setQuiz(null);
        return this;
    }

    public Exam getExam() {
        return this.exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Quiz exam(Exam exam) {
        this.setExam(exam);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quiz)) {
            return false;
        }
        return id != null && id.equals(((Quiz) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quiz{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", question='" + getQuestion() + "'" +
            ", example='" + getExample() + "'" +
            ", selections='" + getSelections() + "'" +
            "}";
    }
}
