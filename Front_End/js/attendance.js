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
  updateAttendanceStatusUI();
  
});

function updateAttendanceStatusUI() {
  // Check the stored status and update the UI accordingly
  $.ajax({
    url: "http://localhost:8080/api/v1/attendance/status",
    type: "GET",
    headers: {
      Authorization: "Bearer " + token,
    },
    success: function (response) {
      console.log(response);
      if (response.data === "clockIn") {
        $("#onStatusBadge").html("You are currently clocked out!"
        );
        $("#clockInBtn").show();
        $("#clockOutBtn").hide();
      } else if (response.data === "clockOut") {
        $("#onStatusBadge").html("You are currently clocked in!"
        );
        $("#clockInBtn").hide();
        $("#clockOutBtn").show();
      } else {
        // $("#onStatusBadge").html("Not Clocked In");
        // $("#clockInBtn").show();
        // $("#clockOutBtn").hide();
      }
    },
    error: function (xhr, status, error) {
      console.log("Error fetching clock status:", error);
    },
  });
}

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
      updateAttendanceStatusUI();
    },
    error: function (xhr, status, error) {
      showAlert("danger", "You have already clocked in for today.");
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
      getAttendance();
      updateAttendanceStatusUI();
    },
    error: function (xhr, status, error) {
      showAlert("danger", "You must clock in before clocking out");
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
        const statusBadge = getStatusBadge(record.status);
        const row = `
              <tr>
                <td>${record.date}</td>
                <td >${statusBadge}</td>
                <td>${record.inTime !== null ? record.inTime : "-"}</td>
                <td>${record.outTime !== null ? record.outTime : "-"}</td>
                <td>${record.totalHours !== null ? record.totalHours : "-"}</td>
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
          const inTime = moment(
            `${record.date} ${record.inTime}`,
            "YYYY-MM-DD hh:mm A"
          );
          const outTime = moment(
            `${record.date} ${record.outTime}`,
            "YYYY-MM-DD hh:mm A"
          );
          totalMinutes += moment.duration(outTime.diff(inTime)).asMinutes();
        }

        const statusBadge = getStatusBadge(status);
        const row = `
          <tr>
            <td>${date}</td>
            <td>${day}</td>
            <td>${statusBadge}</td>
            <td>${record.inTime || "-"}</td>
            <td>${record.outTime || "-"}</td>
          </tr>
        `;
        $tbody.append(row);
      });

      // Summary calculations
      const totalHours = Math.floor(totalMinutes / 60);
      const totalRemMin = Math.floor(totalMinutes % 60);
      const avgHours =
        presentCount > 0
          ? (totalMinutes / presentCount / 60).toFixed(1)
          : "0.0";

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

    const daysOfWeek = [
      "Sunday",
      "Monday",
      "Tuesday",
      "Wednesday",
      "Thursday",
      "Friday",
      "Saturday",
    ];

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
    case "leave":
      badgeClass = "warning";
      break;
  }

  return `<span class="badge badge-gradient-${badgeClass}">${status}</span>`;
}
