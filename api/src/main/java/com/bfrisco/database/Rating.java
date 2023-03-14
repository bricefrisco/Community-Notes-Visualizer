package com.bfrisco.database;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import java.sql.Timestamp;

@RegisterForReflection
@Entity
@Table(name = "ratings")
public class Rating extends PanacheEntityBase {
    @EmbeddedId
    public RatingPK ratingPK;
    @Column(name = "created_at")
    public Timestamp createdAt;
    @Column(name = "version")
    public Integer version;
    @Column(name = "agree")
    public Boolean agree;
    @Column(name = "disagree")
    public Boolean disagree;
    @Column(name = "helpful")
    public Boolean helpful;
    @Column(name = "not_helpful")
    public Boolean notHelpful;
    @Column(name = "helpfulness_level")
    public String helpfulnessLevel;
    @Column(name = "helpful_other")
    public Boolean helpfulOther;
    @Column(name = "helpful_informative")
    public Boolean helpfulInformative;
    @Column(name = "helpful_clear")
    public Boolean helpfulClear;
    @Column(name = "helpful_empathetic")
    @Deprecated
    public Boolean helpfulEmpathetic;
    @Column(name = "helpful_good_sources")
    public Boolean helpfulGoodSources;
    @Column(name = "helpful_unique_context")
    @Deprecated
    public Boolean helpfulUniqueContext;
    @Column(name = "helpful_addresses_claim")
    public Boolean helpfulAddressesClaim;
    @Column(name = "helpful_important_context")
    public Boolean helpfulImportantContext;
    @Column(name = "helpful_unbiased_language")
    public Boolean helpfulUnbiasedLanguage;
    @Column(name = "not_helpful_other")
    public Boolean notHelpfulOther;
    @Column(name = "not_helpful_incorrect")
    public Boolean notHelpfulIncorrect;
    @Column(name = "not_helpful_sources_missing_or_unreliable")
    public Boolean notHelpfulSourcesMissingOrUnreliable;
    @Column(name = "not_helpful_opinion_speculation_or_bias")
    public Boolean notHelpfulOpinionSpeculationOrBias;
    @Column(name = "not_helpful_missing_key_points")
    public Boolean notHelpfulMissingKeyPoints;
    @Column(name = "not_helpful_outdated")
    public Boolean notHelpfulOutdated;
    @Column(name = "not_helpful_hard_to_understand")
    public Boolean notHelpfulHardToUnderstand;
    @Column(name = "not_helpful_argumentative_or_biased")
    public Boolean notHelpfulArgumentativeOrBiased;
    @Column(name = "not_helpful_off_topic")
    public Boolean notHelpfulOffTopic;
    @Column(name = "not_helpful_spam_harassment_or_abuse")
    public Boolean notHelpfulSpamHarassmentOrAbuse;
    @Column(name = "not_helpful_irrelevant_sources")
    public Boolean notHelpfulIrrelevantSources;
    @Column(name = "not_helpful_opinion_speculation")
    public Boolean notHelpfulOpinionSpeculation;
    @Column(name = "not_helpful_note_not_needed")
    public Boolean notHelpfulNoteNotNeeded;
}
