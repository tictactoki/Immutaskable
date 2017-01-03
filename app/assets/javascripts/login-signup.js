/**
 * Created by stephane on 28/12/2016.
 */

var Login = RC({

    getInitialeState: function() {
        return {
            email: this.props.email || "",
            password: this.props.password || ""
        };
    },

    getDefaultProps: function () {
        return {
            email: "",
            password: ""
        };
    },

    setValue: function(field, event) {
        var obj = {};
        obj[field] = event.target.value;
        this.setState(obj);
    },

    authentication: function (event) {
        event.preventDefault();
        var login = {
            email: this.state.email.trim() || "",
            password: this.state.password.trim() || ""
        };
        $.post({
            url: WidgetServerBridge.url("login"),
            data: login,
            data_type: "json"
        }).done(function (result) {
            console.log("success");
            console.log(result);
        }).fail(function (result) {
            console.log("fail");
            console.log(result);
        });
    },


    render: function () {
        return RE("form", { className: "signIn", onSubmit: this.authentication },
            RE("input", { id: "email", refs: "email", onChange: this.setValue.bind(this,'email'), type: "email", placeholder: "Email"}, null),
            RE("input", { id: "password", refs: "password", onChange: this.setValue.bind(this,'password'), type: "password", placeholder: "Password"}, null),
            RE("input", { type: "submit", value: "OK"}, null)
        );
    }

});

$(document).ready(function () {
    ReactDOM.render(RE(Login), document.getElementById("container"));
});

var User = RC({

    getDefaultProps: function () {
        return {
            id: "",
            name: "",
            first_name: "",
            email: "",
            sign_date: "",
            data_type: "user"
        };
    },

    render: function () {
        RE("p", null, "test");
    }


});