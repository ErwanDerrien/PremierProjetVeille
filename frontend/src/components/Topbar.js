import React from "react";
import {useState} from 'react'
import { useHistory } from "react-router-dom";

const Topbar = ({userInformations, setUserInformations}) => {    
  
  let history = useHistory();

  const redirectHome = () => {
    history.push('/home');
  }

  const redirectLogin = () => {
      history.push('/login');
  }
  
  const redirectSignup = () => {
      history.push('/signup');
  }

  const redirectDashboard = () => {
    history.push('/dashboard');
  }
  
  const deleteLocalUserInfo = () => {
    sessionStorage.setItem("jwt", "");
    setUserInformations({
      email: "",
      role: "",
      loggedIn: false,
    });
    redirectHome();
  };

  if (userInformations.loggedIn) {
    return (
      <div style={styles.container}>
          <button onClick={redirectHome}>Home</button>
          <button onClick={redirectDashboard}>Dashboard</button>
          <button onClick={deleteLocalUserInfo}>Deconnexion</button>
      </div>
    );
  }
  return (
    <div style={styles.container}>
        <button onClick={redirectHome}>Home</button>
        <button onClick={redirectSignup}>Inscription</button>
        <button onClick={redirectLogin}>Connexion</button>
    </div>
  );
};

const styles = {
    container: {
      backgroundColor: 'lightblue'
    }
  };

export default Topbar;