const selectCiclo = document.getElementById('selectCiclo');
const tablaContainer = document.getElementById('tablaContainer');
const tablaAsistencias = document.getElementById('tablaAsistencias').getElementsByTagName('tbody')[0];

const datos = {
    ciclo1: [
        { curso: 'Fundamientos de Gestion Empresarial', profesor: 'Prof. José', vecesFalto: 2 },
        { curso: 'Matemática Básica', profesor: 'Prof. Ana', vecesFalto: 1 },
        { curso:  'Introducción a la Algoritmia', profesor: 'Prof. Carlos', vecesFalto: 0 },
        { curso:  'Arquitectura de Entornos Web', profesor: 'Prof. Carlos', vecesFalto: 0 },
        { curso:  'Tecnologias de la Información', profesor: 'Prof. Carlos', vecesFalto: 0 },
        { curso:  'Desarrollo de Habilidades Profesionales', profesor: 'Prof. Carlos', vecesFalto: 0 }
    ],
    ciclo2: [
        { curso: 'Base de Datos I', profesor: 'Prof. José', vecesFalto: 0 },
        { curso: 'Matemática II', profesor: 'Prof. Ana', vecesFalto: 1 },
        { curso:  'Algoritmos y Estructura de Datos', profesor: 'Prof. Carlos', vecesFalto: 0 },
        { curso:  'Desarrollo de Entornos Web', profesor: 'Prof. Carlos', vecesFalto: 0 },
        { curso:  'Modelado de Procesos de Negocios', profesor: 'Prof. Carlos', vecesFalto: 1 },
        { curso:  'Desarrollo de Habilidades Profesionales II', profesor: 'Prof. Carlos', vecesFalto: 0 }
    ],
    ciclo3: [
        { curso: 'Base de Datos II', profesor: 'Prof. José', vecesFalto: 0 },
        { curso: 'Programación Orientada a Objetos', profesor: 'Prof. Ana', vecesFalto: 1 },
        { curso:  'Análisis y Diseño de Sistemas I', profesor: 'Prof. Carlos', vecesFalto: 0 },
        { curso:  'Gestión de Datos Dinámicos', profesor: 'Prof. Carlos', vecesFalto: 0 },
        { curso:  'Lenguaje de Programación', profesor: 'Prof. Carlos', vecesFalto: 1 },
        { curso:  'Desarrollo de Habilidades Profesionales III', profesor: 'Prof. Carlos', vecesFalto: 1 }
    ]
};

// Escuchar el evento de cambio del select
selectCiclo.addEventListener('change', function () {
    const cicloSeleccionado = this.value;

    // Limpiar la tabla antes de llenarla
    tablaAsistencias.innerHTML = '';

    if (cicloSeleccionado) {
        const asistencias = datos[cicloSeleccionado];
        asistencias.forEach(asistencia => {
            const fila = tablaAsistencias.insertRow();
            fila.insertCell(0).textContent = asistencia.curso;
            fila.insertCell(1).textContent = asistencia.profesor;
            fila.insertCell(2).textContent = asistencia.vecesFalto;
        });

        tablaContainer.classList.remove('d-none'); // Mostrar la tabla
    }
});
