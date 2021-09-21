import React from 'react';
import {useState} from 'react'
import jwt_decode from "jwt-decode";


const Login = ({userInformations, setUserInformations}) => {
    const [errorMessage, setErrorMessage] = useState()

    const logUserIn = (evt) => {
        // Prevents the form from being submitted
        evt.nativeEvent.preventDefault();
    
        // Get the form fields
        const formElements = evt.nativeEvent.target.elements;
        const email = formElements.email.value;
        const password = formElements.password.value;
    
        // axios ({
        //     method: 'POST',
        //     baseURL: 'http://localhost:8080/',
        //     url: 'user/login',
        //     data: JSON.stringify({ email: email, password: password }),
        //     headers: {
        //         'content-type': 'application/json',
        //     }
        //   })
        //   .then(function (response) {
        //       if (response.data !== ''){
        //         sessionStorage.setItem('jwt', `Bearer ${response.data}`);

        //         const decodedJWT = jwt_decode(response.data);
        //         setUserInformations({
        //             email: email,
        //             role: decodedJWT.role,
        //             loggedIn: true
        //         });
        //         setErrorMessage();
        //       } else {
        //         let errorMessage = 'Informations ne correspondent à aucun utilisateur sauvegardé'
        //         setErrorMessage(errorMessage);
        //       }
        // });
    }

    const deleteLocalUserInfo = () => {
        sessionStorage.setItem('jwt', '');
        setUserInformations({
            email: "test",
            // username: "",
            role: "",
            loggedIn: false
        });
    }
 
    if(!userInformations.loggedIn) { 
        // return (
        //     <div>
        //         <Grid container spacing={10} direction="column" alignItems="center" justifyContent="center" style={{minHeight: '150vh'}}>
        //             <Typography variant="h4" align="center">Connexion</Typography>

        //             <DialogContentText>{errorMessage}</DialogContentText>
        //             <form noValidate autoComplete="off" onSubmit={logUserIn}>
        //                 <TextField
        //                     margin="normal"
        //                     required
        //                     fullWidth
        //                     id="email"
        //                     label="Email Address"
        //                     name="email"
        //                     autoComplete="email"
        //                     autoFocus
        //                 />
        //                 <TextField
        //                     margin="normal"
        //                     required
        //                     fullWidth
        //                     name="password"
        //                     label="Password"
        //                     type="password"
        //                     id="password"
        //                     autoComplete="current-password"
        //                 />
        //                 <Button type="submit" variant="contained" color="primary" style={{marginTop: "10px"}}>
        //                     Envoyer
        //                 </Button>
        //             </form>
        //         </Grid>
        //     </div>
        // )
        return (
            <form class="form" onSubmit="${(evt) => { this.signIn(evt); }}">
                {/* ${this.successMessage ? html`<div class="form__success entire_column">${this.successMessage}</div>` : '' }
                ${this.errorMessage ? html`<div class="form__error entire_column">${this.errorMessage}</div>` : '' } */}
                <label for="u">Username:</label><input id="u" name="id" type="email" required />
                <label for="p">Password:</label><input id="p" name="password" type="password" required minlength="8" />
                <button class="entire_column" type="submit">Sign Up</button>
            </form> );
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