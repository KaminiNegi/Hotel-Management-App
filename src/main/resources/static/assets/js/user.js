$(document).ready(function () {
    fetchUsers();

    function fetchUsers() {
        $.ajax({
            url: "http://localhost:8080/api/v1/users",
            type: "GET",
            headers: { "Authorization": "Bearer " + localStorage.getItem("token") },
            success: function (response) {
                let tableBody = $("#usersTable tbody").empty();
                response.forEach(user => {
                    let row = `<tr>
                        <td>${user.id}</td>
                        <td>${user.firstname} ${user.lastname}</td>
                        <td>${user.email}</td>
                        <td>${user.roles.map(role => role.name).join(", ")}</td>
                        <td><button class="btn btn-sm btn-danger delete-btn" data-id="${user.id}">Delete</button></td>
                    </tr>`;
                    tableBody.append(row);
                });

                $(".delete-btn").click(function () { deleteUser($(this).data("id")); });
                $("#usersTable").DataTable();
            },
            error: function () { alert("Error fetching users!"); }
        });
    }
});
