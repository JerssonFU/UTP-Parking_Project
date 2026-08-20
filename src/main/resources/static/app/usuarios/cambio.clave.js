document.addEventListener('DOMContentLoaded', () => {
    const ctx = document.querySelector('meta[name="ctx"]')?.content || '';
    const form = document.getElementById('frmCambioClave');
    const password = document.getElementById('password');
    const guardar = document.getElementById('btnGuardarCambioClave');
    const regresar = document.getElementById('btnRegresarCambioClave');

    function mostrar(tipo, titulo, texto) {
        if (typeof window.mensaje === 'function') {
            window.mensaje(tipo, titulo, texto);
        } else {
            alert(texto);
        }
    }

    guardar?.addEventListener('click', async event => {
        event.preventDefault();
        const valor = password?.value?.trim() || '';
        if (valor.length < 4) {
            mostrar('warning', 'Advertencia', 'La contraseña debe tener al menos 4 caracteres.');
            password?.focus();
            return;
        }

        guardar.disabled = true;
        try {
            const response = await fetch(`${ctx}/cambioClave/guardarCambioClave`, {
                method: 'POST',
                body: new FormData(form)
            });
            const data = await response.json();
            if (!response.ok || data.status !== 'Done') {
                throw new Error(data.data || 'No se pudo actualizar la contraseña.');
            }
            password.value = '';
            mostrar('success', 'Success', data.data);
        } catch (error) {
            mostrar('error', 'Error', error.message || 'No se pudo actualizar la contraseña.');
        } finally {
            guardar.disabled = false;
        }
    });

    regresar?.addEventListener('click', () => {
        window.location.href = `${ctx}/inicio`;
    });
});
