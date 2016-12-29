/**
 * Created by stephane on 28/12/2016.
 */

var Login = RC ({
    getDefaultProps: function() {
        return {
            email: "",
            password: ""
        };
    },

    render: function() {

    }

});


var User = RC ({

    getDefaultProps: function() {
        return {
            id: "",
            name: "",
            first_name: "",
            email: "",
            sign_date: "",
            data_type: "user"
        };
    },



});