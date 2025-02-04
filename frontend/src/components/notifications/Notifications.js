// Notifications.js
<<<<<<< HEAD
=======
import React, { useEffect, useState } from "react";
import axios from "axios";
import friendService from "../../services/FriendService";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./Notifications.css";
import Header from "../common/Header";
import FriendRequestList from "../friendRequests/FriendRequestList";

const Notifications = () => {
  const [friendRequests, setFriendRequests] = useState([]);
  const [approvedRequests, setApprovedRequests] = useState([]);
  const [newTopic, setNewTopic] = useState("");
  const [groups, setGroups] = useState({});

  // Fetch pending friend requests
  useEffect(() => {
    const storedUserId = localStorage.getItem("userId");
    if (storedUserId) {
      friendService
        .getPendingRequests(storedUserId)
        .then((response) => {
          setFriendRequests(response.data);
        })
        .catch((error) => {
          console.error("Error fetching pending requests:", error);
          toast.warn("Error fetching pending requests.");
        });
    }

    const fetchApprovedRequests = async () => {
      try {
        const response = await axios.get(`http://localhost:8085/api/join-requests/approved`);
        const requests = response.data;

        const groupIds = requests.map((req) => req.groupId);
        const uniqueGroupIds = [...new Set(groupIds)];

        const groupPromises = uniqueGroupIds.map((groupId) =>
          axios.get(`http://localhost:8085/api/group/get/${groupId}`)
        );

        const groupResponses = await Promise.all(groupPromises);
        const groupData = groupResponses.reduce((acc, curr) => {
          acc[curr.data.id] = curr.data;
          return acc;
        }, {});

        setGroups(groupData);
        setApprovedRequests(requests);
      } catch (error) {
        console.error("Error fetching approved join requests or groups:", error);
        toast.warn("Error fetching approved join requests or groups.");
      }
    };

    const fetchNewTopic = async () => {
      try {
        const response = await axios.get(
          "http://localhost:8085/api/topics/latest"
        );
        setNewTopic(response.data.topic);
        localStorage.setItem("currentTopic", response.data.topic);
      } catch (error) {
        console.error("Error fetching topic:", error);
        toast.warn("Error fetching topic");
      }
    };

    fetchApprovedRequests();
    fetchNewTopic();
  }, []);

  return (
    <>
      <Header />
      <div className="notifications-container">
        <h2 style={{ padding: "1.5rem", paddingBottom: "0" }}>Notifications</h2>

        {/* Pending Friend Requests */}
        <div className="notifications-section notifications-friend-request">
          <FriendRequestList />
        </div>

        {/* Approved Join Requests */}
        <div className="notifications-section notification-list-section">
          <h3>Approved Join Requests</h3>
          <div className="notifications-list">
            {approvedRequests.map((request) => (
              <div key={request.id} className="notification-item">
                <h4>
                  Your request to join Group: {groups[request.groupId]?.groupName || "Unknown"} has
                  been approved.
                </h4>
              </div>
            ))}
          </div>
        </div>

        {/* New Category of the Day Topic */}
        <div className="notifications-section notification-list-section">
          <h3>New Category of the Day</h3>
          <div className="notifications-list">
            <div className="notification-item">
              <h4>Today's category is about '{newTopic}'.</h4>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default Notifications;
>>>>>>> develop
