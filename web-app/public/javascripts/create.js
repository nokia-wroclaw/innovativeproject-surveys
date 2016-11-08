createSub = function(event){
	var user = {};	
	user.name = $("#name").val();
	user.description = $("#description").val();
	user.email = $("#email").val();
	user.question = $("#question").val();
	user.question1 = $("#question1").val();
	user.question2 = $("#question2").val();
	user.question3 = $("#question3").val();
	console.log("POST with create");
	event.preventDefault();					//prevending reloding page
	$.ajax({
		type: 'POST',
		url: '/app/surveys',
		data: JSON.stringify(user),
		dataType: 'json',
		contentType: 'application/json',
		success: function(data, status){
			window.location.replace("/user")
		},
		error: function(data, status){
			if (data.responseText == "empty") {
				$("#error").html("Nie podano żadnego tekstu");
				$("#error").show();
			}else {
				console.log(JSON.stringify(data));
			}
		}
	})
}
		
window.onload = function(){
	$("#error").hide();
	$("#createForm").submit(createSub);
}	
