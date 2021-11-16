function confirmDialog(message, onConfirm) {
    var fClose = function () {
        modal.modal("hide");
    };
    var modal = $("#confirmModal");
    modal.modal("show");
    $("#confirmMessage").empty().append(message);
    $("#confirmOk").unbind().one('click', onConfirm).one('click', fClose);
    $("#confirmCancel").unbind().one("click", fClose);
}

function form_submit_event(form_id){
    const form = $(`#${form_id}`);
    form.find('button:first-of-type').on('click', function(e) {
        e.preventDefault();
        $.ajax({
            url: form.attr('action'),
            type: 'post',
            data: form.serialize(),
            success: function(response) {
                if ($(response).find('.invalid-feedback li').length) {
                    form.replaceWith(response);
                    $(`#${form_id}`).removeClass("d-none");
                }else{
                    location.reload();
                }
                form_submit_event(form_id);
                swap_event(form_id);
            }
        });
    });
}

form_submit_event("form-register");
form_submit_event("form-login");

function swap_event(form_id) {
    $(`#${form_id}`).find('.auth-swap').click(function (e) {
        e.preventDefault();
        $('#form-login').toggleClass("d-none")
        $('#form-register').toggleClass("d-none")
    });
}
swap_event("form-register");
swap_event("form-login");

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
            console.log(responseText)
            // $("#register-response")
            //     .addClass("alert-success")
            //     .removeClass("alert-danger")
            //     .text(responseText);
            // $("#username-register").val("");
            // $("#password-register").val("");
            // $("#password-repeat-register").val("");
        },
        error: function (responseJSON) {
            console.log(responseJSON)
            $.each(responseJSON, function (key) {
                $("#form-register").find('.response').addClass("d-none");
            });
            $.each(responseJSON, function (key, item) {
                $("#form-register").find(`.${key}.response`)
                    .removeClass("d-none")
                    .addClass("alert-danger")
                    .removeClass("alert-success")
                    .text(key + " " + item);
            });
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
        url: "/post/create",
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

$('.delete-post').click(function (e) {
    e.preventDefault();
    const tempThis = $(this);
    confirmDialog("Delete post?", function () {
        const postDOM = tempThis.closest(".post");
        console.log()
        $.ajax({
            type: "POST",
            url: "/post/delete",
            data: {
                'id': postDOM.attr('id')
            },
            success: function (responseText) {
                postDOM.remove();
                $(".response-message-center")
                    .removeClass("d-none")
                    .addClass("alert-success")
                    .removeClass("alert-danger")
                    .css('opacity', '1')
                    .clearQueue()
                    .animate({opacity: 0}, 2000)
                    .text(responseText)
                    .delay(2000)
                    .queue(function () {
                        tempThis.addClass("d-none");
                    });
            },
            error: function ({responseText}) {
                $(".response-message-center")
                    .addClass("alert-danger")
                    .removeClass("alert-success")
                    .css('opacity', '1')
                    .clearQueue()
                    .animate({opacity: 0}, 2000)
                    .text(responseText)
                    .delay(2000)
                    .queue(function () {
                        tempThis.addClass("d-none");
                    });
            }
        })
    });
});

$('.edit-post').click(function (e) {
    console.log($(this).parent().attr('id'))
});

$('.col-comment-send button').click(function (e) {
    e.preventDefault();
    const data = $(this).closest(".card").find("input[name='comment']");
    const id = data.attr('id');
    const comment = data.val();
    $("#post-response").removeClass("d-none");
    $.ajax({
        type: "POST",
        url: "/comment/create",
        data: {
            'id': id,
            'comment': comment
        },
        success: function (responseText) {
            $(".response-message-center")
                .removeClass("d-none")
                .addClass("alert-success")
                .removeClass("alert-danger")
                .css('opacity', '1')
                .clearQueue()
                .animate({opacity: 0}, 1000)
                .text(responseText)
                .delay(1000);
            setTimeout(() => window.location.reload(), 500);
        },
        error: function ({responseText}) {
            $(".response-message-center")
                .removeClass("d-none")
                .addClass("alert-danger")
                .removeClass("alert-success")
                .css('opacity', '1')
                .clearQueue()
                .animate({opacity: 0}, 2000)
                .text(responseText)
                .delay(2000)
                .queue(function () {
                    $(".response-message-center").addClass("d-none");
                });
        }
    })
});

$('.edit-comment').click(function (e) {
    const commentDOM = $(this).closest('.comment');
    const text = commentDOM.find('.comment-text').text().replace(/ /g, '');
    if (commentDOM.find('.btn').length === 0) {
        commentDOM.find('.comment-text')
            .html($('<input class="col-comment form-control float-start" />', {'value': text}).val(text));
        commentDOM.append('<button class="btn btn-secondary float-start">\n' +
            '<i class="bi bi-pencil-fill"></i>\n' +
            '</button>')

            .delegate('.btn', 'click', function (e) {
                e.preventDefault();
                $.ajax({
                    type: "POST",
                    url: "/comment/edit",
                    data: {
                        'id': commentDOM.attr('id'),
                        'comment': commentDOM.find('.col-comment').val()
                    },
                    success: function (responseText) {
                        commentDOM.find('.comment-text').text(commentDOM.find('.col-comment').val());
                        commentDOM.find('.btn').remove();
                    },
                    error: function ({responseText}) {
                        $(".response-message-center")
                            .removeClass("d-none")
                            .addClass("alert-danger")
                            .removeClass("alert-success")
                            .css('opacity', '1')
                            .clearQueue()
                            .animate({opacity: 0}, 2000)
                            .text(responseText)
                            .delay(2000)
                            .queue(function () {
                                $(".response-message-center").addClass("d-none");
                            });
                    }
                });

            });
    }
});

$('.delete-comment').click(function (e) {
    e.preventDefault();
    const tempThis = $(this);
    confirmDialog("Delete comment?", function () {
        const commentDOM = tempThis.closest('.comment');
        console.log(commentDOM.attr('id'))
        $.ajax({
            type: "POST",
            url: "/comment/delete",
            data: {
                'id': commentDOM.attr('id')
            },
            success: function (responseText) {
                console.log('a')
                commentDOM.remove();
                $(".response-message-center")
                    .removeClass("d-none")
                    .addClass("alert-success")
                    .removeClass("alert-danger")
                    .css('opacity', '1')
                    .clearQueue()
                    .animate({opacity: 0}, 2000)
                    .text(responseText)
                    .delay(2000)
                    .queue(function () {
                        tempThis.addClass("d-none");
                    });
            },
            error: function ({responseText}) {
                console.log('b')
                $(".response-message-center")
                    .addClass("alert-danger")
                    .removeClass("alert-success")
                    .css('opacity', '1')
                    .clearQueue()
                    .animate({opacity: 0}, 2000)
                    .text(responseText)
                    .delay(2000)
                    .queue(function () {
                        tempThis.addClass("d-none");
                    });
            }
        })
    });
});

$('#display-auth').click(function () {
    $('header > .container-fluid').toggleClass("d-none")
    $('.shadow-full').removeClass("d-none")
});