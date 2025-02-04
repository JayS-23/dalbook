// Home Page
import React from "react";
import "./Header.css";
import { useNavigate } from "react-router-dom";
<<<<<<< HEAD
=======
import friendService from "../../services/FriendService";
import axios from "axios";
>>>>>>> develop
import {
  IconSearch,
  IconHome,
  IconUsersGroup,
  IconMenu2,
  IconMessages,
  IconBell,
  IconLogout,
  IconUsers,
} from "@tabler/icons-react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function Header() {
  const navigate = useNavigate();
<<<<<<< HEAD
=======
  const [isAdmin, setIsAdmin] = useState(false);
  const [pendingRequests, setPendingRequests] = useState(0);
  const [joinRequestsApproved, setJoinRequestsApproved] = useState(0);
  const [newCategoryTopic, setNewCategoryTopic] = useState(false);

  useEffect(() => {
    const userRole = localStorage.getItem("userRole");
    setIsAdmin(userRole === "System_Admin");
  }, []);

  useEffect(() => {
    const storedUserId = localStorage.getItem("userId");
    if (storedUserId) {
      // Fetch pending friend requests
      friendService
        .getPendingRequests(storedUserId)
        .then((response) => {
          setPendingRequests(response.data.length);
        })
        .catch((error) => {
          console.error("Error fetching pending requests:", error);
          toast.warn("Error fetching pending requests.");
        });

      // Fetch approved join requests
      axios
        .get(`http://localhost:8085/api/join-requests/approved`, {
          params: { userId: storedUserId },
        })
        .then((response) => {
          setJoinRequestsApproved(response.data.length);
        })
        .catch((error) => {
          console.error("Error fetching approved join requests:", error);
          toast.warn("Error fetching approved join requests.");
        });

      // Fetch new category topic
      const latestTopicId = localStorage.getItem("latestTopicId");
      const lastViewedTopicId = localStorage.getItem("lastViewedTopicId");

      if (
        latestTopicId &&
        lastViewedTopicId &&
        latestTopicId !== lastViewedTopicId
      ) {
        setNewCategoryTopic(true);
      } else {
        axios
          .get("http://localhost:8085/api/topics/latest")
          .then((response) => {
            const fetchedTopicId = response.data.id;
            localStorage.setItem("latestTopicId", fetchedTopicId);

            if (fetchedTopicId !== lastViewedTopicId) {
              setNewCategoryTopic(true);
            }
          })
          .catch((error) => {
            console.error("Error fetching latest topic:", error);
            toast.warn("Error fetching latest topic.");
          });
      }
    }
  }, []);
>>>>>>> develop

  const homePage = () => {
    navigate("/home");
  };

  const handleLogout = () => {
    localStorage.removeItem("isLoggedIn");
    localStorage.removeItem("userId");
    localStorage.removeItem("userEmail");
<<<<<<< HEAD
    navigate("/login");
  };

  const handleFriendRequests = () => {
    navigate("/FriendRequest");
  };

  const handleFriendRequestsList = () => {
    navigate("/FriendRequestList");
=======
    localStorage.removeItem("userRole");
    localStorage.removeItem("latestTopicId");
    localStorage.removeItem("currentTopic");
    localStorage.removeItem("lastViewedTopicId");
    navigate("/login");
  };

  const notificationsPage = () => {
    navigate("/notifications");
>>>>>>> develop
  };

  const friendsPage = () => {
    navigate("/friends");
  };

<<<<<<< HEAD
  return (
    <div className="homepage">
      <div className="header">
        <div className="search-header">
          <div className="logo">
            <img
              src="/images/dalbook_logo.png"
              alt="logo"
              style={{
                height: "40px",
              }}
            />
          </div>
          <div className="search-bar">
            <IconSearch stroke={2} />
            <input placeholder="Search Facebook" type="Search" />
          </div>
        </div>
        <div className="navigation-header">
          <div
            className="icon"
            onClick={homePage}
            style={{ cursor: "pointer" }}
          >
            <IconHome stroke={2} size={30} color="#1877F2" />
          </div>
          {/* <div className="icon">
            <IconBuildingStore stroke={2} size={30} color="#1877F2" />
          </div> */}
          <div className="icon" onClick={friendsPage}>
            <IconUsers stroke={2} size={30} color="#1877F2" />
          </div>
          <div className="icon" onClick={handleFriendRequests}>
            <IconUsersGroup stroke={2} size={30} color="#1877F2" />
          </div>
        </div>
        <div className="account-header">
          <div className="menu">
            <IconMenu2 stroke={2} size={30} color="#1877F2" />
          </div>
          <div className="messages">
            <IconMessages stroke={2} size={30} color="#1877F2" />
          </div>
          <div className="notifications" onClick={handleFriendRequestsList}>
            <IconBell stroke={2} size={30} color="#1877F2" />
=======
  const adminPage = () => {
    navigate("/admin");
  };

  const groupsPage = () => {
    navigate("/groupDashboard");
  };

  const hasNotifications =
    pendingRequests > 0 || joinRequestsApproved > 0 || newCategoryTopic;

  return (
    <>
      <ToastContainer />
      <div className="homepage">
        <div className="header">
          <div className="main-header">
            <div className="logo">
              <img
                src="/images/dalbook_logo.png"
                alt="logo"
                style={{
                  height: "40px",
                  cursor: "auto",
                }}
              />
            </div>
            <div className="search-bar">
              <IconSearch stroke={2} />
              <input placeholder="Search Facebook" type="Search" />
            </div>
>>>>>>> develop
          </div>
          <div className="navigation-header">
            <div
              className="icon"
              onClick={homePage}
              style={{ cursor: "pointer" }}
            >
              <IconHome stroke={2} size={30} color="#1877F2" />
            </div>
            <div className="icon" onClick={friendsPage}>
              <IconUsers stroke={2} size={30} color="#1877F2" />
            </div>
            <div className="icon" onClick={groupsPage}>
              <IconUsersGroup stroke={2} size={30} color="#1877f2" />
            </div>
          </div>
          <div className="account-header">
            {isAdmin && (
              <div className="menu" onClick={adminPage}>
                <IconMenu2 stroke={2} size={30} color="#1877F2" />
              </div>
            )}
            <div className="messages">
              <IconMessages stroke={2} size={30} color="#1877F2" />
            </div>
            <div
              className="notifications"
              onClick={notificationsPage}
              style={{ position: "relative" }}
            >
              <IconBell stroke={2} size={30} color="#1877f2" />
              {hasNotifications && (
                <span
                  style={{
                    position: "absolute",
                    top: 0,
                    right: 0,
                    width: "10px",
                    height: "10px",
                    backgroundColor: "red",
                    borderRadius: "50%",
                  }}
                ></span>
              )}
            </div>
            <div
              className="profile-header"
              onClick={handleLogout}
              style={{ cursor: "pointer" }}
            >
              <IconLogout stroke={2} size={30} color="#1877F2" />
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default Header;
