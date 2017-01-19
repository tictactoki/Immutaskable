import React from 'react'
import ReactDOM from 'react-dom'
import { Router, Route, Redirect, browerHistory, Link } from 'react-router'
import $ from "jquery"


class SignUp extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            name: "",
            firstName: "",
            email: "",
            password: "",
            url: "/signUp"
        };
        this.handleChange = this.handleChange.bind(this);
        this.signUp = this.signUp.bind(this);
    }

    handleChange(field, event) {
        var obj = {};
        obj[field] = event.target.value;
        this.setState(obj);
    }

    signUp(event) {
        event.preventDefault();
        $.ajax({
            type: "POST",
            contentType: "application/json",
            dataType: "json",
            url: this.state.url,
            data: JSON.stringify({name: this.state.name, firstName: this.state.firstName, email: this.state.email, password: this.state.password}),
            success: function (data, status, xhr) {
                console.log("success");
                console.log(data);
                console.log(status);
            },
            error: function (xhr, status, error) {
                console.log("error");
                console.log(xhr);
                console.log(status);
                console.log(error);
            }
        })
    }

    render() {
        return (
            <form onSubmit={this.signUp}>
                <label>
                    Name:
                    <input type="text" value={this.state.name} onChange={this.handleChange.bind(this,'name')}/>
                </label>
                <label>
                    Firstname:
                    <input type="text" value={this.state.firstName}
                           onChange={this.handleChange.bind(this,'firstName')}/>
                </label>
                <label>
                    Email:
                    <input type="email" value={this.state.email} onChange={this.handleChange.bind(this,'email')}/>
                </label>
                <label>
                    Password:
                    <input type="password" value={this.state.password}
                           onChange={this.handleChange.bind(this,'password')}/>
                </label>
                <input type="submit" value="SignUp"/>
            </form>
        );
    }

}

export default SignUp;