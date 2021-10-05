import React from "react";
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