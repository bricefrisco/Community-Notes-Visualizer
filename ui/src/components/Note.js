import he from "he";
import Linkify from "react-linkify";
import UserGroup from "../icons/UserGroup";
import { useMemo } from "react";

const statuses = {
  NEEDS_MORE_RATINGS: "Needs more ratings",
  CURRENTLY_RATED_HELPFUL: "Currently rated helpful",
  CURRENTLY_RATED_NOT_HELPFUL: "Currently rated not helpful",
};

const DecoratedHref = (href, text, key) => {
  return (
    <div key={key} className="truncate text-twitter-link">
      <a href={href} rel="noopener noreferrer" target="_blank" key={key}>
        {text}
      </a>
    </div>
  );
};

const Note = ({ summary, score, ratings }) => {
  const { helpful, somewhatHelpful, notHelpful } = ratings;

  const ratingReasons = useMemo(() => {
    return [...Object.keys(helpful), ...Object.keys(notHelpful)]
      .filter(
        (key) => key !== "count" && (helpful[key] > 0 || notHelpful[key] > 0)
      )
      .sort((a, b) => {
        const valA = helpful[a] || notHelpful[a] || 0;
        const valB = helpful[b] || notHelpful[b] || 0;
        return valB - valA;
      })
      .slice(0, 2)
      .join(" · ")
      .replace(/([A-Z])/g, " $1");
  }, [helpful, notHelpful]);

  return (
    <div className="relative border border-twitter-border bg-twitter-bg rounded-lg pb-2 pt-1 px-4 -mt-5 border-t-0 rounded-t-none text-sm">
      <div className="mb-2">
        <div className="font-semibold mb-1 flex">
          <UserGroup className="w-5 h-5 text-blue-500 mr-1.5" />
          Community Note
        </div>
        <div className="font-semibold mb-1">
          {" "}
          {statuses[score.coreRatingStatus]}
        </div>
        <div className="capitalize text-gray-400">{ratingReasons}</div>
        <span className="leading-6 whitespace-pre-line">
          <Linkify componentDecorator={DecoratedHref}>
            {he.decode(summary.replace("    ", "\n"))}
          </Linkify>
        </span>
      </div>

      <div className="flex items-center place-content-between border-t border-twitter-border pt-2">
        <div>
          <span className="font-semibold text-green-400">{helpful.count}</span>
          <span className="mx-1 text-gray-400">/</span>
          <span className="font-semibold text-yellow-400">
            {somewhatHelpful.count}
          </span>
          <span className="mx-1 text-gray-400">/</span>
          <span className="font-semibold text-red-400">{notHelpful.count}</span>
        </div>

        <div className="flex">
          <div className="mr-1 cursor-pointer hover:bg-twitter-hover-bg rounded-full text-center py-1 px-3 border-twitter-button-border border font-bold text-twitter-link text-twitterButton">
            All notes
          </div>
          <div className="cursor-pointer hover:bg-twitter-hover-bg rounded-full text-center py-1 px-3 border-twitter-button-border border font-bold text-twitter-link text-twitterButton">
            Note details
          </div>
        </div>
      </div>
    </div>
  );
};

export default Note;
