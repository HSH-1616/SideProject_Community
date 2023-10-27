const user_id = document.querySelector("#login-user-id");
const user_id_warn = document.querySelector(".login-form form>div span");
const login_form = document.querySelector(".login-form form");
const login_form_warn = document.querySelector(".login-error");
var regex = /^[a-zA-Z0-9]*$/;

user_id.addEventListener("input", function () {
  if (!regex.test(user_id.value)) {
    user_id_warn.style.display = "block";
  } else {
    user_id_warn.style.display = "none";
  }
});

login_form.addEventListener("submit", function (e) {
  if (!regex.test(user_id.value)) {
    e.preventDefault();
    login_form_warn.style.display = "flex";
  }
});
