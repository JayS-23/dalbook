import React, { useEffect, useState } from "react";
import friendService from "../../services/FriendService";
import { Link } from "react-router-dom";
import "./FriendRequest.css";

const FriendRequestList = () => {
  const [requests, setRequests] = useState([]);
  const [userId, setUserId] = useState(null);

  useEffect(() => {
    const storedUserId = localStorage.getItem("userId");
    if (storedUserId) {
      setUserId(storedUserId);
      friendService
        .getPendingRequests(storedUserId)
        .then((response) => {
          setRequests(response.data);
        })
        .catch((error) => {
          console.error("Error fetching pending requests:", error);
          alert("An error occurred. Please try again!");
        });
    }
  }, []);

  const handleAcceptRequest = (requestId) => {
    friendService
      .acceptFriendRequest(requestId)
      .then((response) => {
        setRequests(requests.filter((req) => req.id !== requestId));
      })
      .catch((error) => {
        console.error("Error accepting friend request:", error);
      });
  };

  return (
    <div>
      <h2 style={{ padding: "10px 10px" }}>Pending Friend Requests</h2>
      <h4 style={{ padding: "10px 10px" }}>Current User ID: {userId} </h4>
      <ul>
        {requests.map((request) => (
          <li key={request.id}>
            <div className="request-name">
              {request.sender.firstName + " " + request.sender.lastName}
            </div>
            <button
              onClick={() => handleAcceptRequest(request.id)}
              className="btn send-button"
            >
              Accept Request
            </button>
          </li>
        ))}
      </ul>
      <div className="links">
        <Link
          to="/friendRequest"
          className="btn btn-success text-decoration-none"
        >
          Friend Request
        </Link>
        <Link
          to="/friendsList"
          className="btn btn-success text-decoration-none"
        >
          Friends List
        </Link>
      </div>
    </div>
  );
};

export default FriendRequestList;
