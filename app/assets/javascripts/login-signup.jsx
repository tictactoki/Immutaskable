import React from 'react'
import ReactDOM from 'react-dom'
import { Router, Route, Redirect, browerHistory, Link } from 'react-router'
import $ from "jquery"


class Login extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            email: "",
            password: "",
            url: "/login"
        };
        this.handleChange = this.handleChange.bind(this);
        this.authentication = this.authentication.bind(this);
    }


    handleChange(field, event) {
        let obj = {};
        obj[field] = event.target.value;
        this.setState(obj);
    }

    render() {
        return (
            <form onSubmit={this.authentication}>
                <label>
                    Email:
                    <input type="email" value={this.state.email} onChange={this.handleChange.bind(this,'email')}/>
                </label>
                <label>
                    Password:
                    <input type="password" value={this.state.password}
                           onChange={this.handleChange.bind(this,'password')}/>
                </label>
                <input type="submit" value="Login"/>
            </form>
        );
    }


    authentication(event) {
        event.preventDefault();
        $.ajax({
                type: "POST",
                contentType: "application/json",
                url: this.state.url,
                data: JSON.stringify({email: this.state.email, password: this.state.password}),
                success: function (data, status, xhr) {
                    console.log("success");
                    console.log(data);
                    console.log(status);
                    window.location.replace("./dashboard");
                },
                error: function (xhr, status, error) {
                    console.log("error");
                    console.log(xhr);
                    console.log(status);
                    console.log(error);
                }
            }
        )
    }

}


export default Login;