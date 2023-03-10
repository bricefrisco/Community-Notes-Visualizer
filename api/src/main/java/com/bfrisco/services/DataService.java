package com.bfrisco.services;

import com.bfrisco.database.ScoredNote;
import com.bfrisco.models.NoteDTO;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class DataService {
    public List<NoteDTO> fetchNotes(FinalRatingStatus status, int page, int pageSize) {
        List<ScoredNote> notes = ScoredNote.find("classification = 'MISINFORMED_OR_POTENTIALLY_MISLEADING' AND final_rating_status = ?1", Sort.by("createdAt", Sort.Direction.Descending), status.name())
                .page(page, pageSize)
                .list();

        return DataMapper.toNoteDtos(notes);
    }

    public enum FinalRatingStatus {
        CURRENTLY_RATED_HELPFUL,
        NEEDS_MORE_RATINGS,
        CURRENTLY_RATED_NOT_HELPFUL
    }
}
