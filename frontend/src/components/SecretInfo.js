import React from "react";
import { useState } from "react";
import { useEffect } from "react";

const SecretInfo = ({userInformations, setUserInformations, secretId}) => {

    
    const [secret, setSecret] = useState({
        name: 'testn',
        content: 'testc'
    })

    const getSecretInfo = async () => {
        
        const response = await fetch(`http://localhost:8080/api/v1/secret/${secretId}?decrypt=true&userId=${userInformations.email}`, {
            method: 'GET',
            headers: {
                'authorization': sessionStorage.getItem('JWT'),
            },
            
        })
        
        if (response.status === 204) {
            let errorMessage = "Vous n'avez aucun secrets sauvegardés";
            console.log(errorMessage);
            return;
        }

        if (response.status !== 200) {
            let errorMessage = "Erreur innatendue côté serveur";
            console.log(errorMessage);
            return;
        }
        
        const secretResponse = await response.json()
        setSecret(secretResponse);
        console.log('secret', secret);
    }

    if (!userInformations.loggedIn) {
        return (
            <div>
                <h1>Bienvenue aux informations du secret : </h1>
                <h2>Vous n'êtes pas connecté</h2>
            </div>
        );
    }

    console.log(secret, !secret);
    
    if (!secret || secret.name === 'testn') {
        console.log('a l\'int');
        getSecretInfo();
        return(<div></div>);
    }
    

    return (
        <div>
            <h1>Bienvenue aux informations du secret : {secret.name}</h1>
            <p>{secret.content}</p>
        </div>
    );
};

export default SecretInfo;