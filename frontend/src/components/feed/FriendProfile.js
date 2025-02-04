// FriendProfile.js
import React, { useEffect, useState } from "react";
import axios from "axios";
import "./Profile.css";
import { useParams } from "react-router-dom";
import Header from "../common/Header";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const FriendProfile = () => {
  const { friendEmail } = useParams(); // Get friendEmail from URL
  const [user, setUser] = useState(null);

  useEffect(() => {
    if (friendEmail) {
      axios
        .get(`http://localhost:8085/api/user/getByEmail/${friendEmail}`)
        .then((response) => {
          setUser(response.data);
        })
        .catch((error) => {
          console.error("Error fetching user:", error);
          toast.warn("Error fetching user.");
        });
    }
  }, [friendEmail]);

  if (!user) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <Header />
      <div className="user-profile">
        <img
          src={
            user.profilePic
              ? `http://localhost:8085${user.profilePic}`
              : "/images/dalhousie-logo.png"
          }
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
      </div>
    </div>
  );
};

export default FriendProfile;
