import React from "react";
import { Link, useLocation } from "react-router-dom";
import { BiCheck, BiX } from "react-icons/bi";

const SubmissionPage = () => {
  const location = useLocation();
  const { state } = location;

  // Log the state to verify what is being received
  console.log("Location state:", state);

  const success = state?.success ?? false;
  const message = state?.message ?? "No message provided";

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <div className="text-4xl mb-5">
        {success ? (
          <BiCheck className="size-20 text-custom-green" />
        ) : (
          <BiX className="size-20 text-custom-orange" />
        )}
      </div>
      <h1 className="text-2xl text-center font-bold mb-5">{message}</h1>
      <Link
        to="/"
        className="py-2 px-4 bg-custom-blue text-white font-semibold rounded-md shadow-sm hover:bg-custom-blue-600"
      >
        Regresar a Formulario
      </Link>
    </div>
  );
};

export default SubmissionPage;
