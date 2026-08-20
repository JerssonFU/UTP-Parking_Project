document.addEventListener('DOMContentLoaded', () => {
    const ctx = document.querySelector('meta[name="ctx"]')?.content || '';
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.content || '';
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content || 'X-CSRF-TOKEN';

    const vehiculoSelect = document.getElementById('vehiculo');
    const btnRegistrar = document.getElementById('btnRegistrar');
    const campusInput = document.getElementById('campus');
    const placaInput = document.getElementById('placa');
    const terminosInput = document.getElementById('terminos');

    function headersJson() {
        const headers = { 'Content-Type': 'application/json' };
        if (csrfToken) headers[csrfHeader] = csrfToken;
        return headers;
    }


    async function cargarSedes() {
        const sedesFallback = ['Lima Centro', 'Arequipa', 'Petit Thouars'];
        try {
            const response = await fetch(`${ctx}/sedes`);
            if (!response.ok) throw new Error();
            const data = await response.json();
            const sedes = Array.isArray(data.sedes) ? data.sedes : [];
            const nombres = sedes
                .map(sede => sede && sede.nombre ? String(sede.nombre).trim() : '')
                .filter(Boolean);
            const opciones = nombres.length > 0 ? nombres : sedesFallback;
            campusInput.innerHTML = '';
            opciones.forEach(nombre => {
                const option = document.createElement('option');
                option.value = nombre;
                option.textContent = nombre;
                campusInput.appendChild(option);
            });
        } catch (error) {
            campusInput.innerHTML = '';
            sedesFallback.forEach(nombre => {
                const option = document.createElement('option');
                option.value = nombre;
                option.textContent = nombre;
                campusInput.appendChild(option);
            });
        }
    }

    async function cargarCategorias() {
        try {
            const response = await fetch(`${ctx}/vehiculos/categorias`);
            if (!response.ok) throw new Error();
            const categorias = await response.json();
            vehiculoSelect.innerHTML = '';
            categorias.forEach(categoria => {
                const option = document.createElement('option');
                option.value = categoria;
                option.textContent = categoria;
                vehiculoSelect.appendChild(option);
            });
        } catch (error) {
            vehiculoSelect.innerHTML = '<option value="Auto">Auto</option><option value="Camioneta">Camioneta</option><option value="Motocicleta">Motocicleta</option>';
        }
    }

    async function registrar() {
        const campus = campusInput.value;
        const categoria = vehiculoSelect.value;
        const placa = placaInput.value.trim().toUpperCase();

        if (!campus || !categoria || !placa || !terminosInput.checked) {
            alert('Complete todos los campos obligatorios y acepte los términos.');
            return;
        }

        if (!/^[A-Z0-9-]{5,15}$/.test(placa)) {
            alert('Ingrese una placa válida.');
            return;
        }

        btnRegistrar.disabled = true;
        try {
            const response = await fetch(`${ctx}/vehiculos/registro`, {
                method: 'POST',
                headers: headersJson(),
                body: JSON.stringify({ categoria, placa, campus })
            });
            const data = await response.json();
            if (!response.ok) {
                throw new Error(data.mensaje || 'No se pudo registrar el vehículo.');
            }
            alert('Solicitud registrada exitosamente.');
            window.location.href = `${ctx}/solicitudes`;
        } catch (error) {
            alert(error.message || 'Ocurrió un error durante el proceso.');
        } finally {
            btnRegistrar.disabled = false;
        }
    }

    btnRegistrar?.addEventListener('click', registrar);
    placaInput?.addEventListener('input', () => {
        placaInput.value = placaInput.value.toUpperCase();
    });

    cargarSedes();
    cargarCategorias();
});
