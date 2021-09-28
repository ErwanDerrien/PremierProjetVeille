import React from "react";
import { useState } from "react";

const Dashboard = ({userInformations, setUserInformations}) => {

    const [errorMessage, setErrorMessage] = useState();
    
    const getUserInfo = async () => {
        
        const response = await fetch(`http://localhost:8080/api/v1/secret/`, {
            method: 'GET',
            headers: {
                'authorization': sessionStorage.getItem('JWT'),
            },
            
        });
        
        if (response.status !== 200) {
            let errorMessage = "Votre connexion a échoué. L’identifiant ou le mot de passe que vous avez entré n’est pas valide. Réessayez.";
            setErrorMessage(errorMessage);
            return;
        }

        const responseJson = await response.json()
        console.log(responseJson);
    }

    getUserInfo();

    return (
    <div>
      <h1>Bienvenue au dashboard</h1>
      <p>{errorMessage}</p>
      <h2>Vos secrets</h2>
    </div>
  );
};

export default Dashboard;
