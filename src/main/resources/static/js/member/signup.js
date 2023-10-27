const user_password = document.getElementById("signup-user-password");
const user_password_confirm = document.getElementById("signup-user-password-confirm");
const user_password_confirm_warn = document.getElementById("signup-user-password-confirm-warn");

user_password_confirm.addEventListener("input", function () {
    if (user_password.value != user_password_confirm.value) {
        user_password_confirm_warn.style.display = "block";
    } else {
        user_password_confirm_warn.style.display = "none";
    }
});


const signup_btn = document.querySelector(".signup-form-submit button");

// 화면 시작 시 g-recaptcha 생성
var onloadCallback = function () {
    grecaptcha.render("g-recaptcha", {
        sitekey: "6LfK6sAoAAAAAP0ZiqMRUWhJNSvU5LLc5Gs68kfN",
        callback: verifyCallback,
        "expired-callback": expiredCallback,
    });
};

// 인증 성공 시
var verifyCallback = function (response) {
    signup_btn.classList.remove("disabled-btn");
    signup_btn.disabled = false;
};

// 인증 만료 시
var expiredCallback = function (response) {
    signup_btn.classList.add("disabled-btn");
    signup_btn.disabled = true;
};

// g-recaptcha 리셋
var resetCallback = function () {
    grecaptcha.reset();
};


// document.querySelector(".signup-form-submit button").addEventListener("click", function () {
//     let form_data = new FormData(document.getElementById("signup"))
//
//     fetch("/signup/save", {
//         method: "POST",
//         cache: 'no-cache',
//         body: form_data,
//     })
//         .then((response) => response.json())
//         .then(data => {
//             console.log(data.error[1]);
//         })
//         .catch(error => {
//             console.log("에러")
//         })
// })
