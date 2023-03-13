package com.bfrisco.models;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

import java.sql.Timestamp;

@Data
@RegisterForReflection
public class NoteDTO {
    private String noteId;
    private String participantId;
    private Timestamp createdAt;
    private String tweetId;
    private String classification;
    private String believable;
    private String harmful;
    private String validationDifficulty;
    private Boolean misleadingOther;
    private Boolean misleadingFactualError;
    private Boolean misleadingManipulatedMedia;
    private Boolean misleadingOutdatedInformation;
    private Boolean misleadingMissingImportantContext;
    private Boolean misleadingUnverifiedClaimAsFact;
    private Boolean misleadingSatire;
    private Boolean notMisleadingOther;
    private Boolean notMisleadingFactuallyCorrect;
    private Boolean notMisleadingOutdatedButNotWhenWritten;
    private Boolean notMisleadingClearlySatire;
    private Boolean notMisleadingPersonalOpinion;
    private Boolean trustworthySources;
    private String summary;
    private Score score;
    private Ratings ratings;

    @Data
    @RegisterForReflection
    public static class Score {
        private Double coreNoteIntercept;
        private Double coreNoteSlope;
        private String finalRatingStatus;
        private String firstTag;
        private String secondTag;
        private String coreActiveRules;
        private String activeFilterTags;
        private String classification;
        private Timestamp createdAt;
        private String coreRatingStatus;
        private String metaScorerActiveRules;
        private String decidedBy;
        private Double expansionNoteIntercept;
        private Double expansionNoteFactor1;
        private String expansionRatingStatus;
        private Double coverageNoteIntercept;
        private Double coverageNoteFactor1;
        private String coverageRatingStatus;
    }

    @Data
    @RegisterForReflection
    public static class Ratings {
        private Helpful helpful = new Helpful();
        private SomewhatHelpful somewhatHelpful = new SomewhatHelpful();
        private NotHelpful notHelpful = new NotHelpful();
    }

    @Data
    @RegisterForReflection
    public static class Helpful {
        private Integer count;
        private Integer informative;
        private Integer clear;
        private Integer empathetic;
        private Integer goodSources;
        private Integer uniqueContext;
        private Integer addressesClaim;
        private Integer importantContext;
        private Integer unbiasedLanguage;
    }

    @Data
    public static class SomewhatHelpful {
        private Integer count;
    }

    @Data
    @RegisterForReflection
    public static class NotHelpful {
        private Integer count;
        private Integer incorrect;
        private Integer sourcesMissingOrUnreliable;
        private Integer opinionSpeculationOrBias;
        private Integer missingKeyPoints;
        private Integer outdated;
        private Integer hardToUnderstand;
        private Integer argumentativeOrBiased;
        private Integer offTopic;
        private Integer spamHarassmentOrAbuse;
        private Integer irrelevantSources;
        private Integer opinionSpeculation;
        private Integer noteNotNeeded;
    }
}
