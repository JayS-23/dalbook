// src/components/Groups.js
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./UserGroups.css";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const UserGroups = () => {
  const [groups, setGroups] = useState([]);
  const userId = localStorage.getItem("userId");
  const navigate = useNavigate();

  // Effect to fetch all groups depending on the current user
  useEffect(() => {
    axios
      .get(`http://localhost:8085/api/user/groups/${userId}`)
      .then((response) => {
        console.log(response);
        setGroups(response.data);
      })
      .catch((error) => {
        console.error("There was an error fetching the groups");
        toast.warn("Error fetching groups");
      });
  }, [userId]);

  const leaveGroup = async (event, groupId) => {
    event.stopPropagation(); // Prevent event propagation
    try {
      axios.post(`http://localhost:8085/api/group/leaveGroup`, null, {
        params: { groupId, userId },
      });
      setGroups(groups.filter((group) => group.id !== groupId)); // Update the groups state to remove the left group
      toast.success("Successfully leaving the group.");
    } catch (error) {
      console.error("Error leaving group:", error);
      toast.error("Error leaving group.");
    }
  };

  const viewGroupProfile = (groupId) => {
    navigate(`/groups/${groupId}`);
  };

  return (
    <div className="grouplist-container">
      <div className="grouplist-groupsSection">
        <h1 className="grouplist-groupsHeader">Your Groups</h1>
        <ul className="grouplist-groupList">
          {groups.map((group) => (
            <li
              key={group.id}
              className="grouplist-groupListItem"
              onClick={() => viewGroupProfile(group.id)}
              style={{ cursor: "pointer" }}
            >
              {group.groupName}
              <button
                onClick={(event) => leaveGroup(event, group.id)}
                className="grouplist-removeButton"
              >
                Leave
              </button>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default UserGroups;
