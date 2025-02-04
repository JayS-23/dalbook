<<<<<<< HEAD
// Admin.js
// Admin.js
=======
import React, { useState } from "react";
import "./Admin.css";
import AdminManagement from "./AdminManagement";
import CategoryAdmin from "./CategoryAdmin";
import Header from "../common/Header";

function Admin() {
  const [activeComponent, setActiveComponent] = useState("adminManagement");

  const renderComponent = () => {
    switch (activeComponent) {
      case "adminManagement":
        return <AdminManagement />;
      case "categoryAdmin":
        return <CategoryAdmin />;
      default:
        return <AdminManagement />;
    }
  };

  return (
    <div>
      <Header />
      <div className="admin-dashboard">
        <div className="admin-layout">
          <div className="admin-sidebar">
            <div className="admin-title">
              <h2>Admin Dashboard</h2>
            </div>
            <button
              className={`admin-button ${activeComponent === 'adminManagement' ? 'active' : ''}`}
              onClick={() => setActiveComponent("adminManagement")}
            >
              Admin Management
            </button>
            <button
              className={`admin-button ${activeComponent === 'categoryAdmin' ? 'active' : ''}`}
              onClick={() => setActiveComponent("categoryAdmin")}
            >
              Create Category for the Day
            </button>
          </div>
          <div className="admin-content">{renderComponent()}</div>
        </div>
      </div>
    </div>
  );
};

export default Admin;
>>>>>>> develop
