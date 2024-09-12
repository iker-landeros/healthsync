import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const FormComponent = () => {
  const [formData, setFormData] = useState({
    name: "",
    area: "",
    extension: "",
    description: "",
    category: "",
    type: "",
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Form data submitted:", formData);
    navigate("/success");
  };

  return (
    <div className="flex items-center justify-center min-h-screen w-full bg-gray-100 overflow-auto p-4">
      <div className="sm:w-5/6 flex flex-col p-8 bg-white shadow-md rounded-md">
        <div className="sm:relative mb-6 flex flex-col items-center justify-center">
          <img
            src="/src/assets/hnm_logo.png"
            alt="Logo"
            className="sm:absolute left-0 h-16 w-16 object-contain"
          />
          <h1 className="text-2xl text-center font-bold">Submit Form</h1>
        </div>

        <form onSubmit={handleSubmit} className="flex-grow flex flex-col">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 flex-grow">
            {/* Name Field */}
            <div className="md:col-span-2">
              <label
                htmlFor="name"
                className="text-sm font-medium text-gray-700"
              >
                Name
              </label>
              <input
                type="text"
                id="name"
                name="name"
                value={formData.name}
                onChange={handleChange}
                className="mt-1 w-full border border-gray-300 rounded-md shadow-md focus:border-indigo-500 p-3 text-lg"
                required
              />
            </div>

            {/* Area Field */}
            <div>
              <label
                htmlFor="area"
                className="block text-sm font-medium text-gray-700"
              >
                Area
              </label>
              <select
                id="area"
                name="area"
                value={formData.area}
                onChange={handleChange}
                className="mt-1 w-full border border-gray-300 rounded-md shadow-md focus:border-indigo-500 p-3 text-lg"
                required
              >
                <option value="">Select Area</option>
                <option value="Area 1">Area 1</option>
                <option value="Area 2">Area 2</option>
                <option value="Area 3">Area 3</option>
              </select>
            </div>

            {/* Extension Field */}
            <div>
              <label
                htmlFor="extension"
                className="block text-sm font-medium text-gray-700"
              >
                Extension
              </label>
              <select
                id="extension"
                name="extension"
                value={formData.extension}
                onChange={handleChange}
                className="mt-1 w-full border border-gray-300 rounded-md shadow-md focus:border-indigo-500 p-3 text-lg"
                required
              >
                <option value="">Select Extension</option>
                <option value="1001">1001</option>
                <option value="1002">1002</option>
                <option value="1003">1003</option>
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

            {/* Category Field */}
            <div>
              <label
                htmlFor="category"
                className="block text-sm font-medium text-gray-700"
              >
                Category
              </label>
              <select
                id="category"
                name="category"
                value={formData.category}
                onChange={handleChange}
                className="mt-1 w-full border border-gray-300 rounded-md shadow-md focus:border-indigo-500 p-3 text-lg"
                required
              >
                <option value="">Select Category</option>
                <option value="Category 1">Category 1</option>
                <option value="Category 2">Category 2</option>
                <option value="Category 3">Category 3</option>
              </select>
            </div>

            {/* Type Field */}
            <div>
              <label
                htmlFor="type"
                className="block text-sm font-medium text-gray-700"
              >
                Type
              </label>
              <select
                id="type"
                name="type"
                value={formData.type}
                onChange={handleChange}
                className="mt-1 w-full border border-gray-300 rounded-md shadow-md focus:border-indigo-500 p-3 text-lg"
                required
              >
                <option value="">Select Type</option>
                <option value="Type 1">Type 1</option>
                <option value="Type 2">Type 2</option>
                <option value="Type 3">Type 3</option>
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
