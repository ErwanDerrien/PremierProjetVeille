import React from 'react';
import {useState} from 'react'
import jwt_decode from "jwt-decode";


const Login = ({userInformations, setUserInformations}) => {
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

        console.log('status :', response);

        if (response.status !== 200) {
            let errorMessage = "Votre connexion a échoué. L’identifiant ou le mot de passe que vous avez entré n’est pas valide. Réessayez.";
            setErrorMessage(errorMessage);
            return;
        }

        const decodedJWT = jwt_decode(response.data);
        setUserInformations({
          email: email,
          role: decodedJWT.role,
          loggedIn: true,
        });

        const token = await response.text();
        console.log('token :', token);
        sessionStorage.setItem('JWT', 'Bearer ' + token);
    }

    const deleteLocalUserInfo = () => {
        sessionStorage.setItem('jwt', '');
        setUserInformations({
            email: "test",
            role: "",
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
                <button type="submit">Sign Up</button>
            </form> 
            </div>
        );    
    } else {
        return (
            <div>
                {/* <Grid container spacing={10} direction="column" alignItems="center" justifyContent="center" style={{minHeight: '100vh'}}>
                    <Typography variant="h4" align="center">Connexion</Typography>
                    <DialogContentText>On dirait que vous êtes déjà connecté, voulez-vous vous déconnecter ?</DialogContentText>
                    <Button variant="contained" color="primary" onClick={deleteLocalUserInfo} style={{marginTop: '5vh'}}>Se Déconnecter</Button>
                </Grid> */}
            </div>
        )
    }
}

export default Login;