document.addEventListener("DOMContentLoaded", function() {
    const links = document.querySelectorAll(".header__menu-link");
    const currentPage = window.location.pathname.slice(1);
    console.log("JavaScript подключен и работает!");
    console.log(currentPage);
    console.log(links);

    links.forEach(link => {
        if (link.getAttribute("href") === currentPage) {
            console.log(link.getAttribute("href"))
            link.classList.add("active");
        }
    });
});
