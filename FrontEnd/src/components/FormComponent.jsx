import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const FormComponent = () => {
  const [formData, setFormData] = useState({
    senderName: "", // Renamed from name
    idArea: "", // Renamed from area
    idExtension: "", // Renamed from extension
    description: "", // Same
    idProblemType: "", // Renamed from category
    idDeviceType: "", // Renamed from type
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:3001/tickets", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData), // Send formData as JSON
      });

      if (response.ok) {
        console.log("Form data submitted:", formData);
        navigate("/success");
      } else {
        console.error("Failed to submit the form.");
      }
    } catch (error) {
      console.error("Error occurred during form submission:", error);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen w-full bg-gray-100 overflow-auto p-4">
      <div className="sm:w-5/6 flex flex-col p-8 bg-white shadow-md rounded-md">
        <div className="sm:relative sm:mb-12 mb-8 sm:mt-4 flex flex-col items-center justify-center">
          <img
            src="/src/assets/hnm_logo.png"
            alt="Logo"
            className="sm:absolute left-0 h-28 w-28 object-contain"
          />
          <h1 className="text-2xl text-center font-bold">Submit Form</h1>
        </div>

        <form onSubmit={handleSubmit} className="flex-grow flex flex-col">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 flex-grow">
            {/* Sender Name Field */}
            <div className="md:col-span-2">
              <label
                htmlFor="senderName"
                className="text-sm font-medium text-gray-700"
              >
                Name
              </label>
              <input
                type="text"
                id="senderName"
                name="senderName"
                value={formData.senderName}
                onChange={handleChange}
                className="mt-1 w-full border border-gray-300 rounded-md shadow-md focus:border-indigo-500 p-3 text-lg"
                required
              />
            </div>

            {/* Area Field */}
            <div>
              <label
                htmlFor="idArea"
                className="block text-sm font-medium text-gray-700"
              >
                Area
              </label>
              <select
                id="idArea"
                name="idArea"
                value={formData.idArea}
                onChange={handleChange}
                className="mt-1 w-full border border-gray-300 rounded-md shadow-md focus:border-indigo-500 p-3 text-lg"
                required
              >
                <option value="">Select Area</option>
                <option value="1">Area 1</option>
                <option value="2">Area 2</option>
                <option value="3">Area 3</option>
              </select>
            </div>

            {/* Extension Field */}
            <div>
              <label
                htmlFor="idExtension"
                className="block text-sm font-medium text-gray-700"
              >
                Extension
              </label>
              <select
                id="idExtension"
                name="idExtension"
                value={formData.idExtension}
                onChange={handleChange}
                className="mt-1 w-full border border-gray-300 rounded-md shadow-md focus:border-indigo-500 p-3 text-lg"
                required
              >
                <option value="">Select Extension</option>
                <option value="1">1001</option>
                <option value="2">1002</option>
                <option value="3">1003</option>
              </select>
            </div>

            {/* Description Field */}
            <div className="md:col-span-2 flex-grow">
              <label
                htmlFor="description"
                className="block text-sm font-medium text-gray-700"
              >
                Description
              </label>
              <textarea
                id="description"
                name="description"
                value={formData.description}
                onChange={handleChange}
                className="mt-1 w-full border border-gray-300 rounded-md shadow-md focus:border-indigo-500 p-3 text-lg"
                required
              />
            </div>

            {/* Category (ProblemType) Field */}
            <div>
              <label
                htmlFor="idProblemType"
                className="block text-sm font-medium text-gray-700"
              >
                Problem Type
              </label>
              <select
                id="idProblemType"
                name="idProblemType"
                value={formData.idProblemType}
                onChange={handleChange}
                className="mt-1 w-full border border-gray-300 rounded-md shadow-md focus:border-indigo-500 p-3 text-lg"
                required
              >
                <option value="">Select Problem Type</option>
                <option value="1">Problem 1</option>
                <option value="2">Problem 2</option>
                <option value="3">Problem 3</option>
              </select>
            </div>

            {/* Device Type Field */}
            <div>
              <label
                htmlFor="idDeviceType"
                className="block text-sm font-medium text-gray-700"
              >
                Device Type
              </label>
              <select
                id="idDeviceType"
                name="idDeviceType"
                value={formData.idDeviceType}
                onChange={handleChange}
                className="mt-1 w-full border border-gray-300 rounded-md shadow-md focus:border-indigo-500 p-3 text-lg"
                required
              >
                <option value="">Select Device Type</option>
                <option value="1">Device Type 1</option>
                <option value="2">Device Type 2</option>
                <option value="3">Device Type 3</option>
              </select>
            </div>
          </div>

          {/* Submit Button */}
          <div className="mt-6">
            <button
              type="submit"
              className="w-full py-2 px-4 bg-blue-500 text-white font-semibold rounded-md shadow-md hover:bg-blue-600"
            >
              Send
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default FormComponent;
