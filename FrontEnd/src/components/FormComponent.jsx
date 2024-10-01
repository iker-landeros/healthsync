import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const FormComponent = () => {
  const [formData, setFormData] = useState({
    senderName: "",
    idExtension: "",
    description: "",
    idProblemType: "",
    idDeviceType: "",
  });

  const [areas, setAreas] = useState([]);
  const [extensions, setExtensions] = useState([]);
  const [problemTypes, setProblemTypes] = useState([]);
  const [deviceTypes, setDeviceTypes] = useState([]);
  const [filteredExtensions, setFilteredExtensions] = useState([]);
  const [selectedArea, setSelectedArea] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [areasData, extensionsData, problemTypesData, deviceTypesData] =
          await Promise.all([
            fetch(`${import.meta.env.VITE_API_URL}/tickets/areas`).then((res) =>
              res.json()
            ),
            fetch(`${import.meta.env.VITE_API_URL}/tickets/extensions`).then(
              (res) => res.json()
            ),
            fetch(`${import.meta.env.VITE_API_URL}/tickets/problemTypes`).then(
              (res) => res.json()
            ),
            fetch(`${import.meta.env.VITE_API_URL}/tickets/deviceTypes`).then(
              (res) => res.json()
            ),
          ]);

        setAreas(areasData);
        setExtensions(extensionsData);
        setProblemTypes(problemTypesData);
        setDeviceTypes(deviceTypesData);
      } catch (error) {
        navigate("/submission", {
          state: {
            success: false,
            message:
              "Error de conexión con la API, no se pudieron obtener los datos",
          },
        });
      }
    };

    fetchData();
  }, []);

  useEffect(() => {
    if (selectedArea != "") {
      setFilteredExtensions(
        extensions.filter(
          (extension) =>
            extension.idArea === parseInt(selectedArea) ||
            extension.extensionNumber == "Otro"
        )
      );
    }
  }, [selectedArea]);

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
      const response = await fetch(`${import.meta.env.VITE_API_URL}/tickets`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      const responseBody = await response.json();

      if (response.ok) {
        navigate("/submission", {
          state: {
            success: true,
            message:
              responseBody.message || "¡Formulario enviado exitosamente!",
          },
        });
      } else {
        navigate("/submission", {
          state: {
            success: false,
            message: responseBody.message || "No se pudo enviar el formulario.",
          },
        });
      }
    } catch (error) {
      navigate("/submission", {
        state: {
          success: false,
          message:
            "Se produjo un error en el sistema, intentelo mas tarde o contacte a un técnico.",
        },
      });
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
          <h1 className="text-2xl text-center font-bold">
            Formulario Para Reporte de Fallas
          </h1>
        </div>

        <form onSubmit={handleSubmit} className="flex-grow flex flex-col">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 flex-grow">
            <div className="md:col-span-2">
              <label
                htmlFor="senderName"
                className="text-sm font-medium text-gray-700"
              >
                Nombre
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
                value={selectedArea}
                onChange={(e) => setSelectedArea(e.target.value)} // Handle change correctly
                className="mt-1 w-full border border-gray-300 rounded-md shadow-md focus:border-indigo-500 p-3 text-lg"
                required
              >
                <option value="">Seleccionar Area</option>
                {areas.map((area) => (
                  <option key={area.idArea} value={area.idArea}>
                    {area.areaName}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label
                htmlFor="idExtension"
                className="block text-sm font-medium text-gray-700"
              >
                Extensión
              </label>
              <select
                id="idExtension"
                name="idExtension"
                value={formData.idExtension}
                onChange={handleChange}
                className="mt-1 w-full border border-gray-300 rounded-md shadow-md focus:border-indigo-500 p-3 text-lg"
                disabled={!selectedArea}
                required
              >
                <option value="">Seleccionar Extensión</option>
                {filteredExtensions.map((extension) => (
                  <option
                    key={extension.idExtension}
                    value={extension.idExtension}
                  >
                    {extension.extensionNumber}
                  </option>
                ))}
              </select>
            </div>

            <div className="md:col-span-2 flex-grow">
              <label
                htmlFor="description"
                className="block text-sm font-medium text-gray-700"
              >
                Descripción
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

            <div>
              <label
                htmlFor="idProblemType"
                className="block text-sm font-medium text-gray-700"
              >
                Tipo de Problema
              </label>
              <select
                id="idProblemType"
                name="idProblemType"
                value={formData.idProblemType}
                onChange={handleChange}
                className="mt-1 w-full border border-gray-300 rounded-md shadow-md focus:border-indigo-500 p-3 text-lg"
                required
              >
                <option value="">Seleccionar Tipo de Problema</option>
                {problemTypes.map((problemType) => (
                  <option
                    key={problemType.idProblemType}
                    value={problemType.idProblemType}
                  >
                    {problemType.problemName}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label
                htmlFor="idDeviceType"
                className="block text-sm font-medium text-gray-700"
              >
                Tipo de Dispositivo
              </label>
              <select
                id="idDeviceType"
                name="idDeviceType"
                value={formData.idDeviceType}
                onChange={handleChange}
                className="mt-1 w-full border border-gray-300 rounded-md shadow-md focus:border-indigo-500 p-3 text-lg"
                required
              >
                <option value="">Seleccionar Tipo de Dispositivo</option>
                {deviceTypes.map((deviceType) => (
                  <option
                    key={deviceType.idDeviceType}
                    value={deviceType.idDeviceType}
                  >
                    {deviceType.deviceName}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="mt-6">
            <button
              type="submit"
              className="w-full py-2 px-4 bg-custom-purple text-white font-semibold rounded-md shadow-md hover:bg-custom-purple-600"
            >
              Enviar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default FormComponent;
