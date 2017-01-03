/**
 * Created by stephane on 28/12/2016.
 */


var RC = React.createClass;
var RE = React.createElement;

function WidgetServerBridge(){}

WidgetServerBridge.props = {
    protocol: "http://",
    host: location.hostname,
    port: ":9000"
};

WidgetServerBridge.url = (function(route) {
    return WidgetServerBridge.props.protocol + WidgetServerBridge.props.host + WidgetServerBridge.props.port + "/" + route;
});