import React from 'react';
import ReactDOM from 'react-dom';
import { Router, Route, Redirect, browserHistory } from 'react-router';
import $ from "jquery"
import Login from './login-signup.jsx'
import Dashboard from './dashboard.jsx'

var routes = (
    <Router history={browserHistory}>
        <Route path="/" component={Login}>
            <Route path="dashboard" component={Dashboard}></Route>
            <Route path="logout" component={Login}></Route>
            <Redirect from="logout" to="" component={Login}></Redirect>
        </Route>
    </Router>);


ReactDOM.render(routes, document.getElementById("app"));