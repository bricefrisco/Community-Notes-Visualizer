package com.bfrisco.services;

import com.bfrisco.database.Note;
import com.bfrisco.database.Rating;
import com.bfrisco.database.ScoredNote;
import com.bfrisco.models.NoteDTO;

import java.util.List;

public final class DataMapper {
    public static List<NoteDTO> toNoteDtos(List<ScoredNote> notes) {
        return notes.stream().map(DataMapper::toNoteDto).toList();
    }

    public static NoteDTO toNoteDto(ScoredNote scoredNote) {
        Note note = scoredNote.note;

        NoteDTO dto = new NoteDTO();
        dto.setNoteId(note.noteId);
        dto.setParticipantId(note.participantId);
        dto.setCreatedAt(note.createdAt);
        dto.setTweetId(note.tweetId);
        dto.setClassification(note.classification);
        dto.setBelievable(note.believable);
        dto.setHarmful(note.harmful);
        dto.setValidationDifficulty(note.validationDifficulty);
        dto.setMisleadingOther(note.misleadingOther);
        dto.setMisleadingFactualError(note.misleadingFactualError);
        dto.setMisleadingManipulatedMedia(note.misleadingManipulatedMedia);
        dto.setMisleadingOutdatedInformation(note.misleadingOutdatedInformation);
        dto.setMisleadingMissingImportantContext(note.misleadingMissingImportantContext);
        dto.setMisleadingUnverifiedClaimAsFact(note.misleadingUnverifiedClaimAsFact);
        dto.setMisleadingSatire(note.misleadingSatire);
        dto.setNotMisleadingOther(note.notMisleadingOther);
        dto.setNotMisleadingFactuallyCorrect(note.notMisleadingFactuallyCorrect);
        dto.setNotMisleadingOutdatedButNotWhenWritten(note.notMisleadingOutdatedButNotWhenWritten);
        dto.setNotMisleadingClearlySatire(note.notMisleadingClearlySatire);
        dto.setNotMisleadingPersonalOpinion(note.notMisleadingPersonalOpinion);
        dto.setTrustworthySources(note.trustworthySources);
        dto.setSummary(note.summary);

        NoteDTO.Score score = new NoteDTO.Score();
        score.setCoreNoteIntercept(scoredNote.coreNoteIntercept);
        score.setCoreNoteSlope(scoredNote.coreNoteFactor);
        score.setFinalRatingStatus(scoredNote.finalRatingStatus);
        score.setFirstTag(scoredNote.firstTag);
        score.setSecondTag(scoredNote.secondTag);
        score.setCoreActiveRules(scoredNote.coreActiveRules);
        score.setActiveFilterTags(scoredNote.activeFilterTags);
        score.setClassification(scoredNote.classification);
        score.setCreatedAt(scoredNote.createdAt);
        score.setCoreRatingStatus(scoredNote.coreRatingStatus);
        score.setMetaScorerActiveRules(scoredNote.metaScorerActiveRules);
        score.setDecidedBy(scoredNote.decidedBy);
        score.setExpansionNoteIntercept(scoredNote.expansionNoteIntercept);
        score.setExpansionNoteFactor1(scoredNote.expansionNoteFactor1);
        score.setExpansionRatingStatus(scoredNote.expansionRatingStatus);
        score.setCoverageNoteIntercept(scoredNote.coverageNoteIntercept);
        score.setCoverageNoteFactor1(scoredNote.coverageNoteFactor1);
        score.setCoverageRatingStatus(scoredNote.coverageRatingStatus);
        dto.setScore(score);

        int helpful = 0;
        int somewhatHelpful = 0;
        int notHelpful = 0;
        for (Rating rating : note.ratings) {
            if ("helpful".equalsIgnoreCase(rating.helpfulnessLevel)) {
                helpful++;
            }

            if ("somewhat_helpful".equalsIgnoreCase(rating.helpfulnessLevel)) {
                somewhatHelpful++;
            }

            if ("not_helpful".equalsIgnoreCase(rating.helpfulnessLevel)) {
                notHelpful++;
            }
        }

        NoteDTO.Ratings ratings = new NoteDTO.Ratings();
        ratings.setHelpful(helpful);
        ratings.setSomewhatHelpful(somewhatHelpful);
        ratings.setNotHelpful(notHelpful);
        dto.setRatings(ratings);

        return dto;
    }
}
