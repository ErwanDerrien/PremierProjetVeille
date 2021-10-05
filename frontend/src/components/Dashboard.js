import React from "react";
import { useState } from "react";
import SecretsView from "./SecretsView";

const Dashboard = ({userInformations, setUserInformations, setSecretId}) => {

    
    const [errorMessage, setErrorMessage] = useState();
    
    const [listSecrets, setListSecrets] = useState([]);


    const getUserInfo = async () => {
        
        const response = await fetch(`http://localhost:8080/api/v1/secret/`, {
            method: 'GET',
            headers: {
                'authorization': sessionStorage.getItem('JWT'),
            },
            
        })
        
        if (response.status === 204) {
            let errorMessage = "Vous n'avez aucun secrets sauvegardés";
            setErrorMessage(errorMessage);
            return;
        }

        if (response.status !== 200) {
            let errorMessage = "Erreur innatendue côté serveur";
            setErrorMessage(errorMessage);
            return;
        }
        
        const secrets = await response.json()
        setListSecrets(secrets);

    }

    
    
    if (!userInformations.loggedIn) {
        return (
            <div>
                <h1>Bienvenue au dashboard</h1>
                <h2>Vous n'êtes pas connecté</h2>
            </div>
        );
    }
    
    if (listSecrets.length === 0) {
        getUserInfo();
    }

    
    return (
        <div>
        <h1>Bienvenue au dashboard</h1>
        <p>{errorMessage}</p>
        <h2>Vos secrets</h2>
        <SecretsView userInformations={userInformations} setListSecrets={setListSecrets} secrets={listSecrets} setSecretId={setSecretId}></SecretsView>
        </div>
    );
};

export default Dashboard;