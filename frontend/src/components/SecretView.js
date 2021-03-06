import React from "react";
import Button from "./Button";

const SecretView = ({userInformations, setListSecrets, secret, setSecretId}) => {    
 
    return (
        <li key={secret.id}>
          <p>{secret.name} 
          -{">"} <Button style={{padding: '50px'}} userInformations={userInformations} setListSecrets={setListSecrets} itemToDelete={secret} purposeValue={'info'} setSecretId={setSecretId}/>
          -{">"} <Button style={{padding: '50px'}} userInformations={userInformations} setListSecrets={setListSecrets} itemToDelete={secret} purposeValue={'delete'}/>
          </p>
        </li>
    );
}

export default SecretView;