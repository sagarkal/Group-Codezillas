$(function() {
     $('#myModal').modal({
     backdrop : 'static',
     keyboard : false
     })
     $('#myModal').modal('show');
//    $.get("MainServlet", {
//	type : "getquestions"
//    }, function(json) {
//	console.log(json);
//	appendQuestions(json);
//    });
});

function register(){
	$.get("RegisterServlet", {
		userName : $('#email').val(),
		password : $('#password').val()
	});
}

var usernameg = "";

/* Login function for new login page */

function checkLogin() {
    $.get("LoginServlet", {
	userId : $('#email').val(),
	password : $('#password').val()
    }, function(response) {
	if (response == "true") {
	    // loading main page
		window.location.replace("home.html");
		usernameg = $('#email').val().split('@')[0];
	    $('#myprofile').append(usernameg);
	}
	else
		{
		window.alert("Invalid Username/Password");
		}
    });
}

function fetchQuestions()
{
	$.get("MainServlet", {
		type : "getquestions"
	    }, function(json) {
		console.log("Login servlet wala "+json);
		appendQuestions(json);
	    });
}

function loadRegister() {
	window.location.replace("register.html");
}
/*
function checkLogin() {
    $.get("LoginServlet", {
	userId : $('#inputEmail').val(),
	password : $('#inputPassword').val()
    }, function(response) {
	if (response == "true") {
	    $('#myModal').modal("hide");
	    usernameg = $('#inputEmail').val().split('@')[0];
	    $('#myprofile').append(usernameg);

	    // loading main page
	    $.get("MainServlet", {
		type : "getquestions"
	    }, function(json) {
		console.log(json);
		appendQuestions(json);
	    });

	}
    });
}
*/

function appendQuestions(json) {
    for (i in json) {
	console.log(json[i]);
	var id = 'tableq' + json[i].id;
	var votesqid = 'votes' + json[i].id;
	var cl = json[i].tags
	$('#questions').append(
		$('<div/>').addClass(cl).append($('<table/>').attr({
		    id : id,
		    width : "100%"
		})).append($('<textarea/>').attr({
		    id : "txtarea" + json[i].id,
		    rows : "2",
		    cols : "50",
		    placeholder : "Type your response here.."
		})).append(
			$('<button/>').addClass(
				"btn btn-block btn-md btn-success btn-post")
				.attr({
				    id : "button" + json[i].id,
				    type : "button",
				    onclick : "postResponse(this)"
				}).append('Post')).append('<br><br>'));
	$('#' + id).append(
		$('<tr/>').append($('<td/>').addClass("bb").attr({
		    id : votesqid
		})).append($('<td/>').addClass("bb").append('<h2>Q. ' + json[i].question + '</h2>')).append(
			$('<td/>').addClass("right").append(
				'<span class="label label-info">'
					+ json[i].tags + '</span>').append(
				'<br><br>').append(
				'<span class="label label-warning">'
					+ 'Asked by ' + '<a href="#" id='+json[i].username+'>'+json[i].username+'</a>'
					+ '</span>')));
	addVotes(votesqid, json[i].upvotes - json[i].downvotes);
	addAnswers(json[i].id, json[i].username, json[i].tags);
//	addReputation(json[i].username, json[i].tags);
    }
}

function addReputation(username, lang){
    console.log("language " +lang);
    $.get("MainServlet", {
	type : "getrep",
	username : username
    }, function(json) {
	if(lang == 'Java') {
	    $('#'+username).parent().append('<br><br><span class="label label-warning"> Java: ' + json.java + '</span>');
//		$('#'+id).append('<br>'+json.java);
	} else {
//		$('#'+id).append('<br>'+json.cpp);
	    $('#'+username).parent().append('<br><br><span class="label label-warning"> CPP: ' + json.cpp + '</span>');
	}
	console.log(json);
    });
}

function addAnswers(id, username, lang){
    $.get("MainServlet", {
	type : "getanswers",
	qid : id
    }, function(json) {
	console.log(json);
	    for (i in json) {
	    	$.get("MainServlet", {
	    		type : "saveanswer",
	    		qid : i
	    	    });
		$("#tableq" + id).append(
		$('<tr/>').append($('<td/>').attr('id', "votes" + id + "and"+ json[i].id))
			.append($('<td/>').append(json[i].answer)));
		addVotes("votes"+id+"and"+json[i].id, parseInt(json[i].upvotes) - parseInt(json[i].downvotes));
		$('').append(
			'<span class="label label-warning">'
			+ 'Asked by ' + '<a href="#" id='+username+'>'+	username+'</a>'
			+ '</span>');
	    }
	    addReputation(username, lang);
    });
}

function postResponse(tag) {
    var txt = $(tag).prev().val();
    $(tag).prev().val('');
    var id = $(tag).attr('id').split("button")[1];
    $.get("MainServlet", {
	type : "saveanswer",
	username : usernameg,
	answer : txt,
	qid : id
    }, function(response) {
	var ansid = response;
	$("#tableq" + id).append(
		$('<tr/>').append($('<td/>').attr('id', "votes" +id+"and"+ ansid))
			.append($('<td/>').append(txt)));
	console.log(txt);
	addVotes("votes" +id+"and"+ ansid, 0);
    });
}

function addVotes(id, votes) {
    console.log(id);
    $('#' + id)
	    .append(
		    $('<table/>')
			    .append(
				    $('<tr/>')
					    .append(
						    $('<td/>')
							    .attr(
								    {
									align : 'center'
								    })
							    .append(
								    $('<span/>')
									    .addClass(
										    "glyphicon glyphicon-triangle-top "))))
			    .append($('<tr/>').append($('<td/>').attr({
				align : 'center'
			    }).append(votes)))
			    .append(
				    $('<tr/>')
					    .append(
						    $('<td/>')
							    .attr(
								    {
									align : 'center'
								    })
							    .append(
								    $('<span/>')
									    .addClass(
										    "glyphicon glyphicon-triangle-bottom")))));
    $(".glyphicon-triangle-top, .glyphicon-triangle-bottom").attr('onclick',
	    'updateVotes(this)');
}
	
function updateVotes(tag) {
    console.log(tag.className);
    var c = tag.className;
    var actualid;
    var type;
    var r = 0;
    var up, down;
    if (c.includes('top')) {
	// type = 'q';
	// actualid = id.split('q')[1];
	r = parseInt($(tag).parent().parent().next().first().first().text()) + 1;
	$(tag).parent().parent().next().first().first().text(r);
	console.log("Id from upvotes is"+$(tag).parent());
	$.get("MainServlet", {
		type : "upvote",
		up : r,
		id : $(this).parent().attr('id')
	    });
    } else {
	// type = 'a';
	// actualid = id.split('a')[1];
	r = parseInt($(tag).parent().parent().prev().first().first().text()) - 1;
	$(tag).parent().parent().prev().first().first().text(r);
	$.get("MainServlet", {
		type : "downvote",
		down : r
	    });
    }
    console.log("update votes " + r);
}

function enterKey(e) {
    if (e.keyCode == 13) {
	checkLogin();
    }
}

function onlyQuestions(type) {
    var allExceptType = ":not(" + type + ")";
    $('#questions').children(allExceptType).hide();
    $('#questions').children(type).show();
    console.log('all hidden exept' + type);
}