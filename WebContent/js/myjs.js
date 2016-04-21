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

/* Jquery function to register new users */

function register(){
	$.get("RegisterServlet", {
		userName : $('#email').val(),
		password : $('#password').val()
	});
}

/* Global variable 'usernameg' to store the currently logged in user */

var usernameg = '';

/* New checkLogin function that will no longer be used */

/*
function checkLogin() {
	usernameg = $('#email').val();
    $.get("LoginServlet", {
	userId : $('#email').val(),
	password : $('#password').val()
    }, function(response) {
	if (response === "true") {
		window.location.replace("home.html?hello=" + $('#email').val());	
	//	var url ="home.html";
		//$("#usernameg").attr("href",url);
	}
	 else
		{
		window.alert("Invalid Username/Password"+response);
		}
		
    });
}
*/

/* Below function is the old implementation of checkLogin() that was intended for 
 * old home page with pop up window that asked for login credentials */

function checkLogin() {
    $.get("LoginServlet", {
	userId : $('#inputEmail').val(),
	password : $('#inputPassword').val()
    }, function(response) {
	if (response == "true") {
	    $('#myModal').modal("hide");
	    usernameg = $('#inputEmail').val();
	    $('#myprofile').append(usernameg);

	    // loading main page
	    $.get("MainServlet", {
		type : "getquestions"
	    }, function(json) {
		console.log(json);
		appendQuestions(json);
	    });

	}
	else
	{
	window.alert("Invalid Username/Password"+response);
	}
    });
}


/* Function to get User ID of the person who gave the answer */

function getUserId()
{
	return "skalburg";
}

/* Function that appends all the relevant questions in the form of a table */

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
		    id : votesqid,
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
	//addReputation(json[i].username, json[i].tags);
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

/* The below function updates reputation based on the score obtained in a quiz (1 correct answer = 1 reputation point */

function updateReputation(language, pointsToAdd) {
	 $.get("MainServlet", {
			type : "updaterep",
			username : "skalburg@asu.edu",
			language: language,
			pointsToAdd: pointsToAdd
		    }, function(response)
		    {		    	
		    	console.log("INSIDE UPDATE REPUTATION RESPONSE!!"+response);
		    	if(response === "0"){
		    		window.alert("A quiz cannot be taken more than once!");
		    	}
		    	else{
		    		window.alert("You earned a base reputation of "+response+"!");
		    	}	
		    });
}

/* Function that fetches answers from the database */

function addAnswers(id, username, lang){
    $.get("MainServlet", {
	type : "getanswers",
	qid : id
    }, function(json) {
	
	    for (i in json) {
		$("#tableq" + id).append(
		$('<tr/>').append($('<td />').attr('id',"votes" + id + "and"+ json[i].id))
			.append($('<td/>').append(json[i].answer)).attr({align : "left"}).append($('<td/>').append(
					'<span class="label label-warning">'
					+ 'Answered By' + '<a href="#" id='+getUserId()+'>'+	getUserId()+'</a>'
					+ '</span>')))
							 .append(
							$('<tr/>').append($('<td/>')).append($('<td/>').attr(
								    {
										    width : "100%",
											align : 'left'
										    }).append($('<button/>').addClass("right").addClass(
							"btn btn-default btn-xs")
							.attr({
							    id : "button" + json[i].id,
							    type : "button",
							   //left : "50px"
							    onclick : "openWindowForFeedback("+json[i].id+")",
							}).append('Feedback').append('<br>'))));
		console.log("EXPECTING QUESTION ID: "+id+" FOR ANSWER ID: "+json[i].id);
		addVotes("votes"+id+"and"+json[i].id, parseInt(json[i].upvotes) - parseInt(json[i].downvotes));
		
	    }
	    //addReputation(username, lang);
    });
}

function openWindowForFeedback(id) {
    window.open("feedback.html?id=" + id);
}


