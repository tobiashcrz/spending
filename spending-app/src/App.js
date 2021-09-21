import React, { Component } from 'react';
import './App.css';
import SpendingsList from './SpendingsList';
import SpentEdit from './SpentEdit';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
class App extends Component {
  render() {
    return (
      <Router>
        <Switch>
          <Route path='/' exact={true} component={SpendingsList}/>
          <Route path='/spent' exact={true} component={SpentEdit}/>
          <Route path='/spent/:id' component={SpentEdit}/>
        </Switch>
      </Router>
    )
  }
}

export default App;