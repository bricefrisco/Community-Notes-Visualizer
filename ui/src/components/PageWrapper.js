const PageWrapper = ({ children, className }) => {
  return (
    <div className="min-h-full flex flex-col bg-twitter-bg text-white w-full">
      <div className={`container mx-auto ${className || ""}`}>{children}</div>
    </div>
  );
};

export default PageWrapper;
