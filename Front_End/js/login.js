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
                if (response.data.roles == "ADMIN") {
                    window.location.href = "/pages/admin-dashboard.html";
                }else if (response.data.roles == "EMPLOYEE") {
                    window.location.href = "/pages/user-dashboard.html#dashboardSection";
                }
            },
            error: function (xhr, status, error) {
                showAlert("danger", "Invalid email or password");
                console.error("Error: " + error);
            }
        });
    });
})

function showAlert(type, message) {
    const alertClass = type === "success" ? "bg-success" : "bg-danger";
    const alertHtml = `
                <div class="alert ${alertClass} text-white alert-dismissible fade show" role="alert">
                    ${message}
                </div>
            `;
  
    $("#alertContainer").append(alertHtml);
  
    setTimeout(() => {
      $(".alert").alert("close");
    }, 3000);
  }
