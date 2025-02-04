import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Forgot.css";
import { validateEmail } from "../authentication/SignupValidation";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export default function SecurityQuestion() {
  const [email, setEmail] = useState("");
  const [error, setError] = useState("");
  const [securityAnswer, setSecurityAnswer] = useState("");
  const [securityQuestion, setSecurityQuestion] = useState("");
  const navigate = useNavigate();

  const handleEmailSubmit = async (e) => {
    e.preventDefault();
    let validationErrors = {};
    setError("");

    // Perform login action with email and password
    if (!email || !validateEmail(email)) {
      validationErrors.email =
        "Please enter your email. Only @dal.ca addresses are accepted.";
    }

    if (Object.keys(validationErrors).length > 0) {
      setError(validationErrors);
      return;
    }

    try {
      const response = await fetch(
        `http://localhost:8085/api/user/get/${email}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.ok) {
        const data = await response.json();
        setSecurityQuestion(data.securityAnswer);
        console.log("User information retrieved successfull!");
      } else {
        console.error("Failed to fetch user from backend");
        setError("Error! Email does not exist!");
      }
    } catch (error) {
      console.error("Error fetching user from backend:", error);
      toast.error("Error fetching user from backend.");
    }
  };

  const handleSecurityQuestion = (e) => {
    e.preventDefault();
    let validationErrors = {};
    setError("");

    console.log("Answering security question...");
    if (securityQuestion === securityAnswer) {
      console.log("Security answer is correct");
      navigate("/resetPassword"); // Redirect to Change Password Page
    } else if (!securityAnswer) {
      validationErrors.securityAnswer =
        "Please enter your answer for the security question.";
    } else {
      console.log("Security answer is incorrect");
      validationErrors.securityAnswer =
        "Incorrect answer. Please try again.";
    }
    
    if (Object.keys(validationErrors).length > 0) {
      setError(validationErrors);
      return;
    }
  };

  return (
    <div className="container">
      <ToastContainer />
      <div className="paper">
        <h2 className="subtitle">Email</h2>
        <form
          className="form"
          onSubmit={handleEmailSubmit}
          noValidate
          autoComplete="off"
        >
          <input
            type="email"
            placeholder="Enter your Email"
            className="input"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          {error.email && (
            <p className="text-danger" style={{ paddingBottom: "1rem" }}>
              {error.email}
            </p>
          )}
          <button type="submit" className="button">
            Submit
          </button>
        </form>
      </div>

      {securityQuestion && (
        <div className="paper">
          <h2 className="subtitle">
            Security Question: What is your favorite destination to visit?
          </h2>
          <form
            className="form"
            onSubmit={handleSecurityQuestion}
            noValidate
            autoComplete="off"
          >
            <input
              type="text"
              placeholder="Answer to the Security Question"
              className="input"
              value={securityAnswer}
              onChange={(e) => setSecurityAnswer(e.target.value)}
            />
            {error.securityAnswer && (
              <p className="text-danger" style={{ paddingBottom: "1rem" }}>
                {error.securityAnswer}
              </p>
            )}
            <button type="submit" className="button">
              Submit
            </button>
          </form>
        </div>
      )}
    </div>
  );
}
