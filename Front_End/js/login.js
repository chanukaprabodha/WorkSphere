$(document).ready(function(e) {
    $("#btnLogin").click(function(e) {
        e.preventDefault(); 

        var email = $("#email").val();
        var password = $("#password").val();

        var data = {
            email: email,
            password: password
        };

        $.ajax({
            url: "http://localhost:8080/api/v1/auth/authenticate",
            type: "POST",
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function(response) {
                window.localStorage.setItem("jwt_token", response.data.token);
                console.log(response.data.roles);
                if (response.data.roles == "ADMIN") {
                    window.location.href = "/pages/admin-dashboard.html";
                }else if (response.data.roles == "EMPLOYEE") {
                    window.location.href = "user-dashboard.html";
                }
            },
            error: function(xhr, status, error) {
                console.error("Error: " + error);
            }
        });
    });
})
