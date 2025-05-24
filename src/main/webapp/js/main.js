console.log("JavaScript подключен и работает!");

document.addEventListener("DOMContentLoaded", function() {
    const links = document.querySelectorAll(".header__menu-link");
    const currentPage = window.location.pathname.slice(1);

    links.forEach(link => {
        if (link.getAttribute("href") === currentPage) {
            link.classList.add("active");
        }
    });

    function validatePassword(password) {
        const minLength = 8;
        const specialChars = /[!@#$%&*()]/;
        if (password.length < minLength) {
            return "Пароль должен содержать минимум 8 символов.";
        }
        if (!specialChars.test(password)) {
            return "Пароль должен содержать хотя бы один специальный символ (!@#$%&*()).";
        }
        return null;
    }

    const loginForm = document.querySelector("form[action='login']");
    if (loginForm) {
        loginForm.onsubmit = function(event) {
            const passwordInput = document.querySelector("input[name='password']");
            const errorDiv = document.querySelector(".login-error");
            const errorMsg = validatePassword(passwordInput.value);

            if (errorMsg) {
                event.preventDefault();
                if (!errorDiv) {
                    const newErrorDiv = document.createElement("div");
                    newErrorDiv.className = "login-error";
                    newErrorDiv.style.color = "red";
                    loginForm.appendChild(newErrorDiv);
                }
                document.querySelector(".login-error").textContent = errorMsg;
                return false;
            } else if (errorDiv) {
                errorDiv.remove();
            }
            return true;
        };
    }

    const signupForm = document.querySelector("form[action='signup']");
    if (signupForm) {
        signupForm.onsubmit = function(event) {
            const passwordInput = document.querySelector("input[name='password']");
            const errorDiv = document.querySelector(".signup-error");
            const errorMsg = validatePassword(passwordInput.value);

            if (errorMsg) {
                event.preventDefault();
                if (!errorDiv) {
                    const newErrorDiv = document.createElement("div");
                    newErrorDiv.className = "signup-error";
                    newErrorDiv.style.color = "red";
                    signupForm.appendChild(newErrorDiv);
                }
                document.querySelector(".signup-error").textContent = errorMsg;
                return false;
            } else if (errorDiv) {
                errorDiv.remove();
            }
            return true;
        };
    }
});