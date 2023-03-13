const PageWrapper = ({ children, className }) => {
  return (
    <div className="min-h-full flex flex-col bg-gray-900 text-white w-full">
      <div className={`container mx-auto ${className || ""}`}>{children}</div>
    </div>
  );
};

export default PageWrapper;
