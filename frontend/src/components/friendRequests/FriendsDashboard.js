// FriendsDashboard.js
import { React, useState } from "react";
import "./FriendsDashboard.css";
import Header from "../common/Header";
import FriendsList from "./FriendsList";
import FriendRequest from "./FriendRequest";
import FriendRequestList from "./FriendRequestList";

const FriendsDashboard = () => {
    const [activeComponent, setActiveComponent] = useState("friendsList");

    const renderComponent = () => {
      switch (activeComponent) {
        case "friendRequest":
          return <FriendRequest />;
        case "friendRequestList":
          return <FriendRequestList />;
        default:
          return <FriendsList />;
      }
    };

  return (
    <div>
      <Header />
      <div className="friends-dashboard">
        <div className="friends-layout">
          <div className="friends-sidebar">
            <div className="friends-title">
              <h2>Friends</h2>
            </div>
            <button
              className={`friends-button ${activeComponent === 'friendsList' ? 'active' : ''}`}
              onClick={() => setActiveComponent("friendsList")}
            >
              Your Friends
            </button>
            <button
              className={`friends-button ${activeComponent === 'friendRequest' ? 'active' : ''}`}
              onClick={() => setActiveComponent("friendRequest")}
            >
              Add Friends
            </button>
            <button
              className={`friends-button ${activeComponent === 'friendRequestList' ? 'active' : ''}`}
              onClick={() => setActiveComponent("friendRequestList")}
            >
              Friend Request List
            </button>
          </div>
          <div className="admin-content">{renderComponent()}</div>
        </div>
     </div>
    </div>
  );
};

export default FriendsDashboard;
