import { useEffect, useState } from "react";
import PageWrapper from "./components/PageWrapper";
import Tweet from "./components/Note";
import { TwitterTweetEmbed } from "react-twitter-embed";
import Note from "./components/Note";

const App = () => {
  const [page, setPage] = useState(0);
  const [notes, setNotes] = useState([]);

  useEffect(() => {
    const fetchNotes = async () => {
      const res = await fetch("http://localhost:8080/notes");
      const data = await res.json();

      setNotes((notes) => {
        const ids = notes.map((note) => note.noteId);
        const newNotes = data.filter((note) => !ids.includes(note.noteId));
        return [...notes, ...newNotes];
      });
    };

    fetchNotes();
  }, []);

  console.log(JSON.stringify(notes));

  return (
    <PageWrapper className="flex flex-col items-center">
      <h1 className="text-3xl font-bold mt-5 mb-2">
        Community Notes Visualizer
      </h1>
      <div className="w-full max-w-[550px]">
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
