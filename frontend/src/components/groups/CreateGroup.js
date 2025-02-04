// CreateGroup.js

import React, { useState } from "react";
import axios from "axios";
import "./CreateGroup.css";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const CreateGroup = () => {
  // State variables for group creation form inputs
  const [groupName, setName] = useState("");
  const [faculty, setFaculty] = useState("");
  const [description, setDescription] = useState("");

  // Function to handle the creation of a new group
  const handleCreateGroup = async (event) => {
    event.preventDefault();
    const creatorId = localStorage.getItem("userId");

    const newGroup = { faculty, description, groupName, creatorId }; // Create a new group object

    await axios
      .post("http://localhost:8085/api/group/createGroup", newGroup)
      .then((response) => {
        // Clear the form inputs
        setName("");
        setFaculty("");
        setDescription("");
        toast.success("Congrats! You created your group successfully.");
      })
      .catch((error) => {
        console.error("There was an error creating the group!", error);
        toast.error("Error creating group.");
      });
  };

  return (
    <div className="create-group-container">
      <h2 className="create-group-header">Create your own Group</h2>
      <form onSubmit={handleCreateGroup} className="create-group-form">
        <input
          type="text"
          value={groupName}
          onChange={(e) => setName(e.target.value)}
          placeholder="Group Name"
          required
          className="create-group-input"
        />
        <input
          type="text"
          value={faculty}
          onChange={(e) => setFaculty(e.target.value)}
          placeholder="Faculty"
          required
          className="create-group-input"
        />
        <input
          type="text"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="Description"
          required
          className="create-group-input"
        />
        <button type="submit" className="create-group-button">
          Create Group
        </button>
      </form>
    </div>
  );
};

export default CreateGroup;
