import React, { useEffect, useState } from "react";
import friendService from "../../services/FriendService";
import "./FriendRequest.css";
<<<<<<< HEAD
=======
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
>>>>>>> develop

const FriendRequestList = () => {
  const [requests, setRequests] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const storedUserId = localStorage.getItem("userId");
    if (storedUserId) {
      friendService
        .getPendingRequests(storedUserId)
        .then((response) => {
          setRequests(response.data);
        })
        .catch((error) => {
          console.error("Error fetching pending requests:", error);
          toast.warn("An error occurred. Please try again!");
        });
    }
  }, []);

  const handleAcceptRequest = (event, requestId) => {
    event.stopPropagation(); // Prevent event propagation
    friendService
      .acceptFriendRequest(requestId)
      .then((response) => {
        setRequests(requests.filter((req) => req.id !== requestId));
        toast.success("Successfully added the user as your friend.");
      })
      .catch((error) => {
        console.error("Error accepting friend request:", error);
        toast.error("Error accepting friend request.");
      });
  };

<<<<<<< HEAD
  return (
    <div>
      <h2 style={{ padding: "1.5rem", paddingBottom: "0" }}>Pending Friend Requests</h2>
      <h4 style={{ padding: "1.5rem", paddingBottom: "1rem" }}>Current User ID: {userId} </h4>
      <ul>
=======
  const viewFriendProfile = (friendEmail) => {
    navigate(`/friendProfile/${friendEmail}`);
  };

  return (
    <div>
      <h2 style={{ padding: "1.5rem", paddingBottom: "0" }}>
        Pending Friend Requests
      </h2>
      <div className="friends-list">
>>>>>>> develop
        {requests.map((request) => (
          <div
            key={request.id}
            className="friends-name"
            onClick={() => viewFriendProfile(request.sender.email)}
            style={{ cursor: "pointer" }}
          >
            <img
              src={
                request.sender.profilePic
                  ? `http://localhost:8085${request.sender.profilePic}`
                  : "/images/dalhousie-logo.png"
              }
              alt=""
              style={{ padding: "1rem" }}
            />
            <div className="request-name">
              {request.sender.firstName + " " + request.sender.lastName}
            </div>
            <div>
              <button
                onClick={(event) => handleAcceptRequest(event, request.id)}
                className="btn btn-success"
              >
                Accept Request
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default FriendRequestList;
