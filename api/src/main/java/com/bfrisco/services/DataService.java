package com.bfrisco.services;

import com.bfrisco.database.Note;
import com.bfrisco.models.NoteResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

@ApplicationScoped
public class DataService {
    @Inject
    EntityManager em;


    public NoteResponse fetchNotes(FinalRatingStatus status, String searchQuery, int pageNum, int pageSize) {
        Query countQuery = em.createNativeQuery("""
            SELECT COUNT(*) FROM notes
            WHERE classification = 'MISINFORMED_OR_POTENTIALLY_MISLEADING' AND
            (?1 = '' OR final_rating_status = ?1) AND
            (?2 = '' OR summary_vector @@ plainto_tsquery('english', ?2))
        """);
        countQuery.setParameter(1, status == null ? "" : status.name());
        countQuery.setParameter(2, searchQuery);

        Query query = em.createNativeQuery("""
            SELECT * FROM notes
            WHERE classification = 'MISINFORMED_OR_POTENTIALLY_MISLEADING' AND
            (?1 = '' OR final_rating_status = ?1) AND
            (?2 = '' OR summary_vector @@ plainto_tsquery('english', ?2))
            ORDER BY created_at DESC
            LIMIT ?3 OFFSET ?4
        """, Note.class);
        query.setParameter(1, status == null ? "" : status.name());
        query.setParameter(2, searchQuery);
        query.setParameter(3, pageSize);
        query.setParameter(4, pageNum * pageSize);

        Long count = ((BigInteger) countQuery.getSingleResult()).longValue();
        List<Note> results = query.getResultList();

        NoteResponse response = new NoteResponse();
        response.setResults(results.stream().map(DataMapper::toNoteDto).toList());
        response.setPage(pageNum + 1);
        response.setPageSize(pageSize);
        response.setTotalResults(count);
        response.setTotalPages((int) Math.ceil((double) count / pageSize));
        return response;
    }



    public enum FinalRatingStatus {
        CURRENTLY_RATED_HELPFUL,
        NEEDS_MORE_RATINGS,
        CURRENTLY_RATED_NOT_HELPFUL
    }
}
