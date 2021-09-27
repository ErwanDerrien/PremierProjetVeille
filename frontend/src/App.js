import './App.css';
import { Redirect, BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import Home from './components/Home';
import Login from './components/Login';
import {useState} from 'react'
import Topbar from './components/Topbar';
import Signup from './components/Signup';

function App() {
  const userInformationsObject = {
    email: "",
    // username: "",
    role: "",
    loggedIn: false
  }

  const [userInformations, setUserInformations] = useState(    
    userInformationsObject
  )

  return (
    <Router>
      <div className="App"> 
        <Topbar></Topbar>
        <Switch>
          <Route path="/" exact component={Home}/>
          <Route path="/home" exact component={Home}/>
          <Route 
            path="/login" 
            extact 
            component={() => <Login userInformations={userInformations} setUserInformations={setUserInformations} />}
          />
          <Route path='/signup' exact component={Signup}/>
        </Switch>
        { userInformations.loggedIn ? (<Redirect push to="/home"/>) : null }
      </div>
    </Router>
  );
}

export default App;
