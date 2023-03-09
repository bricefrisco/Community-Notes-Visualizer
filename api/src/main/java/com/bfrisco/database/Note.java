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
    @JoinColumn(name = "participant_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public UserEnrollment createdBy;
    @OneToMany(fetch = FetchType.LAZY)
    public List<NoteStatusHistory> noteStatusHistory;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "ratingPK.noteId")
    public List<Rating> ratings;
    @JoinColumn(name = "note_id", insertable = false, updatable = false)
    @OneToOne(fetch = FetchType.EAGER)
    public ScoredNote scoredNote;
}
