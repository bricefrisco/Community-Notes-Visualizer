import { useEffect, useState } from "react";
import PageWrapper from "./components/PageWrapper";
import { TwitterTweetEmbed } from "react-twitter-embed";
import Note from "./components/Note";

const TabItem = ({ selected, children, onClick }) => {
  return (
    <li className="w-full" onClick={onClick}>
      <span
        class={`inline-block p-4 border-b-2 border-transparent rounded-t-lg cursor-pointer ${
          selected
            ? "text-blue-500 border-blue-500"
            : "hover:border-gray-300 hover:text-gray-300"
        }`}
      >
        {children}
      </span>
    </li>
  );
};

const Tabs = ({ values, selected, onChange }) => {
  return (
    <div className="text-sm font-medium text-center text-gray-500 border-b border-gray-200 dark:text-gray-400 dark:border-gray-700 cursor-pointer">
      <ul className="flex place-content-between -mb-px">
        {values.map((value) => (
          <TabItem
            key={value.value}
            selected={value.value === selected}
            onClick={() => onChange(value.value)}
          >
            {value.label}
          </TabItem>
        ))}
      </ul>
    </div>
  );
};

const App = () => {
  const [page, setPage] = useState(0);
  const [notes, setNotes] = useState([]);
  const [finalRatingStatus, setFinalRatingStatus] =
    useState("NEEDS_MORE_RATINGS");

  const values = [
    { value: "NEEDS_MORE_RATINGS", label: "Needs More Ratings" },
    { value: "CURRENTLY_RATED_HELPFUL", label: "Helpful" },
    { value: "CURRENTLY_RATED_NOT_HELPFUL", label: "Not Helpful" },
  ];

  useEffect(() => {
    const fetchNotes = async () => {
      const res = await fetch(
        `http://localhost:8080/notes?finalRatingStatus=${finalRatingStatus}`
      );
      const data = await res.json();
      setNotes(data);

      // setNotes((notes) => {
      //   const ids = notes.map((note) => note.noteId);
      //   const newNotes = data.filter((note) => !ids.includes(note.noteId));
      //   return [...notes, ...newNotes];
      // });
    };

    fetchNotes();
  }, [finalRatingStatus]);

  return (
    <PageWrapper className="flex flex-col items-center">
      <h1 className="text-3xl font-bold mt-5 mb-2">
        Community Notes Visualizer
      </h1>

      <div className="w-full max-w-[550px]">
        <Tabs
          values={values}
          selected={finalRatingStatus}
          onChange={(e) => setFinalRatingStatus(e)}
        />
        {notes.map((note) => (
          <div key={note.noteId} className="mb-5">
            <TwitterTweetEmbed
              tweetId={note.tweetId.toString()}
              placeholder={<div>Loading...</div>}
              options={{ theme: "dark", conversation: "none", cards: "hidden" }}
            />

            <Note {...note} />
          </div>
        ))}
      </div>
    </PageWrapper>
  );
};

export default App;
