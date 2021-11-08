// AUTH
$('#register').click(function (e) {
    e.preventDefault();
    $("#register-response").removeClass("d-none");
    $.ajax({
        type: "POST",
        url: "/user/register",
        data: {
            'username': $("#username-register").val(),
            'password': $("#password-register").val(),
            'passwordRepeat': $("#password-repeat-register").val()

        },
        success: function (responseText) {
            $("#register-response")
                .addClass("alert-success")
                .removeClass("alert-danger")
                .text(responseText);
            $("#username-register").val("");
            $("#password-register").val("");
            $("#password-repeat-register").val("");
        },
        error: function ({responseText}) {
            $("#register-response")
                .addClass("alert-danger")
                .removeClass("alert-success")
                .text(responseText);
        }
    })
});

$('#login').click(function (e) {
    e.preventDefault();
    $("#login-response").removeClass("d-none");
    $.ajax({
        type: "POST",
        url: "/user/login",
        data: {
            'username': $("#username-login").val(),
            'password': $("#password-login").val()
        },
        success: function (responseText) {
            $("#login-response")
                .addClass("alert-success")
                .removeClass("alert-danger")
                .text(responseText);
            setTimeout(() => window.location.reload(), 500);
        },
        error: function ({responseText}) {
            $("#login-response")
                .addClass("alert-danger")
                .removeClass("alert-success")
                .text(responseText);
        }
    })
});

$('#display-auth').click(function () {
    $('header > .container-fluid').toggleClass("d-none")
    $('.shadow-full').removeClass("d-none")
});

$('.auth-swap').click(function (e) {
    e.preventDefault();
    $('#form-login').toggleClass("d-none")
    $('#form-register').toggleClass("d-none")
});

// BLOG

$('.shadow-full').click(function () {
    $('.shadow-full').addClass("d-none")
    $('header > .container-fluid').addClass("d-none")
});

$('#create-post').click(function (e) {
    e.preventDefault();
    $("#post-response").removeClass("d-none");
    $.ajax({
        type: "POST",
        url: "/create",
        data: {
            'title': $("#title").val(),
            'description': $("#description").val()
        },
        success: function (responseText) {
            $("#post-response")
                .addClass("alert-success")
                .removeClass("alert-danger")
                .text(responseText);
            setTimeout(() => window.location.reload(), 500);
        },
        error: function ({responseText}) {
            $("#post-response")
                .addClass("alert-danger")
                .removeClass("alert-success")
                .text(responseText);
        }
    })
});
