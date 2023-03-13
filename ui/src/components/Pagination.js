export const PaginationPlaceholder = () => {
  return (
    <div className="mt-3 flex items-center justify-end font-semibold text-gray-400 md:mt-0 md:justify-start">
      <svg
        aria-hidden="true"
        role="status"
        className="mr-2 inline h-4 w-4 animate-spin text-gray-600"
        viewBox="0 0 100 101"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z"
          fill="currentColor"
        />
        <path
          d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z"
          fill="rgb(59 130 246)"
        />
      </svg>
      Loading...
    </div>
  );
};

const Pagination = ({ page, pageSize, totalElements }) => {
  // const [searchParams, setSearchParams] = useSearchParams();
  const start = (page - 1) * pageSize + 1;
  const end = page * pageSize > totalElements ? totalElements : page * pageSize;
  const hasPrevious = start > 1;
  const hasNext = end < totalElements;

  //   const back = () => {
  //     searchParams.set("page", (page - 1).toString());
  //     setSearchParams(searchParams);
  //   };

  //   const next = () => {
  //     searchParams.set("page", (page + 1).toString());
  //     setSearchParams(searchParams);
  //   };

  return (
    <div className="mt-3 flex items-center justify-end md:mt-0 md:justify-start">
      <span className="ml-3 mr-3 text-right text-sm text-gray-400 xl:mr-3">
        Showing{" "}
        <span className="font-semibold text-white">
          {end === 0 ? 0 : start}
        </span>{" "}
        to <span className="font-semibold text-white">{end}</span> of{" "}
        <span className="font-semibold text-white">
          {totalElements.toLocaleString()}
        </span>{" "}
        Notes
      </span>
      <div className="xs:mt-0 inline-flex">
        <button
          disabled={!hasPrevious}
          // onClick={back}
          className="inline-flex cursor-pointer items-center rounded-l border-gray-700 bg-gray-800 px-4 py-2 text-sm font-medium text-gray-400 hover:bg-gray-700 hover:text-white disabled:cursor-not-allowed disabled:hover:bg-gray-800 disabled:hover:text-gray-400"
        >
          <svg
            aria-hidden="true"
            className="mr-2 h-5 w-5"
            fill="currentColor"
            viewBox="0 0 20 20"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              fillRule="evenodd"
              d="M7.707 14.707a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 1.414L5.414 9H17a1 1 0 110 2H5.414l2.293 2.293a1 1 0 010 1.414z"
              clipRule="evenodd"
            ></path>
          </svg>
          Prev
        </button>
        <button
          disabled={!hasNext}
          // onClick={next}
          className="inline-flex cursor-pointer items-center rounded-r border-0 border-l border-gray-700 bg-gray-800 px-4 py-2 text-sm font-medium text-gray-400 hover:bg-gray-700 hover:text-white disabled:cursor-not-allowed disabled:hover:bg-gray-800 disabled:hover:text-gray-400"
        >
          Next
          <svg
            aria-hidden="true"
            className="ml-2 h-5 w-5"
            fill="currentColor"
            viewBox="0 0 20 20"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              fillRule="evenodd"
              d="M12.293 5.293a1 1 0 011.414 0l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414-1.414L14.586 11H3a1 1 0 110-2h11.586l-2.293-2.293a1 1 0 010-1.414z"
              clipRule="evenodd"
            ></path>
          </svg>
        </button>
      </div>
    </div>
  );
};

export default Pagination;
