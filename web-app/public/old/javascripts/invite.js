inviteSub = function(event){
	var user = {};
	user.mail = $("#mail").val();
	console.log("POST with invite");
	event.preventDefault();					//prevending reloding page
	$.ajax({
		type: 'POST',
		url: '/app/invitation',
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
			}else if (data.responseText == "good") {
				$("#error").html("Poprawnie wyslano zaproszenie");
				$("#error").show();
			}else if (data.responseText == "zly mail") {
				$("#error").html("Podano zly adres mail");
				$("#error").show();
			}else {
				console.log(JSON.stringify(data));
			}
		}
	})
}
		
window.onload = function(){
	$("#error").hide();
	$("#inviteForm").submit(inviteSub);
}	
