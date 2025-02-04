import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./Feed.css";
import {
  IconUsers,
  IconCalendarStar,
  IconFlag,
  IconChevronDown,
  IconUser,
  IconSearch,
  IconDots,
  IconPhoto,
  IconMoodSmile,
  IconThumbUp,
  IconMessageCircle,
  IconShare3,
  IconCircleCheckFilled,
  IconClockFilled,
  IconSquareMinusFilled,
  IconCircleXFilled,
  IconUsersGroup,
} from "@tabler/icons-react";
import friendService from "../../services/FriendService";
<<<<<<< HEAD
=======
import Post from "./Post";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
>>>>>>> develop

function Feed() {
  const [friends, setFriends] = useState([]);
  const navigate = useNavigate();
  const [userId, setUserId] = useState(null);
  const [user, setUser] = useState();

  const userProfile = () => {
    navigate("/profile");
  };

  const friendsPage = () => {
    navigate("/friends");
  };

  const groupsPage = () => {
    navigate("/groupDashboard");
  };

<<<<<<< HEAD
=======
  const categoryOfDayPage = () => {
    navigate("/categoryOftheDay");
  };

  const pages = () => {
    navigate("/pages");
  };

>>>>>>> develop
  useEffect(() => {
    const storedUserId = localStorage.getItem("userId");
    const storedUserEmail = localStorage.getItem("userEmail");
    if (storedUserId && storedUserEmail) {
      setUserId(storedUserId);

      // Get Current User Information
      const fetchUser = async () => {
        try {
          const response = await axios.get(
            `http://localhost:8085/api/user/get/${storedUserEmail}`
          );
          setUser(response.data);
<<<<<<< HEAD
          console.log("User information retrieved successfully");
=======
>>>>>>> develop
        } catch (error) {
          console.log("Error fetching user", error);
          toast.warn("Error fetching user.");
        }
      };
      fetchUser();

      // Get Current User Friends
      friendService
        .getFriends(storedUserId)
        .then((response) => {
          setFriends(response.data);
        })
        .catch((error) => {
          console.error("Error fetching friends:", error);
          toast.warn("An error occurred. Please try again!");
        });
    }
<<<<<<< HEAD
=======

    const retrieveUsers = async (postsData) => {
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
      setUsers(userMap);
    };

    const fetchPosts = async () => {
      try {
        const response = await axios.get("http://localhost:8085/api/posts");
        const posts = response.data;

        // Sort posts from latest to oldest based on a `timestamp` field
        posts.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));

        retrieveUsers(posts); // Assuming retrieveUsers is used to process posts
        console.log(posts);
        setPosts(posts);
        setIsLoading(false);
      } catch (error) {
        console.error("Error fetching posts:", error);
        toast.warn("Error fetching posts.");
        setIsLoading(false);
      }
    };

    fetchPosts();
>>>>>>> develop
  }, [userId]);

  const getStatusIcon = (status) => {
    switch (status) {
      case "Available":
        return (
          <IconCircleCheckFilled color="green" style={{ paddingLeft: "5px" }} />
        );
      case "Away":
        return (
          <IconClockFilled color="orange" style={{ paddingLeft: "5px" }} />
        );
      case "Busy":
        return (
          <IconSquareMinusFilled color="red" style={{ paddingLeft: "5px" }} />
        );
      case "Offline":
        return (
          <IconCircleXFilled color="gray" style={{ paddingLeft: "5px" }} />
        );
      default:
        return null;
    }
  };

  const viewFriendProfile = (friendEmail) => {
    navigate(`/friendProfile/${friendEmail}`);
  };

  return (
    <div className="main">
      <div className="left-side">
        <div
          className="profile"
          onClick={userProfile}
          style={{ cursor: "pointer" }}
        >
          <img
<<<<<<< HEAD
            src="/images/avatar-1.jpeg"
=======
            src={
              user && user.profilePic
                ? `http://localhost:8085${user.profilePic}`
                : "/images/dalhousie-logo.png"
            }
>>>>>>> develop
            alt=""
            style={{ padding: "1rem" }}
          />
          {user ? (
            <div className="user-name">
              {user.firstName + " " + user.lastName}
              {getStatusIcon(user.status)}
            </div>
          ) : (
            "Loading..."
          )}
        </div>
        <div
          className="panel"
          onClick={friendsPage}
          style={{ cursor: "pointer" }}
        >
          <IconUsers stroke={2} />
          <div>Friends</div>
        </div>
        <div
          className="panel"
          onClick={groupsPage}
          style={{ cursor: "pointer" }}
        >
          <IconUsersGroup stroke={2} />
          <div>Groups</div>
        </div>
        <div className="panel">
          <IconCalendarStar stroke={2} />
          <div>Events</div>
        </div>
        <div className="panel" onClick={pages}>
          <IconFlag stroke={2} />
          <div>Pages</div>
        </div>
        <div className="panel">
          <IconChevronDown stroke={2} />
          <div>See More</div>
        </div>
        <div className="border"></div>
        <br />
        <div className="pages">
          <img
            src="/images/dalhousie-logo.png"
            alt="logo"
            style={{ padding: "1rem" }}
          />
          Dalhousie University
        </div>
        <div className="pages">
          <img
            src="/images/dalhousie-tigers-logo.png"
            alt="logo"
            style={{ padding: "1rem" }}
          />
          Dalhousie University Football
        </div>
        <div className="pages">
          <img src="/images/pulse.png" alt="logo" style={{ padding: "1rem" }} />
          Brigthspace Pulse
        </div>
        <div className="dropdown-panels">
          <IconChevronDown stroke={2} className="chevron" />
          <div>See More</div>
        </div>
      </div>
      <div className="timeline">
        {/* <div className="addStory">
          <div className="story">
            <img
              src="/images/avatar-2.jpeg"
              alt="logo"
              style={{ height: "50px", borderRadius: "50%" }}
            />
            <br />
            John Doe
          </div>
          <div className="story">
            <img
              src="/images/avatar-3.jpeg"
              alt="logo"
              style={{ height: "50px", borderRadius: "50%" }}
            />
            <br />
            John Doe
          </div>
          <div className="story">
            <img
              src="/images/avatar-3.jpeg"
              alt="logo"
              style={{ height: "50px", borderRadius: "50%" }}
            />
            <br />
            John Doe
          </div>
          <div className="story">
            <img
              src="/images/avatar-4.jpeg"
              alt="logo"
              style={{ height: "50px", borderRadius: "50%" }}
            />
            <br />
            John Doe
          </div>
        </div> */}
        <div className="create-post">
          <div className="text">
            <div className="user-post">
              <img
<<<<<<< HEAD
                src="/images/avatar-1.jpeg"
=======
                src={
                  user && user.profilePic
                    ? `http://localhost:8085${user.profilePic}`
                    : "/images/dalhousie-logo.png"
                }
>>>>>>> develop
                alt=""
                style={{ height: "50px", padding: "1rem" }}
              />
              <input type="Mind" placeholder="What's on your mind, John?" />
            </div>
            {/* <div className="border-post"></div> */}
            <div className="post-icons">
              <div className="icon">
                <div className="post-icon">
                  <IconPhoto stroke={2} color="green" />
                  <div>Photo/Video</div>
                </div>
                <div className="post-icon">
                  <IconMoodSmile stroke={2} color="orange" />
                  <div>Feeling/Activity</div>
                </div>
              </div>
            </div>
          </div>
<<<<<<< HEAD
=======
          {showPostPopup && (
            <div className="post-popup">
              <div className="post-popup-content">
                <Post />
                <button
                  className="post-popup-close"
                  onClick={() => setShowPostPopup(false)}
                >
                  Close
                </button>
              </div>
            </div>
          )}
          <div>
            {isLoading
              ? "loading"
              : posts.map((post) => (
                  <div className="posted" key={post.postId}>
                    <div className="post">
                      <div className="feed-profile-picture">
                        <img
                          src={
                            users[post.userId]?.profilePic
                              ? `http://localhost:8085${
                                  users[post.userId]?.profilePic
                                }`
                              : "/images/dalhousie-logo.png"
                          }
                          alt=""
                          style={{ height: "50px", padding: "1rem" }}
                        />
                        <div className="users-name">
                          {users[post.userId]?.firstName || "Unknown"}{" "}
                          {users[post.userId]?.lastName || "User"}
                        </div>
                      </div>
                      <div className="edit">
                        <IconDots stroke={2} />
                      </div>
                    </div>
                    <div className="caption">{post.description}</div>
                    {post.mediaUrl && <img src={post.mediaUrl} alt="post" />}
                    <p>{post.feeling}</p>
                    <div className="reactions">
                      <div className="like">
                        <IconThumbUp stroke={2} />
                        Like
                      </div>
                      <div className="comment">
                        <IconMessageCircle stroke={2} />
                        Comment
                      </div>
                      <div className="share">
                        <IconShare3 stroke={2} />
                        Share
                      </div>
                    </div>
                  </div>
                ))}
          </div>
>>>>>>> develop
          <div className="posted">
            <div className="post">
              <div className="feed-profile-picture">
                <img
                  src="/images/avatar-1.jpeg"
                  alt=""
                  style={{ height: "50px" }}
                ></img>
                <div>John Doe</div>
                <div className="update">Updated his cover image.</div>
              </div>
              <div className="edit">
                <IconDots stroke={2} />
              </div>
            </div>
            <div className="caption">Feeling good today!</div>
            <br />
            <div className="media">
              <img
                src="/images/post.jpg"
                alt=""
                style={{ height: "auto", width: "100%" }}
              />
            </div>
            <div className="reactions">
              <div className="like">
                <IconThumbUp stroke={2} />
                Like
              </div>
              <div className="comment">
                <IconMessageCircle stroke={2} />
                Comment
              </div>
              <div className="share">
                <IconShare3 stroke={2} />
                Share
              </div>
            </div>
          </div>
          <div className="posted">
            <div className="post">
              <div className="feed-profile-picture">
                <img
                  src="/images/avatar-1.jpeg"
                  alt=""
                  style={{ height: "50px" }}
                ></img>
                <div>John Doe</div>
                <div className="update">Updated his cover image.</div>
              </div>
              <div className="edit">
                <IconDots stroke={2} />
              </div>
            </div>
            <div className="caption">Feeling good today!</div>
            <br />
            <div className="media">
              <img
                src="/images/post.jpg"
                alt=""
                style={{ height: "auto", width: "100%" }}
              />
            </div>
            <div className="reactions">
              <div className="like">
                <IconThumbUp stroke={2} />
                Like
              </div>
              <div className="comment">
                <IconMessageCircle stroke={2} />
                Comment
              </div>
              <div className="share">
                <IconShare3 stroke={2} />
                Share
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="right-side">
        <div className="contact">
          <div className="contacts">Friends</div>
          <div className="chat-icon">
            <div className="icons">
              <IconUser stroke={2} />
            </div>
            <div className="icons">
              <IconSearch stroke={2} />
            </div>
            <div className="icons">
              <IconDots stroke={2} />
            </div>
          </div>
        </div>
        <div className="concise">
          {friends.map((friend) => (
            <div
              key={friend.id}
              className="profiles name"
              onClick={() => viewFriendProfile(friend.email)}
              style={{ cursor: "pointer" }}
            >
              <img
<<<<<<< HEAD
                src="/images/avatar-2.jpeg"
=======
                src={
                  friend.profilePic
                    ? `http://localhost:8085${friend.profilePic}`
                    : "/images/dalhousie-logo.png"
                }
>>>>>>> develop
                alt=""
                style={{ padding: "1rem" }}
              />
              {friend.firstName + " " + friend.lastName}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Feed;
