import React, { useState, useEffect } from "react";
import axios from "axios";
import Header from "../common/Header";
import { useParams } from "react-router-dom";
import { IconX } from "@tabler/icons-react";
import "./Group.css";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const Groups = () => {
  const [member, setMember] = useState("");
  const [selectedGroup, setSelectedGroup] = useState("");
  const [userId, setUserId] = useState("");
  const [showAddPopup, setShowAddPopup] = useState(false);
  const [showRemovePopup, setShowRemovePopup] = useState(false);
  const { groupId } = useParams();
  const [group, setGroup] = useState(null);
  const [members, setMembers] = useState([]);
  const [admin, setAdmin] = useState(null);
  const [isGroupAdmin, setIsGroupAdmin] = useState(false);
  const [joinRequests, setJoinRequests] = useState([]);
  const currentUserId = localStorage.getItem("userId");
  const [isMember, setIsMember] = useState(false);
  const [users, setUsers] = useState({});

  const handleAddMember = async (event) => {
    event.preventDefault();
    axios
      .put(`http://localhost:8085/api/group/addUser`, {
        params: { selectedGroup, member },
      })
      .then((response) => {
        setMember("");
        setSelectedGroup("");
        setUserId("");
        setShowAddPopup(false);
        fetchGroupMembers(); // Update member list after adding a member
        toast.success("User has been succesfully added to your group.");
      })
      .catch((error) => {
        console.error("There was an error adding members!", error);
        toast.error("Error adding members.");
      });
  };

  const handleRemoveMember = async (event) => {
    event.preventDefault();
    axios
      .post(`http://localhost:8085/api/group/deleteUser`, null, {
        params: { groupId, userId },
      })
      .then((response) => {
        setShowRemovePopup(false);
        fetchGroupMembers(); // Update member list after removing a member
        toast.success("User has been succesfully removed from your group.");
      })
      .catch((error) => {
        console.error("There was an error removing members!", error);
        toast.error("Error removing members.");
      });
  };

  // Handler to join the group
  const handleJoinGroup = async () => {
    try {
      await axios.post(`http://localhost:8085/api/join-requests`, {
        userId: currentUserId,
        groupId: groupId,
      });
      toast.success("Join request sent successfully");
      fetchGroupMembers(); // Update member list after sending a join request
    } catch (error) {
      console.error("There was an error sending the join request!", error);
      toast.error("Failed to send join request");
    }
  };

  // Fetch Group Members
  const fetchGroupMembers = () => {
    if (groupId) {
      axios
        .get(`http://localhost:8085/api/group/users/${groupId}`)
        .then((response) => {
          setMembers(response.data);
          setIsMember(
            response.data.some(
              (member) => member.id === parseInt(currentUserId)
            )
          );
        })
        .catch((error) => {
          console.error("Error fetching group members:", error);
          toast.warn("Error fetching group members.");
        });
    }
  };

  // Get Join Requests Users Information
  const retrieveUsers = async (postsData) => {
    // Fetch user details for each request
    const userPromises = postsData.map((post) =>
      axios.get(`http://localhost:8085/api/user/getById/${post.userId}`)
    );
    const userResponses = await Promise.all(userPromises);

    // Create a mapping of user IDs to user data
    const userMap = userResponses.reduce((acc, response) => {
      const user = response.data;
      acc[user.id] = user;
      return acc;
    }, {});
    setUsers(userMap);
  };

  // Fetch join requests
  const fetchJoinRequests = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8085/api/join-requests/${groupId}`
      );
      setJoinRequests(response.data);
      retrieveUsers(response.data);
    } catch (error) {
      console.error("Error fetching join requests:", error);
      toast.warn("Error fetching join requests.");
    }
  };

  // Handle join request approval
  const handleApproveRequest = async (requestId) => {
    try {
      const response = await axios
        .put(`http://localhost:8085/api/join-requests/${requestId}/approve`)
        .then((response) => {
          fetchJoinRequests(); // Update join requests after approval
          fetchGroupMembers();
          toast.success("Request approved successfully");
        })
        .catch((error) => {
          console.log(error.response.data.message);
          toast.warn(error.response.data.message);
        });
    } catch (error) {
      console.error("Error approving request:", error);
      toast.error("Error approving request.");
    }
  };

  // Handle join request rejection
  const handleRejectRequest = async (requestId) => {
    try {
      await axios.put(
        `http://localhost:8085/api/join-requests/${requestId}/reject`
      );
      fetchJoinRequests(); // Update join requests after rejection
      toast.success("Request rejected successfully");
    } catch (error) {
      console.error("Error rejecting request:", error);
      toast.error("Error rejecting request.");
    }
  };

  // Check if the current user is a Group_Admin
  const checkIfGroupAdmin = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8085/api/group/checkAdmin/${groupId}/${currentUserId}`
      );
      setIsGroupAdmin(response.data.isAdmin);
    } catch (error) {
      console.error("Error checking if user is group admin:", error);
    }
  };

  // Get Current User Information
  const fetchAdmin = async (creatorId) => {
    try {
      const response = await axios.get(
        `http://localhost:8085/api/user/getById/${creatorId}`
      );
      setAdmin(response.data);
    } catch (error) {
      console.log("Error fetching admin", error);
      toast.warn("Error fetching admin");
    }
  };

  useEffect(() => {
    if (groupId) {
      axios
        .get(`http://localhost:8085/api/group/get/${groupId}`)
        .then((response) => {
          setGroup(response.data);
          fetchAdmin(response.data.creatorId);
          checkIfGroupAdmin();
        })
        .catch((error) => {
          console.error("Error fetching group:", error);
          toast.warn("Error fetching group");
        });
      fetchGroupMembers();
      if (isGroupAdmin) {
        fetchJoinRequests();
      }
    }
  }, [groupId, isGroupAdmin]);

  if (!group) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <Header />
      <div className="group-profile">
        <div className="group-info">
          <h1>{group.groupName}</h1>
          <p>
            <strong>Faculty: {group.faculty}</strong>
          </p>
          <p>
            <strong>Description: {group.description}</strong>
          </p>
          <p>
            <strong>Admin: </strong>
            {admin ? (
              <strong>{admin.firstName + " " + admin.lastName}</strong>
            ) : (
              "Loading..."
            )}
          </p>
          {isGroupAdmin && (
            <>
              <button
                className="action-button add-button"
                onClick={() => setShowAddPopup(true)}
              >
                Add User
              </button>
              <button
                className="action-button remove-button"
                onClick={() => setShowRemovePopup(true)}
              >
                Remove User
              </button>
            </>
          )}
          {!isMember && (
            <button
              className="action-button join-button"
              onClick={handleJoinGroup}
            >
              Join Group
            </button>
          )}
        </div>
      </div>

      <div className="members-list">
        <h3>Group Members</h3>
        <ul>
          {members.map((member) => (
            <li key={member.id}>{member.firstName + " " + member.lastName}</li>
          ))}
        </ul>
      </div>

      {isGroupAdmin && (
        <div className="join-requests-list">
          <h2 style={{ paddingBottom: "1rem" }}>Join Requests</h2>
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Fist Name</th>
                <th>Last Name</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {joinRequests.map((request) => (
                <tr key={request.id}>
                  <td>{request.id}</td>
                  <td>{users[request.userId]?.firstName || "Unknown"}</td>
                  <td>{users[request.userId]?.lastName || "User"} </td>
                  <td>{request.status}</td>
                  <td>
                    <button
                      onClick={() => handleApproveRequest(request.id)}
                      className="group-btn accept-request-btn"
                    >
                      Approve
                    </button>
                    <button
                      onClick={() => handleRejectRequest(request.id)}
                      className="group-btn reject-request-btn"
                    >
                      Reject
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {showAddPopup && (
        <div className="popup">
          <div className="popup-content">
            <span className="close" onClick={() => setShowAddPopup(false)}>
              <IconX stroke={2} />
            </span>
            <h3>Add User</h3>
            <form onSubmit={handleAddMember}>
              <input
                type="text"
                value={member}
                onChange={(e) => setMember(e.target.value)}
                placeholder="Add Member ID"
                required
              />
              <button type="submit" className="add-button">
                Add Member
              </button>
            </form>
          </div>
        </div>
      )}

      {showRemovePopup && (
        <div className="popup">
          <div className="popup-content">
            <span className="close" onClick={() => setShowRemovePopup(false)}>
              <IconX stroke={2} />
            </span>
            <h3>Remove User</h3>
            <form onSubmit={handleRemoveMember}>
              <input
                type="text"
                value={userId}
                onChange={(e) => setUserId(e.target.value)}
                placeholder="User ID"
                required
              />
              <button type="submit" className="remove-button">
                Remove Member
              </button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default Groups;
