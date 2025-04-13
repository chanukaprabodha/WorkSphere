document.addEventListener("DOMContentLoaded", function () {
  const todayBtn = document.getElementById("todayBtn");
  const weekBtn = document.getElementById("weekBtn");
  const monthBtn = document.getElementById("monthBtn");

  const startInput = document.getElementById("startDate");
  const endInput = document.getElementById("endDate");

  // Helper to format date as yyyy-mm-dd
  function formatDate(date) {
    const d = new Date(date);
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, "0");
    const day = String(d.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  }

  todayBtn.addEventListener("click", function () {
    const today = new Date();
    const formatted = formatDate(today);
    startInput.value = formatted;
    endInput.value = formatted;
  });

  weekBtn.addEventListener("click", function () {
    const today = new Date();
    const day = today.getDay(); // 0 = Sunday, 1 = Monday, ...
    const diffToMonday = today.getDate() - day + (day === 0 ? -6 : 1);
    const monday = new Date(today.setDate(diffToMonday));
    const sunday = new Date(monday);
    sunday.setDate(monday.getDate() + 6);

    startInput.value = formatDate(monday);
    endInput.value = formatDate(sunday);
  });

  monthBtn.addEventListener("click", function () {
    const today = new Date();
    const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
  
    startInput.value = formatDate(firstDay);
    endInput.value = formatDate(today); // use today's date, not end of month
  });
  
});

$(document).ready(function () {
  getAttendance();
  // Check the stored status and update the UI accordingly
  var clockInStatus = localStorage.getItem("clockInStatus");
  if (clockInStatus === "clockedIn") {
    var clockInTime = localStorage.getItem("clockInTime");
    $("#onStatusBadge").html(
      '<i class="mdi mdi-check-circle me-2"></i>Clocked In at ' + clockInTime
    );
    $("#clockInBtn").hide();
    $("#clockOutBtn").show();
  } else if (clockInStatus === "clockedOut") {
    var clockOutTime = localStorage.getItem("clockOutTime");
    $("#onStatusBadge").html(
      '<i class="mdi mdi-check-circle me-2"></i>Clocked Out at ' + clockOutTime
    );
    $("#clockInBtn").show();
    $("#clockOutBtn").hide();
  } else {
    $("#clockInBtn").show();
    $("#clockOutBtn").hide();
  }
});

$("#clockInBtn").click(function (e) {
  e.preventDefault();
  var token = localStorage.getItem("jwt_token");
  $.ajax({
    url: "http://localhost:8080/api/v1/attendance/clockIn",
    type: "POST",
    headers: {
      Authorization: "Bearer " + token,
      contentType: "application/json",
    },
    success: function (response) {
      showAlert("success", "You have clocked in successfully!");
      var currentTime = new Date().toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
      });
      $("#onStatusBadge").html(
        '<i class="mdi mdi-check-circle me-2"></i>Clocked In at ' + currentTime
      );
      localStorage.setItem("clockInStatus", "clockedIn");
      localStorage.setItem("clockInTime", currentTime);
      $("#clockInBtn").hide();
      $("#clockOutBtn").show();
    },
    error: function (xhr, status, error) {
      showAlert("danger", "Clocking in failed: ");
      console.log("Error clocking in:", error);
    },
  });
});

$("#clockOutBtn").click(function (e) {
  e.preventDefault();
  var token = localStorage.getItem("jwt_token");
  $.ajax({
    url: "http://localhost:8080/api/v1/attendance/clockOut",
    type: "POST",
    headers: {
      Authorization: "Bearer " + token,
      contentType: "application/json",
    },
    success: function (response) {
      showAlert("success", "You have clocked out successfully!");
      var currentTime = new Date().toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
      });
      $("#onStatusBadge").html(
        '<i class="mdi mdi-check-circle me-2"></i>Clocked Out at ' + currentTime
      );
      localStorage.setItem("clockInStatus", "clockedOut");
      localStorage.setItem("clockOutTime", currentTime);
      $("#clockInBtn").show();
      $("#clockOutBtn").hide();
      getAttendance();
    },
    error: function (xhr, status, error) {
      showAlert("danger", "Clocking out failed");
      console.log("Error clocking out:", error);
    },
  });
});

