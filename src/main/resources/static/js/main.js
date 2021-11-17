// AJAX form
function form_submit_event(form_id) {
    let form = $(`.${form_id}`);
        form.find('button:first-of-type').unbind().on('click', function (e) {
            e.preventDefault();
            form = $(e.currentTarget.closest('form'));
            $.ajax({
                url: form.attr('action'),
                type: 'post',
                data: form.serialize() + (form_id === "form-comment-create" ? `&post_id=${form.closest(".card").attr("id")}` : ''),
                success: function (response) {
                    console.log(response)
                    console.log($(response))
                    console.log($(response).find('.alert-success'))
                    if ($(response).find('.invalid-feedback li').length) {
                        form.replaceWith(response);
                        $(`.${form_id}`).removeClass("d-none");
                    } else if ($(response).find('.alert-success').length) {
                        form.replaceWith(response);
                        setTimeout(() => window.location.reload(), 500);
                    } else {
                        location.reload();
                    }
                    form_submit_event(form_id);
                    swap_auth_event(form_id);
                }
            });
        });
}

// AUTH
form_submit_event("form-register");
form_submit_event("form-login");
form_submit_event("form-post-create");
form_submit_event("form-comment-create");


//SWAP AUTH
function swap_auth_event(form_id) {
    if (form_id === "form-register" || form_id === "form-login") {
        $(`.${form_id}`).find('.auth-swap').click(function (e) {
            e.preventDefault();
            $('.form-login').toggleClass("d-none")
            $('.form-register').toggleClass("d-none")
        });
    }
}

swap_auth_event("form-register");
swap_auth_event("form-login");

$('#display-auth').click(function () {
    $('header > .container-fluid').toggleClass("d-none")
    $('.shadow-full').removeClass("d-none")
});

// confirm
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

// BLOG
$('.shadow-full').click(function () {
    $('.shadow-full').addClass("d-none")
    $('header > .container-fluid').addClass("d-none")
});

$('.edit-post').click(function (e) {
    const postDOM = $(this).closest(".post");
    const postFormDOM = $('#form-post-create');
    postFormDOM.find('#id').val(postDOM.attr('id'))
    postFormDOM.find('#title').val(postDOM.find('.title').text())
    postFormDOM.find('#description').val(postDOM.find('.description').text())
    $(window).scrollTop(0);
});

$('.delete-post').click(function (e) {
    e.preventDefault();
    const tempThis = $(this);
    confirmDialog("Delete post?", function () {
        const postDOM = tempThis.closest(".post");
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

// $('.col-comment-send button').click(function (e) {
//     e.preventDefault();
//     const data = $(this).closest(".card").find("input[name='comment']");
//     const id = data.attr('id');
//     const comment = data.val();
//     $("#post-response").removeClass("d-none");
//     $.ajax({
//         type: "POST",
//         url: "/comment/create",
//         data: {
//             'id': id,
//             'comment': comment
//         },
//         success: function (responseText) {
//             $(".response-message-center")
//                 .removeClass("d-none")
//                 .addClass("alert-success")
//                 .removeClass("alert-danger")
//                 .css('opacity', '1')
//                 .clearQueue()
//                 .animate({opacity: 0}, 1000)
//                 .text(responseText)
//                 .delay(1000);
//             setTimeout(() => window.location.reload(), 500);
//         },
//         error: function ({responseText}) {
//             $(".response-message-center")
//                 .removeClass("d-none")
//                 .addClass("alert-danger")
//                 .removeClass("alert-success")
//                 .css('opacity', '1')
//                 .clearQueue()
//                 .animate({opacity: 0}, 2000)
//                 .text(responseText)
//                 .delay(2000)
//                 .queue(function () {
//                     $(".response-message-center").addClass("d-none");
//                 });
//         }
//     })
// });

// $('.edit-comment').click(function (e) {
//     const commentDOM = $(this).closest('.comment');
//     const text = commentDOM.find('.comment-text').text().replace(/ /g, '');
//     if (commentDOM.find('.btn').length === 0) {
//         commentDOM.find('.comment-text')
//             .html($('<input class="col-comment form-control float-start" />', {'value': text}).val(text));
//         commentDOM.append('<button class="btn btn-secondary float-start">\n' +
//             '<i class="bi bi-pencil-fill"></i>\n' +
//             '</button>')
//
//             .delegate('.btn', 'click', function (e) {
//                 e.preventDefault();
//                 $.ajax({
//                     type: "POST",
//                     url: "/comment/edit",
//                     data: {
//                         'id': commentDOM.attr('id'),
//                         'comment': commentDOM.find('.col-comment').val()
//                     },
//                     success: function (responseText) {
//                         commentDOM.find('.comment-text').text(commentDOM.find('.col-comment').val());
//                         commentDOM.find('.btn').remove();
//                     },
//                     error: function ({responseText}) {
//                         $(".response-message-center")
//                             .removeClass("d-none")
//                             .addClass("alert-danger")
//                             .removeClass("alert-success")
//                             .css('opacity', '1')
//                             .clearQueue()
//                             .animate({opacity: 0}, 2000)
//                             .text(responseText)
//                             .delay(2000)
//                             .queue(function () {
//                                 $(".response-message-center").addClass("d-none");
//                             });
//                     }
//                 });
//
//             });
//     }
// });

$('.delete-comment').click(function (e) {
    e.preventDefault();
    const tempThis = $(this);
    confirmDialog("Delete comment?", function () {
        const commentDOM = tempThis.closest('.comment');
        $.ajax({
            type: "POST",
            url: "/comment/delete",
            data: {
                'id': commentDOM.attr('id')
            },
            success: function (responseText) {
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
