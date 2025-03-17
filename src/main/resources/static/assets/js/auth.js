$(document).ready(function () {
    // Handle Registration
    $("#registerForm").submit(function (event) {
        event.preventDefault();

        let userData = {
            firstname: $("#firstname").val(),
            lastname: $("#lastname").val(),
            email: $("#email").val(),
            password: $("#password").val()
        };

        $.ajax({
            url: "http://localhost:8080/api/v1/auth/register",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(userData),
            success: function () {
                $("#message").html('<div class="alert alert-success">Registration successful! Redirecting to login...</div>');
                setTimeout(() => { window.location.href = "login.html"; }, 2000);
                $("#registerForm")[0].reset();
            },
            error: function () {
                $("#message").html('<div class="alert alert-danger">Error registering. Try again.</div>');
            }
        });
    });

    // Handle Logout
    $("#logoutBtn").click(function () {
        localStorage.removeItem("token");
        window.location.href = "login.html";
    });

    // Ensure user is logged in before accessing dashboard
    if (window.location.pathname.includes("index.html") && !localStorage.getItem("token")) {
        window.location.href = "login.html";
    }
});
