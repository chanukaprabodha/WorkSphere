$('#saveEmployeeBtn').click((e) => {
    e.preventDefault();
    console.log("clicked");
    const employee = {
        firstName : $('#firstName').val(),
        lastName : $('#lastName').val(),
        email : $('#email').val(),
        phone : $('#phone').val(),
        nic : $('#nic').val(),
        position : $('#position').val(),
        salary : $('#salary').val(),
        department : $('#department').val(),
        address : $('#address').val(),
        profilePicture : $('#profilePicture').val()
    }
    $.ajax({
        url: "http://localhost:8080/api/v1/employee/save",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(employee),
        success: function (data) {
            alert("Employee saved successfully!");
        },
        error: function (xhr, status, error) {
            alert("Error: " + error);
        }
    });
})