function getAttendance() {
  var token = localStorage.getItem("jwt_token");
  $.ajax({
    url: "http://localhost:8080/api/v1/attendance/lastTwoRecords",
    type: "GET",
    headers: {
      Authorization: "Bearer " + token,
      contentType: "application/json",
    },
    success: function (response) {
      const attendanceRecords = response.data;
      const tableBody = $("#attendanceTable");
      tableBody.empty();

      if (!Array.isArray(attendanceRecords) || attendanceRecords.length === 0) {
        console.error("No attendance records found.");
        return;
      }

      attendanceRecords.forEach((record) => {
        let statusClass = "";
        switch (record.status.toLowerCase()) {
          case "present":
            statusClass = "text-success";
            break;
          case "absent":
            statusClass = "text-danger";
            break;
          case "leave":
            statusClass = "text-warning";
            break;
          default:
            statusClass = "text-secondary";
        }

        const row = `
              <tr>
                <td>${record.date}</td>
                <td class="${statusClass}">${record.status}</td>
                <td>${record.inTime}</td>
                <td>${record.outTime}</td>
                <td>${record.totalHours}</td>
              </tr>
            `;
        tableBody.append(row);
      });
    },
    error: function (xhr, status, error) {
      showAlert("danger", "Error fetching attendance records");
      console.log("Error fetching attendance records:", error);
    },
  });
}

$("#applyDateRange").click(function () {
  const fromDate = $("#startDate").val();
  const toDate = $("#endDate").val();
  const token = localStorage.getItem("jwt_token");

  if (!fromDate || !toDate) {
    alert("Please select both From and To dates.");
    return;
  }

  $.ajax({
    url: `http://localhost:8080/api/v1/attendance/getAttendanceByRange?fromDate=${fromDate}&toDate=${toDate}`,
    method: "GET",
    headers: {
      Authorization: "Bearer " + token,
      contentType: "application/json",
    },
    success: function (res) {
      const records = res.data;
      const $tbody = $("#attendanceSectionTable");
      $tbody.empty();

      let presentCount = 0;
      let absentCount = 0;
      let totalMinutes = 0;

      $.each(records, function (index, record) {
        const date = record.date;
        const day = moment(date).format("dddd");

        const status = record.status;
        if (status === "PRESENT") presentCount++;
        if (status === "ABSENT") absentCount++;

        // Time calculations
        if (record.inTime && record.outTime) {
          const inTime = moment(`${record.date} ${record.inTime}`, "YYYY-MM-DD hh:mm A");
          const outTime = moment(`${record.date} ${record.outTime}`, "YYYY-MM-DD hh:mm A");
          totalMinutes += moment.duration(outTime.diff(inTime)).asMinutes();
        }

        const statusBadge = getStatusBadge(status);
        const row = `
          <tr>
            <td>${date}</td>
            <td>${day}</td>
            <td>${statusBadge}</td>
            <td>${record.inTime || '-'}</td>
            <td>${record.outTime || '-'}</td>
          </tr>
        `;
        $tbody.append(row);
      });

      // Summary calculations
      const totalHours = Math.floor(totalMinutes / 60);
      const totalRemMin = Math.floor(totalMinutes % 60);
      const avgHours = presentCount > 0 ? (totalMinutes / presentCount / 60).toFixed(1) : "0.0";

      // Update summary cards
      $("#presentDays").text(presentCount);
      $("#absentDays").text(absentCount);
      $("#totalHours").text(`${totalHours}h ${totalRemMin}m`);
      $("#avgHours").text(`${avgHours}h`);
    },
    error: function (xhr, status, error) {
      console.error("Error fetching data:", error);
      showAlert("Something went wrong while fetching attendance.");
    },
  });
});


function updateAttendanceTable(attendanceList) {
  const $tbody = $("#attendanceSectionTable");
  $tbody.empty(); // Clear existing rows

  $.each(attendanceList, function (index, record) {
    const date = new Date(record.date);
  
    const daysOfWeek = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
  
    const dayName = daysOfWeek[date.getDay()];
  
    const statusBadge = getStatusBadge(record.status);
  
    const row = `
      <tr>
        <td>${record.date}</td>
        <td>${dayName}</td> <!-- Use the day name here -->
        <td>${statusBadge}</td>
        <td>${record.inTime}</td>
        <td>${record.outTime}</td>
      </tr>
    `;
  
    $tbody.append(row);
  });
}

function getStatusBadge(status) {
  let badgeClass = "info";

  switch (status.toLowerCase()) {
    case "present":
      badgeClass = "success";
      break;
    case "absent":
      badgeClass = "danger";
      break;
    case "late":
      badgeClass = "warning";
      break;
  }

  return `<span class="badge badge-gradient-${badgeClass}">${status}</span>`;
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
