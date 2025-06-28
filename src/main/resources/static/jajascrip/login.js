// Añadimos los eventos para los botones de registro e inicio de sesión
document.getElementById("btn__registrarse").addEventListener("click", Register);
document.getElementById("btn__iniciar-sesion").addEventListener("click", IniciarSesion);

// Declaración de variables para manejar los formularios y las cajas de animación
var contenedor_login_register = document.querySelector(".contenedor__login-register");
var formulario_login = document.querySelector(".formulario__login");
var formulario_register = document.querySelector(".formulario__register");

var caja_trasera_login = document.querySelector(".caja__trasera-login");
var caja_trasera_register = document.querySelector(".caja__trasera-register");


// Función para mostrar el formulario de inicio de sesión
function IniciarSesion() {
    if (window.innerWidth > 850) {
        formulario_register.style.transform = "scale(0.8)";
        formulario_register.style.opacity = "0";
        setTimeout(() => {
            formulario_register.style.display = "none";
        }, 300); // Retraso para ocultar después de la animación
        formulario_login.style.display = "block";
        setTimeout(() => {
            formulario_login.style.transform = "scale(1)";
            formulario_login.style.opacity = "1";
        }, 10);
        contenedor_login_register.style.left = "10px";
        caja_trasera_register.style.opacity = "1";
        caja_trasera_login.style.opacity = "0";
    } else {
        formulario_register.style.display = "none";
        contenedor_login_register.style.left = "0px";
        formulario_login.style.display = "block";
        caja_trasera_register.style.display = "block";
        caja_trasera_login.style.display = "none";
    }
}

// Función para mostrar el formulario de registro
function Register() {
    if (window.innerWidth > 850) {
        formulario_login.style.transform = "scale(0.8)";
        formulario_login.style.opacity = "0";
        setTimeout(() => {
            formulario_login.style.display = "none";
        }, 300); // Retraso para ocultar después de la animación
        formulario_register.style.display = "block";
        setTimeout(() => {
            formulario_register.style.transform = "scale(1)";
            formulario_register.style.opacity = "1";
        }, 10);
        contenedor_login_register.style.left = "410px";
        caja_trasera_register.style.opacity = "0";
        caja_trasera_login.style.opacity = "1";
    } else {
        formulario_register.style.display = "block";
        contenedor_login_register.style.left = "0px";
        formulario_login.style.display = "none";
        caja_trasera_register.style.display = "none";
        caja_trasera_login.style.display = "block";
        caja_trasera_login.style.opacity = "1";
    }
}
