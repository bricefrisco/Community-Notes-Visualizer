const classifications = {
  NOT_MISLEADING: "Not misleading",
  MISINFORMED_OR_POTENTIALLY_MISLEADING:
    "Misinformed or potentially misleading",
};

const statuses = {
  NEEDS_MORE_RATINGS: "Needs more ratings",
  CURRENTLY_RATED_HELPFUL: "Currently rated helpful",
  CURRENTLY_NOT_RATED_HELPFUL: "Currently not rated helpful",
};

const Note = ({ classification, summary, score, ratings }) => {
  return (
    <div className="border border-twitter-border rounded-lg pb-2 pt-5 px-3 mt-[-20px] border-t-0 rounded-t-none">
      <div className="text-sm font-semibold mb-1">
        {statuses[score.coreRatingStatus]}
      </div>
      <span dangerouslySetInnerHTML={{ __html: summary }} />
      <div className="flex justify-end">
        <span className="mr-3">👍 {ratings.helpful}</span>
        <span className="mr-3">🫱 {ratings.somewhatHelpful}</span>
        <span className="ml-2">👎 {ratings.notHelpful}</span>
      </div>
    </div>
  );
};

export default Note;
