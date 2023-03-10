package com.bfrisco.services;

import com.bfrisco.database.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.*;
import java.sql.Timestamp;

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
                note_id TEXT,
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
                summary TEXT
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

    private static final String CREATE_SCORED_NOTES = """
            CREATE TABLE scored_notes (
                note_id TEXT,
                core_note_intercept DOUBLE PRECISION,
                core_note_factor DOUBLE PRECISION,
                final_rating_status TEXT,
                first_tag TEXT,
                second_tag TEXT,
                core_active_rules TEXT,
                active_filter_tags TEXT,
                classification TEXT,
                created_at TIMESTAMP,
                core_rating_status TEXT,
                meta_scorer_active_rules TEXT,
                decided_by TEXT,
                expansion_note_intercept DOUBLE PRECISION,
                expansion_note_factor_1 DOUBLE PRECISION,
                expansion_rating_status TEXT,
                coverage_note_intercept DOUBLE PRECISION,
                coverage_note_factor_1 DOUBLE PRECISION,
                coverage_rating_status TEXT
            )
            """;

    @Inject
    SessionFactory sessionFactory;

    public void run() throws IOException {
        LOGGER.info("Starting data import...");

        destroy();
        create();
        importUserEnrollment();
        importNotes();
        importNoteStatusHistory();
        importRatings();
        importScoredNotes();
        cleanData();
        createConstraints();
        createIndexes();

        LOGGER.info("Data import complete.");
    }

    private void importUserEnrollment() throws IOException {
        try (Session session = sessionFactory.openSession();
             BufferedReader br = new BufferedReader(new FileReader(
                     "C:\\Users\\Brice\\Desktop\\community-notes-visualizer\\userEnrollment-00000.tsv"))) {
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

    private void importNotes() throws IOException {
        try (Session session = sessionFactory.openSession();
             BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Brice\\Desktop\\community-notes-visualizer\\notes-00000.tsv"))) {
            Transaction transaction = session.beginTransaction();
            int count = 0;

            String line = br.readLine(); // Skip header
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

                session.save(note);

                count++;
                if (count % 10000 == 0) {
                    session.flush();
                    session.clear();
                    LOGGER.info("Imported {} note records", count);
                }
            }

            transaction.commit();
            LOGGER.info("Imported {} note records", count);
        }
    }

    private void importNoteStatusHistory() throws IOException {
        try (Session session = sessionFactory.openSession();
             BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Brice\\Desktop\\community-notes-visualizer\\noteStatusHistory-00000.tsv"))) {
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
             BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Brice\\Desktop\\community-notes-visualizer\\ratings-00000.tsv"))) {
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

    private void importScoredNotes() throws IOException {
        try (Session session = sessionFactory.openSession();
             BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Brice\\Desktop\\community-notes-visualizer\\scored_notes.tsv"))) {
            Transaction transaction = session.beginTransaction();

            int count = 0;
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] columns = line.split("\t");

                ScoredNote note = new ScoredNote();
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

                session.save(note);

                count++;
                if (count % 10000 == 0) {
                    session.flush();
                    session.clear();
                    LOGGER.info("Imported {} scored notes", count);
                }
            }

            transaction.commit();
            LOGGER.info("Imported {} scored notes", count);
        }
    }

    private void cleanData() {
        try (Session session = sessionFactory.openSession()) {
            LOGGER.info("Cleaning data...");
            Transaction transaction = session.beginTransaction();
            LOGGER.info("Cleared {} records from note_status_history with no corresponding note_id",
                    session.createNativeQuery("DELETE FROM note_status_history WHERE note_id NOT IN (SELECT note_id FROM notes)")
                            .executeUpdate());
            LOGGER.info("Cleared {} records from ratings with no corresponding note_id",
                    session.createNativeQuery("DELETE FROM ratings WHERE note_id NOT IN (SELECT note_id FROM notes)")
                            .executeUpdate());
            LOGGER.info("Cleared {} records from ratings with no corresponding participant_id",
                    session.createNativeQuery("DELETE FROM ratings WHERE participant_id NOT IN (SELECT participant_id FROM user_enrollment)")
                            .executeUpdate());
            LOGGER.info("Cleared {} records from scored_notes with no corresponding note_id",
                    session.createNativeQuery("DELETE FROM scored_notes WHERE note_id NOT IN (SELECT note_id FROM notes)")
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
            session.createNativeQuery("ALTER TABLE notes ADD PRIMARY KEY (note_id)").executeUpdate();
            session.createNativeQuery("ALTER TABLE notes ADD CONSTRAINT notes_participant_id_fk FOREIGN KEY (participant_id) REFERENCES user_enrollment (participant_id)").executeUpdate();
            session.createNativeQuery("ALTER TABLE note_status_history ADD CONSTRAINT note_status_history_note_id_fk FOREIGN KEY (note_id) REFERENCES notes (note_id)").executeUpdate();
            session.createNativeQuery("ALTER TABLE note_status_history ADD CONSTRAINT note_status_history_participant_id_fk FOREIGN KEY (participant_id) REFERENCES user_enrollment (participant_id)").executeUpdate();
            session.createNativeQuery("ALTER TABLE ratings ADD PRIMARY KEY (note_id, participant_id)").executeUpdate();
            session.createNativeQuery("ALTER TABLE ratings ADD CONSTRAINT ratings_note_id_fk FOREIGN KEY (note_id) REFERENCES notes(note_id)").executeUpdate();
            session.createNativeQuery("ALTER TABLE ratings ADD CONSTRAINT ratings_participant_id_fk FOREIGN KEY (participant_id) REFERENCES user_enrollment(participant_id)").executeUpdate();
            session.createNativeQuery("ALTER TABLE scored_notes ADD PRIMARY KEY (note_id)").executeUpdate();
            session.createNativeQuery("ALTER TABLE scored_notes ADD CONSTRAINT scored_notes_note_id_fk FOREIGN KEY (note_id) REFERENCES notes(note_id)").executeUpdate();
            transaction.commit();
            LOGGER.info("Constraints created.");
        }
    }

    private void createIndexes() {
        try (Session session = sessionFactory.openSession()) {
            LOGGER.info("Creating indexes...");
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("CREATE INDEX ON notes(participant_id)").executeUpdate();
            session.createNativeQuery("CREATE INDEX ON note_status_history(note_id)").executeUpdate();
            session.createNativeQuery("CREATE INDEX ON note_status_history(note_id ASC, created_at DESC)").executeUpdate();
            session.createNativeQuery("CREATE INDEX ON ratings(note_id)").executeUpdate();
            session.createNativeQuery("CREATE INDEX ON ratings(participant_id)").executeUpdate();
            session.createNativeQuery("CREATE INDEX ON scored_notes(classification, final_rating_status, created_at DESC)").executeUpdate();
            transaction.commit();
            LOGGER.info("Indexes created.");
        }
    }

    private void destroy() {
        try (Session session = sessionFactory.openSession()) {
            LOGGER.info("Deleting tables...");
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("DROP TABLE IF EXISTS scored_notes").executeUpdate();
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
            session.createNativeQuery(CREATE_SCORED_NOTES).executeUpdate();
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