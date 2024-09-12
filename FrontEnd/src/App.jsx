import { BrowserRouter, Routes, Route, Form } from "react-router-dom";
import FormComponent from "./components/FormComponent";
import Success from "./components/Success";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<FormComponent />} />
        <Route path="/success" element={<Success />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
