// GroupDashboard.js
import { React, useState } from "react";
import "./GroupDashboard.css";
import Header from "../common/Header";
import GroupsList from "./GroupsList";
import CreateGroup from "./CreateGroup";
import UserGroups from "./UserGroups";

const GroupDashboard = () => {
  const [activeComponent, setActiveComponent] = useState("userGroups");

  const renderComponent = () => {
    switch (activeComponent) {
      case "createGroup":
        return <CreateGroup />;
      case "groupsList":
        return <GroupsList />;
      default:
        return <UserGroups />;
    }
  };

  return (
    <div>
      <Header />
      <div className="groups-dashboard">
        <div className="groups-layout">
          <div className="groups-sidebar">
            <div className="groups-title">
              <h2>Groups</h2>
            </div>
            <button
              className={`groups-button ${
                activeComponent === "userGroups" ? "active" : ""
              }`}
              onClick={() => setActiveComponent("userGroups")}
            >
              Your Groups
            </button>
            <button
              className={`groups-button ${
                activeComponent === "createGroup" ? "active" : ""
              }`}
              onClick={() => setActiveComponent("createGroup")}
            >
              Create a Group
            </button>
            <button
              className={`groups-button ${
                activeComponent === "groupsList" ? "active" : ""
              }`}
              onClick={() => setActiveComponent("groupsList")}
            >
              Group List
            </button>
          </div>
          <div className="admin-content">{renderComponent()}</div>
        </div>
      </div>
    </div>
  );
};

export default GroupDashboard;
