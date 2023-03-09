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
        private Integer helpful;
        private Integer somewhatHelpful;
        private Integer notHelpful;
    }
}
