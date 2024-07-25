// Admin.js
import React, { useState, useEffect } from "react";
import axios from "axios";
import "./AdminManagement.css";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function AdminManagement() {
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [newRole, setNewRole] = useState("");
  // const [joinRequests, setJoinRequests] = useState([]);
  // const storedUserId = localStorage.getItem("userId");
  // const storedUserEmail = localStorage.getItem("userEmail");
  const storedUserRole = localStorage.getItem("userRole");

  useEffect(() => {
    fetchUsers();
    // fetchJoinRequests();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await axios.get("http://localhost:8085/api/admin/users");
      setUsers(response.data);
    } catch (error) {
      console.error("Error fetching users:", error);
    }
  };

  const toggleUserActivate = async (userId, isActive) => {
    try {
      if (isActive) {
        await axios.put(
          `http://localhost:8085/api/admin/users/deactivate/${userId}`,
          { adminRole: storedUserRole }
        );
        toast.success("User deactivated successfully");
      } else {
        await axios.put(
          `http://localhost:8085/api/admin/users/activate/${userId}`,
          { adminRole: storedUserRole }
        );
        toast.success("User activated successfully");
      }
      fetchUsers();
    } catch (error) {
      console.error("Error toggling user activation:", error);
      toast.warn("An error occurred. Please try again.");
    }
  };

  const deleteUser = async (userId) => {
    try {
      await axios.delete(
        `http://localhost:8085/api/admin/users/remove/${userId}`,
        {
          params: { adminRole: storedUserRole },
        }
      );
      console.log(`User deleted successfully with ID: ${userId}`);
      toast.success("User deleted successfully");
      fetchUsers();
    } catch (error) {
      console.error("Error deleting user:", error);
      toast.warn("An error occurred. Please try again.");
    }
  };

  const handleRoleChange = async () => {
    if (selectedUser && newRole) {
      try {
        await axios.put(
          `http://localhost:8085/api/admin/users/${selectedUser.id}/role`,
          null,
          {
            params: {
              role: newRole,
              adminRole: storedUserRole,
            },
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        toast.success("Role updated successfully");
        fetchUsers();
      } catch (error) {
        console.error("Error updating role:", error);
        toast.warn("An error occurred. Please try again.");
      }
    }
  };

  // const fetchJoinRequests = async () => {
  //   try {
  //     const response = await axios.get(
  //       "http://localhost:8085/api/admin/join-requests"
  //     );
  //     setJoinRequests(response.data);
  //   } catch (error) {
  //     console.error("Error fetching join requests:", error);
  //   }
  // };

  // const handleApproveRequest = async (requestId) => {
  //   try {
  //     await axios.put(
  //       `http://localhost:8085/api/admin/join-requests/${requestId}/approve`
  //     );
  //     toast.success("Request approved successfully");
  //     fetchJoinRequests();
  //   } catch (error) {
  //     console.error("Error approving request:", error);
  //     toast.warn("An error occurred. Please try again.");
  //   }
  // };

  // const handleRejectRequest = async (requestId) => {
  //   try {
  //     await axios.put(
  //       `http://localhost:8085/api/admin/join-requests/${requestId}/reject`
  //     );
  //     toast.success("Request rejected successfully");
  //     fetchJoinRequests();
  //   } catch (error) {
  //     console.error("Error rejecting request:", error);
  //     toast.warn("An error occurred. Please try again.");
  //   }
  // };

  return (
    <div>
      <ToastContainer />
      <div className="admin-interface">
        <h2 style={{ paddingBottom: "1rem" }}>User Management</h2>
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>First Name</th>
              <th>Last Name</th>
              <th>Email</th>
              <th>Role</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id} className={user.isActive ? "" : "deactivated"}>
                <td>{user.id}</td>
                <td>{user.firstName}</td>
                <td>{user.lastName}</td>
                <td>{user.email}</td>
                <td>{user.role}</td>
                <td>
                  <button
                    className="admin-btn admin-change-role"
                    onClick={() => setSelectedUser(user)}
                    disabled={!user.isActive}
                  >
                    Change Role
                  </button>
                  <button
                    className={
                      user.isActive
                        ? "admin-btn admin-deactivate"
                        : "admin-btn admin-activate"
                    }
                    onClick={() => toggleUserActivate(user.id, user.isActive)}
                  >
                    {user.isActive ? "Deactivate User" : "Activate User"}
                  </button>
                  <button
                    className="admin-btn admin-delete"
                    onClick={() => deleteUser(user.id)}
                  >
                    Remove User
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {selectedUser && (
          <div className="role-change-form">
            <h3>
              Change Role for {selectedUser.firstName} {selectedUser.lastName}{" "}
              with ID {selectedUser.id}
            </h3>
            <select
              value={newRole}
              onChange={(e) => setNewRole(e.target.value)}
            >
              <option value="">Select Role</option>
              <option value="Student">Student</option>
              <option value="Professor">Professor</option>
              <option value="Faculty">Faculty</option>
              <option value="System_Admin">System Admin</option>
            </select>
            <button
              className="admin-btn admin-update-role"
              onClick={handleRoleChange}
              disabled={!selectedUser.isActive}
            >
              Update Role
            </button>
          </div>
        )}

        {/* <h2>Join Requests</h2>
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>User ID</th>
              <th>Group ID</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {joinRequests.map((request) => (
              <tr key={request.id}>
                <td>{request.id}</td>
                <td>{request.userId}</td>
                <td>{request.groupId}</td>
                <td>{request.status}</td>
                <td>
                  <button onClick={() => handleApproveRequest(request.id)}>
                    Approve
                  </button>
                  <button onClick={() => handleRejectRequest(request.id)}>
                    Reject
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table> */}
      </div>
    </div>
  );
};

export default AdminManagement;