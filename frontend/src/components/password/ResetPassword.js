import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Forgot.css";
import { validateEmail, validatePassword } from "../authentication/SignupValidation";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export default function ResetPassword() {
  const [email, setEmail] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState(null);
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();

  const handleChangePassword = async (e) => {
    e.preventDefault();
    let validationErrors = {};
    console.log("Changing password...");

    // Perform validation action with new password
    if (!email) {
      validationErrors.email =
        "Cannot leave the email empty.";
    }

    if (!validateEmail(email)){
      validationErrors.email =
        "Please enter your @dal.ca email.";
    }

    const passwordErrors = validatePassword(newPassword);
    if (passwordErrors.length > 0) {
      validationErrors.password = passwordErrors;
    }

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    const updatedUser = {
      email: email,
      newPassword: newPassword,
    };

    try {
      const response = await fetch(
        `http://localhost:8085/api/user/reset-password`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(updatedUser),
        }
      );

      if (!response.ok) {
        throw new Error("Failed to update password");
      }

      toast.success("Password updated successfully");
      setEmail("");
      setNewPassword("");
      setErrorMessage(null);
      navigate("/login"); // Redirect to Login Page
    } catch (error) {
      console.error("Error updating password:", error);
      setErrorMessage("Failed to update password");
    }
  };

  return (
    <div className="container">
      <ToastContainer />
      <div className="paper">
        <h2 className="subtitle">Reset Password</h2>
        <form
          className="form"
          onSubmit={handleChangePassword}
          noValidate
          autoComplete="off"
        >
          <input
            type="email"
            placeholder="Email"
            className="input"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          {errors.email && <p className="text-danger"  style={{ paddingBottom: "1rem" }}>{errors.email}</p>}
          <input
            type="password"
            placeholder="New Password"
            className="input"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
          />
          {errors.password && (
            <ul className="text-danger" style={{ paddingBottom: "1rem" }}>
              {errors.password.map((error, index) => (
                <li key={index}>{error}</li>
              ))}
            </ul>
          )}
          <button type="submit" className="button">
            Reset Password
          </button>
          {errorMessage && <div className="error">{errorMessage}</div>}
        </form>
      </div>
    </div>
  );
}
