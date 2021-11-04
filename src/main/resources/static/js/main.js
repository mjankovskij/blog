// AUTH

$('#register').click(function (e) {
    e.preventDefault();
    $("#post-response").removeClass("d-none");
    $.ajax({
        type: "POST",
        url: "/create",
        data: {
            'username': $("#title").val(),
            'password': $("#description").val(),
            'passwordRepeat': $("#description").val()

        },
        success: function (responseText) {
            $("#post-response")
                .addClass("alert-success")
                .removeClass("alert-danger")
                .text(responseText);
            $("#title").val("");
            $("#description").val("");
        },
        error: function ({responseText}) {
            $("#post-response")
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
            $("#title").val("");
            $("#description").val("");
        },
        error: function ({responseText}) {
            $("#post-response")
                .addClass("alert-danger")
                .removeClass("alert-success")
                .text(responseText);
        }
    })
});
