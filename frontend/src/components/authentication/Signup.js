import React, { useState } from "react";
import "./Login.css"; // Using the same CSS file for simplicity
import { Link, useLocation, useNavigate } from "react-router-dom";
import { validateEmail, validatePassword } from "./SignupValidation";
import axios from "axios";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function Signup() {
  const [firstName, setFname] = useState("");
  const [lastName, setLname] = useState("");
  const [dateOfBirth, setBirthday] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [securityAnswer, setSecurityAnswer] = useState("");
  const [errors, setErrors] = useState({});
  const location = useLocation();
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    let validationErrors = {};

    if (!firstName) {
      validationErrors.name = "First Name is required.";
    }

    if (!dateOfBirth) {
      validationErrors.birthday = "Date of Birth is required.";
    }

    if (!email || !validateEmail(email)) {
      validationErrors.email =
        "Invalid email. Only @dal.ca addresses are accepted.";
    }

    if (!securityAnswer) {
      validationErrors.securityAnswer = "Security Answer is required.";
    }

    const passwordErrors = validatePassword(password);
    if (passwordErrors.length > 0) {
      validationErrors.password = passwordErrors;
    }

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    const user = {
      firstName,
      lastName,
      dateOfBirth,
      email,
      password,
      securityAnswer,
    };

    try {
      // Submit form data to the backend
      const response = await axios.post(
        "http://localhost:8085/api/user/signup",
        user
      );
      if (response.status === 200) {
        const userId = response.data;
        console.log(`User created successfully with ID: ${userId}`);
        toast.success("User created successfully!");

        // Store login flag in local storage
        localStorage.setItem("isLoggedIn", "true");
        localStorage.setItem("userId", userId);
<<<<<<< HEAD
=======
        localStorage.setItem("userEmail", email);
        localStorage.setItem("userRole", role);
>>>>>>> develop

        // Redirect to the home page
        navigate("/home");
      }
    } catch (error) {
      if (error.response && error.response.status === 500) {
        setErrors({ email: "Email already taken" });
      } else {
        console.error("Error signing up:", error);
        toast.error("An error occurred. Please try again!");
      }
    }
  };

  return (
    <div className="d-flex justify-content-center align-items-center login-page">
      <ToastContainer />
      <div className="p-3 bg-white w-25">
        <form onSubmit={handleSubmit} className="signup-form">
          <div>
            <label htmlFor="name" className="label-name">
              First Name
            </label>
            <input
              type="text"
              className="form-control"
              value={firstName}
              onChange={(e) => setFname(e.target.value)}
            />
            {errors.name && <p className="text-danger">{errors.name}</p>}
          </div>
          <div>
            <label htmlFor="name" className="label-name">
              Last Name
            </label>
            <input
              type="text"
              className="form-control"
              value={lastName}
              onChange={(e) => setLname(e.target.value)}
            />
            {errors.lastname && (
              <p className="text-danger">{errors.lastname}</p>
            )}
          </div>

          <div>
            <label htmlFor="birthday" className="label-name">
              Birthday
            </label>
            <input
              type="date"
              className="form-control"
              value={dateOfBirth}
              onChange={(e) => setBirthday(e.target.value)}
            />
            {errors.birthday && (
              <p className="text-danger">{errors.birthday}</p>
            )}
          </div>

          <div>
            <label htmlFor="email" className="label-name">
              Email
            </label>
            <input
              type="email"
              placeholder="@dal.ca"
              className="form-control"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            {errors.email && <p className="text-danger">{errors.email}</p>}
          </div>

          <div>
            <label htmlFor="password" className="label-name">
              Password
            </label>
            <input
              type="password"
              className="form-control"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            {errors.password && (
              <ul className="text-danger">
                {errors.password.map((error, index) => (
                  <li key={index}>{error}</li>
                ))}
              </ul>
            )}
          </div>

          <div>
            <label htmlFor="securityAnswer" className="label-name">
              Security Answer
            </label>
            <input
              type="text"
              className="form-control"
              placeholder="What is your favorite destination to visit?"
              value={securityAnswer}
              onChange={(e) => setSecurityAnswer(e.target.value)}
            />
            {errors.securityAnswer && (
              <p className="text-danger">{errors.securityAnswer}</p>
            )}
          </div>

          <div className="btn-group">
            <Link
              to="/login"
              className={`btn ${
                location.pathname === "/login" ? "btn-success" : "btn-default"
              } text-decoration-none`}
            >
              Login
            </Link>
            <button
              className={`btn ${
                location.pathname === "/signup" ? "btn-success" : "btn-default"
              }`}
              type="submit"
            >
              Sign Up
            </button>
          </div>

          <p className="mt-3">You agree to our terms and policies</p>
        </form>
      </div>
    </div>
  );
}
export default Signup;
