import './App.css';
import { Redirect, BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import Home from './components/Home';
import Login from './components/Login';
import {useState} from 'react'
import Topbar from './components/Topbar';
import Signup from './components/Signup';
import Dashboard from './components/Dashboard';



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
          <Route 
            path="/" 
            exact 
            component={() => <Home userInformations={userInformations} setUserInformations={setUserInformations} />}
          />
          <Route 
            path="/login" 
            exact 
            component={() => <Login userInformations={userInformations} setUserInformations={setUserInformations} />}
          />
          <Route 
            path="/signup" 
            exact 
            component={() => <Signup userInformations={userInformations} setUserInformations={setUserInformations} />}
          />
          <Route 
            path="/dashboard" 
            exact 
            component={() => <Dashboard userInformations={userInformations} setUserInformations={setUserInformations} />}
          />
        </Switch>
        { userInformations.loggedIn ? (<Redirect push to="/dashboard"/>) : null }
      </div>
    </Router>
  );
}

export default App;
