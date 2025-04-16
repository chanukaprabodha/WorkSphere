var token = localStorage.getItem("jwt_token");

$(document).ready(function () { 
  loadLeaveTypes();
  loadLeaveHistory();
});

function loadLeaveHistory() { 
  $.ajax({
    url: `http://localhost:8080/api/v1/leave/recentLeaves`,
    method: "GET",
    headers: {
      Authorization: "Bearer " + token,
      contentType: "application/json",
    },
    success: function (response) {
      const leaveList = response.data;

      const tbody = $("#recentLeaveTable");
      tbody.empty();

      leaveList.forEach(leave => {
        console.log(leave);
        const dateApplied = formatDate(leave.createdAt);
        const from = formatDate(leave.startDate);
        const to = formatDate(leave.endDate);
        const type = leave.leaveType?.charAt(0) + leave.leaveType?.slice(1).toLowerCase() + " Leave";

        let statusClass = "";
        switch (leave.status) {
          case "APPROVED":
            statusClass = "badge-gradient-success";
            break;
          case "PENDING":
            statusClass = "badge-gradient-warning";
            break;
          case "REJECTED":
            statusClass = "badge-gradient-danger";
            break;
          default:
            statusClass = "badge-secondary";
        }

        const row = `
          <tr>
            <td>${dateApplied}</td>
            <td>${type}</td>
            <td>${from}</td>
            <td>${to}</td>
            <td><span class="badge ${statusClass}">${capitalize(leave.status)}</span></td>
          </tr>
        `;
        tbody.append(row);
      });
    },
    error: function (xhr, status, error) {
      showAlert("danger", "Error loading leave history");
      console.error("Error loading leave history:", error);
    }
  });
}

// Helper function to format date (e.g., "Apr 12, 2025")
function formatDate(dateStr) {
  if (!dateStr) return "-";
  const date = new Date(dateStr);
  return date.toLocaleDateString("en-US", {
    year: "numeric",
    month: "short",
    day: "numeric"
  });
}

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

function capitalize(word) {
  return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();
}

function loadLeaveTypes() {
  $.ajax({
    url: "http://localhost:8080/api/v1/leave/types",
    method: "GET",
    headers: {
      Authorization: "Bearer " + token,
    },
    success: function (response) {
      const applyLeaveSelect = $("#leaveType");
      applyLeaveSelect.empty();
      response.data.forEach((type) => {
        const formatted = capitalize(type);
        applyLeaveSelect.append(`<option value="${type}"">${formatted}</option>`);
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
    const leaveReason = $("#leaveReason").val();

    if (!leaveType || !startDate || !endDate || !leaveReason) {
      showAlert("danger", "Please fill in all the fields.");
      return;
    }

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
        leaveReason: leaveReason,
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

$("#leaveHistoryModal").on("show.bs.modal", function () {

  $.ajax({
    url: `http://localhost:8080/api/v1/leave/history`,
    type: "GET",
    headers: {
      Authorization: "Bearer " + token,
      contentType: "application/json",
    },
    success: function (response) {
      const leaveList = response.data;

      const tbody = $("#leaveHistoryTable");
      tbody.empty();

      leaveList.forEach(leave => {
        console.log(leave);
        const dateApplied = formatDate(leave.createdAt);
        const from = formatDate(leave.startDate);
        const to = formatDate(leave.endDate);
        const type = leave.leaveType?.charAt(0) + leave.leaveType?.slice(1).toLowerCase() + " Leave";
        const reason = leave.reason;

        let statusClass = "";
        switch (leave.status) {
          case "APPROVED":
            statusClass = "badge-gradient-success";
            break;
          case "PENDING":
            statusClass = "badge-gradient-warning";
            break;
          case "REJECTED":
            statusClass = "badge-gradient-danger";
            break;
          default:
            statusClass = "badge-secondary";
        }

        const row = `
          <tr>
            <td>${dateApplied}</td>
            <td>${type}</td>
            <td>${from}</td>
            <td>${to}</td>
            <td>${reason !== null ? reason : "-"}</td>
            <td><span class="badge ${statusClass}">${capitalize(leave.status)}</span></td>
          </tr>
        `;
        tbody.append(row);
      });
    },
    error: function (xhr, status, error) {
      console.error("Error fetching leave history:", error);
    },
  });
});
