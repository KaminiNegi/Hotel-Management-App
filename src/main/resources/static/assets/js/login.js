$(document).ready(function () {
    $("#loginForm").submit(function (event) {
        event.preventDefault();

        fetch("http://localhost:8080/api/v1/auth/authenticate", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: $("#email").val(),
                password: $("#password").val()
            })
        })
        .then(response => response.json())
        .then(data => {
            if (data.access_token) {
                localStorage.setItem("token", data.access_token);
                window.location.href = "index.html";
            } else {
                alert("Invalid credentials!");
            }
        })
        .catch(error => console.error("Error:", error));
    });
});
