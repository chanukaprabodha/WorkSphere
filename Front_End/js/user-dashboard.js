$(document).ready(function () {
  var token = localStorage.getItem("jwt_token"); // Retrieve the stored JWT token
  console.log(token);

  $.ajax({
    url: "http://localhost:8080/api/v1/employee/getEmployeeDetails",
    type: "GET",
    headers: {
      Authorization: "Bearer " + token,
      contentType: "application/json",
  },
    success: function (response) {
      if (response.code === 200) {
        console.log("Employee Details:", response.data);
        $("#employeeName").text(response.data.firstName + " " + response.data.lastName);
        $("#employeePosition").text(response.data.position);
      } else {
        console.error("Error:", response.message);
      }
    },
    error: function (xhr, status, error) {
      console.log("Error fetching employee details:", error);
      // alert("Failed to load employee data. Please log in again.");
    },
  });
});
