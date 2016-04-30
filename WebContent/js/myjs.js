$(function() {
	var url = window.location.href;
	var username = url.split("=")[1].split('#')[0];
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
	return url.split("=")[1].split('#')[0];
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
		    cols : "75",
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
		})).append($('<td/>').addClass("bb").append('<h3><b>Q. ' + json[i].question + '</b></h3>')).append(
			$('<td/>').addClass("right").addClass("bb").append(
				'<span class="label label-info">'
					+ json[i].tags + '</span>').append(
				'<br><br>').append(
				'<span class="label label-warning">'
					+ 'Asked by ' + '<a href="#" id='+json[i].username+'question' + json[i].id+ '>'+json[i].username+'</a>'
					+ '</span>')
					));
	addVotes(votesqid, json[i].upvotes - json[i].downvotes);
	addAnswers(json[i].id, json[i].username, json[i].tags);
	addReputation(json[i].username+'question' + json[i].id, json[i].tags);	
    }
}

function addReputation(username, lang){
	var un;
	if(username.indexOf("answer")>-1)
		un = username.split("answer")[0];
	else
		un = username.split("question")[0];
    $.get("MainServlet", {
	type : "getrep",
	username : un
    }, function(json) {
    var reputation;
	switch(lang.toLowerCase()) {
	case "java" :
		reputation = json.java;
	    break;
	case "javascript" :
		reputation = json.javascript;
	    break;
	case "python" :
		reputation = json.python;
	    break;
	case "csharp" :
		reputation = json.csharp;
	    break;
	case "cpp" :
		reputation = json.cpp;
	    break;
	}
    $('#'+username).parent().parent().append('<br><br><span class="label label-warning">' + lang + ' reputation : ' + reputation + '</span><br><br>');
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

function addAnswers(id, username, lang){
	
    $.get("MainServlet", {
	type : "getanswers",
	qid : id
    }, function(json) {
    	for (i in json) {
    		var hrefVal = "othersProfile.html?myuserid="+username+"&touserid="+json[i].username;
    		$("#tableq" + id).append(
    		$('<tr/>').append($('<td />').attr('id',"votes" + id + "and"+ json[i].id))
    			.append($('<td/>').attr({
    				width : "60%"
    			}).append(json[i].answer)).attr({align : "left"}).append($('<td/>').append(
    					'<br><span class="label label-warning">'
    					+ 'Answered By' + '<a href="' +hrefVal + '" id='+json[i].username+'answer' + json[i].id+'>'+	json[i].username+'</a>'
    					+ '</span>')
    					))
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
    							}).append('Give feedback').append('<br>'))));
    		addVotes("votes"+id+"and"+json[i].id, parseInt(json[i].upvotes) - parseInt(json[i].downvotes));
    		addReputation(json[i].username+'answer' + json[i].id, lang);
	    }
    });
}

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
	$("#tableq" + id).append(
			$('<tr/>').append($('<td/>').attr('id', "votes" +id+"and"+ ansid))
				.append($('<td/>').attr({
					width : "60%"
				}).append(txt)).append($('<td/>').append(
						'<span class="label label-warning">'
						+ 'Answered By' + '<a href="#" id='+username+'>'+username+'</a>'
						
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
								    type : "button",
								    'data-toggle' : 'modal',
	    							'data-target': '#myModal',
	    							onclick : "userIdForFeedback("+ansid+")"
								}).append('Feedback').append('<br>'))));
	addVotes("votes" +id+"and"+ ansid, 0);
	addReputation(username+'answer' +ansid, $(tag).parent().attr('class'));
    });
}

function loadAnothersProfile(otherUsername){
	window.location.replace("othersProfile.html?user=" + otherUsername);
}

function userIdForFeedback(id)
{
	window.location.hash = id;
}

function updateFeedback(novice, details, unique, motivation, id, comments)
{
	 $.get("MainServlet", {
			type : "feedback",
			novice : novice,
			details : details,
			unique : unique,
			motivation : motivation,
			id : id,
			comments : comments
		    });
}

function accessProfile(){
	var userid = $('#myprofile').text();
	window.location.replace("profile.html?userid=" + userid);
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
    var user, u;
    console.log("PURE ID: "+id1);
    if(!id1.includes("and"))
    	{
    	user = $(tag).parent().parent().parent().parent().parent().next().next().children().next().next().next().children().attr('id');
    	u = user.split("question")[0];
        console.log("IN UPVOTES section, TRACING TO USERNAME; "+u);
    
    	} else{
    		u = $(tag).parent().parent().parent().parent().parent().next().next().children().next().children().text();
    		console.log("ITS AN ANSWER! :"+ u);
    	}
    id1 = id1+"user" +u;
    console.log("CONTINUING IN UPVOTES, ID: "+id1);
    var actualid;
    var type;
    var r = 0;
    var up, down;
    if (c.includes('top')) {
	r = parseInt($(tag).parent().parent().next().first().first().text()) + 1;
	$(tag).parent().parent().next().children().text(r);
	console.log("In UPVOTE section, r: "+r);
	$.get("MainServlet", {
		type : "upvote",
		up : r,
		id : id1.split("votes")[1]
	    }, function(response){
	    	if(response === "0")
	    		{
	    		$(tag).parent().parent().next().children().text(r-1);
	    		window.alert("You have already upvoted this item!");
	    		}   		
	    });
    } else {
	r = parseInt($(tag).parent().parent().prev().first().first().text()) - 1;
	$(tag).parent().parent().prev().children().text(r);
	$.get("MainServlet", {
		type : "downvote",
		down : r,
		id : id1.split("votes")[1]
	    }, function(response){
	    	if(response === "0")
    		{
	    	$(tag).parent().parent().prev().children().text(r+1);
    		window.alert("You have already downvoted this item!");
    		}   		
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