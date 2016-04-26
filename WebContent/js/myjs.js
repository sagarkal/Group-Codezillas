$(function() {
	var url = window.location.href;
	var username = url.split("=")[1];
	//$('#myprofile').text(username);
    $.get("MainServlet", {
	type : "getquestions"
    }, function(json) {
	appendQuestions(json);
    });
});

/* Jquery function to register new users */

function register(){
	$.get("RegisterServlet", {
		userName : $('#email').val(),
		password : $('#password').val()
	},function(){
		window.location.replace("login.html");
	});
}

function getNewQid(){
    var a=[];
    $(".questions").each(function( index ) {
	a[index] = $(this).attr('id').split("tableq")[1];
    });
    return Math.max.apply(Math,a)+1;
}
function postQuestion()
{
    alert($('#newTag').val());
	$.get("MainServlet", {
		type : "savequestion",
		username : getUserId(),
		question : $('#postedQuestion').val(),
		tag : $('#newTag').val(),
		qid : getNewQid()
	    }, function(json) {
		removeOld();
		appendQuestions(json);
	});
}

function removeOld(){
    $("#questions").children().each(function( index ) {
	if (index>0){
	    $(this.remove());
	}
    });
}


/* Function to get User ID of the person who gave the answer */

function getUserId()
{
	var url = window.location.href;
	return url.split("=")[1];
}

/* Function that appends all the relevant questions in the form of a table */

function appendQuestions(json) {
    for (i in json) {
	var id = 'tableq' + json[i].id;
	var votesqid = 'votes' + json[i].id;
	var cl = json[i].tags
	$('#questions').append(
		$('<div/>').addClass(cl).append($('<table/>').attr({
		    id : id,
		    width : "100%"
		}).addClass("questions")).append($('<textarea/>').attr({
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
			$('<td/>').addClass("right").addClass("bb").append(
				'<span class="label label-info">'
					+ json[i].tags + '</span>').append(
				'<br><br>').append(
				'<span class="label label-warning">'
					+ 'Asked by ' + '<a href="#" id='+json[i].username+'q' + json[i].id+ '>'+json[i].username+'</a>'
					+ '</span>')));
	addVotes(votesqid, json[i].upvotes - json[i].downvotes);
	addAnswers(json[i].id, json[i].username, json[i].tags);
	console.log(i);
	addReputation(json[i].username+'q' + json[i].id, json[i].tags);
    }
}

function addReputation(username, lang){
    console.log("language " +lang);
    $.get("MainServlet", {
	type : "getrep",
	username : username
    }, function(json) {

	switch(lang.toLowerCase()) {
	case "java" :
	    rep = json.java;
	    break;
	case "javascript" :
	    rep = json.javascript;
	    break;
	case "python" :
	    rep = json.python;
	    break;
	case "csharp" :
	    rep = json.csharp;
	    break;
	case "cpp" :
	    rep = json.cpp;
	    break;
	}

    $('#'+username).parent().append('<br><br><span class="label label-warning">' + lang + ': ' + rep + '</span>');
	console.log("aaaaaaaaaaaaaaaaaaaaaaa");
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
    			.append($('<td/>').attr({
    				width : "60%"
    			}).append(json[i].answer)).attr({align : "left"}).append($('<td/>').append(
    					'<span class="label label-warning">'
    					+ 'Answered By' + '<a href="#" id='+json[i].username+'a' + json[i].id+'>'+	json[i].username+'</a>'
    					+ '</span>')))
    							 .append(
    							$('<tr/>').append($('<td/>')).append($('<td/>').attr(
    								    {
    										    width : "100%",
    											align : 'left'
    										    }). append($('<button/>').addClass("bb").addClass("right").addClass(
    							"btn btn-default btn-xs")
    							.attr({							
    							    id : json[i].username,
    							    type : "button",
    							    'data-toggle' : 'modal',
    								'data-target': '#myModal',
    							   //left : "50px"
    								onclick : "userIdForFeedback("+json[i].id+")"
    							}).append('Feedback').append('<br>'))));
    		addVotes("votes"+id+"and"+json[i].id, parseInt(json[i].upvotes) - parseInt(json[i].downvotes));
    		addReputation(json[i].username+'a' + json[i].id, lang);



	    }

    });
}

function loadAnothersProfile(otherUsername){
	window.location.replace("othersProfile.html?user=" + otherUsername);
}

function userIdForFeedback(id)
{
	window.location.hash = id;
}

//function openWindowForFeedback(id) {
//    window.open("feedback.html?id=" + id);
//}


function updateFeedback(accuracy, conciseness, redundancy, grammar, id, comments)
{
	 $.get("MainServlet", {
			type : "feedback",
			accuracy : accuracy,
			conciseness : conciseness,
			redundancy : redundancy,
			grammar : grammar,
			id : id,
			comments : comments
		    });
}

function accessProfile(){
	var userid = $('#myprofile').text();
	window.location.replace("profile.html?userid=" + userid);
}

/* Below function posts the answers written and submitted in the textarea */

function postResponse(tag) {
    var txt = $(tag).prev().val();
    $(tag).prev().val('');
    var id = $(tag).attr('id').split("button")[1];
    $.get("MainServlet", {
	type : "saveanswer",
	username : getUserId(),
	answer : txt,
	qid : id
    }, function(response) {
	var ansid = response;

	var username = $('#myprofile').text();

//addVotes("votes"+id+"and"+json[i].id, parseInt(json[i].upvotes) - parseInt(json[i].downvotes));
	$("#tableq" + id).append(
			$('<tr/>').append($('<td/>').attr('id', "votes" +id+"and"+ ansid))
				.append($('<td/>').attr({
					width : "60%"
				}).append(txt)).append($('<td/>').append(
						'<span class="label label-warning">'
						+ 'Answered By' + '<a href="#" id='+username+'>'+	username+'</a>'
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
	addReputation(username, $(tag).parent().attr('class'));
    });
}

/* Below function adds the graphical icons required for upvotes and downvotes */

function addVotes(id, votes) {
    $('#' + id)
	    .append(
		    $('<table/>').attr({
			   // width : "150%"
			})
			    .append(
				    $('<tr/>')
					    .append(
						    $('<td/>')
							    .append(
								    $('<span/>')
									    .addClass(
										    "glyphicon glyphicon-triangle-top "))))
			    .append($('<tr/>').append($('<td/>').addClass("tdcenter").append(votes)))
			    .append(
				    $('<tr/>')
					    .append(
						    $('<td/>')
							    .append(
								    $('<span/>')
									    .addClass(
										    "glyphicon glyphicon-triangle-bottom")))));
    $(".glyphicon-triangle-top, .glyphicon-triangle-bottom").attr('onclick',
	    'updateVotes(this)');
}

/* Below function updates the votes */

function updateVotes(tag) {
    var c = tag.className;
    var id1 = $(tag).parent().parent().parent().parent().parent().attr('id');
    var actualid;
    var type;
    var r = 0;
    var up, down;
    if (c.includes('top')) {
	r = parseInt($(tag).parent().parent().next().first().first().text()) + 1;
	$(tag).parent().parent().next().children().text(r);
	$.get("MainServlet", {
		type : "upvote",
		up : r,
		id : id1.split("votes")[1]
	    });
    } else {
	r = parseInt($(tag).parent().parent().prev().first().first().text()) - 1;
	$(tag).parent().parent().prev().children().text(r);
	$.get("MainServlet", {
		type : "downvote",
		down : r,
		id : id1.split("votes")[1]
	    });
    }
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