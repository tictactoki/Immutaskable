/**
 * Created by stephane on 26/12/2016.
 */

/*var Test = React.createClass ({

    render: function() {
        return (
          <div>
              <p>test</p>
          </div>
        );
    }

});*/
var elem = React.createElement;


var Test = React.createClass({

    render: function() {
        return elem('div', null, "hello");
    }

});


$(function() {

    $('#login-form-link').click(function(e) {
        $("#login-form").delay(100).fadeIn(100);
        $("#register-form").fadeOut(100);
        $('#register-form-link').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
    });
    $('#register-form-link').click(function(e) {
        $("#register-form").delay(100).fadeIn(100);
        $("#login-form").fadeOut(100);
        $('#login-form-link').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
    });

});


//console.log("test");