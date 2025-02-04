import { React, useEffect } from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import "./css/App.css";
import ForgotPassword from "./components/password/ForgotPassword";
import ResetPassword from "./components/password/ResetPassword";
import Login from "./components/authentication/Login";
import Signup from "./components/authentication/Signup";
import Home from "./components/common/Home";
import Error from "./components/common/Error";
import EditProfile from "./components/feed/EditProfile";
import Profile from "./components/feed/Profile";
import FriendRequest from "./components/friendRequests/FriendRequest";
import FriendRequestList from "./components/friendRequests/FriendRequestList";
import FriendsList from "./components/friendRequests/FriendsList";
import PrivateRoute from "./components/common/PrivateRoute";
<<<<<<< HEAD
=======
import Post from "./components/feed/Post";
import Admin from "./components/admin/Admin";
import AdminRoute from "./components/common/AdminRoute";
import CategoryOfTheDay from "./components/feed/CategoryOfTheDay";
import CategoryAdmin from "./components/admin/CategoryAdmin";
import AdminManagement from "./components/admin/AdminManagement";
import FriendProfile from "./components/feed/FriendProfile";
import FriendsDashboard from "./components/friendRequests/FriendsDashboard";
import Notifications from "./components/notifications/Notifications";
import Pages from "./components/feed/Pages";
import Groups from "./components/groups/Groups";
import GroupDashboard from "./components/groups/GroupDashboard";
import UserGroups from "./components/groups/UserGroups";
import CreateGroup from "./components/groups/CreateGroup";
import GroupsList from "./components/groups/GroupsList";
>>>>>>> develop

function App() {
  useEffect(() => {
    document.title = "DalBook";
  }, []);

  return (
    <BrowserRouter>
      <Routes>
        {/* Common paths user can access before login/signup */}
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/forgotPassword" element={<ForgotPassword />} />
        <Route path="/resetPassword" element={<ResetPassword />} />

        {/* Restricted paths that user can access after login/signup */}
        <Route
          path="/home"
          element={
            <PrivateRoute>
              <Home />
            </PrivateRoute>
          }
        />
        <Route
          path="/pages"
          element={
            <PrivateRoute>
              <Pages />
            </PrivateRoute>
          }
        />
        <Route
          path="/profile"
          element={
            <PrivateRoute>
              <Profile />
            </PrivateRoute>
          }
        />
        <Route
          path="/editProfile"
          element={
            <PrivateRoute>
              <EditProfile />
            </PrivateRoute>
          }
        />
        <Route
          path="/notifications"
          element={
            <PrivateRoute>
              <Notifications />
            </PrivateRoute>
          }
        />
        <Route
          path="/post"
          element={
            <PrivateRoute>
              <Post />
            </PrivateRoute>
          }
        />
        <Route
          path="/categoryOfTheDay"
          element={
            <PrivateRoute>
              <CategoryOfTheDay />
            </PrivateRoute>
          }
        />

        {/* Friends Links */}
        <Route
          path="/friends"
          element={
            <PrivateRoute>
              <FriendsDashboard />
            </PrivateRoute>
          }
        />
        <Route
          path="/friendRequest"
          element={
            <PrivateRoute>
              <FriendRequest />
            </PrivateRoute>
          }
        />
        <Route
          path="/friendRequestList"
          element={
            <PrivateRoute>
              <FriendRequestList />
            </PrivateRoute>
          }
        />
        <Route
          path="/friendsList"
          element={
            <PrivateRoute>
              <FriendsList />
            </PrivateRoute>
          }
        />
<<<<<<< HEAD
=======
        <Route
          path="/friendProfile/:friendEmail"
          element={
            <PrivateRoute>
              <FriendProfile />
            </PrivateRoute>
          }
        />

        {/* Groups Links */}
        <Route
          path="/userGroups"
          element={
            <PrivateRoute>
              <UserGroups />
            </PrivateRoute>
          }
        />
        <Route
          path="/groupsList"
          element={
            <PrivateRoute>
              <GroupsList />
            </PrivateRoute>
          }
        />
        <Route
          path="/createGroup"
          element={
            <PrivateRoute>
              <CreateGroup />
            </PrivateRoute>
          }
        />
        <Route
          path="/groupDashboard"
          element={
            <PrivateRoute>
              <GroupDashboard />
            </PrivateRoute>
          }
        />
        <Route
          path="/groups/:groupId"
          element={
            <PrivateRoute>
              <Groups />
            </PrivateRoute>
          }
        />

        {/* Admin Links */}
        <Route
          path="/admin"
          element={
            <AdminRoute>
              <Admin />
            </AdminRoute>
          }
        />
        <Route
          path="/adminManagement"
          element={
            <AdminRoute>
              <AdminManagement />
            </AdminRoute>
          }
        />
        <Route
          path="/categoryAdmin"
          element={
            <PrivateRoute>
              <CategoryAdmin />
            </PrivateRoute>
          }
        />
>>>>>>> develop
        <Route path="*" element={<Error />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
