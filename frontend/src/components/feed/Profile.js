import React, { useEffect, useState } from "react";
import axios from "axios";
import "./Profile.css";
import { Link } from "react-router-dom";

const Profile = () => {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const storedUserEmail = localStorage.getItem("userEmail");
    if (storedUserEmail) {
      axios
        .get(`http://localhost:8085/api/user/get/${storedUserEmail}`)
        .then((response) => {
          setUser(response.data);
        })
        .catch((error) => {
          console.error("Error fetching user:", error);
        });
    }
  }, []);

  if (!user) {
    return <div>Loading...</div>;
  }

  return (
    <div className="user-profile">
      <img
        src={user.profilePicture || "/images/dalhousie-logo.png"}
        alt="Profile"
        className="profile-picture"
      />
      <div className="user-info">
        <h1>
          {user.firstName} {user.lastName}
        </h1>
        <p>
          <strong>Email:</strong> {user.email}
        </p>
        <p>
          <strong>Birthday:</strong> {user.dateOfBirth}
        </p>
        <p>
          <strong>Bio:</strong> {user.bio}
        </p>
        <p>
          <strong>Status:</strong> {user.status}
        </p>
      </div>
      <div className="user-buttons">
        <Link to="/home" className="btn btn-success text-decoration-none">
          Homepage
        </Link>
        <Link to="/editProfile" className="btn btn-default text-decoration-none">
          Edit Profile
        </Link>
      </div>
    </div>
  );
};

export default Profile;