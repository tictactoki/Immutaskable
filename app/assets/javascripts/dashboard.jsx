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

var routes =
    <Router history={browserHistory}>
        <Route path="/" component={Test}></Route>
        <Route path="/dashboard" component={Test}></Route>
    </Router>;

ReactDOM.render(routes, document.getElementById("app"));