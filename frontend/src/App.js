import './App.css';
import { Redirect, BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import Home from './components/Home';
import Login from './components/Login';
import {useState} from 'react'
import Topbar from './components/Topbar';
import Signup from './components/Signup';
import Dashboard from './components/Dashboard';
import { useHistory } from "react-router-dom";


function App() {
  const userInformationsObject = {
    email: '',
    loggedIn: false
  }

  const [userInformations, setUserInformations] = useState(    
    userInformationsObject
  );

  return (
    <Router>
      <div className="App"> 
        <Topbar userInformations={userInformations} setUserInformations={setUserInformations}></Topbar>
        <Switch>
          <Route path="/" exact component={Home}/>
          <Route path="/home" exact component={Home}/>
          <Route 
            path="/login" 
            extact 
            component={() => <Login userInformations={userInformations} setUserInformations={setUserInformations} />}
          />
          <Route 
            path="/signup" 
            extact 
            component={() => <Signup userInformations={userInformations} setUserInformations={setUserInformations} />}
          />
          <Route 
            path="/dashboard" 
            extact 
            component={() => <Dashboard userInformations={userInformations} setUserInformations={setUserInformations} />}
          />
        </Switch>
        { userInformations.loggedIn ? (<Redirect push to="/dashboard"/>) : null }
      </div>
    </Router>
  );
}

export default App;
