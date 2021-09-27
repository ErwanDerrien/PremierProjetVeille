import React from "react";
import { useHistory } from "react-router-dom";

const Topbar = () => {    
  // {redirectHome, redirectLogin, redirectSignup}
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