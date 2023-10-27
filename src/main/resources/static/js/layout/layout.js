// menu popup버튼 이벤트
const media_query = matchMedia("screen and (min-width: 768px)");
const popup_open = document.querySelector(".menu-button button");
const popup_close = document.querySelector(".menu-popup-header button");
const popup_area = document.querySelector(".menu-popup-area");

popup_open.addEventListener("click", function () {
  popup_area.style.display = "block";
});

popup_close.addEventListener("click", function () {
  popup_area.style.display = "none";
});

media_query.addEventListener("change", function () {
  popup_area.style.display = "none";
});
