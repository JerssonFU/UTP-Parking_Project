document.addEventListener('DOMContentLoaded', () => {
    const ctx = document.querySelector('meta[name="ctx"]')?.content || '';
    const userId = document.querySelector('meta[name="user-id"]')?.content || '';
    const userRole = document.querySelector('meta[name="user-role"]')?.content || '';
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.content || '';
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content || 'X-CSRF-TOKEN';
    const tbody = document.getElementById('solicitudes-body');
    const comentarioBody = document.getElementById('comentario-body');
    const exportarExcel = document.getElementById('exportarExcel');

    let solicitudesRenderizadas = [];

    function headersJson() {
        const headers = { 'Content-Type': 'application/json' };
        if (csrfToken) headers[csrfHeader] = csrfToken;
        return headers;
    }

    function formatDate(value) {
        if (!value) return '-';
        const date = new Date(value);
        return Number.isNaN(date.getTime()) ? '-' : date.toLocaleDateString('es-PE');
    }

    function puedeVerTodas() {
        return ['PERSONAL_SAE', 'ADMINISTRATIVO', 'JEFE_SEGURIDAD'].includes(userRole);
    }

    function puedeResponder() {
        return ['PERSONAL_SAE', 'ADMINISTRATIVO'].includes(userRole);
    }

    async function cargarSolicitudes() {
        if (!tbody) return;
        tbody.innerHTML = '<tr><td colspan="7" class="text-center text-muted">Cargando solicitudes...</td></tr>';

        try {
            const url = puedeVerTodas() ? `${ctx}/solicitudes/listar` : `${ctx}/solicitudes/${userId}`;
            const response = await fetch(url, { credentials: 'same-origin' });
            if (!response.ok) throw new Error('No se pudieron cargar las solicitudes.');

            const data = await response.json();
            const solicitudes = Array.isArray(data.solicitudes) ? data.solicitudes : [];
            solicitudesRenderizadas = solicitudes.map(solicitud => ({
                ...solicitud,
                placa: solicitud.placa || '-',
                categoria: solicitud.categoria || '-'
            }));

            renderSolicitudes();
        } catch (error) {
            console.error(error);
            tbody.innerHTML = '<tr><td colspan="7" class="text-center text-danger">No se pudieron cargar las solicitudes.</td></tr>';
        }
    }

    async function cambiarEstado(idSolicitud, estado) {
        let comentario = '';

        if (estado === 'Aceptado') {
            const confirmar = window.confirm('¿Desea aceptar esta solicitud? Se reservará un espacio del campus seleccionado.');
            if (!confirmar) return;
        }

        if (estado === 'Rechazado') {
            const motivo = window.prompt('Ingrese el motivo del rechazo:');
            if (motivo === null) return;
            comentario = motivo.trim();
            if (!comentario) {
                alert('Debe ingresar un motivo para rechazar la solicitud.');
                return;
            }
        }

        try {
            const response = await fetch(`${ctx}/solicitudes/respuesta/${idSolicitud}`, {
                method: 'PATCH',
                credentials: 'same-origin',
                headers: headersJson(),
                body: JSON.stringify({ estado, comentario })
            });

            const data = await response.json().catch(() => ({}));
            if (!response.ok) {
                throw new Error(data.mensaje || 'No se pudo actualizar la solicitud.');
            }

            alert(data.mensaje || 'Solicitud actualizada correctamente.');
            await cargarSolicitudes();
        } catch (error) {
            console.error(error);
            alert(error.message || 'No se pudo actualizar la solicitud.');
        }
    }

    function renderSolicitudes() {
        if (!tbody) return;
        tbody.innerHTML = '';

        if (solicitudesRenderizadas.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" class="text-center text-muted">No hay solicitudes registradas.</td></tr>';
            return;
        }

        solicitudesRenderizadas.forEach(solicitud => {
            const row = document.createElement('tr');
            const values = [
                solicitud.idSolicitud,
                formatDate(solicitud.fechaSolicitud),
                solicitud.placa,
                solicitud.categoria,
                solicitud.estado || '-',
                null,
                formatDate(solicitud.fechaRespuesta)
            ];

            values.forEach((value, index) => {
                const cell = document.createElement(index === 0 ? 'th' : 'td');

                if (index === 4) {
                    const estado = document.createElement('div');
                    estado.textContent = value == null ? '-' : String(value);
                    cell.appendChild(estado);

                    const pendiente = String(solicitud.estado || '').toLowerCase() === 'por verificar';
                    if (puedeResponder() && pendiente) {
                        const acciones = document.createElement('div');
                        acciones.className = 'mt-2 d-flex gap-1 flex-wrap';

                        const aceptar = document.createElement('button');
                        aceptar.type = 'button';
                        aceptar.className = 'btn btn-sm btn-success';
                        aceptar.textContent = 'Aceptar';
                        aceptar.addEventListener('click', () => cambiarEstado(solicitud.idSolicitud, 'Aceptado'));

                        const rechazar = document.createElement('button');
                        rechazar.type = 'button';
                        rechazar.className = 'btn btn-sm btn-danger';
                        rechazar.textContent = 'Rechazar';
                        rechazar.addEventListener('click', () => cambiarEstado(solicitud.idSolicitud, 'Rechazado'));

                        acciones.appendChild(aceptar);
                        acciones.appendChild(rechazar);
                        cell.appendChild(acciones);
                    }
                } else if (index === 5) {
                    cell.className = 'comentario';
                    cell.textContent = 'Ver comentario';
                    cell.setAttribute('data-bs-toggle', 'modal');
                    cell.setAttribute('data-bs-target', '#comentarioModal');
                    cell.addEventListener('click', () => {
                        if (comentarioBody) comentarioBody.textContent = solicitud.comentario || 'Sin comentario.';
                    });
                } else {
                    cell.textContent = value == null ? '-' : String(value);
                }

                row.appendChild(cell);
            });

            tbody.appendChild(row);
        });
    }

    function exportarExcelSolicitudes() {
        window.location.href = `${ctx}/export/solicitudes`;
    }

    exportarExcel?.addEventListener('click', exportarExcelSolicitudes);
    cargarSolicitudes();
});
