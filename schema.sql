CREATE TABLE user_enrollment (
    participant_id TEXT PRIMARY KEY,
    enrollment_state TEXT,
    successful_rating_needed_to_earn_in INTEGER,
    timestamp_of_last_state_change TIMESTAMP
);

CREATE TABLE notes (
    note_id TEXT PRIMARY KEY,
    participant_id TEXT references user_enrollment(participant_id),
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
);

CREATE TABLE note_status_history (
    id SERIAL PRIMARY KEY,
    note_id TEXT REFERENCES notes(note_id),
    participant_id TEXT REFERENCES user_enrollment(participant_id),
    created_at TIMESTAMP,
    timestamp_first_non_nmr_status TIMESTAMP,
    first_non_nmr_status TEXT,
    timestamp_of_current_status TIMESTAMP,
    current_status TEXT,
    timestamp_latest_non_nmr_status TIMESTAMP,
    latest_non_nmr_status TEXT
);

CREATE TABLE ratings (
    id SERIAL PRIMARY KEY,
    note_id TEXT REFERENCES notes(note_id),
    participant_id TEXT REFERENCES user_enrollment(participant_id),
    created_at TIMESTAMP,
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
    not_helpful_spam_harrassment_or_abuse BOOLEAN,
    not_helpful_irrelevant_sources BOOLEAN,
    not_helpful_opinion_speculation BOOLEAN,
    not_helpful_note_not_needed BOOLEAN
);