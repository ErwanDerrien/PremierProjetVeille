import React from "react";
import {useState} from 'react'
import Button from "./Button";
import { useHistory } from "react-router-dom";

const SecretView = ({secret}) => {    
 
    return (
        <li key={secret.id}>
          <p>{secret.name} -{">"} <Button style="padding: 50px" itemToDelete={secret}/></p>
          
        </li>
    );
}

export default SecretView;