console.log("JavaScript подключен и работает!");

document.addEventListener("DOMContentLoaded", function() {
    const links = document.querySelectorAll(".header__menu-link");
    const currentPage = window.location.pathname.slice(1);

    links.forEach(link => {
        if (link.getAttribute("href") === currentPage) {
            link.classList.add("active");
        }
    });
});
