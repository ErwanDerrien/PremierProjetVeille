import React from "react";
import {useState} from 'react'
import { useHistory } from "react-router-dom";
import SecretView from "./SecretView";

const SecretsView = ({userInformations, setListSecrets, secrets, setSecretId}) => {    
 
    const secretItems = secrets.map((secret) =>
        <SecretView userInformations={userInformations} setListSecrets={setListSecrets} secret={secret} setSecretId={setSecretId}/>
    );

    return (
        <ul>
          {secretItems}
        </ul>
    );
}

export default SecretsView;