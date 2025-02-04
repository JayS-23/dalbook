import React, { useState } from "react";
import Header from "../common/Header";
import COTDPost from "../feed/COTDPost.js";
import "./Pages.css";

function Pages() {
  const [activeComponent, setActiveComponent] = useState("CategoryOfTheDay");

  const renderComponent = () => {
    switch (activeComponent) {
      default:
        return <COTDPost />;
    }
  };

  return (
    <div>
      <Header />
      <div className="pages-dashboard">
        <div className="pages-layout">
          <div className="pages-sidebar">
            <div className="pages-title">
              <h2>Pages Dashboard</h2>
            </div>
            <button
              className={`pages-button ${activeComponent === 'CategoryOfTheDay' ? 'active' : ''}`}
              onClick={() => setActiveComponent("CategoryOfTheDay")}
            >
              Category for the Day Posts
            </button>
          </div>
          <div className="pages-content">{renderComponent()}</div>
        </div>
      </div>
    </div>
  );
};

export default Pages;
