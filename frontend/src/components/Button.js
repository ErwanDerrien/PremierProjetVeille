import React from "react";
import {useState} from 'react'
import { useHistory } from "react-router-dom";

const Button = ({userInformations, setListSecrets, itemToDelete, purposeValue, setSecretId}) => {    

    const [purpose, setPurpose] = useState(purposeValue)

    const deleteItem = async () => {
        
        const response = await fetch(`http://localhost:8080/api/v1/secret/${itemToDelete.id}?=${userInformations.email}`, {
            method: 'DELETE',
            headers: {
                'authorization': sessionStorage.getItem('JWT'),
            },
            
        })
        
        if (response.status !== 200) {
            console.log('Erreur innatendue côté serveur')
            return;
        }
        
        getUserInfo();

    }

    const getUserInfo = async () => {
        
        const response = await fetch(`http://localhost:8080/api/v1/secret/`, {
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
        
        const secrets = await response.json()
        setListSecrets(secrets);

    }

    let history = useHistory();

    const redirectInfo = () => {
        setSecretId(itemToDelete.id);
        history.push('/secret');
    }

    if (purpose == 'delete') {
        return (
            <button onClick={deleteItem}>Supprimer</button>
        );
    }

    if (purpose == 'info') {
        return (
            <button onClick={redirectInfo}>Plus d'info</button>
        );
    }

}

export default Button;