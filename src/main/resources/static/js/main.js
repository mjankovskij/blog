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
                if ($(response).find('.invalid-feedback li').length) {
                    form.replaceWith(response);
                    $(`.${form_id}`).removeClass("d-none");
                } else if ($(response).find('.alert-success').length) {
                    form.replaceWith(response);
                    if (form_id === "form-post-create" && form.find("#id").val() === '') {
                        setTimeout(() => window.location.href = window.location.origin, 500);
                    } else {
                        setTimeout(() => window.location.reload(), 500);
                    }
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

$('.shadow-full').click(function () {
    $('.shadow-full').addClass("d-none")
    $('header > .container-fluid').addClass("d-none")
});

swap_auth_event("form-register");
swap_auth_event("form-login");

$('#display-auth').click(function () {
    $('header > .container-fluid').toggleClass("d-none")
    $('.shadow-full').removeClass("d-none")
});

// confirm
function confirmDialog(message, onConfirm) {
    const fClose = function () {
        modal.modal("hide");
    };
    const modal = $("#confirmModal");
    modal.modal("show");
    $("#confirmMessage").empty().append(message);
    $("#confirmOk").text(strings['lt.blog.confirm']).unbind().one('click', onConfirm).one('click', fClose);
    $("#confirmCancel").text(strings['lt.blog.cancel']).unbind().one("click", fClose);
}

// BLOG
$('.edit-post').click(function (e) {
    const postDOM = $(this).closest(".post");
    const postFormDOM = $('.form-post-create');
    postFormDOM.find('#id').val(postDOM.attr('id'));
    postFormDOM.find('#title').val(postDOM.find('.title').text());
    postFormDOM.find('#description').val(postDOM.find('.description').text());
    $(".post").css('opacity', '1');
    postDOM.css('opacity', '0.5');
    $(window).scrollTop(0);
    $(".single-edit").removeClass("d-none")
});

// COMMENT
$('.edit-comment').click(function (e) {
    const postDOM = $(this).closest(".post");
    const commentDOM = $(this).closest(".comment");
    const commentFormDOM = $('.form-comment-create');
    commentFormDOM.find('#id').val(commentDOM.attr('id'));
    commentFormDOM.find('#text').val(commentDOM.find('.text').text());
    $(".comment").css('opacity', '1');
    commentDOM.css('opacity', '0.5');
    $(window).scrollTop(postDOM.height() + postDOM.offset().top - window.innerHeight / 2);
});

//DELETE
function delete_form_event(value) {
    $(`.delete-${value}`).click(function (e) {
        e.preventDefault();
        const tempThis = $(this);
        confirmDialog(strings['lt.blog.areYouSureDelete'], function () {
            const valDOM = tempThis.closest(`.${value}`);
            const form = tempThis.closest('form');
            $.ajax({
                type: "POST",
                url: form.attr('action'),
                data: {
                    'id': valDOM.attr('id'),
                    '_csrf': form.find('input[name="_csrf"]').val()
                },
                success: function (responseText) {
                    if(window.location.pathname.split('/')[1]==='post'){
                        window.location.href = window.location.origin
                    }
                    valDOM.remove();
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
}

delete_form_event("comment");
delete_form_event("post");