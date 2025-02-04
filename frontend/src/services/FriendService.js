import axios from "axios";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const API_URL = "http://localhost:8085/api/friends";

const sendFriendRequest = async (senderId, receiverEmail) => {
  try {
    const response = await axios.post(`${API_URL}/send`, null, {
      params: { senderId, receiverEmail },
    });
    console.log("Friend request sent successfully:", response.data);
    return response.data;
  } catch (error) {
    if (error.response) {
      const { timestamp, message, details } = error.response.data;
      console.error("Error details:", timestamp, message, details);

      // Handle different error scenarios based on `message`
      if (message === "Friend request already sent") {
        toast.warn(message);
      } else if (message === "Already friends") {
        toast.warn(message);
      } else {
        toast.warn(message);
      }
    } else {
      // Handle cases where error.response is undefined
      console.error("Error:", error.message);
      toast.warn("Failed to send friend request due to a network error");
    }
  }
};

const acceptFriendRequest = (requestId) => {
  return axios.post(`${API_URL}/accept`, null, { params: { requestId } });
};

const getPendingRequests = (userId) => {
  return axios.get(`${API_URL}/pending`, { params: { userId } });
};

const getFriends = (userId) => {
  return axios.get(`${API_URL}/list`, { params: { userId } });
};

const deleteFriend = (userId, friendId) => {
  return axios.delete(`${API_URL}/delete`, {
    params: { userId, friendId },
  });
};

// Assign to a variable before exporting
const FriendService = {
  sendFriendRequest,
  acceptFriendRequest,
  getPendingRequests,
  getFriends,
  deleteFriend
};

export default FriendService;
