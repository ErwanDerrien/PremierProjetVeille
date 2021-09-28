import React from 'react';
import {useState} from 'react'
import jwt_decode from "jwt-decode";


const Login = ({userInformations, setUserInformations, setLoginLabel}) => {
    const [errorMessage, setErrorMessage] = useState()

    const logUserIn = async (evt) => {
        // Prevents the form from being submitted
        evt.nativeEvent.preventDefault();
    
        // Get the form fields
        const formElements = evt.nativeEvent.target.elements;
        const email = formElements.email.value;
        const password = formElements.password.value;

    
        const response = await fetch('http://localhost:8080/api/v1/login', {
            method: 'POST',
            body: JSON.stringify({ id: email, password: password }),
            headers: {
                'content-type': 'application/json',
            },
        });

        if (response.status !== 200) {
            let errorMessage = "Votre connexion a échoué. L’identifiant ou le mot de passe que vous avez entré n’est pas valide. Réessayez.";
            setErrorMessage(errorMessage);
            return;
        }

        const token = await response.text();
        console.log('token :', token);
        sessionStorage.setItem('JWT', 'Bearer ' + token);
        
        setUserInformations({
          email: email,
          loggedIn: true,
        });
    }

    const deleteLocalUserInfo = () => {
        sessionStorage.setItem('jwt', '');
        setUserInformations({
            email: '',
            loggedIn: false
        });
    }
 
    if(!userInformations.loggedIn) { 
        return (
            <div>
            <p>{errorMessage}</p>
            <form className="form" onSubmit={logUserIn}>
                <label>Adresse courriel:</label><input id="email" name="email" type="email" required />
                <label>Mot de passe:</label><input id="password" name="password" type="password" required minLength="8" />
                <button type="submit">Connexion</button>
            </form> 
            </div>
        );    
    } else {
        return (
            <div>
                <p>On dirait que vous êtes déja connecté, voulez-vous vous déconnecter ?</p>
                <button onClick={deleteLocalUserInfo}>Déconnexion</button>
            </div>
        )
    }
}

export default Login;