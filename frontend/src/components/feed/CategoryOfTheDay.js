// CategoryOfTheDay.js
<<<<<<< HEAD
=======
import React, { useState, useEffect } from "react";
import axios from "axios";
import "./CategoryOfTheDay.css";
import Header from "../common/Header";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function CategoryOfTheDay() {
  const [content, setContent] = useState("");
  const [topic, setTopic] = useState("");

  useEffect(() => {
    const fetchTopic = async () => {
      try {
        const response = await axios.get(
          "http://localhost:8085/api/topics/latest"
        );
        setTopic(response.data.topic);
        localStorage.setItem("currentTopic", response.data.topic);
      } catch (error) {
        console.error("Error fetching topic:", error);
        toast.warn("Error fetching topic.");
      }
    };

    fetchTopic();
  }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();
    const userId = localStorage.getItem("userId");
    const post = { userId, topic, content };

    try {
      const response = await axios.post(
        "http://localhost:8085/api/category/posts",
        post
      );
      console.log("Post created:", response.data);
      toast.success("Post created successfully!");
    } catch (error) {
      console.error("Error creating post:", error);
      toast.error("An error occurred. Please try again!");
    }
  };

  return (
    <div>
      <Header />
      <form className="category-form" onSubmit={handleSubmit}>
        <div>
          <label className="category-label">Topic:</label>
          <h2 className="category-topic">
            Today's category is about '{topic}'. Tell us what you think.
          </h2>
        </div>
        <div>
          <label className="category-label" style={{ paddingTop: "20px" }}>
            Post Content:
          </label>
          <textarea
            className="category-textarea"
            value={content}
            placeholder="Post your moment here..."
            onChange={(e) => setContent(e.target.value)}
            rows="10"
            required
          ></textarea>
        </div>
        <button className="category-button" type="submit">
          Submit
        </button>
      </form>
    </div>
  );
}

export default CategoryOfTheDay;
>>>>>>> develop
