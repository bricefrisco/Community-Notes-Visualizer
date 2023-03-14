package com.bfrisco.database;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@RegisterForReflection
@Entity
@Table(name = "notes")
public class Note extends PanacheEntityBase {
    @Id
    @Column(name = "note_id")
    public String noteId;
    @Column(name = "participant_id")
    public String participantId;
    @Column(name = "created_at")
    public Timestamp createdAt;
    @Column(name = "tweet_id")
    public String tweetId;
    @Column(name = "classification")
    public String classification;
    @Deprecated
    @Column(name = "believable")
    public String believable;
    @Deprecated
    @Column(name = "harmful")
    public String harmful;
    @Column(name = "validation_difficulty")
    public String validationDifficulty;
    @Column(name = "misleading_other")
    public Boolean misleadingOther;
    @Column(name = "misleading_factual_error")
    public Boolean misleadingFactualError;
    @Column(name = "misleading_manipulated_media")
    public Boolean misleadingManipulatedMedia;
    @Column(name = "misleading_outdated_information")
    public Boolean misleadingOutdatedInformation;
    @Column(name = "misleading_missing_important_context")
    public Boolean misleadingMissingImportantContext;
    @Column(name = "misleading_unverified_claim_as_fact")
    public Boolean misleadingUnverifiedClaimAsFact;
    @Column(name = "misleading_satire")
    public Boolean misleadingSatire;
    @Column(name = "not_misleading_other")
    public Boolean notMisleadingOther;
    @Column(name = "not_misleading_factually_correct")
    public Boolean notMisleadingFactuallyCorrect;
    @Column(name = "not_misleading_outdated_but_not_when_written")
    public Boolean notMisleadingOutdatedButNotWhenWritten;
    @Column(name = "not_misleading_clearly_satire")
    public Boolean notMisleadingClearlySatire;
    @Column(name = "not_misleading_personal_opinion")
    public Boolean notMisleadingPersonalOpinion;
    @Column(name = "trustworthy_sources")
    public Boolean trustworthySources;
    @Column(name = "summary")
    public String summary;
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
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "ratingPK.noteId")
    public List<Rating> ratings;
}