function updateFeedback(accuracy, conciseness, redundancy, grammar, id)
{
	 $.get("MainServlet", {
			type : "feedback",
			accuracy : accuracy, 
			conciseness : conciseness,
			redundancy : redundancy,
			grammar : grammar,
			id : id
		    });
}

function accessProfile(){
	var userid = $('#myprofile').text();
	console.log("USER ID IN ACCESS PROFILE"+userid);
	window.open("donut.html?userid=" + userid);
}
/* Below function is the old implementation of addAnswers that did not show the username 
 * of the answer giver on the right-hand side of the screen  */


/*
function addAnswers(id, username, lang){
    $.get("MainServlet", {
	type : "getanswers",
	qid : id
    }, function(json) {
	console.log(json);
	    for (i in json) {
		$("#tableq" + id).append(
		$('<tr/>').append($('<td/>').attr('id', "votes" + id + "and"+ json[i].id))
			.append($('<td/>').append(json[i].answer)));
		addVotes("votes"+id+"and"+json[i].id, parseInt(json[i].upvotes) - parseInt(json[i].downvotes));
		$('<td/>').addClass("right").append(
			'<span class="label label-warning">'
			+ 'Answered by ' + '<a href="#" id='+username+'>'+	username+'</a>'
			+ '</span>');
	    }
	    addReputation(username, lang);
    });
}
*/

/* Below function posts the answers written and submitted in the textarea */

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
	 
			
//addVotes("votes"+id+"and"+json[i].id, parseInt(json[i].upvotes) - parseInt(json[i].downvotes));
	$("#tableq" + id).append(
			$('<tr/>').append($('<td/>').attr('id', "votes" +id+"and"+ ansid))
				.append($('<td/>').append(txt)).append($('<td/>').append(
						'<span class="label label-warning">'
						+ 'Answered By' + '<a href="#" id='+getUserId()+'>'+	getUserId()+'</a>'
						+ '</span>')))								
						.append(
								$('<tr/>').append($('<td/>')).append($('<td/>').attr(
									    {
											    width : "0%",
												align : 'left'
											    }).append($('<button/>').addClass("right").addClass(
								"btn btn-default btn-xs")
								.attr({
								    id : "button" + ansid,
								    type : "button"
								}).append('Feedback').append('<br>'))));
	addVotes("votes" +id+"and"+ ansid, 0);
	//addAnswers(id, "skalburg", $(tag).prev().val(''));
    });
}

/* Below function adds the graphical icons required for upvotes and downvotes */

function addVotes(id, votes) {
    console.log("INSIDE ADD VOTES ID: "+this);
    $('#' + id)
	    .append(
		    $('<table/>').attr({
			   // width : "150%"
			})
			    .append(
				    $('<tr/>')
					    .append(
						    $('<td/>')
							    .attr(
								    {
								  //  width : "2%",
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
	    'updateVotes(this,'+ id+')');
}

/* Below function updates the votes */

function updateVotes(tag, id1) {
    console.log(tag.className);
   // var temp = id1.split('s')[1].
    var c = tag.className;
    var actualid;
    var type;
    var r = 0;
    var up, down;
    console.log("Splitting on votes: "+ id1.split("votes")[1]);
    if (c.includes('top')) {
	// type = 'q';
	 //actualid = id.split('q')[1];
	r = parseInt($(tag).parent().parent().next().first().first().text()) + 1;
	$(tag).parent().parent().next().first().first().first().text(r);
	console.log("Id from upvotes is"+$(tag).parent());
	$.get("MainServlet", {
		type : "upvote",
		up : r,
		id : id1.split("votes")[1]
	    });
    } else {
	// type = 'a';
	// actualid = id.split('a')[1];
	r = parseInt($(tag).parent().parent().prev().first().first().text()) - 1;
	$(tag).parent().parent().prev().first().first().text(r);
	$.get("MainServlet", {
		type : "downvote",
		down : r,
		id : id1.split("votes")[1]
	    });
    }
    console.log("update votes " + r);
    addVotes($(this).parent().attr('id'), r);
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