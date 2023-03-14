package com.bfrisco.services;

import com.bfrisco.database.Note;
import com.bfrisco.database.Rating;
import com.bfrisco.models.NoteDTO;

import java.util.List;

public final class DataMapper {
    public static List<NoteDTO> toNoteDtos(List<Note> notes) {
        return notes.stream().map(DataMapper::toNoteDto).toList();
    }

    public static NoteDTO toNoteDto(Note note) {
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
        score.setCoreNoteIntercept(note.coreNoteIntercept);
        score.setCoreNoteSlope(note.coreNoteFactor);
        score.setFinalRatingStatus(note.finalRatingStatus);
        score.setFirstTag(note.firstTag);
        score.setSecondTag(note.secondTag);
        score.setCoreActiveRules(note.coreActiveRules);
        score.setActiveFilterTags(note.activeFilterTags);
        score.setClassification(note.classification);
        score.setCreatedAt(note.createdAt);
        score.setCoreRatingStatus(note.coreRatingStatus);
        score.setMetaScorerActiveRules(note.metaScorerActiveRules);
        score.setDecidedBy(note.decidedBy);
        score.setExpansionNoteIntercept(note.expansionNoteIntercept);
        score.setExpansionNoteFactor1(note.expansionNoteFactor1);
        score.setExpansionRatingStatus(note.expansionRatingStatus);
        score.setCoverageNoteIntercept(note.coverageNoteIntercept);
        score.setCoverageNoteFactor1(note.coverageNoteFactor1);
        score.setCoverageRatingStatus(note.coverageRatingStatus);
        dto.setScore(score);

        int helpful = 0;
        int somewhatHelpful = 0;
        int notHelpful = 0;

        int informative = 0;
        int clear = 0;
        int empathetic = 0;
        int goodSources = 0;
        int uniqueContext = 0;
        int addressesClaim = 0;
        int importantContext = 0;
        int unbiasedLanguage = 0;

        int incorrect = 0;
        int sourcesMissingOrUnreliable = 0;
        int opinionSpeculationOrBias = 0;
        int missingKeyPoints = 0;
        int outdated = 0;
        int hardToUnderstand = 0;
        int argumentativeOrBiased = 0;
        int offTopic = 0;
        int spamHarassmentOrAbuse = 0;
        int irrelevantSources = 0;
        int opinionSpeculation = 0;
        int noteNotNeeded = 0;

        for (Rating rating : note.ratings) {
            switch (rating.helpfulnessLevel) {
                case "HELPFUL" -> helpful++;
                case "SOMEWHAT_HELPFUL" -> somewhatHelpful++;
                case "NOT_HELPFUL" -> notHelpful++;
            }

            if (rating.helpfulInformative) informative++;
            if (rating.helpfulClear) clear++;
            if (rating.helpfulEmpathetic) empathetic++;
            if (rating.helpfulGoodSources) goodSources++;
            if (rating.helpfulUniqueContext) uniqueContext++;
            if (rating.helpfulAddressesClaim) addressesClaim++;
            if (rating.helpfulImportantContext) importantContext++;
            if (rating.helpfulUnbiasedLanguage) unbiasedLanguage++;

            if (rating.notHelpfulIncorrect) incorrect++;
            if (rating.notHelpfulSourcesMissingOrUnreliable) sourcesMissingOrUnreliable++;
            if (rating.notHelpfulOpinionSpeculationOrBias) opinionSpeculationOrBias++;
            if (rating.notHelpfulMissingKeyPoints) missingKeyPoints++;
            if (rating.notHelpfulOutdated) outdated++;
            if (rating.notHelpfulHardToUnderstand) hardToUnderstand++;
            if (rating.notHelpfulArgumentativeOrBiased) argumentativeOrBiased++;
            if (rating.notHelpfulOffTopic) offTopic++;
            if (rating.notHelpfulSpamHarassmentOrAbuse) spamHarassmentOrAbuse++;
            if (rating.notHelpfulIrrelevantSources) irrelevantSources++;
            if (rating.notHelpfulOpinionSpeculation) opinionSpeculation++;
            if (rating.notHelpfulNoteNotNeeded) noteNotNeeded++;
        }

        NoteDTO.Ratings ratings = new NoteDTO.Ratings();
        ratings.getHelpful().setCount(helpful);
        ratings.getHelpful().setInformative(informative);
        ratings.getHelpful().setClear(clear);
        ratings.getHelpful().setEmpathetic(empathetic);
        ratings.getHelpful().setGoodSources(goodSources);
        ratings.getHelpful().setUniqueContext(uniqueContext);
        ratings.getHelpful().setAddressesClaim(addressesClaim);
        ratings.getHelpful().setImportantContext(importantContext);
        ratings.getHelpful().setUnbiasedLanguage(unbiasedLanguage);

        ratings.getSomewhatHelpful().setCount(somewhatHelpful);

        ratings.getNotHelpful().setCount(notHelpful);
        ratings.getNotHelpful().setIncorrect(incorrect);
        ratings.getNotHelpful().setSourcesMissingOrUnreliable(sourcesMissingOrUnreliable);
        ratings.getNotHelpful().setOpinionSpeculationOrBias(opinionSpeculationOrBias);
        ratings.getNotHelpful().setMissingKeyPoints(missingKeyPoints);
        ratings.getNotHelpful().setOutdated(outdated);
        ratings.getNotHelpful().setHardToUnderstand(hardToUnderstand);
        ratings.getNotHelpful().setArgumentativeOrBiased(argumentativeOrBiased);
        ratings.getNotHelpful().setOffTopic(offTopic);
        ratings.getNotHelpful().setSpamHarassmentOrAbuse(spamHarassmentOrAbuse);
        ratings.getNotHelpful().setIrrelevantSources(irrelevantSources);
        ratings.getNotHelpful().setOpinionSpeculation(opinionSpeculation);
        ratings.getNotHelpful().setNoteNotNeeded(noteNotNeeded);

        dto.setRatings(ratings);

        return dto;
    }
}
