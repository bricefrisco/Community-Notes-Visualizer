import he from "he";
import Linkify from "react-linkify";
import UserGroup from "../icons/UserGroup";
import { useMemo } from "react";
import TwitterButton from "./TwitterButton";
import Circle from "../icons/Circle";

const statuses = {
  NEEDS_MORE_RATINGS: {
    text: "Needs more ratings",
    className: "text-gray-400",
  },
  CURRENTLY_RATED_HELPFUL: {
    text: "Currently rated helpful",
    className: "text-green-400",
  },
  CURRENTLY_RATED_NOT_HELPFUL: {
    text: "Currently rated not helpful",
    className: "text-red-400",
  },
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
        <div className="font-semibold mb-1 flex items-center">
          <Circle
            className={`w-2 h-2 mr-1 ${
              statuses[score.coreRatingStatus].className
            }`}
          />
          {statuses[score.coreRatingStatus].text}
        </div>
        <div className="capitalize text-gray-400 mb-1">{ratingReasons}</div>
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
          <TwitterButton className="mr-1">All notes</TwitterButton>
          <TwitterButton>Note details</TwitterButton>
        </div>
      </div>
    </div>
  );
};

export default Note;
