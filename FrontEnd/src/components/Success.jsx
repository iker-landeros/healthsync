// SubmissionPage.jsx
import React from "react";
import { Link } from "react-router-dom";

const Success = () => {
  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <h1 className="text-2xl font-bold mb-4">Form Submitted</h1>
      <p className="text-gray-600 mb-4">Thank you for your submission!</p>
      <Link
        to="/"
        className="py-2 px-4 bg-blue-500 text-white font-semibold rounded-md shadow-sm hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
      >
        Back to Form
      </Link>
    </div>
  );
};

export default Success;
