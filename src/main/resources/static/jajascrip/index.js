// Redireccionar al seleccionar una opción del primer select
const info = document.getElementById('selectInfo');

// Escucha el evento 'change' del select
info.addEventListener('change', fn_redirigirInfo);

function fn_redirigirInfo() {
    const value = info.value; // Obtener el valor del select
    if (value) {
        window.open(value, '_blank'); // Abrir en una nueva pestaña
        info.selectedIndex = 0; // Restaurar la opción predeterminada
    }
}


// Redireccionar al seleccionar una opción del segundo select
const pago = document.getElementById('selectPago');

// Escucha el evento 'change' del select
pago.addEventListener('change', fn_redirigirPago);

function fn_redirigirPago() {
    const value = pago.value; // Obtener el valor del select
    if (value) {
        window.open(value, '_blank'); // Abrir en una nueva pestaña
        pago.selectedIndex = 0; // Restaurar la opción predeterminada
    }
}