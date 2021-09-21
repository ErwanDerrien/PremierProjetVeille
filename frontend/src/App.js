import './App.css';
import { Redirect, BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import Home from './components/Home';
import Login from './components/Login';
import {useState} from 'react'

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
        <Switch>
          <Route path="/home" exact component={Home}/>
          <Route 
            path="/login" 
            extact 
            component={() => <Login userInformations={userInformations} setUserInformations={setUserInformations} />}
          />
        </Switch>
        { userInformations.loggedIn ? (<Redirect push to="/home"/>) : null }
      </div>
    </Router>
  );
}

export default App;
