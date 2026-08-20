document.addEventListener('DOMContentLoaded', () => {
    const ctx = document.querySelector('meta[name="ctx"]')?.content || '';
    const rol = document.querySelector('meta[name="user-role"]')?.content || '';
    const fechaFormateada = new Date().toLocaleDateString('es-PE');

    function renderSinSedes() {
        const sedesContainer = document.getElementById('sedesContainer');
        const modalSedesContainer = document.getElementById('modalSedesContainer');
        if (sedesContainer) {
            sedesContainer.innerHTML = `
                <div class="col-md-4 mt-3">
                    <div class="card border-light shadow-sm rounded">
                        <div class="card-body">
                            <h5 class="card-title fw-bold">Estacionamiento</h5>
                            <h6 class="card-subtitle mb-3 text-muted">Sin sedes configuradas</h6>
                            <p class="text-center fw-bold text-muted">No hay espacios disponibles para mostrar.</p>
                        </div>
                    </div>
                </div>`;
        }
        if (modalSedesContainer) {
            modalSedesContainer.innerHTML = '<div class="col-12 text-muted">No hay sedes disponibles.</div>';
        }
    }

    async function obtenerSedes() {
        try {
            const response = await fetch(`${ctx}/sedes`);
            if (!response.ok) {
                renderSinSedes();
                return;
            }
            const data = await response.json();
            const sedes = Array.isArray(data.sedes) ? data.sedes : [];
            if (sedes.length === 0) {
                renderSinSedes();
                return;
            }
            renderSedes(sedes);
        } catch (error) {
            renderSinSedes();
        }
    }

    function renderSedes(sedes) {
        const sedesContainer = document.getElementById('sedesContainer');
        const modalSedesContainer = document.getElementById('modalSedesContainer');
        if (!sedesContainer || !modalSedesContainer) return;

        sedesContainer.innerHTML = '';
        modalSedesContainer.innerHTML = '';

        sedes.forEach(sede => {
            const cantidad = Number.isFinite(Number(sede.cantidad)) ? Number(sede.cantidad) : 0;
            const card = document.createElement('div');
            card.className = 'col-md-4 mt-3';
            card.innerHTML = `
                <div class="card border-light shadow-sm rounded">
                    <div class="card-body">
                        <h5 class="card-title fw-bold"></h5>
                        <h6 class="card-subtitle mb-3 text-muted"></h6>
                        <p class="text-center fw-bold ${cantidad > 20 ? 'text-success' : cantidad > 13 ? 'text-warning' : 'text-danger'}">
                            Espacios disponibles ${cantidad}/30 <i class="bx bxs-car"></i>
                        </p>
                    </div>
                </div>`;
            card.querySelector('.card-title').textContent = sede.nombre || '';
            card.querySelector('.card-subtitle').textContent = sede.direccion || '';
            sedesContainer.appendChild(card);

            const modalCard = document.createElement('div');
            modalCard.className = 'col-md-4';
            modalCard.innerHTML = `
                <div class="card card-sede mt-2 mb-2" data-bs-dismiss="modal">
                    <div class="card-body">
                        <h5 class="card-title fw-bold"></h5>
                        <h6 class="card-subtitle mb-2 text-muted"></h6>
                    </div>
                </div>`;
            modalCard.querySelector('.card-title').textContent = sede.nombre || '';
            modalCard.querySelector('.card-subtitle').textContent = sede.direccion || '';
            modalSedesContainer.appendChild(modalCard);
        });
    }

    const mensajeRol = document.getElementById('mensajeRol');
    if (mensajeRol) {
        mensajeRol.textContent = rol === 'SEGURIDAD'
            ? `Hoy, ${fechaFormateada}, estos son los espacios disponibles:`
            : `Hoy, ${fechaFormateada}, estos son los espacios disponibles para ti:`;
    }

    const registroLink = document.getElementById('registroLink');
    if (registroLink) registroLink.style.display = ['ALUMNO', 'DOCENTE'].includes(rol) ? 'block' : 'none';

    const seguridadSection = document.getElementById('seguridadSection');
    if (seguridadSection) seguridadSection.style.display = ['SEGURIDAD', 'JEFE_SEGURIDAD'].includes(rol) ? 'block' : 'none';

    document.getElementById('formIngreso')?.addEventListener('submit', event => {
        event.preventDefault();
        alert('Seleccione un estacionamiento disponible antes de confirmar el ingreso.');
    });

    document.getElementById('formSalida')?.addEventListener('submit', event => {
        event.preventDefault();
        alert('La salida se confirmará desde el módulo de control de estacionamiento.');
    });

    obtenerSedes();
});
