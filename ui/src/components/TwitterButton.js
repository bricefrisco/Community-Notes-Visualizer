const TwitterButton = ({ className, children }) => {
  return (
    <div
      className={`cursor-pointer hover:bg-twitter-hover-bg rounded-full text-center py-1 px-3 border-twitter-button-border border font-bold text-twitter-link text-twitterButton ${
        className || ""
      }`}
    >
      {children}
    </div>
  );
};

export default TwitterButton;
