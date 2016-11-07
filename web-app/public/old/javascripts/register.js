registerSub = function(event){
	var user = {};
	var login = $("#login").val();
	user.password = $("#password").val();
	user.rePassword = $("#rePassword").val();
	user.email = $("#email").val();
	user.firstName = $("#firstName").val();
	user.lastName = $("#lastName").val();
	console.log("POST with register");
	event.preventDefault();					//prevending reloding page
	$.ajax({
		type: 'PUT',
		url: '/app/user/'+login,
		data: JSON.stringify(user),
		dataType: 'json',
		contentType: 'application/json',
		success: function(data, status){
			window.location.replace("/user");
		},
		error: function(data, status){
			if (data.status == 200) {
				window.location.replace("/invitations")
			}
			$("#error").html(data.responseText);
			$("#error").show();
			/*if (data.responseText == "Bad login") {
				$("#error").html("Nie ma takiego użytkownika.");
				$("#error").show();
			} else if (data.responseText == "Bad password") {
				$("#error").html("Złe hasło!");
				$("#error").show();
			} else {
				console.log(JSON.stringify(data));
			}*/
		}
	})
}
		
$("#error").hide();
$("#registerForm").submit(registerSub);
