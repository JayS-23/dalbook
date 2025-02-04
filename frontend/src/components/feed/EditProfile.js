import React, { useState, useEffect } from "react";
import "./EditProfile.css";
import axios from "axios";
import { Link } from "react-router-dom";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function UserProfile() {
  const [user, setUser] = useState({
    firstName: "",
    lastName: "",
    bio: "",
    profilePicture: null,
    status: "Available",
  });
  const [errors, setErrors] = useState({});
  const [message, setMessage] = useState("");
  const [previewProfilePicture, setPreviewProfilePicture] = useState(null);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const storedUserEmail = localStorage.getItem("userEmail");
        const response = await axios.get(
          `http://localhost:8085/api/user/get/${storedUserEmail}`
        );
        setUser(response.data);
        setPreviewProfilePicture(response.data.profilePicture);
      } catch (error) {
        console.error("Error fetching user data:", error);
        toast.warn("Error fetching user data.");
      }
    };

    fetchUserData();
  }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setErrors(""); // Reset error message

    let validationErrors = {};
    if (!user.bio) {
      validationErrors.bio = "Bio is required.";
    }

    if (!user.profilePicture) {
      validationErrors.profilePicture = "Profile picture is required.";
    }

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
    } else {
      const formData = new FormData();
      formData.append("firstName", user.firstName);
      formData.append("lastName", user.lastName);
      formData.append("bio", user.bio);
      formData.append("status", user.status);

      if (user.profilePicture) {
        formData.append("profilePicture", user.profilePicture);
      }

      try {
        await axios.put(
          `http://localhost:8085/api/user/profile/update?email=${user.email}`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );
        setMessage("Profile updated successfully!");
        setErrors({});
      } catch (error) {
        console.error(error);
        setMessage("An error occurred. Please try again!");
      }
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUser({ ...user, [name]: value });
  };

  const handleFileChange = (e) => {
    const { name, files } = e.target;
    setUser({ ...user, [name]: files[0] });
    if (name === "profilePicture") {
      setPreviewProfilePicture(URL.createObjectURL(files[0]));
    }
  };

  return (
    <div className="d-flex vh-100 justify-content-center align-items-center bg-light">
      <div className="p-3 bg-white w-50">
        <h1>Edit Profile</h1>
        {message && <h3 className="text-info">{message}</h3>}
        <form onSubmit={handleSubmit} className="edit-form">
          <div className="edit-form-group">
            <label htmlFor="firstName">First Name:</label>
            <input
              type="text"
              id="firstName"
              name="firstName"
              placeholder="Enter your first name"
              className="form-control"
              value={user.firstName}
              onChange={handleInputChange}
            />
          </div>
          <div className="edit-form-group">
            <label htmlFor="lastName">Last Name:</label>
            <input
              type="text"
              id="lastName"
              name="lastName"
              placeholder="Enter your last name"
              className="form-control"
              value={user.lastName}
              onChange={handleInputChange}
            />
          </div>
          <div className="edit-form-group">
            <label htmlFor="bio">Bio:</label>
            <textarea
              id="bio"
              name="bio"
              placeholder="Enter your bio"
              className="form-control"
              value={user.bio}
              onChange={handleInputChange}
            ></textarea>
            {errors.bio && <p className="text-danger">{errors.bio}</p>}
          </div>
          <div className="edit-form-group">
            <label htmlFor="profilePicture">Profile Picture:</label>
            <input
              type="file"
              id="profilePicture"
              name="profilePicture"
              className="form-control"
              onChange={handleFileChange}
            />
            {previewProfilePicture && (
              <img
                src={previewProfilePicture}
                alt="Profile Preview"
                className="img-thumbnail mt-2"
                style={{ width: "150px", height: "150px" }}
              />
            )}
            {errors.profilePicture && (
              <p className="text-danger">{errors.profilePicture}</p>
            )}
          </div>
          <div className="edit-form-group">
            <label htmlFor="status">Status:</label>
            <select
              id="status"
              name="status"
              className="form-control"
              value={user.status}
              onChange={handleInputChange}
            >
              <option value="Available">Available</option>
              <option value="Busy">Busy</option>
              <option value="Away">Away</option>
              <option value="Offline">Offline</option>
            </select>
          </div>
          <div className="reset-password">
            <Link to="/resetPassword">Reset password?</Link>
          </div>
          <button
            type="submit"
            className="btn btn-primary ml-2"
            style={{ margin: "1rem 0" }}
          >
            Save Changes
          </button>
          <Link to="/profile" className="btn btn-default text-decoration-none">
            Back to Profile
          </Link>
        </form>
      </div>
    </div>
  );
}

export default UserProfile;
