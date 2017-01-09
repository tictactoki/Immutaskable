import React from 'react';
import ReactDOM from 'react-dom';
import { Router, Route, Redirect } from 'react-router';


class Test extends React.Component {



    render() {
        return (<h1>Hello, {this.props.name}</h1>);
    }
}

Test.defaultProps = {
    name: "Stéphane"
};

var routes =
    <Router>
        <Route path="/" component={Test}>
        </Route>
    </Router>;


ReactDOM.render(routes, document.getElementById("app"));