import React from 'react';
import ReactDOM from 'react-dom';
import { Router, Route, Redirect, browserHistory } from 'react-router';
import $ from "jquery"
import Login from './login-signup.jsx'

class Test extends React.Component {



    render() {
        return (<h1>Hello, {this.props.name}</h1>);
    }

    handleForm(event) {

    }

}

Test.defaultProps = {
    name: "Stéphane"
};


class Dashboard extends React.Component {

    render() {
        return (<h1>dashboard</h1>);
    }

}


var routes =
    <Router history={browserHistory}>
        <Route path="/" component={Login}></Route>
        <Route path="/sign/up" component={SignUp}></Route>
        <Route path="/dashboard" component={Dashboard}></Route>
    </Router>;


ReactDOM.render(routes, document.getElementById("app"));