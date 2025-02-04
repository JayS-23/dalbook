import React, { useState, useEffect } from "react";
import axios from "axios";
import "./COTDPost.css";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const COTDPost = () => {
  const [posts, setPosts] = useState([]);
  const [users, setUsers] = useState({});
  const topic = localStorage.getItem("currentTopic");

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        // Fetch posts based on the topic
        const postsResponse = await axios.get(
          "http://localhost:8085/api/category/getPosts",
          { params: { topic } }
        );
        const postsData = postsResponse.data;
        console.log(postsData);
        setPosts(postsData);

        // Fetch user details for each post
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
        console.log(userMap);
        setUsers(userMap);
      } catch (error) {
        console.error("Error fetching posts:", error);
        toast.warn("Error fetching posts.");
      }
    };

    if (topic) {
      fetchPosts();
    }
  }, [topic]);

  if (!topic) {
    return (
      <div className="no-posts-container">
        You have to post on CategoryOfTheDay first before seeing any posts
        here...
      </div>
    );
  }

  return (
    <div className="cotd-posts-container">
      <h3 style={{ textAlign: "center", paddingBottom: "20px" }}>
        Posts about '{topic}'
      </h3>
      {posts.length > 0 ? (
        posts.map((post) => (
          <div key={post.id} className="cotd-post-item">
            <h4>
              {users[post.userId]?.firstName || "Unknown"}{" "}
              {users[post.userId]?.lastName || "User"}
            </h4>
            <p>{post.content}</p>
          </div>
        ))
      ) : (
        <div className="no-posts-message">
          No posts available for this topic yet.
        </div>
      )}
    </div>
  );
};

export default COTDPost;
