var token = localStorage.getItem("jwt_token");

document.getElementById("profilePicture").addEventListener("change", function (event) {
    const file = event.target.files[0];
    const preview = document.getElementById("imagePreview");
  
    if (file && file.type.startsWith("image/")) {
      const reader = new FileReader();
  
      reader.onload = function (e) {
        preview.style.backgroundImage = `url(${e.target.result})`;
  
        // Optionally hide the icon after image is loaded
        const icon = preview.querySelector("i");
        if (icon) icon.style.display = "none";
      };
  
      reader.readAsDataURL(file);
    }
  });
  

  $('#saveEmployeeBtn').click((e) => {
    e.preventDefault();

    const formData = new FormData();

    formData.append("firstName", $('#firstName').val());
    formData.append("lastName", $('#lastName').val());
    formData.append("email", $('#email').val());
    formData.append("phone", $('#phone').val());
    formData.append("nic", $('#nic').val());
    formData.append("position", $('#position').val());
    formData.append("salary", $('#salary').val());
    formData.append("departmentId", $('#department').val());
    formData.append("address", $('#address').val());
    formData.append("roles", $('#roles').val());
    formData.append("birthday", $('#birthday').val());
    formData.append("profilePicture", $('#profilePicture')[0].files[0]);

    for (let pair of formData.entries()) {
        console.log(pair[0] + ':', pair[1]);
      }
      
    $.ajax({
        url: "http://localhost:8080/api/v1/employee/save",
        type: "POST",
        contentType: false,
        processData: false,
        headers: {
            Authorization: "Bearer " + token,
        },
        data: formData,
        success: function (data) {
            cleaFields();
            getEmployeesWithoutSystemAccess();
            getTotalEmployeeCount();
            showAlert.success("Employee saved successfully!");
        },
        error: function (xhr, status, error) {
            console.error("Response Text:", xhr.responseText);
            showAlert("danger", "Error saving employee: " + error);
        }
    });
});


function cleaFields() {
    $('#firstName').val('');
    $('#lastName').val('');
    $('#email').val('');
    $('#phone').val('');
    $('#nic').val('');
    $('#position').val('');
    $('#salary').val('');
    $('#department').val('');
    $('#address').val('');
    $('#profilePicture').val('');
    $('#roles').val('');
    $('#birthday').val('');
}