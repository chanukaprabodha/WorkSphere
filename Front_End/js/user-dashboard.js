$(document).ready(function () {
  getEmployeeDetails();
  loadUpcomingBirthdays();
  gethMonthlyAttendance();
});

function gethMonthlyAttendance() { 
  const token = localStorage.getItem("jwt_token");

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
  var token = localStorage.getItem("jwt_token");
  $.ajax({
    url: "http://localhost:8080/api/v1/employee/getEmployeeDetails",
    type: "GET",
    headers: {
      Authorization: "Bearer " + token,
      contentType: "application/json",
    },
    success: function (response) {
        $("#employeeName").text(
          response.data.firstName + " " + response.data.lastName
        );
        $("#employeePosition").text(response.data.position);
        $("#leaveBalance").text(response.data.leaveBalance);
      
    },
    error: function (xhr, status, error) {
      console.log("Error fetching employee details:", error);
      // alert("Failed to load employee data. Please log in again.");
    },
  });
}

function loadUpcomingBirthdays() {
  const token = localStorage.getItem("jwt_token");

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
  const token = localStorage.getItem("jwt_token");

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
