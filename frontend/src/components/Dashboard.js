import React from "react";
import { useState } from "react";
import { useEffect } from "react";

const Dashboard = ({userInformations, setUserInformations}) => {

    
    const [errorMessage, setErrorMessage] = useState();
    
    const [listItems, setListItems] = useState({secrets: []});

    let secretList;

    const getUserInfo = async () => {
        
        const response = await fetch(`http://localhost:8080/api/v1/secret/`, {
            method: 'GET',
            headers: {
                'authorization': sessionStorage.getItem('JWT'),
            },
            
        })
        .then(response => response.json())
        .then(data => {
            setListItems({secrets: data});
        });
        
        if (response.status !== 200) {
            let errorMessage = "Votre connexion a échoué. L’identifiant ou le mot de passe que vous avez entré n’est pas valide. Réessayez.";
            setErrorMessage(errorMessage);
            return;
        }
        
        const responseJson = await response.json()

        console.log(responseJson)

    }

    
    
    if (!userInformations.loggedIn) {
        return (
            <div>
                <h1>Bienvenue au dashboard</h1>
                <h2>Vous n'êtes pas connecté</h2>
            </div>
        );
    }
    
    getUserInfo();

    
    return (
        <div>
        <h1>Bienvenue au dashboard</h1>
        <p>{errorMessage}</p>
        <h2>Vos secrets</h2>
        <ul>
          {listItems}
        </ul>
        </div>
    );
};

export default Dashboard;