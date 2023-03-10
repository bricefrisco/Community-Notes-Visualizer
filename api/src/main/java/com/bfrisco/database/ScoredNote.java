package com.bfrisco.database;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import javax.persistence.*;
import java.sql.Timestamp;

@RegisterForReflection
@Entity
@Table(name = "scored_notes")
public class ScoredNote extends PanacheEntityBase {
    @Id
    @Column(name = "note_id")
    public String noteId;
    @Column(name = "core_note_intercept")
    public Double coreNoteIntercept;
    @Column(name = "core_note_factor")
    public Double coreNoteFactor;
    @Column(name = "final_rating_status")
    public String finalRatingStatus;
    @Column(name = "first_tag")
    public String firstTag;
    @Column(name = "second_tag")
    public String secondTag;
    @Column(name = "core_active_rules")
    public String coreActiveRules;
    @Column(name = "active_filter_tags")
    public String activeFilterTags;
    @Column(name = "classification")
    public String classification;
    @Column(name = "created_at")
    public Timestamp createdAt;
    @Column(name = "core_rating_status")
    public String coreRatingStatus;
    @Column(name = "meta_scorer_active_rules")
    public String metaScorerActiveRules;
    @Column(name = "decided_by")
    public String decidedBy;
    @Column(name = "expansion_note_intercept")
    public Double expansionNoteIntercept;
    @Column(name = "expansion_note_factor_1")
    public Double expansionNoteFactor1;
    @Column(name = "expansion_rating_status")
    public String expansionRatingStatus;
    @Column(name = "coverage_note_intercept")
    public Double coverageNoteIntercept;
    @Column(name = "coverage_note_factor_1")
    public Double coverageNoteFactor1;
    @Column(name = "coverage_rating_status")
    public String coverageRatingStatus;
    @JoinColumn(name = "note_id", insertable = false, updatable = false)
    @OneToOne(fetch = FetchType.EAGER)
    public Note note;
}
