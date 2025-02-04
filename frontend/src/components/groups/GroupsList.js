// src/components/Groups.js
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./UserGroups.css";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const GroupsList = () => {
  const [groups, setGroups] = useState([]);
  const userId = localStorage.getItem("userId");
  const navigate = useNavigate();

  // Effect to fetch all groups depending on the current user
  useEffect(() => {
    axios
      .get(`http://localhost:8085/api/group/groups`)
      .then((response) => {
        console.log(response);
        setGroups(response.data);
      })
      .catch((error) => {
        console.error("There was an error fetching the groups");
        toast.warn("Error fetching groups");
      });
  }, [userId]);

  const viewGroupProfile = (groupId) => {
    navigate(`/groups/${groupId}`);
  };

  return (
    <div className="grouplist-container">
      <div className="grouplist-groupsSection">
        <h1 className="grouplist-groupsHeader">Groups List</h1>
        <ul className="grouplist-groupList">
          {groups.map((group) => (
            <li
              key={group.id}
              className="grouplist-groupListItem"
              onClick={() => viewGroupProfile(group.id)}
              style={{ cursor: "pointer" }}
            >
              {group.groupName}
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default GroupsList;
