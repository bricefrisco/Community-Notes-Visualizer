package com.bfrisco.services;

import com.bfrisco.database.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.DoubleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DataImporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataImporter.class);

    private static final String CREATE_USER_ENROLLMENT = """
            CREATE TABLE user_enrollment (
                participant_id TEXT,
                enrollment_state TEXT,
                successful_rating_needed_to_earn_in INTEGER,
                timestamp_of_last_state_change TIMESTAMP
            )
            """;

    private static final String CREATE_NOTES = """
            CREATE TABLE notes (
                note_id TEXT PRIMARY KEY,
                participant_id TEXT,
                created_at TIMESTAMP,
                tweet_id TEXT,
                classification TEXT,
                believable TEXT,
                harmful TEXT,
                validation_difficulty TEXT,
                misleading_other BOOLEAN,
                misleading_factual_error BOOLEAN,
                misleading_manipulated_media BOOLEAN,
                misleading_outdated_information BOOLEAN,
                misleading_missing_important_context BOOLEAN,
                misleading_unverified_claim_as_fact BOOLEAN,
                misleading_satire BOOLEAN,
                not_misleading_other BOOLEAN,
                not_misleading_factually_correct BOOLEAN,
                not_misleading_outdated_but_not_when_written BOOLEAN,
                not_misleading_clearly_satire BOOLEAN,
                not_misleading_personal_opinion BOOLEAN,
                trustworthy_sources BOOLEAN,
                core_note_intercept DOUBLE PRECISION,
                core_note_factor DOUBLE PRECISION,
                final_rating_status TEXT,
                first_tag TEXT,
                second_tag TEXT,
                core_active_rules TEXT,
                active_filter_tags TEXT,
                core_rating_status TEXT,
                meta_scorer_active_rules TEXT,
                decided_by TEXT,
                expansion_note_intercept DOUBLE PRECISION,
                expansion_note_factor_1 DOUBLE PRECISION,
                expansion_rating_status TEXT,
                coverage_note_intercept DOUBLE PRECISION,
                coverage_note_factor_1 DOUBLE PRECISION,
                coverage_rating_status TEXT,
                summary TEXT,
                summary_vector TSVECTOR
            )
            """;

    private static final String CREATE_NOTE_STATUS_HISTORY = """
            CREATE TABLE note_status_history (
                id SERIAL PRIMARY KEY,
                note_id TEXT,
                participant_id TEXT,
                created_at TIMESTAMP,
                timestamp_first_non_nmr_status TIMESTAMP,
                first_non_nmr_status TEXT,
                timestamp_of_current_status TIMESTAMP,
                current_status TEXT,
                timestamp_latest_non_nmr_status TIMESTAMP,
                latest_non_nmr_status TEXT
            )
            """;

    private static final String CREATE_RATINGS = """
            CREATE TABLE ratings (
                note_id TEXT,
                participant_id TEXT,
                created_at TIMESTAMP,
                version INTEGER,
                agree BOOLEAN,
                disagree BOOLEAN,
                helpful BOOLEAN,
                not_helpful BOOLEAN,
                helpfulness_level TEXT,
                helpful_other BOOLEAN,
                helpful_informative BOOLEAN,
                helpful_clear BOOLEAN,
                helpful_empathetic BOOLEAN,
                helpful_good_sources BOOLEAN,
                helpful_unique_context BOOLEAN,
                helpful_addresses_claim BOOLEAN,
                helpful_important_context BOOLEAN,
                helpful_unbiased_language BOOLEAN,
                not_helpful_other BOOLEAN,
                not_helpful_incorrect BOOLEAN,
                not_helpful_sources_missing_or_unreliable BOOLEAN,
                not_helpful_opinion_speculation_or_bias BOOLEAN,
                not_helpful_missing_key_points BOOLEAN,
                not_helpful_outdated BOOLEAN,
                not_helpful_hard_to_understand BOOLEAN,
                not_helpful_argumentative_or_biased BOOLEAN,
                not_helpful_off_topic BOOLEAN,
                not_helpful_spam_harassment_or_abuse BOOLEAN,
                not_helpful_irrelevant_sources BOOLEAN,
                not_helpful_opinion_speculation BOOLEAN,
                not_helpful_note_not_needed BOOLEAN
            )
            """;

    @Inject
    SessionFactory sessionFactory;

    public void run() throws IOException {
        LOGGER.info("Starting data import...");

        // destroy();
        // create();
        // importUserEnrollment();
        // importNotes();
        // importNoteStatusHistory();
        // importRatings();
        // importScoredNotes();
        cleanData();
        createConstraints();
        createFullTextSearch();
        createIndexes();

        LOGGER.info("Data import complete.");
    }

    private void importUserEnrollment() throws IOException {
        try (Session session = sessionFactory.openSession();
             BufferedReader br = new BufferedReader(new FileReader(
                     "C:\\Users\\Brice\\Desktop\\community-notes-visualizer\\data\\userEnrollment-00000.tsv", StandardCharsets.UTF_8))) {
            Transaction transaction = session.beginTransaction();
            int count = 0;

            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] columns = line.split("\t");

                UserEnrollment user = new UserEnrollment();
                user.participantId = columns[0];
                user.enrollmentState = columns[1];
                user.successfulRatingNeededToEarnIn = toInteger(columns[2]);
                user.timestampOfLastStateChange = toTimestamp(columns[3]);

                session.save(user);

                count++;
                if (count % 10000 == 0) {
                    session.flush();
                    session.clear();
                    LOGGER.info("Imported {} user records", count);
                }
            }

            transaction.commit();
            LOGGER.info("Imported {} user records", count);
        }
    }

    private NativeQuery<Note> buildNotesQuery(List<Note> notes, Session session) {
        StringBuilder query = new StringBuilder("""
                INSERT INTO notes (
                note_id,
                participant_id,
                created_at,
                tweet_id,
                classification,
                believable,
                harmful,
                validation_difficulty,
                misleading_other,
                misleading_factual_error,
                misleading_manipulated_media,
                misleading_outdated_information,
                misleading_missing_important_context,
                misleading_unverified_claim_as_fact,
                misleading_satire,
                not_misleading_other,
                not_misleading_factually_correct,
                not_misleading_outdated_but_not_when_written,
                not_misleading_clearly_satire,
                not_misleading_personal_opinion,
                trustworthy_sources,
                summary
                ) VALUES
                """);

        for (int i = 0; i < notes.size(); i++) {
            query.append("(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            if (i < notes.size() - 1) {
                query.append(",");
            }
        }

        query.append("""
                ON CONFLICT (note_id) DO UPDATE SET
                participant_id = EXCLUDED.participant_id,
                created_at = EXCLUDED.created_at,
                tweet_id = EXCLUDED.tweet_id,
                classification = EXCLUDED.classification,
                believable = EXCLUDED.believable,
                harmful = EXCLUDED.harmful,
                validation_difficulty = EXCLUDED.validation_difficulty,
                misleading_other = EXCLUDED.misleading_other,
                misleading_factual_error = EXCLUDED.misleading_factual_error,
                misleading_manipulated_media = EXCLUDED.misleading_manipulated_media,
                misleading_outdated_information = EXCLUDED.misleading_outdated_information,
                misleading_missing_important_context = EXCLUDED.misleading_missing_important_context,
                misleading_unverified_claim_as_fact = EXCLUDED.misleading_unverified_claim_as_fact,
                misleading_satire = EXCLUDED.misleading_satire,
                not_misleading_other = EXCLUDED.not_misleading_other,
                not_misleading_factually_correct = EXCLUDED.not_misleading_factually_correct,
                not_misleading_outdated_but_not_when_written = EXCLUDED.not_misleading_outdated_but_not_when_written,
                not_misleading_clearly_satire = EXCLUDED.not_misleading_clearly_satire,
                not_misleading_personal_opinion = EXCLUDED.not_misleading_personal_opinion,
                trustworthy_sources = EXCLUDED.trustworthy_sources,
                summary = EXCLUDED.summary
                """);

        int NUM_COLS = 22;
        NativeQuery<Note> result = session.createNativeQuery(query.toString(), Note.class);
        for (int i = 0; i < notes.size(); i++) {
            Note n = notes.get(i);
            result.setParameter(i * NUM_COLS + 1, n.noteId);
            result.setParameter(i * NUM_COLS + 2, n.participantId);
            result.setParameter(i * NUM_COLS + 3, n.createdAt);
            result.setParameter(i * NUM_COLS + 4, n.tweetId);
            result.setParameter(i * NUM_COLS + 5, n.classification);
            result.setParameter(i * NUM_COLS + 6, n.believable);
            result.setParameter(i * NUM_COLS + 7, n.harmful);
            result.setParameter(i * NUM_COLS + 8, n.validationDifficulty);
            result.setParameter(i * NUM_COLS + 9, n.misleadingOther);
            result.setParameter(i * NUM_COLS + 10, n.misleadingFactualError);
            result.setParameter(i * NUM_COLS + 11, n.misleadingManipulatedMedia);
            result.setParameter(i * NUM_COLS + 12, n.misleadingOutdatedInformation);
            result.setParameter(i * NUM_COLS + 13, n.misleadingMissingImportantContext);
            result.setParameter(i * NUM_COLS + 14, n.misleadingUnverifiedClaimAsFact);
            result.setParameter(i * NUM_COLS + 15, n.misleadingSatire);
            result.setParameter(i * NUM_COLS + 16, n.notMisleadingOther);
            result.setParameter(i * NUM_COLS + 17, n.notMisleadingFactuallyCorrect);
            result.setParameter(i * NUM_COLS + 18, n.notMisleadingOutdatedButNotWhenWritten);
            result.setParameter(i * NUM_COLS + 19, n.notMisleadingClearlySatire);
            result.setParameter(i * NUM_COLS + 20, n.notMisleadingPersonalOpinion);
            result.setParameter(i * NUM_COLS + 21, n.trustworthySources);
            result.setParameter(i * NUM_COLS + 22, n.summary);
        }

        return result;
    }

    private void importNotes() throws IOException {
        try (Session session = sessionFactory.openSession();
             BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Brice\\Desktop\\community-notes-visualizer\\data\\notes-00000.tsv", StandardCharsets.UTF_8))) {
            Transaction transaction = session.beginTransaction();
            int count = 0;

            String line = br.readLine(); // Skip header
            List<Note> batch = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                String[] columns = line.split("\t");

                Note note = new Note();
                note.noteId = columns[0];
                note.participantId = columns[1];
                note.createdAt = toTimestamp(columns[2]);
                note.tweetId = columns[3];
                note.classification = columns[4];
                note.believable = columns[5];
                note.harmful = columns[6];
                note.validationDifficulty = columns[7];
                note.misleadingOther = toBoolean(columns[8]);
                note.misleadingFactualError = toBoolean(columns[9]);
                note.misleadingManipulatedMedia = toBoolean(columns[10]);
                note.misleadingOutdatedInformation = toBoolean(columns[11]);
                note.misleadingMissingImportantContext = toBoolean(columns[12]);
                note.misleadingUnverifiedClaimAsFact = toBoolean(columns[13]);
                note.misleadingSatire = toBoolean(columns[14]);
                note.notMisleadingOther = toBoolean(columns[15]);
                note.notMisleadingFactuallyCorrect = toBoolean(columns[16]);
                note.notMisleadingOutdatedButNotWhenWritten = toBoolean(columns[17]);
                note.notMisleadingClearlySatire = toBoolean(columns[18]);
                note.notMisleadingPersonalOpinion = toBoolean(columns[19]);
                note.trustworthySources = toBoolean(columns[20]);
                note.summary = columns[21];

                batch.add(note);
                count++;

                if (batch.size() == 1000) {
                    buildNotesQuery(batch, session).executeUpdate();
                    session.flush();
                    batch.clear();
                    session.clear();
                    LOGGER.info("Inserted/updated {} note records", count);
                }
            }

            buildNotesQuery(batch, session).executeUpdate();
            transaction.commit();
            LOGGER.info("Inserted/updated {} note records", count);
        }
    }

    private void importNoteStatusHistory() throws IOException {
        try (Session session = sessionFactory.openSession();
             BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Brice\\Desktop\\community-notes-visualizer\\data\\noteStatusHistory-00000.tsv", StandardCharsets.UTF_8))) {
            Transaction transaction = session.beginTransaction();
            int count = 0;

            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] columns = line.split("\t");

                NoteStatusHistory noteStatusHistory = new NoteStatusHistory();
                noteStatusHistory.noteId = columns[0];
                // noteAuthorParticipantId = columns[1]
                noteStatusHistory.createdAt = toTimestamp(columns[2]);
                noteStatusHistory.timestampFirstNonNmrStatus = toTimestamp(columns[3]);
                noteStatusHistory.firstNonNmrStatus = columns[4];
                noteStatusHistory.timestampOfCurrentStatus = toTimestamp(columns[5]);
                noteStatusHistory.currentStatus = columns[6];
                noteStatusHistory.timestampLatestNonNmrStatus = toTimestamp(columns[7]);
                noteStatusHistory.latestNonNmrStatus = columns[8];

                session.save(noteStatusHistory);

                count++;
                if (count % 10000 == 0) {
                    session.flush();
                    session.clear();
                    LOGGER.info("Imported {} note status history records", count);
                }
            }

            transaction.commit();
            LOGGER.info("Imported {} note status history records", count);
        }
    }

    private void importRatings() throws IOException {
        try (Session session = sessionFactory.openSession();
             BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Brice\\Desktop\\community-notes-visualizer\\data\\ratings-00000.tsv", StandardCharsets.UTF_8))) {
            Transaction transaction = session.beginTransaction();
            int count = 0;

            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] columns = line.split("\t");

                Rating rating = new Rating();
                rating.ratingPK = new RatingPK(columns[0], columns[1]);
                rating.createdAt = toTimestamp(columns[2]);
                rating.version = toInteger(columns[3]);
                rating.agree = toBoolean(columns[4]);
                rating.disagree = toBoolean(columns[5]);
                rating.helpful = toBoolean(columns[6]);
                rating.notHelpful = toBoolean(columns[7]);
                rating.helpfulnessLevel = columns[8];
                rating.helpfulOther = toBoolean(columns[9]);
                rating.helpfulInformative = toBoolean(columns[10]);
                rating.helpfulClear = toBoolean(columns[11]);
                rating.helpfulEmpathetic = toBoolean(columns[12]);
                rating.helpfulGoodSources = toBoolean(columns[13]);
                rating.helpfulUniqueContext = toBoolean(columns[14]);
                rating.helpfulAddressesClaim = toBoolean(columns[15]);
                rating.helpfulImportantContext = toBoolean(columns[16]);
                rating.helpfulUnbiasedLanguage = toBoolean(columns[17]);
                rating.notHelpfulOther = toBoolean(columns[18]);
                rating.notHelpfulIncorrect = toBoolean(columns[19]);
                rating.notHelpfulSourcesMissingOrUnreliable = toBoolean(columns[20]);
                rating.notHelpfulOpinionSpeculationOrBias = toBoolean(columns[21]);
                rating.notHelpfulMissingKeyPoints = toBoolean(columns[22]);
                rating.notHelpfulOutdated = toBoolean(columns[23]);
                rating.notHelpfulHardToUnderstand = toBoolean(columns[24]);
                rating.notHelpfulArgumentativeOrBiased = toBoolean(columns[25]);
                rating.notHelpfulOffTopic = toBoolean(columns[26]);
                rating.notHelpfulSpamHarassmentOrAbuse = toBoolean(columns[27]);
                rating.notHelpfulIrrelevantSources = toBoolean(columns[28]);
                rating.notHelpfulOpinionSpeculation = toBoolean(columns[29]);
                rating.notHelpfulNoteNotNeeded = toBoolean(columns[30]);

                session.save(rating);

                count++;
                if (count % 10000 == 0) {
                    session.flush();
                    session.clear();
                    LOGGER.info("Imported {} ratings", count);
                }
            }

            transaction.commit();
            LOGGER.info("Imported {} ratings", count);
        }
    }

    private NativeQuery<Note> buildNoteScoreQuery(List<Note> notes, Session session) {
        StringBuilder query = new StringBuilder("""
                INSERT INTO notes (
                note_id,
                core_note_intercept,
                core_note_factor,
                final_rating_status,
                first_tag,
                second_tag,
                core_active_rules,
                active_filter_tags,
                classification,
                created_at,
                core_rating_status,
                meta_scorer_active_rules,
                decided_by,
                expansion_note_intercept,
                expansion_note_factor_1,
                expansion_rating_status,
                coverage_note_intercept,
                coverage_note_factor_1,
                coverage_rating_status
                ) VALUES
                """);

        for (int i = 0; i < notes.size(); i++) {
            query.append("(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            if (i < notes.size() - 1) {
                query.append(",");
            }
        }

        query.append("""
                ON CONFLICT (note_id) DO UPDATE SET
                core_note_intercept = excluded.core_note_intercept,
                core_note_factor = excluded.core_note_factor,
                final_rating_status = excluded.final_rating_status,
                first_tag = excluded.first_tag,
                second_tag = excluded.second_tag,
                core_active_rules = excluded.core_active_rules,
                active_filter_tags = excluded.active_filter_tags,
                classification = excluded.classification,
                created_at = excluded.created_at,
                core_rating_status = excluded.core_rating_status,
                meta_scorer_active_rules = excluded.meta_scorer_active_rules,
                decided_by = excluded.decided_by,
                expansion_note_intercept = excluded.expansion_note_intercept,
                expansion_note_factor_1 = excluded.expansion_note_factor_1,
                expansion_rating_status = excluded.expansion_rating_status,
                coverage_note_intercept = excluded.coverage_note_intercept,
                coverage_note_factor_1 = excluded.coverage_note_factor_1,
                coverage_rating_status = excluded.coverage_rating_status
                """);

        int NUM_COLS = 19;
        NativeQuery<Note> result = session.createNativeQuery(query.toString(), Note.class);
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            result.setParameter(i * NUM_COLS + 1, note.noteId);
            result.setParameter(i * NUM_COLS + 2, note.coreNoteIntercept, DoubleType.INSTANCE);
            result.setParameter(i * NUM_COLS + 3, note.coreNoteFactor, DoubleType.INSTANCE);
            result.setParameter(i * NUM_COLS + 4, note.finalRatingStatus);
            result.setParameter(i * NUM_COLS + 5, note.firstTag);
            result.setParameter(i * NUM_COLS + 6, note.secondTag);
            result.setParameter(i * NUM_COLS + 7, note.coreActiveRules);
            result.setParameter(i * NUM_COLS + 8, note.activeFilterTags);
            result.setParameter(i * NUM_COLS + 9, note.classification);
            result.setParameter(i * NUM_COLS + 10, note.createdAt);
            result.setParameter(i * NUM_COLS + 11, note.coreRatingStatus);
            result.setParameter(i * NUM_COLS + 12, note.metaScorerActiveRules);
            result.setParameter(i * NUM_COLS + 13, note.decidedBy);
            result.setParameter(i * NUM_COLS + 14, note.expansionNoteIntercept, DoubleType.INSTANCE);
            result.setParameter(i * NUM_COLS + 15, note.expansionNoteFactor1, DoubleType.INSTANCE);
            result.setParameter(i * NUM_COLS + 16, note.expansionRatingStatus);
            result.setParameter(i * NUM_COLS + 17, note.coverageNoteIntercept, DoubleType.INSTANCE);
            result.setParameter(i * NUM_COLS + 18, note.coverageNoteFactor1, DoubleType.INSTANCE);
            result.setParameter(i * NUM_COLS + 19, note.coverageRatingStatus);
        }

        return result;
    }

    private void importScoredNotes() throws IOException {
        try (Session session = sessionFactory.openSession();
             BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Brice\\Desktop\\community-notes-visualizer\\data\\scored_notes.tsv", StandardCharsets.UTF_8))) {
            Transaction transaction = session.beginTransaction();

            int count = 0;
            String line = br.readLine(); // Skip header
            List<Note> batch = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] columns = line.split("\t");

                Note note = new Note();
                note.noteId = columns[0];
                note.coreNoteIntercept = toDouble(columns[1]);
                note.coreNoteFactor = toDouble(columns[2]);
                note.finalRatingStatus = columns[3];
                note.firstTag = columns[4];
                note.secondTag = columns[5];
                note.coreActiveRules = columns[6];
                note.activeFilterTags = columns[7];
                note.classification = columns[8];
                note.createdAt = toTimestamp(columns[9]);
                note.coreRatingStatus = columns[10];
                note.metaScorerActiveRules = columns[11];
                note.decidedBy = columns[12];
                note.expansionNoteIntercept = toDouble(columns[13]);
                note.expansionNoteFactor1 = toDouble(columns[14]);
                note.expansionRatingStatus = columns[15];
                note.coverageNoteIntercept = toDouble(columns[16]);
                note.coverageNoteFactor1 = toDouble(columns[17]);
                note.coverageRatingStatus = columns[18];

                batch.add(note);
                count++;

                if (batch.size() == 1000) {
                    buildNoteScoreQuery(batch, session).executeUpdate();
                    session.flush();
                    batch.clear();
                    session.clear();
                    LOGGER.info("Inserted/updated {} notes (scores)", count);
                }
            }

            buildNoteScoreQuery(batch, session).executeUpdate();
            transaction.commit();
            LOGGER.info("Inserted/updated {} notes (scores)", count);
        }
    }

    private void cleanData() {
        try (Session session = sessionFactory.openSession()) {
            LOGGER.info("Cleaning data...");
            Transaction transaction = session.beginTransaction();
            LOGGER.info("Cleared {} records from notes with no corresponding tweet_id",
                    session.createNativeQuery("DELETE FROM notes WHERE tweet_id IS NULL")
                            .executeUpdate());
            LOGGER.info("Cleared {} records from note_status_history with no corresponding note_id",
                    session.createNativeQuery("DELETE FROM note_status_history WHERE note_id NOT IN (SELECT note_id FROM notes)")
                            .executeUpdate());
            LOGGER.info("Cleared {} records from ratings with no corresponding note_id",
                    session.createNativeQuery("DELETE FROM ratings WHERE note_id NOT IN (SELECT note_id FROM notes)")
                            .executeUpdate());
            // TODO: Make sure we want to do this (only 23 records last checked)
            LOGGER.info("Cleared {} records from ratings with no corresponding participant_id",
                    session.createNativeQuery("DELETE FROM ratings WHERE participant_id NOT IN (SELECT participant_id FROM user_enrollment)")
                            .executeUpdate());
            transaction.commit();
            LOGGER.info("Data cleaned.");
        }
    }

    private void createConstraints() {
        try (Session session = sessionFactory.openSession()) {
            LOGGER.info("Creating constraints...");
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("ALTER TABLE user_enrollment ADD PRIMARY KEY (participant_id)").executeUpdate();
            session.createNativeQuery("ALTER TABLE notes ADD CONSTRAINT notes_participant_id_fk FOREIGN KEY (participant_id) REFERENCES user_enrollment (participant_id)").executeUpdate();
            session.createNativeQuery("ALTER TABLE note_status_history ADD CONSTRAINT note_status_history_note_id_fk FOREIGN KEY (note_id) REFERENCES notes (note_id)").executeUpdate();
            session.createNativeQuery("ALTER TABLE note_status_history ADD CONSTRAINT note_status_history_participant_id_fk FOREIGN KEY (participant_id) REFERENCES user_enrollment (participant_id)").executeUpdate();
            session.createNativeQuery("ALTER TABLE ratings ADD PRIMARY KEY (note_id, participant_id)").executeUpdate();
            session.createNativeQuery("ALTER TABLE ratings ADD CONSTRAINT ratings_note_id_fk FOREIGN KEY (note_id) REFERENCES notes(note_id)").executeUpdate();
            session.createNativeQuery("ALTER TABLE ratings ADD CONSTRAINT ratings_participant_id_fk FOREIGN KEY (participant_id) REFERENCES user_enrollment(participant_id)").executeUpdate();
            transaction.commit();
            LOGGER.info("Constraints created.");
        }
    }

    private void createFullTextSearch() {
        try (Session session = sessionFactory.openSession()) {
            LOGGER.info("Creating full text search field...");
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("UPDATE notes SET summary_vector = to_tsvector('english', summary)").executeUpdate();
            transaction.commit();
            LOGGER.info("Full text search field created.");
        }
    }

    private void createIndexes() {
        try (Session session = sessionFactory.openSession()) {
            LOGGER.info("Indexing data...");
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("CREATE INDEX ON notes(participant_id)").executeUpdate();
            session.createNativeQuery("CREATE INDEX ON notes(classification, final_rating_status, created_at DESC)").executeUpdate();
            session.createNativeQuery("CREATE INDEX ON note_status_history(note_id)").executeUpdate();
            session.createNativeQuery("CREATE INDEX ON note_status_history(note_id ASC, created_at DESC)").executeUpdate();
            session.createNativeQuery("CREATE INDEX ON ratings(note_id)").executeUpdate();
            session.createNativeQuery("CREATE INDEX ON ratings(participant_id)").executeUpdate();
            transaction.commit();
            LOGGER.info("Data indexed.");
        }
    }

    private void destroy() {
        try (Session session = sessionFactory.openSession()) {
            LOGGER.info("Deleting tables...");
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("DROP TABLE IF EXISTS note_status_history").executeUpdate();
            session.createNativeQuery("DROP TABLE IF EXISTS ratings").executeUpdate();
            session.createNativeQuery("DROP TABLE IF EXISTS notes").executeUpdate();
            session.createNativeQuery("DROP TABLE IF EXISTS user_enrollment").executeUpdate();
            transaction.commit();
            LOGGER.info("Tables deleted.");
        }
    }

    private void create() {
        try (Session session = sessionFactory.openSession()) {
            LOGGER.info("Creating tables...");
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery(CREATE_USER_ENROLLMENT).executeUpdate();
            session.createNativeQuery(CREATE_NOTES).executeUpdate();
            session.createNativeQuery(CREATE_NOTE_STATUS_HISTORY).executeUpdate();
            session.createNativeQuery(CREATE_RATINGS).executeUpdate();
            transaction.commit();
            LOGGER.info("Tables created.");
        }
    }

    private static Integer toInteger(String str) {
        if (str == null || str.isBlank()) {
            return null;
        }

        return Integer.valueOf(str);
    }

    public static Double toDouble(String str) {
        if (str == null || str.isBlank()) {
            return null;
        }

        return Double.valueOf(str);
    }

    private static Timestamp toTimestamp(String str) {
        if (str == null || str.isBlank()) {
            return null;
        }

        return new Timestamp(Long.parseLong(str));
    }

    public static Boolean toBoolean(String str) {
        if (str == null || str.isBlank()) {
            return null;
        }

        if (str.equals("1")) {
            return Boolean.TRUE;
        }

        if (str.equals("0")) {
            return Boolean.FALSE;
        }

        return null;
    }
}