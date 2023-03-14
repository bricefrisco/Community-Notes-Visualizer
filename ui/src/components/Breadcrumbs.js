import CaretRight from "../icons/CaretRight";
import Home from "../icons/Home";
import { Link } from "react-router-dom";

export const Breadcrumb = ({ children, hasPrevious, to }) => {
  return (
    <>
      {hasPrevious && <CaretRight className="h-6 w-6" />}
      <Link to={to}>
        <li className="flex cursor-pointer items-center text-sm font-medium capitalize hover:text-white">
          {children === "Home" && <Home className="mr-2 h-4 w-4" />}
          {children}
        </li>
      </Link>
    </>
  );
};

export const Breadcrumbs = ({ children, className }) => {
  return (
    <nav className="flex">
      <ol
        className={`inline-flex items-center space-x-1 text-gray-400 md:space-x-3 ${className}`}
      >
        {children}
      </ol>
    </nav>
  );
};
