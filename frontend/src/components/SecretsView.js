import React from "react";
import {useState} from 'react'
import { useHistory } from "react-router-dom";
import SecretView from "./SecretView";

const SecretsView = ({secrets}) => {    
 
    const secretItems = secrets.map((secret) =>
        <SecretView secret={secret} />
    );

    return (
        <ul>
          {secretItems}
        </ul>
    );
}

export default SecretsView;