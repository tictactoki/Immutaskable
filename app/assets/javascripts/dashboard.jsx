import React from 'react';
import ReactDOM from 'react-dom';
import { Router, Route, Redirect, browserHistory } from 'react-router';
import $ from "jquery"
import Login from './login-signup.jsx'


class Dashboard extends React.Component {

    constructor(props) {
        super(props);

    }

    componentDidMount() {

    }

    render() {
        return (
          <h1>test</h1>
        );
    }




}

$(document).ready(function() {
    ReactDOM.render(Dashboard, document.getElementById("app"));
});

