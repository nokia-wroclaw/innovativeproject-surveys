loginSub = function(event){
	var user = {};
	user.login = $("#login").val();
	user.password = $("#password").val();
	console.log("POST with login");
	event.preventDefault();					//prevending reloding page
	$.ajax({
		type: 'POST',
		url: '/app/login',
		data: JSON.stringify(user),
		dataType: 'json',
		contentType: 'application/json',
		success: function(data, status){
			window.location.replace("/user")
		},
		error: function(data, status){
			if (data.responseText == "Bad login") {
				$("#error").html("Nie ma takiego użytkownika.");
				$("#error").show();
			} else if (data.responseText == "Bad password") {
				$("#error").html("Złe hasło!");
				$("#error").show();
			} else {
				console.log(JSON.stringify(data));
			}
		}
	})
}
		
window.onload = function(){
	$("#error").hide();
	$("#loginForm").submit(loginSub);
}	
