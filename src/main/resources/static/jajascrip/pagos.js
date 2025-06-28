// Datos de boletas simuladas
const boletas = [
    {
        documento: "BV700-0463555",
        descripcion: "COMPUTACION E INFORM",
        fechaVenc: "16/10/2024",
        fechaEmi: "23/09/2024",
        monto: "539.00",
        mora: "0.00",
        subtotal: "539.00"
    },
    {
        documento: "BV600-0338645",
        descripcion: "COMPUTACION E INFORM",
        fechaVenc: "25/10/2024",
        fechaEmi: "26/09/2024",
        monto: "240.00",
        mora: "0.00",
        subtotal: "240.00"
    }
];

// Función para visualizar boletas dinámicamente
function visualizarBoletas() {
    const boletasContainer = document.getElementById("boletas-container");
    const boletasBody = document.getElementById("boletas-body");

    boletasContainer.style.display = "block"; // Mostrar la tabla

    // Vaciar el contenido anterior de la tabla
    boletasBody.innerHTML = "";

    // Agregar dinámicamente las boletas a la tabla
    boletas.forEach((boleta, index) => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td><input type="checkbox" name="pension" value="${boleta.subtotal}" data-documento="${boleta.documento}" onclick="actualizarConsolidado()"></td>
            <td>${boleta.documento}</td>
            <td>${boleta.descripcion}</td>
            <td>${boleta.fechaVenc}</td>
            <td>${boleta.fechaEmi}</td>
            <td>S/ ${boleta.monto}</td>
            <td>S/ ${boleta.mora}</td>
            <td>S/ ${boleta.subtotal}</td>
        `;

        boletasBody.appendChild(row);
    });
}

// Función para actualizar el consolidado de pagos
function actualizarConsolidado() {
    const checkboxes = document.querySelectorAll('input[name="pension"]:checked');
    let documentosSeleccionados = "";
    let total = 0;

    checkboxes.forEach((checkbox) => {
        const documento = checkbox.getAttribute('data-documento');
        const monto = parseFloat(checkbox.value);

        documentosSeleccionados += `<p>Documento: ${documento} - Monto: S/ ${monto.toFixed(2)}</p>`;
        total += monto;
    });

    // Mostrar documentos seleccionados en el consolidado
    const consolidadoDocumentos = document.getElementById('consolidado-documentos');
    consolidadoDocumentos.innerHTML = documentosSeleccionados || "<p>Ningún documento seleccionado</p>";

    // Mostrar el total acumulado
    const consolidadoTotal = document.getElementById('consolidado-total');
    consolidadoTotal.textContent = `Total: S/ ${total.toFixed(2)}`;

    // Mostrar el botón de pago solo si hay documentos seleccionados
    const pagarButton = document.getElementById('pagar');
    pagarButton.style.display = total > 0 ? 'inline-block' : 'none';
}

// Función para simular el pago
function realizarPago() {
    alert("Procesando el pago...");
    // Aquí puedes agregar la lógica para procesar el pago real
}
