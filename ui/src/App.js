import { useEffect, useState } from "react";
import PageWrapper from "./components/PageWrapper";
import Note from "./components/Note";
import Masonry from "react-masonry-css";
import RadioGroup from "./components/RadioGroup";
import Tweet from "./components/Tweet";
import Search from "./components/Search";
import Pagination from "./components/Pagination";

const App = () => {
  const [page, setPage] = useState(1);
  const [data, setData] = useState([]);
  const [finalRatingStatus, setFinalRatingStatus] = useState("");

  useEffect(() => {
    const fetchNotes = async () => {
      const res = await fetch(
        `http://localhost:8080/notes?finalRatingStatus=${finalRatingStatus}&page=${page}`
      );

      const data = await res.json();
      setData(data);
    };

    fetchNotes();
  }, [finalRatingStatus, page]);

  return (
    <PageWrapper className="">
      <h1 className="text-2xl font-bold mt-5 mb-10">Community Notes Details</h1>

      <div className="w-full flex">
        <div className="w-full max-w-[250px]">
          <div className="font-bold">Filters</div>

          <RadioGroup
            className="mt-5"
            label="Rating Status"
            options={[
              { name: "Any", value: "" },
              { name: "Helpful", value: "CURRENTLY_RATED_HELPFUL" },
              { name: "Not Helpful", value: "CURRENTLY_RATED_NOT_HELPFUL" },
              { name: "Needs More Ratings", value: "NEEDS_MORE_RATINGS" },
            ]}
            value={finalRatingStatus}
            onChange={(e) => setFinalRatingStatus(e.target.value)}
          />
        </div>

        <div>
          <div className="flex items-center place-content-between">
            <Search className="w-full max-w-xl" />

            {data?.results?.length > 0 && (
              <Pagination
                page={page}
                pageSize={10}
                totalElements={data.totalResults}
              />
            )}
          </div>

          <Masonry
            className="flex -ml-3 max-w-[1150px]"
            columnClassName="ml-3"
            breakpointCols={{ default: 3 }}
          >
            {data?.results?.map((note) => (
              <div key={note.noteId} className="mb-3 max-w-[350px]">
                <Tweet
                  tweetId={note.tweetId.toString()}
                  options={{
                    theme: "dark",
                    conversation: "none",
                    cards: "hidden",
                    chrome: "noborders",
                  }}
                />

                <Note {...note} />
              </div>
            ))}
          </Masonry>
        </div>
      </div>
    </PageWrapper>
  );
};

export default App;
