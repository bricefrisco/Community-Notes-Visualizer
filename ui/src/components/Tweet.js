import { useState } from "react";
import { TwitterTweetEmbed } from "react-twitter-embed";
import TwitterButton from "./TwitterButton";

const PlaceholderTweet = ({ children }) => {
  return (
    <div className="bg-twitter-bg border-twitter-border rounded-lg border border-b-0 rounded-b-none text-sm p-4 mt-2">
      <div className="border-twitter-border border-b min-h-[100px] mb-2">
        {children}
      </div>
    </div>
  );
};

const Tweet = ({ tweetId, options }) => {
  const [error, setError] = useState(false);

  const onLoad = (e) => {
    if (e === undefined) {
      setError(true);
    }
  };

  if (error) {
    return (
      <PlaceholderTweet>
        Error loading tweet. It is possible that the tweet was deleted after
        this note was created.
        <a
          href={`https://twitter.com/x/status/${tweetId}`}
          target="_blank"
          rel="noopener noreferrer"
        >
          <TwitterButton className="mt-2.5">View on Twitter</TwitterButton>
        </a>
      </PlaceholderTweet>
    );
  }

  return (
    <TwitterTweetEmbed
      tweetId={tweetId}
      placeholder={<PlaceholderTweet>Loading tweet...</PlaceholderTweet>}
      options={options}
      onLoad={onLoad}
    />
  );
};

export default Tweet;
