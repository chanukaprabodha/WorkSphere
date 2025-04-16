var token = localStorage.getItem("jwt_token");

$(document).ready(function () {
  getEmployeeDetails();
  loadUpcomingBirthdays();
  gethMonthlyAttendance();
  loadLeaveTypesDashboard();
});

function logout(e) {
  e.preventDefault();
  Swal.fire({
    title: "Are you sure?",
    text: "You will be logged out of the system.",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    cancelButtonColor: "#d33",
    confirmButtonText: "Yes, log out!",
  }).then((result) => {
    if (result.isConfirmed) {
      localStorage.removeItem("jwt_token");
      window.location.href = "login.html";
    }
  });
}

function loadLeaveTypesDashboard() {
  $.ajax({
    url: "http://localhost:8080/api/v1/leave/types",
    method: "GET",
    headers: {
      Authorization: "Bearer " + token,
    },
    success: function (response) {
      const dashboardSelect = $("#leaveTypeDashboard");
      dashboardSelect.empty();
      response.data.forEach((type) => {
        dashboardSelect.append(`<option value="${type}"">${type}</option>`);
      });
    },
    error: function () {
      showAlert("danger", "Failed to load leave types");
      console.error("Failed to load leave types");
    },
  });
}

function sendLeaveRequestDashboard(e) {
  e.preventDefault();
  if (confirm("Are you sure you want to apply for leave?")) {
    const leaveType = $("#leaveTypeDashboard").val();
    const startDate = $("#fromDateDashboard").val();
    const endDate = $("#toDateDashboard").val();

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

function gethMonthlyAttendance() {
  $.ajax({
    url: "http://localhost:8080/api/v1/attendance/getMonthlyAttendance",
    method: "GET",
    headers: {
      Authorization: "Bearer " + token,
    },
    success: function (res) {
      const records = res.data;
      const today = moment();
      const totalDays = today.date(); // e.g., 30 on 30th

      let presentCount = 0;
      $.each(records, function (index, record) {
        if (record.status === "PRESENT") presentCount++;
      });

      $("#totalAttendance").text(`${presentCount}/${totalDays} Days`);
      $("#attendanceSummaryText").text(`Present this Month`);
    },
    error: function (xhr, status, error) {
      console.error("Failed to fetch monthly attendance:", error);
    },
  });
}

function getEmployeeDetails() {
  $.ajax({
    url: "http://localhost:8080/api/v1/employee/getEmployeeDetails",
    type: "GET",
    headers: {
      Authorization: "Bearer " + token,
      contentType: "application/json",
    },
    success: function (response) {
      const data = response.data;
      $("#employeeName").text(
        data.firstName + " " + data.lastName
      );
      $("#employeePosition").text(data.position);
      $("#leaveBalance").text(data.leaveBalance);
      $("#employeeProfileImg").attr("src", data.profilePicture);
      $("#employeeProfileImgNavBar").attr("src", data.profilePicture);

      // Set the leave details
      const totalEntilment = 28;
      const usedLeaves = 28 - data.leaveBalance;
      $("#totalLeaveCount").text(totalEntilment);
      $("#leaveUsedCount").text(usedLeaves);
      $("#leaveBalanceCount").text(data.leaveBalance);

      // Casual
      $("#casualTotal").text(7);
      const casualUsed = Math.max(7 - data.casualLeave);
      $("#casualUsed").text(casualUsed);
      $("#casualBalance").text(7 - casualUsed);

      // Sick
      $("#sickTotal").text(7);
      const sickUsed = Math.max(7 - data.sickLeave);
      $("#sickUsed").text(sickUsed);
      $("#sickBalance").text(7 - sickUsed);

      // Annual
      $("#annualTotal").text(14);
      const annualUsed = Math.max(14 - data.annualLeaves);
      $("#annualUsed").text(annualUsed);
      $("#annualBalance").text(14 - annualUsed);

      // Totals
      $("#totalTotal").text(totalEntilment);
      $("#totalUsed").text(usedLeaves);
      $("#totalBalance").text(data.leaveBalance);
      
    },
    error: function (xhr, status, error) {
      console.log("Error fetching employee details:", error);
      // alert("Failed to load employee data. Please log in again.");
    },
  });
}

function loadUpcomingBirthdays() {
  $.ajax({
    url: "http://localhost:8080/api/v1/employee/upcomingBirthdays",
    method: "GET",
    headers: {
      Authorization: "Bearer " + token,
    },
    success: function (response) {
      const birthdayList = $("#birthdays .list-group");
      birthdayList.empty();

      response.data.forEach((emp) => {
        const birthday = new Date(emp.birthday);
        const formattedDate = birthday.toLocaleDateString("en-US", {
          month: "long",
          day: "numeric",
        });

        const birthdayBadge = `<span class="badge bg-primary rounded-pill">${formattedDate}</span>`;

        const employeeHTML = `
          <div class="list-group-item ">
            <div class = "d-flex align-items-center justify-content-around">
              <img src="${emp.profilePicture}" class="rounded-circle me-3" alt="Employee Avatar" style="width: 50px; height: 50px; object-fit: cover"/>
              <div class = "d-flex flex-column">
                <h5 class="mb-1">${emp.firstName}</h5>
                <p class="text-muted mb-0">${emp.departmentId}</p>
              </div>
              ${birthdayBadge}
            </div>
          </div>`;
        birthdayList.append(employeeHTML);
      });
    },
    error: function () {
      console.error("Error fetching upcoming birthdays");
    },
  });
}

function loadAllBirthdays() {

  $.ajax({
    url: "http://localhost:8080/api/v1/employee/allBirthdays",
    method: "GET",
    headers: {
      Authorization: "Bearer " + token,
    },
    success: function (response) {
      const allList = $("#allBirthdaysList");
      allList.empty();

      response.data.forEach((emp) => {
        const birthday = new Date(emp.birthday);
        const formattedDate = birthday.toLocaleDateString("en-US", {
          month: "long",
          day: "numeric",
        });

        const item = `
          <li class="list-group-item d-flex justify-content-between align-items-center">
            <div class="d-flex align-items-center">
            <img src="${emp.profilePicture}" class="rounded-circle me-3" alt="Employee Avatar" style="width: 50px; height: 50px; object-fit: cover"/>
              <div class = "d-flex flex-column">
                <h6 class="mb-1">${emp.firstName}</h6>
                <small class="text-muted">${emp.departmentId}</small>
              </div>
            </div>
            <span class="badge bg-info rounded-pill">${formattedDate}</span>
          </li>`;
        allList.append(item);
      });
    },
    error: function () {
      console.error("Error loading all birthdays");
    },
  });
}

$("#employeeSearch").on("input", function () {
  const keyword = $(this).val();
  if (keyword.trim() === "") {
    $("#employeeSearchResults").hide();
    $("#employeeCards").empty();
    return;
  }

  $.ajax({
    url: `http://localhost:8080/api/v1/employee/search?keyword=${keyword}`,
    type: "GET",
    headers: {
      Authorization: "Bearer " + token,
    },
    success: function (response) {
      console.log(response);
      const employees = response.data;
      $("#employeeCards").empty();
      if (employees.length > 0) {
        $("#employeeSearchResults").show();
        employees.forEach(emp => {
          const card = `
            <div class="col-md-6 mb-3">
              <div class="card employee-card" onclick="loadEmployeeDetails('${emp.id}')">
                <div class="card-body">
                  <div class="d-flex align-items-center mb-3">
                    <div class="employee-avatar me-3">
                      <img src="${emp.profilePicture || '/assets/images/faces/face1.jpg'}" alt="Employee" class="rounded-circle" width="60" height="60" style="object-fit: cover;">
                      <span class="status-indicator active"></span>
                    </div>
                    <div>
                      <h5 class="employee-name mb-1">${emp.firstName} ${emp.lastName}</h5>
                      <p class="text-muted mb-0">ID: ${emp.id}</p>
                    </div>
                  </div>
                  <div class="employee-details">
                    <p class="mb-1"><i class="mdi mdi-email-outline text-primary me-1"></i> ${emp.email}</p>
                    <p class="mb-1"><i class="mdi mdi-briefcase text-success me-1"></i> ${emp.departmentId}</p>
                    <p class="mb-1"><i class="mdi mdi-phone text-info me-1"></i> ${emp.phone}</p>
                  </div>
                </div>
              </div>
            </div>`;
          $("#employeeCards").append(card);
        });
      } else {
        $("#employeeSearchResults").hide();
      }
    },
    error: function () {
      console.log("Search failed");
    },
  });
});

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
