import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import FormComponent from "./components/FormComponent";
import SubmissionPage from "./components/SubmissionPage";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<FormComponent />} />
        <Route path="/submission" element={<SubmissionPage />} />
      </Routes>
    </Router>
  );
}

export default App;
