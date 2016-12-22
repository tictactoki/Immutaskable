/**
 * Created by stephane on 06/12/2016.
 */
var websocketActor;

function connectToWSA() {
    var endpoint = document.getElementById("endpoint").value;
    if (websocketActor !== undefined) {
        websocketActor.close()
    }

    console.log(endpoint);

    websocketActor = new WebSocket(endpoint);


    websocketActor.onmessage = function(event) {
        var length;
        if(event.data.size === undefined){
            length = event.data.length;
        }
        else {
            length = event.data.size;
        }
        console.log("onmessage. size: " + length + ", content: " + event.data);
        document.getElementById("test").innerHTML = event.data;
    }

    websocketActor.onopen = function(event) {
        console.log("websocket open");
    }

    websocketActor.onclose = function(event) {
        if( websocketActor !== undefined) websocketActor.send(JSON.stringify({msg: "Disconnected"}));
        console.log("websocket close");
    }

    websocketActor.onerror = function(event) {
        console.log(event);
    }
}

var user = {
    _id: "11111111",
    name: "wong",
    firstName: "Stephane",
    email: "a@a.com",
    password: "azerty",
    signDate: "2016-12-06"
};

var st = {
    _id: "90485406540",
    owner: user,
    title: "title",
    description: "description",
    time: 0,
    data_type: "simple_task"

};

var stt = st;
var gt = {
    _id: "64654",
    owner: user,
    title: "gp",
    description: "desc",
    data_type: "grouping_task",
    tasks: [st,stt]
}

var tm = {
    data_type: "task_manager",
    _id: "10",
    task: st,
    pushDate: "2016-12-07",
    gapTime: 50
}

function sendMsg() {
    var message = document.getElementById("message").value;
    websocketActor.send(JSON.stringify(gt));
}

window.onunload = function(event) {
    if(websocketActor !== undefined) websocketActor.send(JSON.stringify({msg: "Disconnected"}));
}

function closeConn(){

    websocketActor.close();
}