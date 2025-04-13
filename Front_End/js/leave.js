var token = localStorage.getItem("jwt_token");
$(document).ready(function () {
  loadLeaveTypes();
});

function leaveToday(e) {
  e.preventDefault();

  if (confirm("Are you sure you want to apply for leave today?")) {
    const today = new Date();
    const tomorrow = new Date();
    tomorrow.setDate(today.getDate() + 1);

    // Format both dates to YYYY-MM-DD
    const startDate = today.toISOString().split("T")[0];
    const endDate = tomorrow.toISOString().split("T")[0];

    $.ajax({
      url: "http://localhost:8080/api/v1/leave/apply",
      type: "POST",
      contentType: "application/json",
      headers: {
        Authorization: "Bearer " + token,
      },
      data: JSON.stringify({
        startDate: startDate,
        endDate: endDate,
        leaveType: "ANNUAL",
      }),
      success: function (response) {
        showAlert("success", "Leave applied successfully!");
      },
      error: function (xhr, status, error) {
        showAlert("danger", "Error applying leave: " + error);
      },
    });
  } else {
    return;
  }
}

function leaveTomorrow(e) {
  e.preventDefault();
  if (confirm("Are you sure you want to apply for leave tomorrow?")) {
    const day1 = new Date();
    day1.setDate(day1.getDate() + 1);
    const day2 = new Date();
    day2.setDate(day2.getDate() + 2);

    // Format both dates to YYYY-MM-DD
    const startDate = day1.toISOString().split("T")[0];
    const endDate = day2.toISOString().split("T")[0];

    $.ajax({
      url: "http://localhost:8080/api/v1/leave/apply",
      type: "POST",
      contentType: "application/json",
      headers: {
        Authorization: "Bearer " + token,
      },
      data: JSON.stringify({
        startDate: day1,
        endDate: day2,
        leaveType: "ANNUAL",
      }),
      success: function (response) {
        showAlert("success", "Leave applied successfully!");
      },
      error: function (xhr, status, error) {
        showAlert("danger", "Error applying leave: " + error);
      },
    });
  } else {
    return;
  }
}

function showAlert(type, message) {
  const alertClass = type === "success" ? "bg-success" : "bg-danger";
  const alertHtml = `
              <div class="alert ${alertClass} text-white alert-dismissible fade show" role="alert">
                  ${message}
                  <button type="button" class="btn-close btn-close-white" data-bs-dismiss="alert" aria-label="Close"></button>
              </div>
          `;

  $("#alertContainer").append(alertHtml);

  setTimeout(() => {
    $(".alert").alert("close");
  }, 3000);
}

function loadLeaveTypes() {
  $.ajax({
    url: "http://localhost:8080/api/v1/leave/types",
    method: "GET",
    headers: {
      Authorization: "Bearer " + token,
    },
    success: function (response) {
      const select = $("#leaveType");
      select.empty();

      response.data.forEach((type) => {
        select.append(`<option value="${type}">${type}</option>`);
      });
    },
    error: function () {
      console.error("Failed to load leave types");
    },
  });
}

function sendLeaveRequest(e) {
  e.preventDefault();
  if (confirm("Are you sure you want to apply for leave?")) {
    const leaveType = $("#leaveType").val();
    const startDate = $("#fromDate").val();
    const endDate = $("#toDate").val();

    $.ajax({
      url: "http://localhost:8080/api/v1/leave/apply",
      method: "POST",
      contentType: "application/json",
      headers: {
        Authorization: "Bearer " + token,
      },
      data: JSON.stringify({
        leaveType: leaveType,
        startDate: startDate,
        endDate: endDate,
      }),
      success: function (response) {
        showAlert("success", "Leave request sent successfully!");
      },
      error: function (xhr, status, error) {
        showAlert("danger", "Error sending leave request: ");
        console.error(error);
      },
    });
  } else {
    return;
  }
}
