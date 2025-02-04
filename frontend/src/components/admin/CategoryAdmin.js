import { React, useState, useEffect } from "react";
import axios from "axios";
import "../feed/CategoryOfTheDay.css";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function CategoryAdmin() {
  const [topic, setTopic] = useState("");
  const [topicList, setTopicList] = useState([]);
  const [showForm, setShowForm] = useState(false);

  useEffect(() => {
    fetchTopics();
  }, []);

  const fetchTopics = async () => {
    try {
      const response = await axios.get("http://localhost:8085/api/topics");
      setTopicList(response.data);
    } catch (error) {
      console.error("Error fetching categories:", error);
      toast.warn(
        "An error occurred while fetching categories. Please try again!"
      );
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const newTopic = { topic };
    try {
      const response = await axios.post(
        "http://localhost:8085/api/topics",
        newTopic
      );
      console.log("Topic created:", response.data);
      toast.success("Topic created successfully!");
      setShowForm(false);
      fetchTopics(); // Fetch the updated list of categories
    } catch (error) {
      console.error("Error creating topic:", error);
      toast.error("An error occurred. Please try again!");
    }
  };

  return (
    <div className="category-container">
      <h2 className="category-title">
        Topics
        <button
          className="category-add-button"
          onClick={() => setShowForm(true)}
        >
          Add
        </button>
      </h2>
      <div className="categories-list">
        {topicList.map((topic) => (
          <div key={topic.id} className="category-item">
            {topic.topic}
          </div>
        ))}
      </div>
      {showForm && (
        <div className="form-popup">
          <div className="form-container">
            <span className="close-icon" onClick={() => setShowForm(false)}>
              &times;
            </span>
            <form className="category-form" onSubmit={handleSubmit}>
              <div>
                <label className="category-label">
                  <strong>Topic:</strong>
                </label>
                <input
                  className="category-input"
                  type="text"
                  value={topic}
                  onChange={(e) => setTopic(e.target.value)}
                  placeholder="Enter the topic of the day"
                />
              </div>
              <button className="category-button" type="submit">
                Submit
              </button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default CategoryAdmin;
